import { Stomp } from "@stomp/stompjs";
import axios from "axios";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { DeleteAnimation } from "../../components/game/DeleteAnimation";
import "./SingleGame.css";
import { Button } from "react-bootstrap";
import { SortAnimation } from "../../components/game/SortAnimation";
import { AddAnimation } from "../../components/game/AddAnimation";
import { AddWordAnimation } from "../../components/game/AddWordAnimation";
export const SingleGameTest = () => {
  const [display, setDisplay] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [sorting, setSorting] = useState(false);
  const [adding, setAdding] = useState(false);
  const [count, setCount] = useState(0);
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
  ];
  const addList = [];
  const bufferList = [
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
  ];
  const handleAdd = () => {
    // setWordList((prev)=>[...prev,addList[count]]);
    wordList.push(bufferList[count]);
    setCount((prev) => prev + 1);
  };
  const reverseIndex = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0];
  const [dTop, setDTop] = useState([["물", ""]]);
  const [dBottom, setDBottom] = useState([]);
  const [sendList, setSendList] = useState(wordList);
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
          {/* <div className="displaylayer2"></div> */}

          {display && (
            <div className="displaylayer ">
              {adding && (
                <AddWordAnimation
                  wordsToAdd={addList}
                  targetWord="집"
                ></AddWordAnimation>
                // <AddAnimation
                //   wordsToAdd={addList}
                //   targetWord="집"
                // ></AddAnimation>
              )}
              <ul className="wordlist">
                {sorting && (
                  <SortAnimation
                    sendList={sendList.reverse()}
                    sortedIndex={reverseIndex}
                  ></SortAnimation>
                )}
                {deleting && (
                  <DeleteAnimation
                    initialList={deleteList.reverse()}
                    targetIndex={2}
                  ></DeleteAnimation>
                )}
              </ul>
            </div>
          )}
          <div className="bglist">
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
        <div style={{ width: "300px" }}>
          <Button
            size="lg"
            onClick={() => {
              setDisplay((prev) => !prev);
            }}
          >
            레이어 토글
          </Button>
          <br />
          <Button
            size="lg"
            onClick={() => {
              setDeleting(true);
              setTimeout(() => setDeleting(false), 300);
            }}
          >
            삭제 모션
          </Button>
          <br />
          <Button
            size="lg"
            onClick={() => {
              setSorting((prev) => !prev);
            }}
          >
            정렬 모션
          </Button>
          <br />
          <Button
            size="lg"
            onClick={() => {
              setAdding((prev) => !prev);
            }}
          >
            추가 토글 {adding.toString()}
          </Button>
          <br />
          <Button
            size="lg"
            onClick={() => {
              handleAdd();
            }}
          >
            추가 모션
          </Button>
        </div>
      </div>
    </>
  );
};
