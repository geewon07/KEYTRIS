package com.ssafy.confidentIs.keytris.dto.multiDto;

import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MultiGameResultResponse {

    private List<PlayerResult> playerResultList;

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerResult implements Comparable<PlayerResult> {

        private String playerId;
        private String nickname;
        private Long score;

        @Override
        public int compareTo(PlayerResult other) {
            return Long.compare(other.score, this.score);
        }
    }

    public void sortPlayerResults() {
        Collections.sort(playerResultList);
    }

}
