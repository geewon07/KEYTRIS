import React from "react";
import { useLocation } from "react-router-dom";
import Score from "../../components/list/Score";
import TodayNew from "../../components/list/TodayNew";

export const SingleGameResult = () => {

  const location = useLocation();
  const overRequestDto = location.state?.overRequestDto;
  console.log(overRequestDto);

  return (
    <div className="total" >
      <Score overRequestDto={ overRequestDto }></Score>
      <TodayNew></TodayNew>
    </div>
  );
};
