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
    private List<MultiPlayer> playerList;
    private StartWordList startWordList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class StartWordList {
        private String targetWord;
        private List<String> subWordList;

        public StartWordList(MultiRoom room) {
            this.targetWord = room.getTargetWordList().get(0);
            this.subWordList = room.getSubWordList().subList(0, 9);
        }
    }

}
