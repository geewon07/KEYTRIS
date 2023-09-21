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


      const enterPlayerInfo = (messageBody) => {
        const content = JSON.parse(messageBody);
        const gameInfo = content.gameInfo;
        console.log(gameInfo.roomId);
      };

      subscribe(`/topic/multi/${roomId}`, enterPlayerInfo);

      const startGameInfo = (messageBody) => {
        const content = JSON.parse(messageBody);
        const gameInfo = content.gameInfo;
        console.log(gameInfo.roomId);
      };

      subscribe(`/topic/multi/start/${roomId}`, startGameInfo);
      

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