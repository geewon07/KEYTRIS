package com.ssafy.confidentIs.keytris.service;

import java.util.List;

public interface WordService {
  Boolean checkDataBase(String guess);

  List<String> sortByProximity(List<String> currentWordList);

  List<String> getWords(String type, int category, int amount);

//  String[] getTargetWords(String type);

}
