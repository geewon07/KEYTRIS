package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.dto.GuessRequest;
import com.ssafy.confidentIs.keytris.dto.StartResponse;
import com.ssafy.confidentIs.keytris.dto.StatusResponse;
import com.ssafy.confidentIs.keytris.dto.WordListResponse;
import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import java.util.List;
import java.util.UUID;

public interface RoomService {

  //게임(방 생성)
  StatusResponse createRoom(int category);

  //게임 시작
  StartResponse startRoom(String roomId);

  WordListResponse enterWord(GuessRequest request);

  void gameOver(String roomId,String lastWord,Long score);

  //게임 상태 변경 준비->시작->
  void updateRoomStatus(String roomId, RoomStatus rStatus);

  //레디 확인
  Boolean checkReady(RoomStatus rStatus, PlayerStatus pStatus);

  //플레이어 상태변경 + 플레이어
  void updatePlayerStatus(Room room, SinglePlayer player, String step);

}
