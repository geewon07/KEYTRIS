import React, { useEffect, useState} from "react";
import { Outlet } from "react-router-dom";
import { 
  startGame,
  insertWord,
  overGame
} from "../../api/singleGame/singleGameApi.js";
import { connect, disconnect , subscribe } from "../../api/stompClient.js";
import TextChatting from "../../components/chatting/ChatTest.js"


export const SingleGame = () => {
  const [guessWord, setGuessWord] = useState(null);
  const [currentWordList, setCurrentWordList] = useState([]);
  const [targetWord, setTargetWord] = useState(null);
  // const [streak, setStreak] = useState(null);
  const [nickname, setNickname] = useState(null);
  const [playerId, setPlayerId] = useState(null);
  const [playerStatus, setPlayerStatus] = useState(null);
  const [roomId, setRoomId] = useState(null);
  const [roomStatus, setRoomStatus] = useState(null);
  const [subWordList, setSubWordList] = useState([]);
  const [sortedWordList, setSortedWordList] = useState([]);
  const [score, setScore] = useState(0);

  useEffect(() => {
    let subscription;

    if (roomId !== null) {
      console.log("ddddd");
      connect();

      // const destination = '/your/destination';
      // const body = { room_id: roomId };
      
      // sendMsg(destination, body);

      const callback = (messageBody) => {
        console.log(messageBody);
      };

      subscription = subscribe(`/topic/room/level-word/${roomId}`, callback);
    }

    // return () => {
    //   if (subscription) {
    //     subscription.unsubscribe();
    // }
    
    // if (roomId !== null) {
    //     disconnect();
    // }
    // };
  }, [roomId]);

  const handleStartGame = async (statusRequestDto) => { //게임시작api
    try {
      const res = await startGame(statusRequestDto);
      const startResponseDto = res.data.data.StartResponse;
      // 게임방 만들어질 때 playerId, roomId 넘겨받음 => 이 api에서는 playerStatus, roomStatus만 변경
      setPlayerStatus(startResponseDto.statusResponse.playerStatus);
      setRoomStatus(startResponseDto.statusResponse.roomStatus);

      setSubWordList(startResponseDto.wordListResponse.subWordList);
      setTargetWord(startResponseDto.wordListResponse.targetWord);
      // currentWordList 저장해야함 -> 어떻게 저장?? subWordList에 TargetWord뒤에 추가???

    } catch (error) {
      console.error(error);
    }
  };

  const statusRequestDto = {
    "playerId" : playerId,
    "playerStatus" : playerStatus,
    "roomId" : roomId,
    "roomStatus" : roomStatus
  }

  useEffect(() => {
   setPlayerId("f41c4135-78b4-436e-824a-9e6ff19a54a5");
   setRoomId("27d4216f-c5e2-413a-8252-71e737e1c7ea");
   setPlayerStatus("READY");
   setRoomStatus("PREPARED");
  },[]);

  // useEffect(() => {
  //   console.log(subWordList); // subWordList가 변경될 때마다 이 로그가 출력
  // }, [subWordList]);

  // useEffect(() => {

  //   handleStartGame(statusRequestDto);
  //   console.log(subWordList);

  // },[statusRequestDto]);

  const handleInsertWord = async (insertRequestDto) => {
    try {
      const res = await insertWord(insertRequestDto);
      const SortedWordResponseDto = res.data.data.SortedWordListResponse;
      
      
      setSortedWordList(SortedWordResponseDto.sortedWordList);
       // 나중에 다 하고 쓰세여~
      setScore(SortedWordResponseDto.score);
      setSubWordList(SortedWordResponseDto.subWordList);
      setTargetWord(SortedWordResponseDto.targetWord);
    } catch (error) {
      console.error(error);
    }
  };

  const insertRequestDto = {
    "roomId" : roomId,
    "currentWordList" : currentWordList,
    "guessWord" : guessWord, // 입력한 단어 넣어주기
    "targetWord" : targetWord
  }
  
//   // API 호출 예시
//   handleInsertWord({
//     roomId : "4384279d-5535-438c-bf75-52edf1b99814",
//     currentWordList : [ "마부","여우","별", "꽃","자립", "천사","옷","아기자기","나라","한국"],
//     guessWord : "나방",
//     targetWord : "한국" 
//  });

  const handleOverGame = async (overRequestDto) => {
  try {
    const res = await overGame(overRequestDto);
    const OverResponseDto = res.data.data.OverResponse;
    // 페이지 result로 전환되면서 데이터 넘겨주기? 우선 넘겨줄거를 overResultDto로 만들게요
    // 근데 그냥 위에 overResponseDto 넘겨주는게 나을 것 같아서 그냥 안만들었습니다.
  } catch (error) {
    console.error(error);
  }
  };

  const overRequestDto = {
    "roomId" : roomId,
    "lastWord" : targetWord,
    "score" : score
  }
  
  // 사라짐...
  // const handleOutRoom = async (statusRequestDto) => { // 위에 dto있음!
  //   try {
  //     const res = await outRoom(statusRequestDto);
  //     const OverResponseDto = res.data.data.OverResponse;
  //   } catch (error) {
  //     console.error(error);
  //   }
  // };




  return (
    <div> 
      <button onClick={() => handleStartGame(statusRequestDto)}></button>
      <TextChatting></TextChatting>
      <Outlet />
    </div>
  );
};
