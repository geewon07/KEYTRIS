package com.example.demo.controller;


import com.example.demo.component.TfidfModel;
import com.example.demo.component.WordModel;
import com.example.demo.dto.request.GuessRequest;
import com.example.demo.dto.request.RefillRequest;
import com.example.demo.dto.response.ResponseDto;
import com.example.demo.enums.ListType;
import com.example.demo.enums.TfidfTrainType;
import com.example.demo.service.DataService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class WordController {

    private WordModel wordModel;
    private DataService dataService;


    public WordController(WordModel wordModel, DataService dataService) {
        this.wordModel = wordModel;
        this.dataService = dataService;
    }

    @GetMapping("/train")
    public int train() throws IOException {
        return dataService.train();

    }


    @PostMapping("api/data/guess-words")
    public ResponseEntity<?> guess(@RequestBody GuessRequest guessRequest) {
        log.info("단어추측 : "+ " 추측어 : " + guessRequest.getGuessWord() + "  단어리스트: " + guessRequest.getCurrentWordList().toString());
        ResponseDto responseDto = new ResponseDto("success", "단어 유사도 정렬 성공",
                Collections.singletonMap("calWordList", dataService.guessWord(guessRequest.getCurrentWordList(),
                        guessRequest.getGuessWord())));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

//    @GetMapping("/vector/{word}")
//    public ResponseEntity<?> value(@PathVariable("word") String word){
//        log.info("단어 벡터 : " + word);
//        return ResponseEntity.status(HttpStatus.OK).body(wordModel.getVectorValue(word));
//    }
//
//    @GetMapping("/vector/{word1}/{word2}")
//    public ResponseEntity<?> value(@PathVariable("word1") String word1, @PathVariable("word2") String word2){
//        log.info("두 단어 비교");
//        return ResponseEntity.status(HttpStatus.OK).body(wordModel.getSimilarities(word1, word2));
//    }



    @PostMapping("api/data/get-words")
    public ResponseEntity<?> refill(@RequestBody RefillRequest refillRequest) {
        log.info("리필하기 : "+ " 타입 : " + refillRequest.getType() + "  카테고리: " + refillRequest.getCategory() + "   개수 :"+ refillRequest.getAmount());
        // 분야 [100: 정치, 101: 경제, 102: 사회, 103: 생활/문화, 104: 세계, 105: IT/과학]
        ResponseDto responseDto;
        String category = Integer.toString(refillRequest.getCategory());

        if(refillRequest.getType().equals(ListType.TARGET)) {
            responseDto = new ResponseDto("success", "타켓어 단어 리필 성공",
                    Collections.singletonMap("wordList", dataService.getWords(refillRequest.getType(),category, refillRequest.getAmount())));
        } else if(refillRequest.getType().equals(ListType.SUB)){
            responseDto = new ResponseDto("success", "서브어 단어 리필 성공",
                    Collections.singletonMap("wordList", dataService.getWords(refillRequest.getType(), category, refillRequest.getAmount())));
        }else {
            responseDto = new ResponseDto("success", "레벨어 단어 리필 성공",
                    Collections.singletonMap("wordList", dataService.getWords(refillRequest.getType(), category, refillRequest.getAmount())));
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

}
