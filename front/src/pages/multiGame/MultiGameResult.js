import React from "react";

import { useLocation } from "react-router-dom";
import TeamScore from "../../components/list/TeamScore";
import TodayNew from "../../components/list/TodayNew";

export const MultiGameResult = () => {
  const location = useLocation();

  const lastWord = location.state?.lastWord;
  const playerId = location.state?.playerId;
  const playerResultList = location.state?.playerResultList;
  return (
    <div>
      <TeamScore
        playerId={playerId}
        playerResultList={playerResultList}
      ></TeamScore>
      <TodayNew lastWord={lastWord}></TodayNew>
    </div>
  );
};