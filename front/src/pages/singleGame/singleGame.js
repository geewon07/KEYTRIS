import React, { useState, useEffect } from "react";
import { Stomp } from "@stomp/stompjs";
import { Outlet } from "react-router-dom";
import {
  startGame,
  insertWord,
  overGame,
  outRoom,
  createRoom,
} from "../../api/singleGame/SingleGameApi.js";
import SockJS from "sockjs-client";
import axios from "axios";

export const Single = (props) => {
  // const { category } = props;
  // const [streak, setStreak] = useState(null);
  const [playerId, setPlayerId] = useState(null);
  const [playerStatus, setPlayerStatus] = useState(null);
  const [roomId, setRoomId] = useState(null);
  const [roomStatus, setRoomStatus] = useState(null);

  const [subWordList, setSubWordList] = useState([]);
  const [targetWord, setTargetWord] = useState(null);

  const [guessWord, setGuessWord] = useState("");
  const [currentWordList, setCurrentWordList] = useState([]);

  const [sortedWordList, setSortedWordList] = useState([]);
  const [score, setScore] = useState(0);

  const [sockJS, setSockJS] = useState();
  const sock = new SockJS("http:localhost:8765/keytris");
  const stomp = Stomp.over(sock);

  useEffect(() => {
    if (roomId != null)
      stomp.connect({}, () => {
        stomp.send("/app/games/room/" + roomId);
        stomp.subscribe("/topic/room/level-word/" + roomId, (message) => {
          console.log(message.body);
          const msg = message.body;//JSON.parse(message.body);
          setPlayerStatus(msg.playerStatus);
          setRoomStatus(msg.roomStatus);
         
        });
      });
  }, [roomId]);

  const handleCreate = async () => {
    try {
      const category = 101;
      const res = await createRoom({ category: 101 });
      console.log(category)
      setSockJS(sock);
      const statusResponseDto = res.data.data.StatusResponse;
      console.log(statusResponseDto);
      // 게임방 만들어질 때 playerId, roomId 넘겨받음 => 이 api에서는 playerStatus, roomStatus만 변경
      setPlayerStatus(statusResponseDto.playerStatus);
      setRoomStatus(statusResponseDto.roomStatus);
      setPlayerId(statusResponseDto.playerId);
      setRoomId(statusResponseDto.roomId);
      setSubWordList(statusResponseDto.subWordList);
      setTargetWord(statusResponseDto.wordListResponse.targetWord);

      console.log(roomId);
    } catch (error) {
      console.error(error);
    }
  };
  const statusRequestDto = {
    playerId: playerId,
    playerStatus: playerStatus,
    roomId: roomId,
    roomStatus: roomStatus,
  };
  const handleStartGame = async () => {
    //게임시작api
    try {
      const res = await startGame(statusRequestDto);
      const startResponseDto = res.data.data.StartResponse;
      const statusResponse = startResponseDto.statusResponse;
      const wordListResponse = startResponseDto.wordListResponse;
      console.log(startResponseDto);
      console.log(wordListResponse);
      setPlayerStatus(startResponseDto.statusResponse.playerStatus);
      setRoomStatus(startResponseDto.statusResponse.roomStatus);
      
      setSubWordList(startResponseDto.wordListResponse.subWordList);
      setCurrentWordList(wordListResponse.subWordList);
      setTargetWord(startResponseDto.wordListResponse.targetWord);
     
      setCurrentWordList((prev) => [...prev,startResponseDto.wordListResponse.targetWord]);
      setScore(startResponseDto.wordListResponse.score);

      console.log(currentWordList);
      // currentWordList 저장해야함 -> 어떻게 저장?? subWordList에 TargetWord뒤에 추가???
    } catch (error) {
      console.error(error);
    }
  };

  const handleScoring = (newList, newScore) => {
    //여기서 새값 들어오기전에 먼저 효과를 주기
    //TODO: 1 단어정렬, 2 점수 효과, 3 득점X 효과
    if (newScore === score) {
      console.log("did not score");
      //흔들리는 모션
      setCurrentWordList(newList);
    } else {
      const toDelete = newList.find(targetWord);
      setCurrentWordList(...newList.slice(0, toDelete), newList.slice(4));
    }
    console.log(newList);
    console.log(currentWordList);
  };
  // useEffect(() => {
  //  setPlayerId("983ff30d-8bc2-46b4-9f79-f97069de0754");
  //  setRoomId("e1bceba5-2213-4e1a-bcf6-c76992476c69");
  //  setPlayerStatus("READY");
  //  setRoomStatus("PREPARED");
  // },[]);

  // useEffect(() => {
  //   console.log(subWordList); // subWordList가 변경될 때마다 이 로그가 출력
  // }, [subWordList]);

  // useEffect(() => {

  //   handleStartGame(statusRequestDto);
  //   console.log(subWordList);

  // },[statusRequestDto]);
  const insertRequestDto = {
    roomId: roomId,
    currentWordList: currentWordList,
    guessWord: guessWord, // 입력한 단어 넣어주기
    targetWord: targetWord,
  };
  const handleInsertWord = async () => {
    try {
      const res = await insertWord(insertRequestDto);
      const SortedWordResponseDto = res.data.data.SortedWordListResponse;
      const sorted = SortedWordResponseDto.sortedWordList;
      const newScore = SortedWordResponseDto.score;
      handleScoring(sorted, newScore);
      //효과를 다 하고 쓰세여~

      setSortedWordList(sorted);
      setScore(newScore);
      setSubWordList(SortedWordResponseDto.subWordList);
      setTargetWord(SortedWordResponseDto.targetWord);
      setCurrentWordList((prev)=>[...prev, subWordList,targetWord]);
    
    } catch (error) {
      console.error(error);
    }
  };

  const handleInputChange = (e) => {
    const { value } = e.target;
    setGuessWord(value);
  };
  //   // API 호출 예시
  //   handleInsertWord({
  //     roomId : "4384279d-5535-438c-bf75-52edf1b99814",
  //     currentWordList : [ "마부","여우","별", "꽃","자립", "천사","옷","아기자기","나라","한국"],
  //     guessWord : "나방",
  //     targetWord : "한국"
  //  });

  const handleOverGame = async () => {
    try {
      // const res = await overGame(overRequestDto);
      // const OverResponseDto = res.data.data.OverResponse;
      // 페이지 화면 전환
      // 페이지 result로 전환되면서 데이터 넘겨주기? 우선 넘겨줄거를 overResultDto로 만들게요
      // 근데 그냥 위에 overResponseDto 넘겨주는게 나을 것 같아서 그냥 안만들었습니다.
      stomp.disconnect();
    } catch (error) {
      console.error(error);
    }
  };

  const overRequestDto = {
    roomId: roomId,
    lastWord: targetWord,
    score: score,
  };

  // 사라짐...
  // const handleOutRoom = async (statusRequestDto) => { // 위에 dto있음!
  //   try {
  //     const res = await outRoom(statusRequestDto);
  //     const OverResponseDto = res.data.data.OverResponse;
  //   } catch (error) {
  //     console.error(error);
  //   }
  // };

  const listing = currentWordList
    ?.slice()
    .reverse()
    .map((value, index) => (
      <li
        key={index}
        style={{ lineHeight: "1.5rem" }}
        className={value === targetWord ? "targetWord" : ""}
      >
        {value}+{currentWordList.length - index}
      </li>
    ));
  // console.log(target);

  //   sockJS.onmessage = function (e) {
  //     //   setReceivedData(e.data)
  //     console.log(e.data);
  //   };

  // useEffect(() => {
  //   const interval = setInterval(() => {
  //     if(words.length<20){
  //     // Generate a random word and add it to the list
  //     const timestamp = new Date().getSeconds();
  //     const randomWord = timestamp;
  //     // setWords((prevWords) => [ randomWord,...prevWords]);}
  //     setWords((prevWords) => [...prevWords, randomWord]);}

  //   }, 2000); // Add a random word every 2 seconds}

  //   return () => clearInterval(interval); // Cleanup on unmount
  // }, [words.length]);


  return (
    <>
      <div
        style={{
          textAlign: "center",
          alignItems: "center",
          display: "flex",
          flexDirection: "row",
        }}
      >
        <div style={{ width: "45%" }}>
          <h5>
            player :{playerId} , room :{roomId}
          </h5>
          <div>
            <button
              onClick={(e) => {
                e.preventDefault();
                handleCreate();
                // onClickConnectBtn();
              }}
            >
              1인 게임 입장
            </button>
          </div>
          <h1>
            status: player {playerStatus}, room {roomStatus}
          </h1>
          <h1 style={{ flex: "none", position: "sticky", top: 0 }}>
            score:{score}
          </h1>
          {/* <Menu></Menu> */}
          <div>
            <button
              onClick={(e) => {
                e.preventDefault();
                handleStartGame();
              }}
            >
              게임 시작
            </button>
            <button onClick={handleOverGame}>게임 종료/소켓 종료</button>
          </div>
        </div>

        <div
          className="wordlist"
          style={{
            height: "100vh",
            display: "flex",
            width: "25%",
            flexDirection: "column",
            // alignItems:"center",
            justifyContent: "flex-end",
            textAlign: "start",
          }}
        >
          <ul style={{ listStyle: "none" }}>{listing}</ul>
          <input
            type="text"
            placeholder="입력하세요"
            value={guessWord}
            onChange={handleInputChange}
          ></input>
        </div>

        <div>
          <form
            onKeyDown={(e) => {
              if (e.key === "Enter") e.preventDefault();
              handleInsertWord();
            }}
            // onSubmit={enterWord}
          >
            {/* <button type="submit">버튼</button> */}
          </form>
        </div>
      </div>
    </>
  );
};
