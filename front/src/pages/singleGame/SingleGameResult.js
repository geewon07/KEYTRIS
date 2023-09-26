import React from "react";
import Score from "../../components/list/Score";
import TodayNew from "../../components/list/TodayNew";

export const SingleGameResult = () => {

  return (
    <div className="total" >
      <Score></Score>
      <TodayNew></TodayNew>
    </div>
  );
};
