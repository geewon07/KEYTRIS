package com.ssafy.confidentIs.keytris.dto.multiDto;

import com.ssafy.confidentIs.keytris.dto.WordListResponse;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import com.ssafy.confidentIs.keytris.model.MultiPlayer;
import com.ssafy.confidentIs.keytris.model.MultiRoom;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MultiGameInfoResponse {

    private String roomId;
    private RoomStatus roomStatus;
    private int category;
    private List<MultiPlayer> playerList;
    private WordListResponse wordListResponse;

//    private StartWordList startWordList;

//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @ToString
//    public static class StartWordList {
//        private String targetWord;
//        private List<String> subWordList;
//
//        public StartWordList(MultiRoom room) {
//            this.targetWord = room.getTargetWordList().get(0);
//            this.subWordList = room.getSubWordList().subList(0, 9);
//        }
//    }

}
