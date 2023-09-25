import { Stomp } from "@stomp/stompjs";
import axios from "axios";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { DeleteAnimation } from "../../components/game/DeleteAnimation";
import "./SingleGame.css";
import { Button } from "react-bootstrap";
export const SingleGameTest = () => {
  const [display, setDisplay] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const wordList = [
    ["집", ""],
    ["나무", ""],
    ["해변", ""],
    ["음식", ""],
    ["물", ""],
    ["사과", ""],
    ["바나나", ""],
    ["컴퓨터", ""],
    ["학교", ""],
    ["자동차", ""],
    ["강아지", ""],
    ["고양이", ""],
    ["책", ""],
    ["휴대폰", ""],
    ["친구", ""],
  ];
  const deleteList = [
    ["집", ""],
    ["나무", ""],
    ["해변", ""],
    ["음식", ""],
    ["물", ""],
  ];
  const renderWordList = (list) => {
    return list
      .slice()
      .reverse()
      .map((item, index) => {
        // if (Array.isArray(item)) {
        // This is a 2D array with points
        const [word, point] = item;
        return (
          <li key={deleteList.length - index - 1} className={"wordline"}>
            <div
              className={
                "집" === word ? "targetWord wordline left" : "wordline left"
              }
            >
              {word}
            </div>
            <div className="right points">{point}</div>
          </li>
        );
        // }
      });
  };
  const listIndexStandard = [
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1,
  ];
  const listing = listIndexStandard?.slice().map((value, index) => (
    <li
      key={index}
      className={20 - index - 1 === 9 ? "targetWord wordline" : "wordline"}
    >
      {value}
    </li>
  ));
  return (
    <>
      <div
        style={{
          textAlign: "center",
          alignItems: "center",
          display: "flex",
          flexDirection: "row",
    justifyContent: "center",
        }}
      >
        <div className="gamecontainer" style={{ margin: 0 }}>
       
          <div className="bglist">
          {display && 
          <div className="displaylayer">
              <DeleteAnimation initialList={deleteList.reverse()}></DeleteAnimation>
            </div>
            }
            <div className="score"></div>
            
            <div className="overlaybox"></div>

            <ul className="indexlist">{listing}</ul>
            <ul className="wordlist">{renderWordList(wordList)}</ul>

            <input className="guessbox Neo" disabled></input>
            <input
              className="inputcase Neo"
              type="text"
              placeholder="입력하세요"
              // value={guessWord}
              // onChange={handleInputChange}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  e.preventDefault();
                  // handleInsertWord();
                }
              }}
            ></input>
          </div>
        </div>
        <div>
          <Button
            size="lg"
            onClick={() => {
              setDisplay((prev) => !prev);
            }}
          >
            레이어 토글
          </Button>
          <Button
            size="lg"
            onClick={() => {
              setDeleting((prev) => !prev);
            }}
          >
            삭제 모션
          </Button>
        </div>
      </div>
    </>
  );
};
