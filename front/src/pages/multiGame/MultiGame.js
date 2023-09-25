import React, { useEffect, useState } from "react";
import {
  connect,
  sendMsg,
  subscribe
} from "../../api/stompClient.js";
import Chat from "../../components/chatting/ChatTest";

export const MultiGame = () => {
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
        console.log(context);
        setChatContent(context);
      };

      connect().then(() => {
        subscribe(`/topic/multi/chat/${roomId}`, enterChat);
      });
    }
  }, [roomId]);


  const handleSendMessage = (inputText) => {
    const body = { playerId: playerId, content: inputText };
    sendMsg(`/app/multi/chat/${roomId}`, body);
  };

  useEffect(() => {
    setPlayerId("360cafd7-e56b-4c19-bf96-2181cc81c07e");
    setRoomId("f0eb6157-6ee9-4652-857e-30299f2ac5ef");
    setPlayerList([
      {
        "playerId": "360cafd7-e56b-4c19-bf96-2181cc81c07e",
        "playerStatus": "GAMING",
        "score": 0,
        "targetWordIndex": 0,
        "subWordIndex": 0,
        "nickname": "nickname0",
        "isMaster": true,
        "overTime": null
      },
      {
        "playerId": "playerId1",
        "playerStatus": "GAMING",
        "score": 0,
        "targetWordIndex": 0,
        "subWordIndex": 0,
        "nickname": "nickname1",
        "isMaster": false,
        "overTime": null
      },
      {
        "playerId": "playerId2",
        "playerStatus": "GAMING",
        "score": 0,
        "targetWordIndex": 0,
        "subWordIndex": 0,
        "nickname": "nickname2",
        "isMaster": false,
        "overTime": null
      },
      {
        "playerId": "playerId3",
        "playerStatus": "GAMING",
        "score": 0,
        "targetWordIndex": 0,
        "subWordIndex": 0,
        "nickname": "nickname3",
        "isMaster": false,
        "overTime": null
      }
    ])
    // setPlayerId("ohohohooh");
    console.log("Ddd");
    // connect();
  }, []);

  return (
    <div>
      <button onClick={() => setRoomId("f0eb6157-6ee9-4652-857e-30299f2ac5ef")}>fdgfgffgf</button>
      <Chat
        onSendMessage={handleSendMessage}
        chatContent={chatContent}
        playerList={playerList}
        playerId={playerId}
      ></Chat>
    </div>
  );
};
