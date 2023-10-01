import React from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "../button/ButtonTest";
import "./Score.css";

function Score({ playerId, playerResultList }) {
  const navigate = useNavigate();

  const handleButtonClickToGO = (path = "/") => {
    console.log("페이지 이동 경로:", path);
    navigate(path);
  };

  const listing = playerResultList?.map((value, index) => (
    <div key={index} className="rank-item" style={{ lineHeight: "1.5rem" }} >
      <div className="rank-index">{index + 1}위</div>
      <div className="rank-score" style={{ textAlign: "right" }} >{value.score}</div>
      <div className="rank-nickname" style={{ textAlign: "right" }} >{value.nickname}</div>
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
          <div
            style={{
              lineHeight: "2rem",
              marginTop: "1rem",
              marginBottom: "1rem",
              border: "0.2rem solid #CCFF00",
              color: "#CCFF00",
              fontSize: "1.5rem",
              wordSpacing: "2rem",
            }}
            className="rank-index"
          >
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
