package com.ssafy.confidentIs.keytris.model;

import java.sql.Timestamp;
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
  private String[] currentWordList;
  private String[] targetWordList;
  private String[] subWordList;
  private Timestamp overTime;//멀티모드
  private int streak;
  private String lastWord;

  @Builder //1인 모드 초기 값
  public Player(PlayerStatus playerStatus, int score,
      String[] targetWordList, String[] subWordList, int streak) {
    this.playerStatus = playerStatus;
    this.score = score;
    this.targetWordList = targetWordList;
    this.subWordList = subWordList;
    this.streak = streak;
  }

  @Builder
  public Player(String playerId, PlayerStatus playerStatus, int score, String[] targetWordList,
      String[] subWordList, int streak) {
    this.playerId = playerId;
    this.playerStatus = playerStatus;
    this.score = score;
    this.targetWordList = targetWordList;
    this.subWordList = subWordList;
    this.streak = streak;
  }
}
