package com.example.demo.component;


import com.example.demo.enums.ListType;
import com.example.demo.enums.TfidfTrainType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.CountVectorizer;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.split;


@Getter
@Slf4j
@Component
@RequiredArgsConstructor
public class TfidfModel {

    @Value("${spring.hdfs.baseUrl}")
    private String hdfsBaseUrl;

    private final WordModel wordModel;

    private final  SparkConf sparkConf;
    private final JavaSparkContext javaSparkContext;

    private ConcurrentHashMap<String, List<String>> targetWordsMap = new ConcurrentHashMap<>(); // 카테고리별 타켓어 리스트
    private ConcurrentHashMap<String, List<String>> subWordsMap = new ConcurrentHashMap<>(); // 카테고리별 서브어 리스트
    private ConcurrentHashMap<String, List<String>> levelWordsMap = new ConcurrentHashMap<>(); // 카테고리별 레벨어 리스트
    private List<String> allFrequentWords = new ArrayList<>();


    public void loadAndTrain(TfidfTrainType type) throws IOException {
        String[] csvPaths = {"100","101", "102", "103", "104", "105"}; // 각  뉴스 분야 카테고리 값
        String fileKeyWordspath = "src/main/resources/models/tfidf/keyWords/"; // 타켓어 폴더 경로
        String fileSubWordspath = "src/main/resources/models/tfidf/subWords/"; // 서브어 폴더 경로
        String fileLevelWordspath = "src/main/resources/models/tfidf/levelWords/"; // 레벨어 폴더 경로

        for (String csvPath : csvPaths) { // 각각의 카테고리마다 타켓어랑 서브어, 레벨어는 중복되면 안됨 => 카테고리마다 타켓어 뽑고 제외 단어 서브어,레벨어로 분리!

            File keywordFile = new File(fileKeyWordspath + csvPath + ".txt");
            File subWordsWordsFile = new File(fileSubWordspath + csvPath + ".txt");
            File levelWordsFile = new File(fileLevelWordspath + csvPath + ".txt");

            if ( TfidfTrainType.RESTART.equals(type) && keywordFile.exists() && subWordsWordsFile.exists() && levelWordsFile.exists()) { // 파일존재한다면 파일 단어 리스트로 변환
                List<String> keywordList = loadWordsFromFile(fileKeyWordspath + csvPath + ".txt");
                targetWordsMap.put(csvPath, keywordList);
                List<String> subWordsWordsList = loadWordsFromFile(fileSubWordspath + csvPath + ".txt");
                subWordsMap.put(csvPath, subWordsWordsList);
                List<String> levelWordsList = loadWordsFromFile(fileLevelWordspath + csvPath + ".txt");
                levelWordsMap.put(csvPath, levelWordsList);
            } else {
                log.info("학습을 시킵니닷");
                train(csvPath, fileKeyWordspath, fileSubWordspath,fileLevelWordspath);
            }
        }
    }

    public void getFrequentWords(SparkSession spark) {
        // frequent_word CSV 파일에서 단어가져와서 word2vec 단어들로 필터링
        Dataset<Row> frequentWordsDf = spark.read().format("csv")
                .option("header", "true")
                .load("src/main/resources/data/frequent_words_noun.csv"); // frequentWord 파일 가져오기
        for (Row row : frequentWordsDf.collectAsList()) {
            String word = row.getString(0);
            if (wordModel.getKeys().contains(word)) {
                allFrequentWords.add(word);
            }
        }
    }

