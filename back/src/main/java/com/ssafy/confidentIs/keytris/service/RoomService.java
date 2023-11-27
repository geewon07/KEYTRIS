package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.common.dto.ResponseDto;
import com.ssafy.confidentIs.keytris.dto.gameDto.GuessRequest;
import com.ssafy.confidentIs.keytris.dto.gameDto.OverRequest;
import com.ssafy.confidentIs.keytris.dto.gameDto.OverResponse;
import com.ssafy.confidentIs.keytris.dto.gameDto.StartResponse;
import com.ssafy.confidentIs.keytris.dto.gameDto.StatusResponse;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import com.ssafy.confidentIs.keytris.model.WordType;

public interface RoomService {

  //게임(방 생성)
  StatusResponse createRoom(int category);

  //입장-단어를 받기위한 소켓연결&준비상태
  StatusResponse enterRoom(String roomId);

  //게임 시작
  StartResponse startRoom(String roomId);

  ResponseDto enterWord(GuessRequest request);

  OverResponse gameOver(OverRequest request);

  //레디 확인
  Boolean checkReady(RoomStatus rStatus, PlayerStatus pStatus);

  //플레이어 상태변경 + 플레이어
  void updatePlayerStatus(Room room, SinglePlayer player, PlayerStatus playerStatus);

  //게임 상태 변경 준비->시작->
  void updateRoomStatus(String roomId, RoomStatus rStatus);

  //단어가 모자란가 확인 후 요청
  void checkRefill(Room room, WordType type);

//  List<RankingResponse> addHighscore(String nickname, String roomId);

  Room findByRoomId(String roomId);
}
