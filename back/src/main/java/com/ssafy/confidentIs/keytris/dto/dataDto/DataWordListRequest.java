package com.ssafy.confidentIs.keytris.dto.dataDto;

import com.ssafy.confidentIs.keytris.model.WordType;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataWordListRequest {

    private WordType type;
    private int category;
    private int amount;

}