    public void train(String csvPath,String fileKeyWordspath,String fileSubWordspath,String fileLevelWordspath) throws IOException {
        log.info("시작합니다ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ");
        SparkSession spark = SparkSession
                .builder()
                .config(sparkConf)
                .getOrCreate();


        if(allFrequentWords.isEmpty()) { // FrequentWords - word2vec에 있는 단어들로 골라내기
            getFrequentWords(spark);
        }
        Dataset<Row> df = spark.read().format("csv")
                .option("header", "true")
//                        .load("src/main/resources/data/"+csvPath+".csv") // hdfs에 지정된 주소로 바꾸기!!!!!!!!!!!!
                .load(hdfsBaseUrl + "/data-noun/" + csvPath + "/*.csv")
                .withColumn("words", split(col("words"), ","));

        // CountVectorizerModel 생성
        CountVectorizer cv = new CountVectorizer()
                .setInputCol("words")
                .setOutputCol("rawFeatures");

        CountVectorizerModel cvm = cv.fit(df);
        Dataset<Row> featurizedData = cvm.transform(df);

        // IDF 모델 생성 및 적용
        IDF idf = new IDF().setInputCol("rawFeatures").setOutputCol("features");
        IDFModel idfModel = idf.fit(featurizedData);
        Dataset<Row> rescaledData = idfModel.transform(featurizedData);
        log.info(csvPath);
        rescaledData.show(false);

        // 각 단어의 TF-IDF 점수 합산
        List<Row> rowsList = rescaledData.collectAsList();
        HashMap<String, Double> tfidfSums = new HashMap<>();
        for (Row row : rowsList) {
            SparseVector featuresVector = row.getAs("features");
            String[] vocabulary = cvm.vocabulary();

            int[] indices = featuresVector.indices();
            double[] values = featuresVector.values();

            for (int i = 0; i < indices.length; i++) {
                int featureIndex = indices[i];
                double tfidfScore = values[i];
                String word = vocabulary[featureIndex];

                if (tfidfSums.containsKey(word)) {
                    double currentSum = tfidfSums.get(word);
                    tfidfSums.put(word, currentSum + tfidfScore);
                } else {
                    tfidfSums.put(word, tfidfScore);
                }
            }
        }

        // 상위 300개의 키워드 찾기
        int k = 300;
        PriorityQueue<Map.Entry<String, Double>> topKeywordsQueue = new PriorityQueue<>(k, Map.Entry.comparingByValue());

        for (Map.Entry<String, Double> entry : tfidfSums.entrySet()) {
            topKeywordsQueue.offer(entry);

            if (topKeywordsQueue.size() > k) {
                topKeywordsQueue.poll();
            }
        }
        // 키워드를 Set에 저장 ( contain을 찾는데는 hashset 시간복잡도 O(1)
        Set<String> topKeywords = new HashSet<>();
        for (Map.Entry<String, Double> entry : topKeywordsQueue) {
            topKeywords.add(entry.getKey());
        }

        List<String> keywordList = new ArrayList<>(topKeywords);
        targetWordsMap.put(csvPath, keywordList); // 타켓어 리스트 변환후 저장

        // allFrequentWords에서 topKeywords를 제외한 나머지 단어들 선택
        List<String> remainingWords = new ArrayList<>();
        for (String word : allFrequentWords) {
            if (!topKeywords.contains(word)) {
                remainingWords.add(word);
            }
        }

        int size = remainingWords.size(); // 서브어랑 레벨어 나누기! ( 중복되지 않도록 )
        List<String> subWordList = new ArrayList<>(remainingWords.subList(0, size / 2));
        List<String> levelWordList = new ArrayList<>(remainingWords.subList(size / 2, size));

        subWordsMap.put(csvPath, subWordList);
        levelWordsMap.put(csvPath, levelWordList);

        saveWordsToFile(keywordList, fileKeyWordspath + csvPath + ".txt"); // 파일 저장
        saveWordsToFile(subWordList, fileSubWordspath + csvPath + ".txt"); // 파일 저장
        saveWordsToFile(levelWordList, fileLevelWordspath + csvPath + ".txt"); // 파일 저장
        System.out.println(size);
    }


    public void saveWordsToFile(List<String> keywords, String filePath) throws IOException {
        try (BufferedWriter writer= new BufferedWriter(new FileWriter(filePath))) {

            for(String keyword: keywords){
                writer.write(keyword);
                writer.newLine();
            }
        } catch(IOException e){
            throw e;
        }
    }
    public List<String> loadWordsFromFile(String filePath) throws IOException {
        List<String> keywords= new ArrayList<>();

        try(BufferedReader reader= new BufferedReader(new FileReader(filePath))){
            String line;

            while((line= reader.readLine())!= null){
                keywords.add(line);
            }

        }catch(IOException e){
            throw e;
        }
        return keywords;
    }

    public List<String> giveWords(ListType listType, String category, int amount) {

        if(ListType.TARGET.equals(listType)) {
            List<String> targetWordList = targetWordsMap.get(category);

            Collections.shuffle(targetWordList);

            if (amount > targetWordList.size()) {
                System.out.println("단어리스트의 개수보다 요청한 단어의 개수가 더 많습니다.");
                return targetWordList;
            }

            List<String> inputWords = targetWordList.subList(0, amount);
            return inputWords;
        } else if(ListType.SUB.equals(listType)) {
            List<String> subWordList = subWordsMap.get(category);

            Collections.shuffle(subWordList);

            if (amount > subWordList.size()) {
                System.out.println("단어리스트의 개수보다 요청한 단어의 개수가 더 많습니다.");
                return subWordList;
            }

            List<String> inputWords = subWordList.subList(0, amount);
            return inputWords;
        } else {
            List<String> levelWordList = levelWordsMap.get(category);

            Collections.shuffle(levelWordList);

            if (amount > levelWordList.size()) {
                System.out.println("단어리스트의 개수보다 요청한 단어의 개수가 더 많습니다.");
                return levelWordList;
            }
            List<String> inputWords = levelWordList.subList(0, amount);
            return inputWords;
        }
    }
}
