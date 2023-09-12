package com.ssafy.confidentIs.keytrisData.common.dto.response;

import java.util.Map;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseDto {

    private String success;
    private String message;
    private Map<String, ?> data;

    public ResponseDto() {
    }

    public ResponseDto(String success, String message, Map<String, ?> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ResponseDto(String success, String message) {
        this.success = success;
        this.message = message;
    }

}
