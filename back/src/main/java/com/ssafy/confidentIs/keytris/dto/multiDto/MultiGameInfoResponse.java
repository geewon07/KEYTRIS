package com.ssafy.confidentIs.keytris.dto.multiDto;

import com.ssafy.confidentIs.keytris.model.RoomStatus;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiPlayer;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiRoom;
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
    private String masterId;
    private List<MultiPlayer> playerList;
    private StartWordList startWordList;


    public MultiGameInfoResponse(MultiRoom room) {
        this.roomId = room.getRoomId();
        this.roomStatus = room.getRoomStatus();
        this.category = room.getCategory();
        this.masterId = room.getMaster().getPlayerId();
        this.playerList = room.getPlayerList();
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class StartWordList {
        private String targetWord;
        private List<String> subWordList;

        public void updateStartWordList(MultiRoom room) {
            this.targetWord = room.getTargetWordList().get(0);
            this.subWordList = room.getSubWordList().subList(0, 9);
        }
    }

}
