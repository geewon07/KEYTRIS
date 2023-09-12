package com.ssafy.confidentIs.keytris.dto.dataDto;

import com.ssafy.confidentIs.keytris.model.Category;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataWordListRequest {

    private String type;
    private Category category;
    private Integer amount;

}
