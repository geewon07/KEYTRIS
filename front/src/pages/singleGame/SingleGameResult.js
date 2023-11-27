import React from "react";
import { useLocation } from "react-router-dom";
import Score from "../../components/list/Score";
import TodayNew from "../../components/list/TodayNew";
import '../../components/list/Score.css';

export const SingleGameResult = () => {

  const location = useLocation();
  const overRequestDto = location.state?.overRequestDto;
  const lastWord = overRequestDto?.lastWord[0][0];
  
  return (
    <div className="total" >
      <Score overRequestDto={ overRequestDto }></Score>
      <TodayNew lastWord={ lastWord }></TodayNew>
    </div>
  );
};
