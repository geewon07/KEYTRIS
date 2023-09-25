package com.ssafy.confidentIs.keytris.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonRoomServiceImpl {


    // 단어가 유사도 정렬 후 어느 위치로 이동했는 지 구하기
    public int[] calculateSortedIndex(String[][] sortedWordList, String[][] currentWordList) {
        int currentListSize = currentWordList.length;
        int[] sortedIndex = new int[currentListSize];

        // sortedWordList의 원소 위치를 해시맵에 저장
        Map<String, Integer> positionMap = new HashMap<>();
        for (int i = 0; i < sortedWordList.length; i++) {
            positionMap.put(sortedWordList[i][0], i);
        }

        // currentWordList의 각 원소 위치를 찾아 sortedIndex에 저장
        for (int i = 0; i < currentWordList.length; i++) {
            sortedIndex[i] = positionMap.get(currentWordList[i][0]);
        }

        log.info("sortedWordList {}", Arrays.deepToString(sortedWordList));

        return sortedIndex;
    }


    // 정렬 후 타겟어의 순위를 계산하는 메서드
    public int calculateTargetWordRank(String[][] sortedWordList, String target) {
        for (int i = 0; i < sortedWordList.length; i++) {
            if (sortedWordList[i][0].equals(target)) {
                return i;
            }
        }
        return -1;
    }


    // 새로운 subWordList를 2차원 배열로 만드는 메서드
    public String[][] convertTo2DArray(List<String> subWordList) {
        if (subWordList == null || subWordList.isEmpty()) {
            return null;
        }

        String[][] newSubWordList = new String[subWordList.size()][2];
        for (int i = 0; i < subWordList.size(); i++) {
            newSubWordList[i][0] = subWordList.get(i);
            newSubWordList[i][1] = "";
        }

        return newSubWordList;
    }


}
