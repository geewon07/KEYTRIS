package com.ssafy.confidentIs.keytrisData.dto;

import lombok.Getter;

@Getter
public class RefillRequest {
    private String type;
    private int category;
    private int amount;
}
