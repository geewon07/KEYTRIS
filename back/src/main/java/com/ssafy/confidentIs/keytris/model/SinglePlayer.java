package com.ssafy.confidentIs.keytris.model;

import java.util.UUID;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

// 만들때 넣을 속성값을 가지고 생성자 위에 붙여주면 나중에 .속성(값).build()로 만들기 가능

@SuperBuilder
public class SinglePlayer extends BasePlayer {
//
//  private UUID playerId;
//  private PlayerStatus playerStatus;
//  private long score;
//  private int targetWordIndex;
//  private int subWordIndex;

}

//  public void updateWords(List<String> subWordList, String targetWord,
//      List<String> currentWordList) {
//    this.currentWordList = currentWordList;
//    this.subWordList = subWordList;
//    this.targetWord = targetWord;
//  }


