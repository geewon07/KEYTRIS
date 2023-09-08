package com.ssafy.confidentIs.keytris.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
@Slf4j // 로그 찍기
@RequiredArgsConstructor // private final 타입 의존성 주입을 자동으로 해줌
public class TestController {

    @GetMapping
    public ResponseEntity<?> test(){//@PathVariable String enterWord
        String[] s = {"하잉","언니"};
        return ResponseEntity.ok(s);
    }

//    @PutMapping("/{enterWord}")
//    public ResponseEntity<?> exchange(@PathVariable String enterWord, @RequestBody String[] words){
//        System.out.println("get called "+enterWord);
//        String[] s = {"하잉","언니"};
//        Arrays.sort(words);
//        return ResponseEntity.ok(words);
//    }
}
