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
                // roomStatus !== "PREPARED" &&
                // roomStatus !== "PREPARING" &&
                // roomStatus !== null &&
                data.score}
            </>
          )}
          <div className="overlaybox"></div>
          {/* <ul className="indexlist">{listing}</ul>
          <ul className="wordlist">{renderWordList(currentWordList)}</ul> */}

          {/* <input
            className="guessbox Neo" */}
          {/* // value={lastGuess} disabled> */}
          {/* ></input> */}
          <input
            className="inputcase Neo"
            type="text"
            placeholder="언니!! 다른플레이어화면에는 여기에 닉네임 나오게할거야!! "
            // value={guessWord}
            // onChange={handleInputChange}
            // onKeyDown={(e) => {
            //   if (e.key === "Enter") {
            //     e.preventDefault();
            //     handleInsertWord();
            //   }
            // }}
          ></input>
        </div>
      </div>
    </div>
  );
};
