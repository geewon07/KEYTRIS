package com.ssafy.confidentIs.keytris.dto.gameDto;

import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponse {
    // 단어 변동 시 보낼 것 -> 현재 미사용
    private String playerId;
    private PlayerStatus playerStatus;
    private String targetWord;
    private List<String> subWordList;

    public PlayerResponse makeSinglePlayer(SinglePlayer player) {
        return PlayerResponse.builder()
                .playerId(player.getPlayerId())
                .playerStatus(player.getPlayerStatus())
                .build();
    }
}
