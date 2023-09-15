package com.ssafy.confidentIs.keytrisData.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataService {

    Boolean existWord(String guess);

    double cosineSimilarity(double[] vec1, double[] vec2);

    double calculateSimilarity(String word1, String word2);

    String[][] calculateSimilarities(List<String> currentWordList, String guessWord);

    List<String> giveTargetWords(int category, int amount);

    List<String> giveSubWords(int amount);
}
