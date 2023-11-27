import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import {
  connect,
  sendMsg,
  disconnect,
  subscribe,
} from "../../api/stompClient2.js";
import Chat from "../../components/chatting/ChatTest";

export const TestSocket = () => {
  const location = useLocation();
  const responseData = location.state?.responseData;

  // const [streak, setStreak] = useState(null);
  // const [nickname, setNickname] = useState(null);
  const [playerId, setPlayerId] = useState(null);
  const [roomId, setRoomId] = useState(null);
  const [playerList, setPlayerList] = useState([]);
  const [chatContent, setChatContent] = useState("");

  useEffect(() => {
    if (roomId !== null) {
      const enterChat = (messageBody) => {
        const context = messageBody;
        //console.log(context);
        setChatContent(context);
      };

      connect("MULTI", roomId, playerId).then(() => {
        subscribe(`/topic/multi/chat/${roomId}`, enterChat);
        subscribe(`/topic/multi/start/${roomId}`, enterChat);
        subscribe(`/topic/multi/player-ready/${roomId}`, enterChat);
        subscribe(`/topic/multi/player-over/${roomId}`, enterChat);
        subscribe(`/topic/multi/end/${roomId}`, enterChat);
        subscribe(`/topic/multi/${roomId}`, enterChat);
      });

      // Cleanup funciton
      return () => {
        disconnect();
      };
    }
  }, [roomId, playerId]);

  const joinChatRoom = () => {
    setRoomId(responseData.gameInfo.roomId);
    setPlayerId(responseData.gameInfo.currentPlayerId);
  };

  const startGame = () => {
    const body = { playerId: playerId };
    sendMsg(`/app/multi/start/${roomId}`, body);
  };

  const updatePlayerStatusOver = () => {
    const body = { playerId: playerId };
    sendMsg(`/app/multi/player-over/${roomId}`, body);
  };

  const handleSendMessage = (inputText) => {
    const body = { playerId: playerId, content: inputText };
    sendMsg(`/app/multi/chat/${roomId}`, body);
  };

  return (
    <div>
      <button onClick={joinChatRoom}>소켓 연결</button>
      <button onClick={startGame}>게임 시작</button>
      <Chat
        onSendMessage={handleSendMessage}
        chatContent={chatContent}
        playerList={playerList}
        playerId={playerId}
      ></Chat>
    </div>
  );
};
