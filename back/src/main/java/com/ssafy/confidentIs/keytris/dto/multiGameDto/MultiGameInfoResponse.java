package com.ssafy.confidentIs.keytris.dto.multiGameDto;

import com.ssafy.confidentIs.keytris.dto.WordListResponse;
import com.ssafy.confidentIs.keytris.model.MultiPlayer;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
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

}
