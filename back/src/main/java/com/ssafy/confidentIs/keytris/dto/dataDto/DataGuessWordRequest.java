package com.ssafy.confidentIs.keytris.dto.dataDto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataGuessWordRequest {
    private List<String> currentWordList;
    private String guessWord;
}
