package com.example.demo.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class GuessRequest {
    private List<String> currentWordList;
    private String guessWord;
}
