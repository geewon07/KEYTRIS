package com.ssafy.confidentIs.keytrisData.service;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DataServiceImpl implements DataService {

    @Override
    public Boolean existWord(String guess) {
        int randomNum = (int)(Math.random() * 100 ) + 1;
        if(guess.equals("없는단어")) {
            return false;
        } else {
            return true;
        }
    }

    // 코사인 유사도 값에서 *100을 하고 소수점 둘째까지 반환하기
    @Override
    public double cosineSimilarity(double[] vec1, double[] vec2) {
        return 0;
    }

    @Override
    public double calculateSimilarity(String word1, String word2) {
        return 0;
    }

    @Override
    public String[][] calculateSimilarities(List<String> currentWordList, String guessWord) {
        if (existWord(guessWord)) {
            String[][] similarities = new String[currentWordList.size()][2];
            for(int i=0;i<currentWordList.size();i++) {
                Random rand = new Random();
                double randomValue =
                    Math.round(rand.nextDouble() * 100 * 100) / 100.0; // 소수점 둘째 자리까지
                similarities[i][0] = currentWordList.get(i);
                similarities[i][1] = String.valueOf(randomValue);
            }
            Arrays.sort(similarities, new Comparator<String[]>() {
                @Override
                public int compare(String[] a, String[] b) {
                    double aSimilarity = Double.parseDouble(a[1]);
                    double bSimilarity = Double.parseDouble(b[1]);
                    return Double.compare(bSimilarity, aSimilarity); // 역순 비교
                }
            });

            return similarities;
        }
        return null;
    }

    @Override
    public List<String> giveTargetWords(int category, int amount) {
        // category 기준으로 뽑아오기
        List<String> wordList = Arrays.asList("가방", "강아지", "고양이", "곰돌이", "기린",
            "나무", "냉장고", "눈사람", "다람쥐", "더위",
            "라디오", "마법", "무지개", "물고기", "바나나",
            "사과", "소녀", "솜사탕", "아기", "안녕",
            "영화", "오렌지", "우산", "으리으리", "이불",
            "자두", "잠자리", "재미", "조카", "차",
            "초콜릿", "치즈", "친구", "토끼", "포도",
            "하늘", "하루", "하얀", "한국", "햇빛");
        if (amount > wordList.size()) {
            System.out.println("단어리스트의 개수보다 요청한 단어의 개수가 더 많습니다.");
            return null;
        }
        Collections.shuffle(wordList);
        List<String> inputWords = wordList.subList(0, amount);
        return inputWords;
    }

    @Override
    public List<String> giveSubWords(int amount) {
        List<String> wordList = Arrays.asList( "가나", "나라", "다리", "라면", "마부",
            "바위", "사회", "아기", "자립", "차력",
            "카드", "타자", "파크", "하늘","거울", "고구마", "공부", "꽃", "나비",
            "물", "바다", "바이올린", "별", "빵",
            "사탕", "선물", "손", "숟가락", "식탁",
            "신발", "아기자기", "안경", "양말", "여우",
            "열쇠", "옷", "외국", "유리", "자전거",
            "저녁", "주먹", "천사", "체리", "초롱",
            "컴퓨터", "키", "포크", "피아노", "호랑이");
        if (amount > wordList.size()) {
            System.out.println("단어리스트의 개수보다 요청한 단어의 개수가 더 많습니다.");
            return null;
        }
        Collections.shuffle(wordList);
        List<String> inputWords = wordList.subList(0, amount);
        return inputWords;
    }

}
