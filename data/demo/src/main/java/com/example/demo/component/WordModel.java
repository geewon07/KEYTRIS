package com.example.demo.component;

import com.example.demo.dto.response.ErrorCode;
import com.example.demo.dto.response.customException.InvalidWordException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.Word2VecModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.split;
@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class WordModel {

    // 실제 모델
    private final SparkConf sparkConf;
    private final JavaSparkContext javaSparkContext;
    private HashMap<String, double[]> wordVectorCache;
    private List<String> keys;

    // 벡터 로드해오는 함수
    public HashMap<String, double[]> loadWordVectorCache() throws IOException {
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/models/word2vec/model.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.wordVectorCache = (HashMap<String, double[]>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
            this.train();
        }
        this.keys = new ArrayList<>(wordVectorCache.keySet());
        return wordVectorCache;
    }


    public List<String> getRandomKeys(int i){
        Collections.shuffle(this.keys);
        return keys.subList(0, Math.min(i, keys.size()));
    }


    // 두 단어의 유사도 반환
    @Deprecated
    public Optional<Double> getSimilarities(String word1, String word2) {
        return Optional.ofNullable( getVectorValue(word1))
                .flatMap(v1 -> Optional.ofNullable(getVectorValue(word2))
                        .map(v2 -> {
                            double dotProduct = 0.0, normA = 0.0, normB = 0.0;
                            for (int i = 0; i < v1.length; i++) {
                                dotProduct += v1[i] * v2[i];
                                normA += Math.pow(v1[i], 2);
                                normB += Math.pow(v2[i], 2);
                            }
                            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
                        }));
    }

    // 코사인 유사도 검사 함수
    private double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += vectorA[i] * vectorA[i];
            normB += vectorB[i] * vectorB[i];
        }
        return Math.round((dotProduct / Math.sqrt(normA * normB)) * 20000) / 100.0;
    }


    // 캐싱해서 벡터값 반환
    public double[] getVectorValue(String word) {return wordVectorCache.getOrDefault(word, null);}

    private double mapValue(double x) {
        // 원래 범위의 최솟값과 최댓값
        double originalMin = -200.0;
        double originalMax = 200.0;

        // 새로운 범위의 최솟값과 최댓값
        double newMin = 0;
        double newMax = 100;

        // 매핑 공식을 사용하여 x를 새로운 범위로 변환
        return ((x - originalMin) / (originalMax - originalMin)) * (newMax - newMin) + newMin;
    }

    // 순서대로 정렬해서 보내주는 함수
    public List<List<Object>> sortedSimilarities(List<String> currentList, String guessWord) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Optional.ofNullable(getVectorValue(guessWord))
                .map(guessVec ->
                        currentList.stream()
                                .map(word -> {
                                    List<Object> wordWithSimilarity = new ArrayList<>();
                                    Optional<double[]> vec = Optional.ofNullable(getVectorValue(word));
                                    if (vec.isPresent()) {
                                        double similarity = mapValue(cosineSimilarity(guessVec, vec.get()));
                                        similarity = Double.parseDouble(df.format(similarity));
                                        wordWithSimilarity.add(word);
                                        wordWithSimilarity.add(similarity);  // 원래의 실수 값을 저장
                                    }
                                    return wordWithSimilarity;
                                })
                                .filter(list -> !list.isEmpty())
                                .sorted((l1, l2) -> {
                                    double sim1 = (double) l1.get(1);
                                    double sim2 = (double) l2.get(1);
                                    return Double.compare(sim2, sim1);
                                })
                                .collect(Collectors.toList())
                )
                .orElseThrow(() -> new InvalidWordException("입력할 수 없는 단어입니다.", ErrorCode.INVALID_WORD));
    }



    // 학습 함수
    public int train() throws IOException{
        Word2VecModel model;

        SparkSession sparkSession = SparkSession
                .builder()
                .config(sparkConf)
                .getOrCreate();
        long startTime = System.currentTimeMillis();

        Dataset<Row> documentDF = sparkSession.read()
                .format("csv")
                .option("header", "true")
                .load("hdfs://ip-172-26-2-236:9000/data/*/*.csv")
                .withColumn("words", split(col("words"), ","));
        documentDF.show();
        documentDF = documentDF.filter(col("words").isNotNull());
        org.apache.spark.ml.feature.Word2Vec word2Vec = new org.apache.spark.ml.feature.Word2Vec()
                .setInputCol("words")
                .setOutputCol("result")
                .setVectorSize(200)
                .setMinCount(3)
                .setMaxIter(30);

        model = word2Vec.fit(documentDF);

        // 벡터 캐싱
        Dataset<Row> vectors = model.getVectors();
        this.wordVectorCache = new HashMap<>();

        for (Row row : vectors.collectAsList()) {
            String word = row.getAs("word");
            double[] vector = ((org.apache.spark.ml.linalg.DenseVector) row.getAs("vector")).toArray();
            wordVectorCache.put(word, vector);
        }
        log.info("단어 개수 : " + this.wordVectorCache.size());

        // 벡터 저장
        try {
            FileOutputStream fos = new FileOutputStream("src/main/resources/models/word2vec/model.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.wordVectorCache);
            oos.close();
            fos.close();
            System.out.println("Word vector cache is saved!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Dataset<Row> result = model.transform(documentDF);
        result = model.getVectors();
        result.show();
        sparkSession.stop();
        long endTime = System.currentTimeMillis();
        return (int)(endTime - startTime) / 1000;
    }
}


