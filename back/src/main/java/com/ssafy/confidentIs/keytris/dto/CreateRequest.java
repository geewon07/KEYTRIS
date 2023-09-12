package com.ssafy.confidentIs.keytris.dto;

import lombok.Getter;

@Getter
public class CreateRequest {
  private String type;
  private int category;
}
//TODO : 타입 = 모드, 타입을 ENUM 으로 할지, 숫자로 할지, 멀티플레이어가 참여자 숫자를 받아와야 하는가? 초대 코드 뿌리고 선착순 3명?