package com.example.demo.dto.request;

import com.example.demo.enums.ListType;
import lombok.Getter;

@Getter
public class RefillRequest {
    private ListType type;
    private int category;
    private int amount;
}
