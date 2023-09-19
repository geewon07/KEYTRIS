import React, { useEffect, useState } from "react";
import axios from "axios";
import { Outlet } from "react-router-dom";
import { 
  startGame,
  insertWord,
  overGame,
  outRoom
} from "../../api/singleGame/singleGameApi.js";

export const SingleGame = () => {
  const [newComment, setNewComment] = useState(null);

  useEffect(() => {
  const handleStartGame = async (statusRequestDto) => {
    try {
      const res = await startGame(statusRequestDto);
      const newCommentData = res.data.data.StartResponse;
      console.log(newCommentData);
      setNewComment(newCommentData);
    } catch (error) {
      console.error(error);
    }
  };
  
  // API 호출 예시
  // handleStartGame({
  //   playerId: "53c3af6c-3a91-47a5-851e-4c68b2af27e8",
  //   playerStatus : "UNREADY",
  //   roomId : "80355a36-fc5b-4e35-b9cf-ff669595b270",
  //   roomStatus : "PREPARING"
  // });

  const handleInsertWord = async (insertRequestDto) => {
    try {
      const res = await insertWord(insertRequestDto);
      const newCommentData = res.data.data.StartResponse;
      console.log(newCommentData);
      setNewComment(newCommentData);
    } catch (error) {
      console.error(error);
    }
  };
  
  // API 호출 예시
  handleInsertWord({
    roomId : "80355a36-fc5b-4e35-b9cf-ff669595b270",
    currentWordList : ["가방", "강아지", "고양이","수도"],
    guessWord : "과자",
    targetWord : "수도" 
    });


}, []);



  return (
    <div>
      {newComment && (
        <div>
          <p></p>
        </div>
      )}
      <Outlet />
    </div>
  );
};
