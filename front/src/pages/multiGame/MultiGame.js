import React, { useEffect, useState, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { connect, sendMsg, subscribe } from "../../api/stompClient.js";
import Chat from "../../components/chatting/ChatTest";
import { MyGameDisplay } from "./MyGameDisplay.js";
import { PlayersDisplay } from "./PlayersDisplay.js";
import "./MultiGame.css";

export const MultiGame = () => {
  const [playerId, setPlayerId] = useState(null); // 넘겨 받는 정보니까 useState 나중에 지우기
  const [playerList, setPlayerList] = useState(null);
  const [roomId, setRoomId] = useState(null);
  const [roomStatus, setRoomStatus] = useState(null);
  const [chatContent, setChatContent] = useState("");
  const [currentPlayerData, setCurrentPlayerData] = useState(null);
  const [otherPlayerData1, setOtherPlayerData1] = useState(null);
  const [otherPlayerData2, setOtherPlayerData2] = useState(null);
  const [otherPlayerData3, setOtherPlayerData3] = useState(null);
  const [wordListResponse, setWordListResponse] = useState(null);
  const [newLevelWord, setNewLevelWord] = useState(null);
  const [currentPlayerGameInfo, setCurrentPlayerGameInfo] = useState(null);
  const [otherPlayerGame1, setOtherPlayerGame1] = useState(null);
  const [otherPlayerGame2, setOtherPlayerGame2] = useState(null);
  const [otherPlayerGame3, setOtherPlayerGame3] = useState(null);

  const playRef = useRef();
  const playReadyRef = useRef();
  const playOverRef = useRef();

  const location = useLocation();
  const responseData = location.state?.responseData;

  const navigate = useNavigate();

  useEffect(() => {
    if (!responseData || responseData === null) {
      alert("잘못된 접근입니다. 메인화면에서 [게임 참여]를 통해 접속해주세요.");
      navigate("/");
    } else {
      setPlayerId(responseData.gameInfo.currentPlayerId);
      setPlayerList(responseData.gameInfo.playerList);
      setRoomId(responseData.gameInfo.roomId);
      setRoomStatus(responseData.gameInfo.roomStatus);
    }
  }, [responseData, navigate]);

  playRef.current = (messageBody) => {
    const playInfo = messageBody;
    const currentPlayerId = playInfo.playerId;

    if (currentPlayerData?.playerId === currentPlayerId) {
      setCurrentPlayerGameInfo(playInfo);
    } else {
      setOtherPlayerData1((prevOtherPlayerData1) => {
        if (prevOtherPlayerData1?.playerId === currentPlayerId) {
          setOtherPlayerGame1(playInfo);
          return prevOtherPlayerData1;
        } else {
          return prevOtherPlayerData1;
        }
      });
      setOtherPlayerData2((prevOtherPlayerData2) => {
        if (prevOtherPlayerData2?.playerId === currentPlayerId) {
          setOtherPlayerGame2(playInfo);
          return prevOtherPlayerData2;
        } else {
          return prevOtherPlayerData2;
        }
      });
      setOtherPlayerData3((prevOtherPlayerData3) => {
        if (prevOtherPlayerData3?.playerId === currentPlayerId) {
          setOtherPlayerGame3(playInfo);
          return prevOtherPlayerData3;
        } else {
          return prevOtherPlayerData3;
        }
      });
    }
  };

  playReadyRef.current = (playerReadyInfo) => {
    if (roomStatus !== playerReadyInfo.roomStatus) {
      setRoomStatus(playerReadyInfo.roomStatus);
    }
    const playerUpdateInfo = playerReadyInfo.player;
    const currentPlayerId = playerUpdateInfo.playerId;

    if (currentPlayerData?.playerId === currentPlayerId) {
      setCurrentPlayerData(playerUpdateInfo);
    } else {
      setOtherPlayerData1((prevOtherPlayerData1) => {
        if (prevOtherPlayerData1?.playerId === currentPlayerId) {
          console.log(prevOtherPlayerData1);
          return playerUpdateInfo;
        } else {
          return prevOtherPlayerData1;
        }
      });
      setOtherPlayerData2((prevOtherPlayerData2) => {
        if (prevOtherPlayerData2?.playerId === currentPlayerId) {
          return playerUpdateInfo;
        } else {
          return prevOtherPlayerData2;
        }
      });
      setOtherPlayerData3((prevOtherPlayerData3) => {
        if (prevOtherPlayerData3?.playerId === currentPlayerId) {
          return playerUpdateInfo;
        } else {
          return prevOtherPlayerData3;
        }
      });
    }
  };

  playOverRef.current = (playerOverInfo) => {
    if (roomStatus !== playerOverInfo.roomStatus) {
      setRoomStatus(playerOverInfo.roomStatus);
    }
    const playerUpdateInfo = playerOverInfo.player;
    const currentPlayerId = playerUpdateInfo.playerId;

    if (currentPlayerData?.playerId === currentPlayerId) {
      console.log(playerUpdateInfo);
      setCurrentPlayerData(playerUpdateInfo);
    } else {
      setOtherPlayerData1((prevOtherPlayerData1) => {
        if (prevOtherPlayerData1?.playerId === currentPlayerId) {
          return playerUpdateInfo;
        } else {
          return prevOtherPlayerData1;
        }
      });
      setOtherPlayerData2((prevOtherPlayerData2) => {
        if (prevOtherPlayerData2?.playerId === currentPlayerId) {
          return playerUpdateInfo;
        } else {
          return prevOtherPlayerData2;
        }
      });
      setOtherPlayerData3((prevOtherPlayerData3) => {
        if (prevOtherPlayerData3?.playerId === currentPlayerId) {
          return playerUpdateInfo;
        } else {
          return prevOtherPlayerData3;
        }
      });
    }
  };

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
      const gameEndInfo = (messageBody) => {
        const gameEndInfo = messageBody;
      };

      const levelWordInfo = (messageBody) => {
        console.log(messageBody);
        setNewLevelWord(messageBody);
      };

      const errorInfo = (messageBody) => {
        const errorInfo = messageBody;
      };

      connect().then(() => {
        subscribe(`/topic/multi/chat/${roomId}`, enterChat);
        subscribe(`/topic/multi/${roomId}`, enterPlayer);
        subscribe(`/topic/multi/start/${roomId}`, startGame);
        subscribe(`/topic/multi/player-ready/${roomId}`, playReadyRef.current);
        subscribe(`/topic/multi/player-over/${roomId}`, playOverRef.current);
        subscribe(`/topic/multi/end/${roomId}`, gameEndInfo);
        subscribe(`/topic/multi/play/${roomId}`, playRef.current);
        subscribe(`/topic/multi/level-word/${roomId}`, levelWordInfo);
        subscribe(`topic/muitl/error/${roomId}/${playerId}`, errorInfo);
      });
    }
  }, [roomId]);

  const handleSendMessage = (inputText) => {
    const body = { playerId: playerId, content: inputText };
    sendMsg(`/app/multi/chat/${roomId}`, body);
  };

  const handleStartGame = () => {
    const body = { playerId: playerId };
    sendMsg(`/app/multi/start/${roomId}`, body);
  };

  const handlePlayerReady = () => {
    const body = { playerId: playerId };
    sendMsg(`/app/multi/player-ready/${roomId}`, body);
  };

  const updatePlayerToOver = () => {
    const body = { playerId: playerId };
    sendMsg(`/app/multi/player-over/${roomId}`, body);
  };

  const insertWord = (insertRequestDto) => {
    const body = {
      playerId: playerId,
      currentWordList: insertRequestDto.currentWordList,
      guessWord: insertRequestDto.guessWord,
      targetWord: insertRequestDto.targetWord,
    };
    sendMsg(`/app/multi/play/${roomId}`, body);
  };

  // useEffect(() => {
  //   setPlayerId("5e532d13-ea01-47f1-8229-a12e2d9e01ae");
  //   setRoomId("c1a55567-4d83-40b5-85b2-e50f047fdcc4");
  //   setRoomStatus("PREPARING");
  //   setPlayerList([
  //     {
  //       playerId: "5e532d13-ea01-47f1-8229-a12e2d9e01ae",
  //       playerStatus: "OVER",
  //       score: 0,
  //       targetWordIndex: 0,
  //       subWordIndex: 9,
  //       nickname: "ㅎㅇㅎㅇ",
  //       isMaster: true,
  //       overTime: null,
  //     },
  //     {
  //       playerId: "playerId1",
  //       playerStatus: "READY",
  //       score: 0,
  //       targetWordIndex: 0,
  //       subWordIndex: 0,
  //       nickname: "nickname1",
  //       isMaster: false,
  //       overTime: null,
  //     },
  //     {
  //       playerId: "playerId2",
  //       playerStatus: "UNREADY",
  //       score: 0,
  //       targetWordIndex: 0,
  //       subWordIndex: 0,
  //       nickname: "nickname2",
  //       isMaster: false,
  //       overTime: null,
  //     },
  //     // {
  //     //   playerId: "playerId3",
  //     //   playerStatus: "GAMING",
  //     //   score: 0,
  //     //   targetWordIndex: 0,
  //     //   subWordIndex: 0,
  //     //   nickname: "nickname3",
  //     //   isMaster: false,
  //     //   overTime: null,
  //     // },
  //   ]);
  // }, []);

  useEffect(() => {
    console.log("dd");
    // props로 넘겨받는 playerId와 playerList
    if (playerId && playerList) {
      console.log("dddd");
      console.log(playerList);
      console.log(playerId);
      const me = playerList.find((player) => player.playerId === playerId);
      console.log("힣");
      console.log(playerList.find((player) => player.playerId === playerId));
      const otherPlayers = playerList.filter(
        (player) => player.playerId !== playerId
      );
      console.log(otherPlayers);
      console.log(otherPlayers[0]);
      setCurrentPlayerData(me);
      setOtherPlayerData1(otherPlayers[0] || null);
      setOtherPlayerData2(otherPlayers[1] || null);
      setOtherPlayerData3(otherPlayers[2] || null);
    }
  }, [playerId, playerList]);

  return (
    <div className="multi-display">
      <div className="my-display">
        {currentPlayerData && (
          <MyGameDisplay
            data={currentPlayerData}
            roomStatus={roomStatus}
            handleStartGame={handleStartGame}
            handlePlayerReady={handlePlayerReady}
            wordListResponse={wordListResponse}
            insertWord={insertWord}
            currentPlayerGameInfo={currentPlayerGameInfo}
            newLevelWord={newLevelWord}
            updatePlayerToOver={updatePlayerToOver}
          />
        )}
      </div>
      <div className="others">
        <div className="others-display">
          <PlayersDisplay
            data={otherPlayerData1}
            roomStatus={roomStatus}
            wordListResponse={wordListResponse}
            otherPlayerGame1={otherPlayerGame1}
            newLevelWord={newLevelWord}
          />
          <PlayersDisplay
            data={otherPlayerData2}
            roomStatus={roomStatus}
            wordListResponse={wordListResponse}
            otherPlayerGame2={otherPlayerGame2}
            newLevelWord={newLevelWord}
          />
          <PlayersDisplay
            data={otherPlayerData3}
            roomStatus={roomStatus}
            wordListResponse={wordListResponse}
            otherPlayerGame3={otherPlayerGame3}
            newLevelWord={newLevelWord}
          />
        </div>
        <div className="chat-container">
          <Chat
            onSendMessage={handleSendMessage}
            chatContent={chatContent}
            playerList={playerList}
            newLevelWord={newLevelWord}
            myplayerId={playerId}
          ></Chat>
        </div>
      </div>
    </div>
  );
};
