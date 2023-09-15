package com.ssafy.confidentIs.keytris.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
  //naver news api results
  private String title;
  private String description;
  private String link;

}
