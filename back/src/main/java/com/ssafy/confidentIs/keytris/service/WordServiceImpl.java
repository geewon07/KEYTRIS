package com.ssafy.confidentIs.keytris.service;

import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class WordServiceImpl implements WordService {

  @Override //TODO: 지원 API
  public Boolean checkDataBase(String guessWord) {

    return guessWord != null;
  }

  @Override //TODO: 지원과 조율 필요 ,데이터에 단어가져올 때 API 요청이 들어간다고?
  public String[] sortByProximity(String[] currentWordList) {
    Arrays.sort(currentWordList);
    return currentWordList;
  }

  //TODO: 나중에 단어뽑아올때 어느 카테고리에서 얼마만큼 가져올것인지, 키워드는 따로 뽑는 로직이 있는 지 추가
  @Override
  public String[] getWords(String type, int number) {
    //TODO: string "TY
    
    if (type.equals("categoryOrSub")) {
      return new String[]{"가방", "강아지", "고양이", "곰돌이", "기린",
          "나무", "냉장고", "눈사람", "다람쥐", "더위",
          "라디오", "마법", "무지개", "물고기", "바나나",
          "사과", "소녀", "솜사탕", "아기", "안녕",
          "영화", "오렌지", "우산", "으리으리", "이불",
          "자두", "잠자리", "재미", "조카", "차",
          "초콜릿", "치즈", "친구", "토끼", "포도",
          "하늘", "하루", "하얀", "한국", "햇빛"};
    } else if (type.equals("categoryOrTarget")) {
      return new String[]{
          "가", "나", "다", "라", "마",
          "바", "사", "아", "자", "차",
          "카", "타", "파", "하"};
    } else if (number==3) {
      return new String[]{
          "거울", "고구마", "공부", "꽃", "나비",
          "물", "바다", "바이올린", "별", "빵",
          "사탕", "선물", "손", "숟가락", "식탁",
          "신발", "아기자기", "안경", "양말", "여우",
          "열쇠", "옷", "외국", "유리", "자전거",
          "저녁", "주먹", "천사", "체리", "초롱",
          "컴퓨터", "키", "포크", "피아노", "호랑이"};
    }

    return new String[0];
  }

}
