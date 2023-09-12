package com.ssafy.confidentIs.keytris.model;

import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder // 만들때 넣을 속성값을 가지고 생성자 위에 붙여주면 나중에 .속성(값).build()로 만들기 가능
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Player {

  private String playerId = "testerPlayer";
  private String nickname;
  private PlayerStatus playerStatus;
  private int score;
  private int targetWordIndex;
  private int subWordIndex;
  private List<String> currentWordList;
  private String targetWord;
  private List<String> subWordList;
  private Timestamp overTime;//멀티모드
  private int streak;
  private String lastWord;

  @Builder //1인 모드 초기 값
  public Player(PlayerStatus playerStatus, int score,
      String targetWord, List<String> subWordList, int streak) {
    this.playerStatus = playerStatus;
    this.score = score;
    this.targetWord = targetWord;
    this.subWordList = subWordList;
    this.streak = streak;
  }

  @Builder
  public Player(String playerId, PlayerStatus playerStatus, int score, String targetWord,
      List<String> subWordList, int streak) {
    this.playerId = playerId;
    this.playerStatus = playerStatus;
    this.score = score;
    this.targetWord = targetWord;
    this.subWordList = subWordList;
    this.streak = streak;
  }

  public Player(String playerId, PlayerStatus playerStatus, int score, int targetWordIndex,
      int subWordIndex) {
    this.playerId = playerId;
    this.playerStatus = playerStatus;
    this.score = score;
    this.targetWordIndex = targetWordIndex;
    this.subWordIndex = subWordIndex;
  }

  public void updateWords(List<String> subWordList, String targetWord,
      List<String> currentWordList) {
    this.currentWordList = currentWordList;
    this.subWordList = subWordList;
    this.targetWord = targetWord;
  }

  public void updateIndex(int subWordIndex, int targetWordIndex) {
    this.subWordIndex = subWordIndex;
    this.targetWordIndex = targetWordIndex;
  }

  public void updateStatus(PlayerStatus playerStatus) {
    this.playerStatus = playerStatus;
  }

  public void updateLastWord(String lastWord) {
    this.lastWord = lastWord;
  }
}
