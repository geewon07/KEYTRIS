package com.ssafy.confidentIs.keytris.service;

public interface WordService {
  Boolean checkDataBase(String guess);

  String[] sortByProximity(String[] currentWordList);

  String[] getWords(String type,int number);

//  String[] getTargetWords(String type);

}
