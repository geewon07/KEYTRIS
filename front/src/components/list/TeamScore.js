import React from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "../button/ButtonTest";
import "./Score.css";

function Score() {
  const playerId = "playerId0";
  const playerResultList = [
    {
      playerId: "playerId0",
      nickname: "nickname0",
      score: 50,
    },
    {
      playerId: "playerId1",
      nickname: "nickname1",
      score: 30,
    },
    {
      playerId: "playerId2",
      nickname: "nickname2",
      score: 10,
    },
    {
      playerId: "playerId3",
      nickname: "nickname3",
      score: 1,
    },
  ];

  const navigate = useNavigate();

  const handleButtonClickToGO = (path = "/") => {
    console.log("페이지 이동 경로:", path);
    navigate(path);
  };

  const listing = playerResultList?.map((value, index) => (
    <div key={index} style={{ lineHeight: "1.5rem" }} className="rank-index">
      {index + 1}위&nbsp;&nbsp;{value.score}&nbsp;&nbsp;
      {value.nickname}
    </div>
  ));

  const currentPlayerIndex = playerResultList.findIndex(
    (player) => player.playerId === playerId
  );

  const currentPlayer = playerResultList[currentPlayerIndex];

  return (
    <>
      <div className="rank-container">
        <div className="list-title">랭킹</div>
        <hr />
        {playerResultList && <div className="rank-list">{listing}</div>}
        {currentPlayer && (
          <div style={{ lineHeight: "1.5rem" }} className="rank-index">
            {currentPlayerIndex + 1}위&nbsp;&nbsp;&nbsp;{currentPlayer.score}
            &nbsp;&nbsp;
            {currentPlayer.nickname}
          </div>
        )}

        <Button
          label="메인으로"
          onClick={() => handleButtonClickToGO("/")}
        ></Button>
      </div>
    </>
  );
}

export default Score;
