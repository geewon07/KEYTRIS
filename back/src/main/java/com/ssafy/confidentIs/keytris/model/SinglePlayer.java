package com.ssafy.confidentIs.keytris.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

// @SuperBuilder 상속관계에서 부모 자식 모두 붙여줘야 함
@SuperBuilder
@Getter
@ToString(callSuper=true)
@AllArgsConstructor
public class SinglePlayer extends BasePlayer {

}
