import React, { useEffect, useState} from "react";
import { Outlet } from "react-router-dom";
import { connect, disconnect ,sendMsg, subscribe } from "../../api/stompClient.js";

export const MultiGame = () => {
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

      connect();


      const enterPlayer = (messageBody) => {
        const content = JSON.parse(messageBody);
        const gameInfo = content.gameInfo;
        console.log(gameInfo.roomId);
      };

      subscribe(`/topic/multi/${roomId}`, enterPlayer);

      const enterChat = (messageBody) => {
        const content = JSON.parse(messageBody);
      };

      subscribe(`/topic/multi/chat/${roomId}`, enterChat);

      const startGame = (messageBody) => {
        const startGameInfo = JSON.parse(messageBody);
        console.log(startGameInfo.roomId);
      };

      subscribe(`/topic/multi/start/${roomId}`, startGame);

      const playerReady = (messageBody) => {
        const playerReadyInfo = JSON.parse(messageBody);
      };

      subscribe(`/topic/multi/player-ready/${roomId}`, playerReady);

      const playerOverInfo = (messageBody) => {
        const playerOverInfo = JSON.parse(messageBody);
      };

      subscribe(`/topic/multi/player-ready/${roomId}`, playerOverInfo);

      const play = (messageBody) => {
        const playInfo = JSON.parse(messageBody);
      };

      subscribe(`/topic/multi/play/${roomId}`, play);

      const chat = (messageBody) => {
        const chatMessage = JSON.parse(messageBody);
      };

      subscribe(`/topic/multi/play/${roomId}`, chat);
      

    // return () => {
    //   if (subscription) {
    //     subscription.unsubscribe();
    // }
    
    // if (roomId !== null) {
    //     disconnect();
    // }
    // };
  });


  const startGame = () => {
    const body = { "playerId": playerId };
    sendMsg(`/app/multi/start/${roomId}`,body);
  }

  const updatePlayerToReady = () => {
    const body = { "playerId": playerId };
    sendMsg(`/app/multi/player-ready/${roomId}`,body);
  }
  
  const updatePlayerToOver = () => {
    const body = { "playerId": playerId };
    sendMsg(`/app/multi/player-over/${roomId}`,body);
  }

  const insertWord = () => {
    const body = { "playerId": playerId,
                   "currentWordList" : currentWordList,
                   "guessWord" : guessWord,
                   "targetWord" : targetWord
                  };
    sendMsg(`/app/multi/play/${roomId}`,body);
  }

  const insertChatMessage = () => {
    const body = { "playerId": playerId,
                   "content" : "채팅메시징"
                  };
    sendMsg(`/app/multi/play/${roomId}`,body);
  }
  
  useEffect(() => {
    setPlayerId("360cafd7-e56b-4c19-bf96-2181cc81c07e")
    setRoomId("f0eb6157-6ee9-4652-857e-30299f2ac5ef");
  },[]);
    
  return (
    <div>
      <button onClick={() => startGame()}></button>
      <Outlet />
    </div>
  )  
}; 