package com.ssafy.confidentIs.keytris.dto.multiGameDto;

import lombok.*;

import java.sql.Timestamp;
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
        private Timestamp overTime;

        @Override
        public int compareTo(PlayerResult other) {
            int scoreComparison = Long.compare(other.score, this.score);

            if (scoreComparison != 0) {
                return scoreComparison;
            }

            // score가 같을 경우 overTime 비교
            return other.overTime.compareTo(this.overTime);
        }
    }

    public void sortPlayerResults() {
        Collections.sort(playerResultList);
    }

}
