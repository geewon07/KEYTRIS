/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.common.dto.response.ResponseDto;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
 	private final RoomService roomService;
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//	public ResponseEntity<?> refill() {
//		//@RequestBody String type, String category
//		ResponseDto responseDto;
//		StatusResponse statusResponse;
//		WordListResponse wordListResponse = new WordListResponse();
//		//    wordListResponse.refill();
//		responseDto = new ResponseDto("success", "단어 목록 보강",
//				Collections.singletonMap("wordListResponse",
//						wordListResponse.refill(wordService.getWords("sub", 0, REFILL_SUB_AMOUNT),
//								wordService.getWords("target", 0, REFILL_TARGET_AMOUNT))));
//		return new ResponseEntity<>(responseDto, HttpStatus.OK);
//	}
//	@Scheduled(fixedRate = 5000)
//	public void reportCurrentTime() {
//		log.info("The time is now {}", dateFormat.format(new Date()));
//	}
}