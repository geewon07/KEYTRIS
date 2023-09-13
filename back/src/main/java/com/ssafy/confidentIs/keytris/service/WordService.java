package com.ssafy.confidentIs.keytris.service;

import java.util.List;

public interface WordService {
  Boolean checkDataBase(String guess);

  List<String> sortByProximity(List<String> currentWordList, String guess);

  List<String> getWords(String type, int category, int amount);

  int getIndex(String target,List<String> sortedWordList);
//  String[] getTargetWords(String type);

}
