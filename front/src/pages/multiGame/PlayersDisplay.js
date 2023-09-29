import React, { useEffect, useState } from "react";
import "./GameDisplay.css";

export const PlayersDisplay = ({ data, roomStatus }) => {
  return (
    <div className="gamecontainer" style={{}}>
      <div className="bglist">
        <div className="multi-score">
          {!data || data === null ? (
            <div>EMPTY</div>
          ) : (
            <>
              {data &&
                roomStatus !== null &&
                roomStatus !== "ONGOING" &&
                roomStatus !== "FINISHED" &&
                data.playerStatus === "READY" && <div>READY</div>}
              {data &&
                roomStatus !== null &&
                roomStatus !== "ONGOING" &&
                roomStatus !== "FINISHED" &&
                !data.isMaster &&
                data.playerStatus === "UNREADY" && <div>UNREADY</div>}
              {data &&
                data.playerStatus === "GAMING" &&
                roomStatus !== "PREPARED" &&
                roomStatus !== "PREPARING" &&
                roomStatus !== null &&
                data.score}
            </>
          )}
        </div>
        <div className="overlaybox-player"></div>
        <div className="inputcase-playerName">{data?.nickname}</div>
        {/* <div className="inputcase-playerName">mickname영역</div> */}
      </div>
    </div>
  );
};
