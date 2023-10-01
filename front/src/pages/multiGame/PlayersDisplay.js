import React, { useEffect, useState, useRef } from "react";
import { AddWordAnimation } from "../../components/game/AddWordAnimation";
import { SortAnimation } from "../../components/game/SortAnimation";
import { DeleteAnimation } from "../../components/game/DeleteAnimation";
// import "../singleGame/SingleGame.css";
import "./PlayersGame.css";

export const PlayersDisplay = ({
  data,
  roomStatus,
  wordListResponse,
  otherPlayerGame,
  newLevelWord,
}) => {
  const [subWordList, setSubWordList] = useState([]);
  const [targetWord, setTargetWord] = useState("");

  const [currentWordList, setCurrentWordList] = useState([]);
  const [deleteList, setDeleteList] = useState([]);

  const [sortedWordList, setSortedWordList] = useState([]);

  const [score, setScore] = useState(0);
  const [levelWord, setLevelWord] = useState([]);
  const [targetWordIndex, setTargetWordIndex] = useState(null);

  const [sortedIdx, setSortedIdx] = useState([]);
  const [display, setDisplay] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [sorting, setSorting] = useState(false);
  const [adding, setAdding] = useState(false);

  useEffect(() => {
    if (newLevelWord !== null && data?.playerStatus === "GAMING") {
      const toTwoD = [newLevelWord, ""];
      setLevelWord((prev) => [...prev, toTwoD]);
    }
  }, [newLevelWord, data?.playerStatus]);

  useEffect(() => {
    // levelword 오면 등록되어 바뀜, 바뀌었을때  useEffect 발동,
    // 먼저 모션 레이어를 키고, 전달한 levelword로 모션을 보여줌
    if (levelWord.length > 0) {
      setDisplay(true);
      setAdding(true);
      setTimeout(() => {}, 200);
      //소켓으로
      setTimeout(() => {
        // 타이밍 문제로 중간에 씹힐 수 있음, 타겟단어 또 따로 줄까?
        console.log("add level word");
        console.log(levelWord);
        setCurrentWordList((prev) => [...prev, ...levelWord]);
        setLevelWord([]);
        setDisplay(false);
        setAdding(false);
      }, 300);
      setTimeout(() => {
        // setCurrentWordList((prev) => [...prev, ...levelWord]);
      }, 400);
    }
  }, [levelWord]);

  useEffect(() => {
    if (deleteList.length > 0) {
      setDisplay(true);
      setDeleting(true);
      setTimeout(() => {
        // console.log("delete log how many times");
        const update = [
          ...sortedWordList.slice(0, targetWordIndex),
          ...sortedWordList.slice(4),
          ...currentWordList.slice(sortedWordList.length),
        ];
        setCurrentWordList(update);
      }, 200);

      setTimeout(() => {
        setDisplay(false);
        setDeleting(false);
      }, 300);
    }
  }, [deleteList]);

  useEffect(() => {
    // setTimeout(() => {
    if (sortedWordList.length > 0) {
      // console.log(sortedIdx);
      setDisplay(true);
      // setAdding(false);
      setSorting(true);
      setTimeout(() => {
        // console.log("sort log, set current");
        const update = [
          ...sortedWordList,
          ...currentWordList.slice(sortedWordList.length),
        ];
        setCurrentWordList(update);
        // setCurrentWordList([...sortedWordList,...currentWordList.slice(sortedWordList.length)])
      }, 100);

      setTimeout(() => {
        setDisplay(false);
        setSorting(false);
      }, 500);
    }
    // }, 800);
  }, [sortedWordList]);

  useEffect(() => {
    if (data !== null && data.playerStatus === "GAMING") {
      console.log(wordListResponse.newTargetWord);
      setTargetWord(wordListResponse.newTargetWord);
      setTargetWordIndex(9);
      setCurrentWordList([...wordListResponse.sortedWordList]);
      setScore(wordListResponse.newScore);
    }
  }, [data]);

  const renderWordList = (list) => {
    return list
      .slice()
      .reverse()
      .map((item, index) => {
        // if (Array.isArray(item)) {
        // This is a 2D array with points
        const [word, point] = item;
        return (
          <li key={currentWordList.length - index - 1} className={"m-wordline"}>
            <div
              className={
                targetWord[0][0] === word
                  ? "m-targetWord m-wordline left"
                  : "m-wordline left"
              }
            >
              {word}
            </div>
            <div className="right m-points">{point}</div>
          </li>
        );
        // }
      });
  };
  // index {currentWordList.length - index - 1}
  const listIndexStandard = [
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1,
  ];

  const listing = listIndexStandard?.slice().map((value, index) => (
    <li
      key={index}
      className={
        20 - index - 1 === targetWordIndex
          ? "m-targetWord m-wordline"
          : "m-wordline"
      }
    >
      {value}
    </li>
  ));

  useEffect(() => {
    if (otherPlayerGame && otherPlayerGame !== null) {
      const sortedRes = otherPlayerGame;
      console.log("sorted ");
      console.log(sortedRes);
      const sorted = otherPlayerGame.sortedWordList;
      const {
        newScore,
        newSubWordList,
        newTargetWord,
        sortedIndex,
        targetWordRank,
      } = sortedRes;

      setSortedIdx(sortedIndex); //0 start
      setSortedWordList(sorted);
      setTargetWordIndex(targetWordRank);

      //득점 성공시

      if (newScore === score) {
        return;
      } else {
        setTimeout(() => {
          setDeleteList([...sorted.slice(targetWordRank, 4)]);
          setScore(newScore);
        }, 500);
        setTimeout(() => {
          setTargetWord(newTargetWord);
          // setCurrentWordList((prev)=>[...prev,...newTargetWord]);
          setLevelWord((prev) => [...newTargetWord]);
          // setSubWordList([]);
        }, 1700);
        // setCurrentWordList(update);
      }
      // console.log(sorted);
      //추가 단어 여부와 추가
      setTimeout(() => {
        if (newSubWordList !== null) {
          setSubWordList(...newSubWordList);
        } else {
          setSubWordList(newSubWordList);
        }

        if (newSubWordList && newSubWordList.length > 0) {
          setLevelWord((prev) => [...prev, ...newSubWordList]);
          setSubWordList([]);
        }
      }, 1500);

      //정렬 발동 1.2초 후에

      // handleScoring(sorted, newScore, SortedWordResponseDto);
      //효과를 다 하고 쓰세여~

      // 단어 삭제 모션 있고 난 다음에 변경
    }
  }, [otherPlayerGame]);
  // 점수 나오게 해야함 점수 접근 방법 {socre}
  return (
    <div className="m-gamecontainer" style={{}}>
      <div className="m-bglist">
        <div
          className={
            !data || data === null || data.playerStatus !== "GAMING"
              ? "m-status over"
              : "m-status"
          }
        >
          {!data || data === null ? (
            <div style={{ fontSize: "24px" }}>EMPTY</div>
          ) : (
            <>
              {data &&
                roomStatus !== null &&
                roomStatus !== "ONGOING" &&
                roomStatus !== "FINISHED" &&
                data.playerStatus === "READY" && (
                  <div style={{ fontSize: "24px" }}>READY</div>
                )}
              {data &&
                roomStatus !== null &&
                roomStatus !== "ONGOING" &&
                roomStatus !== "FINISHED" &&
                !data.isMaster &&
                data.playerStatus === "UNREADY" && (
                  <div style={{ fontSize: "24px" }}>UNREADY</div>
                )}
              {(data.playerStatus === "OVER" ||
                data.playerStatus === "GAMING") && (
                <>
                  {data.playerStatus === "OVER" &&
                    roomStatus !== "PREPARED" &&
                    roomStatus !== "PREPARING" &&
                    roomStatus != null && (
                      <div style={{ fontSize: "24px" }}>OVER</div>
                    )}
                  <div className="m-score">{score}</div>
                </>
              )}
            </>
          )}
        </div>

        <div className="overlaybox">
          <li className="m-wordline">&nbsp;</li>
          <li className="m-wordline">&nbsp;</li>
          <li className="m-wordline">&nbsp;</li>
          <li className="m-wordline">&nbsp;</li>
        </div>

        <div className="list-container">
          <ul className="indexlist">
            <div className="m-playerName">{data?.nickname}&nbsp;</div>
            <li
              className={
                currentWordList.length <= 17
                  ? "m-wordline purple"
                  : "m-wordline red"
              }
            >
              &nbsp;
            </li>
            {listing}
          </ul>
          {!display && (
            <ul className="m-wordlist">{renderWordList(currentWordList)}</ul>
          )}
          {display && (
            // <div className="bglist2 ">
            <>
              {adding && (
                <>
                  <AddWordAnimation
                    bufferList={levelWord}
                    targetWord={targetWord}
                  ></AddWordAnimation>
                  {!sorting && (
                    <ul className="m-wordlist">
                      {renderWordList(currentWordList)}
                    </ul>
                  )}
                </>
              )}
              <ul className="m-wordlist">
                {sorting && (
                  <>
                    <SortAnimation
                      sendList={sortedWordList.slice().reverse()}
                      beforeIndex={sortedIdx}
                      targetWord={targetWord}
                    ></SortAnimation>
                  </>
                )}
                {deleting && (
                  <>
                    {
                      <div sytle={{ backgroundColor: "red" }}>
                        {renderWordList(
                          currentWordList.slice(4, currentWordList.length)
                        )}
                      </div>
                    }
                    <DeleteAnimation
                      initialList={currentWordList.slice()}
                      targetIndex={targetWordIndex}
                      targetWord={targetWord}
                    ></DeleteAnimation>
                    {
                      <div sytle={{ backgroundColor: "red" }}>
                        {renderWordList(
                          currentWordList.slice(0, targetWordIndex)
                        )}
                      </div>
                    }
                  </>
                )}
              </ul>
            </>
          )}
        </div>
      </div>
    </div>
  );
};
