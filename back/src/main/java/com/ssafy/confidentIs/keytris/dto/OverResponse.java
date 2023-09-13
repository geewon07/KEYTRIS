package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.Article;
import com.ssafy.confidentIs.keytris.model.Room;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OverResponse {
  private boolean isRecord;
  private List<RankingResponse> recordList;
  private List<Article> newsList;
  StatusResponse statusResponse;

  public OverResponse gameOver(boolean isRecord, Room room, List<Article> newsList,List<RankingResponse> recordList) {
    StatusResponse sResponse = new StatusResponse();

    return OverResponse.builder()
        .isRecord(isRecord)
        .recordList(recordList)
        .newsList(newsList)
        .statusResponse(sResponse.idStatus(room.getPlayerList().get(0),room))
        .build();
  }

}
