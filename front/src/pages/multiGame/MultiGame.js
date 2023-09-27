import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { connect, sendMsg, subscribe } from "../../api/stompClient.js";
import Chat from "../../components/chatting/ChatTest";
import { MyGameDisplay } from "./MyGameDisplay.js";
import { PlayersDisplay } from "./PlayersDisplay.js";
import "./GameDisplay.css";

export const MultiGame = () => {
  const [playerId, setPlayerId] = useState(null); // 넘겨 받는 정보니까 useState 나중에 지우기
  const [playerList, setPlayerList] = useState(null);
  const [roomId, setRoomId] = useState(null);
  const [roomStatus, setRoomStatus] = useState(null);
  const [chatContent, setChatContent] = useState("");
  const [currentPlayerData, setCurrentPlayerData] = useState();
  const [otherPlayerData1, setOtherPlayerData1] = useState();
  const [otherPlayerData2,setOtherPlayerData2]=useState();
  const [otherPlayerData3,setOtherPlayerData3]=useState();
  const [wordListResponse, setWordListResponse] = useState();

  const location = useLocation();
  const responseData = location.state?.responseData;

  const navigate = useNavigate();

  useEffect(() => {
    if(!responseData) {
      alert("잘못된 접근입니다. 메인화면에서 [게임 참여]를 통해 접속해주세요.");
      navigate('/');
    } else {
      console.log(responseData.gameInfo.currentPlayerId);
      setPlayerId(responseData.gameInfo.currentPlayerId);
      setPlayerList(responseData.gameInfo.playerList);
      setRoomId(responseData.gameInfo.roomId);
      setRoomStatus(responseData.gameInfo.roomStatus);
    } 
  }, [responseData, navigate]);

  useEffect(() => {
    if (roomId !== null) {
      const enterChat = (messageBody) => {
        const context = messageBody;
        console.log(context);
        setChatContent(context);
      };

      const enterPlayer = (messageBody) => {
        const gameInfo = messageBody;
        console.log(gameInfo);
        setPlayerList(gameInfo.playerList);
        setRoomStatus(gameInfo.roomStatus);
      };

      const startGame = (messageBody) => {
        const startGameInfo = messageBody;
        console.log(startGameInfo);
        setPlayerList(startGameInfo.playerList);
        setRoomStatus(startGameInfo.roomStatus);
        setWordListResponse(startGameInfo.wordListResponse);
      };

      const playerReady = (messageBody) => {
        const playerReadyInfo = messageBody;
      };

      const playerOverInfo = (messageBody) => {
        const playerOverInfo = messageBody;
      };

      const play = (messageBody) => {
        const playInfo = messageBody;
      };

      const gameEndInfo = (messageBody) => {
        const gameEndInfo = messageBody;
      }

      const levelWordInfo = (messageBody) => {
        const toTwoD = [messageBody, ""];
        // setLevelWord((prev) => [...prev, toTwoD]);
      };

      const errorInfo = (messageBody) => {
        const errorInfo = messageBody;
      }

      connect().then(() => {
        subscribe(`/topic/multi/chat/${roomId}`, enterChat);
        subscribe(`/topic/multi/${roomId}`, enterPlayer);
        subscribe(`/topic/multi/start/${roomId}`, startGame);
        subscribe(`/topic/multi/player-ready/${roomId}`, playerReady);
        subscribe(`/topic/multi/player-over/${roomId}`, playerOverInfo);
        subscribe(`/topic/multi/end/${roomId}`, gameEndInfo);
        subscribe(`/topic/multi/play/${roomId}`, play);
        subscribe(`/topic/room/level-word/${roomId}`, levelWordInfo);
        subscribe(`topic/muitl/error/${roomId}/${playerId}`,errorInfo);
      });
    }
  }, [roomId]);

  const handleSendMessage = (inputText) => {
    const body = { playerId: playerId, content: inputText };
    sendMsg(`/app/multi/chat/${roomId}`, body);
  };

  const handleStartGame = () => {
    const body = { playerId: playerId };
    sendMsg(`/app/multi/start/${roomId}`,body);
  }

  const handlePlayerReady = () => {
    const body = { playerId : playerId };
    sendMsg(`/app/multi/player-ready/${roomId}`,body);
  }
  
  const updatePlayerToOver = () => {
    const body = { playerId : playerId };
    sendMsg(`/app/multi/player-over/${roomId}`,body);
  }

  const insertWord = (current,) => {
    const body = { playerId : playerId,
                  //  "currentWordList" : currentWordList,
                  //  "guessWord" : guessWord,
                  //  "targetWord" : targetWord
                  };
    sendMsg(`/app/multi/play/${roomId}`,body);
  }

  
  useEffect(() => {
    // setPlayerId("5e532d13-ea01-47f1-8229-a12e2d9e01ae");
    // setRoomId("c1a55567-4d83-40b5-85b2-e50f047fdcc4");
    // setRoomStatus("PREPARING");
    // setPlayerList([
    //   {
    //     playerId: "5e532d13-ea01-47f1-8229-a12e2d9e01ae",
    //     playerStatus: "READY",
    //     score: 0,
    //     targetWordIndex: 0,
    //     subWordIndex: 9,
    //     nickname: "ㅎㅇㅎㅇ",
    //     isMaster: true,
    //     overTime: null
    //   },
    //   {
    //     playerId: "playerId1",
    //     playerStatus: "READY",
    //     score: 0,
    //     targetWordIndex: 0,
    //     subWordIndex: 0,
    //     nickname: "nickname1",
    //     isMaster: false,
    //     overTime: null
    //   },
    //   {
    //     playerId: "playerId2",
    //     playerStatus: "UNREADY",
    //     score: 0,
    //     targetWordIndex: 0,
    //     subWordIndex: 0,
    //     nickname: "nickname2",
    //     isMaster: false,
    //     overTime: null
    //   }
      // ,
      // {
      //   playerId: "playerId3",
      //   playerStatus: "GAMING",
      //   score: 0,
      //   targetWordIndex: 0,
      //   subWordIndex: 0,
      //   nickname: "nickname3",
      //   isMaster: false,
      //   overTime: null
      // },
    // ]);
  }, []);

  useEffect(() => {
    // props로 넘겨받는 playerId와 playerList
    if (playerId && playerList) {
      const otherPlayers = playerList.filter(player => player.playerId !== playerId);
      setCurrentPlayerData(playerList.find(player => player.playerId === playerId));
      setOtherPlayerData1(otherPlayers[0] || null);
      setOtherPlayerData2(otherPlayers[1] || null);
      setOtherPlayerData3(otherPlayers[2] || null);
    }
  }, [playerId, playerList]);


  return (
    <div className = "multi-display">
        <div className ="my-display">
          {currentPlayerData && <MyGameDisplay data={currentPlayerData} roomStatus={roomStatus} handleStartGame={handleStartGame} handlePlayerReady={handlePlayerReady}  wordListResponse={wordListResponse} insertWord={insertWord}/>}
        </div>
        <div className ="others">
          <div className = "others-display">
            <PlayersDisplay data={otherPlayerData1} roomStatus={roomStatus} wordListResponse={wordListResponse}/>
            <PlayersDisplay data={otherPlayerData2} roomStatus={roomStatus} wordListResponse={wordListResponse}/>
            <PlayersDisplay data={otherPlayerData3} roomStatus={roomStatus} wordListResponse={wordListResponse}/>
          </div>
          <div>
            <Chat
              className="chat-container"
              onSendMessage={handleSendMessage}
              chatContent={chatContent}
              playerList={playerList}
              playerId={playerId}
            ></Chat>
          </div>
        </div>
      </div>
  );
};
