import React, { useEffect, useState, useRef } from "react";
import { Button } from "../../components/button/ButtonTest";
import { AddWordAnimation } from "../../components/game/AddWordAnimation";
import { SortAnimation } from "../../components/game/SortAnimation";
import { DeleteAnimation } from "../../components/game/DeleteAnimation";
import "../singleGame/SingleGame.css";
import { toast } from "react-toastify";

export const MyGameDisplay = ({
  data,
  roomStatus,
  handleStartGame,
  handlePlayerReady,
  wordListResponse,
  insertWord,
  currentPlayerGameInfo,
  newLevelWord,
  updatePlayerToOver,
  category,
}) => {
  const [subWordList, setSubWordList] = useState([]);
  const [targetWord, setTargetWord] = useState([]);

  const [guessWord, setGuessWord] = useState("");
  const [lastGuess, setLastGuess] = useState("");
  const [currentWordList, setCurrentWordList] = useState([]);
  const [deleteList, setDeleteList] = useState([]);

  const [sortedWordList, setSortedWordList] = useState([]);

  const [score, setScore] = useState(0);
  const [levelWord, setLevelWord] = useState([]);
  const [sendList, setSendList] = useState([]);
  const [targetWordIndex, setTargetWordIndex] = useState(null);

  const [isTarget, setIsTarget] = useState(false);
  const [sortedIdx, setSortedIdx] = useState([]);
  const [display, setDisplay] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [sorting, setSorting] = useState(false);
  const [adding, setAdding] = useState(false);
  const [isSub, setSub] = useState(false);

  const inputRef = useRef(null);

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
      // setTimeout(() => {}, 200);
      //소켓으로
      setTimeout(() => {
        // 타이밍 문제로 중간에 씹힐 수 있음, 타겟단어 또 따로 줄까?
        console.log("add level word");
        console.log(levelWord);
        setCurrentWordList((prev) => [...prev, ...levelWord]);
        setLevelWord([]);
        setDisplay(false);
        setAdding(false);
      }, 100);
      // setTimeout(() => {
      //   // setCurrentWordList((prev) => [...prev, ...levelWord]);
      // }, 400);
    }
  }, [levelWord]);

  useEffect(() => {
    // levelword 오면 등록되어 바뀜, 바뀌었을때  useEffect 발동,
    // 먼저 모션 레이어를 키고, 전달한 levelword로 모션을 보여줌
    setDisplay(true);
    setIsTarget(true);
    // setAdding(true);
    setTimeout(() => {}, 200);
    //소켓으로
    setTimeout(() => {
      // 타이밍 문제로 중간에 씹힐 수 있음, 타겟단어 또 따로 줄까?
      console.log("add target word");
      console.log(targetWord);

      setCurrentWordList((prev) => [...prev, ...targetWord]);
      setTargetWordIndex(currentWordList.length);
      // setLevelWord([]);
      setDisplay(false);
      setIsTarget(false);
      // setAdding(false);
    }, 200);
    setTimeout(() => {
      // setCurrentWordList((prev) => [...prev, ...levelWord]);
    }, 400);
  }, [targetWord]);

  useEffect(() => {
    // levelword 오면 등록되어 바뀜, 바뀌었을때  useEffect 발동,
    // 먼저 모션 레이어를 키고, 전달한 levelword로 모션을 보여줌
    if (subWordList && subWordList.length < 1) {
    } else {
      setDisplay(true);
      setSub(true);
      // setAdding(true);
      setTimeout(() => {}, 200);
      //소켓으로
      setTimeout(() => {
        // 타이밍 문제로 중간에 씹힐 수 있음, 타겟단어 또 따로 줄까?
        console.log("add sub word");
        console.log(subWordList);

        setDisplay(false);
        setSub(false);
        // setAdding(false);
      }, 200);
      setTimeout(() => {
        // setSubWordList([]);
      }, 400);
    }
  }, [subWordList]);

  useEffect(() => {
    if (!sorting) {
      inputRef.current.focus();
    }
  }, [sorting]);

  useEffect(() => {
    if (deleteList.length > 0) {
      setDisplay(true);
      setTimeout(() => {});
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
        // setDisplay(false);
        setDeleting(false);
      }, 300);
      setTimeout(() => {
        setDisplay(false);
        // setDeleting(false);
      }, 400);
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
      setTargetWord(wordListResponse.newTargetWord);
      setTargetWordIndex(9);
      setCurrentWordList([...wordListResponse.sortedWordList.slice(0, -1)]);
      setScore(wordListResponse.newScore);
    }
  }, [data]);

  const handleInputChange = (e) => {
    const { value } = e.target;
    setGuessWord(value);
  };

  useEffect(() => {
    if (currentWordList.length >= 21) {
      console.log(targetWord);
      updatePlayerToOver();
    }
    // eslint-disable-next-line
  }, [currentWordList]);

  const renderWordList = (list) => {
    return list
      .slice()
      .reverse()
      .map((item, index) => {
        // if (Array.isArray(item)) {
        // This is a 2D array with points
        const [word, point] = item;
        return (
          <li
            key={currentWordList.length - index - 1}
            className={
              targetWord[0][0] === word ? "wordline accent" : "wordline"
            }
          >
            <div
              className={
                targetWord[0][0] === word
                  ? "targetWord wordline left"
                  : "wordline left"
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
  // index {currentWordList.length - index - 1}
  const listIndexStandard = [
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1,
  ];
  const listing = listIndexStandard?.slice().map((value, index) => (
    <li
      key={index}
      className={
        20 - index - 1 === targetWordIndex ? "targetWord wordline" : "wordline"
      }
    >
      {value}
    </li>
  ));

  const insertRequestDto = {
    currentWordList: currentWordList,
    guessWord: guessWord,
    targetWord: targetWord,
  };

  const handleInsertWord = () => {
    if (guessWord === "") {
      toast.error("단어를 입력해주세요.");
      return;
    }

    setLastGuess(guessWord);

    const target = targetWord[0][0];
    if (target.includes(guessWord) || guessWord.includes(target)) {
      toast.error("타겟어에 포함되는 단어를 입력할 수 없습니다.");
      setGuessWord("");
      return;
    }

    const forbiddenWords = {
      100: ["정치"],
      101: ["경제"],
      102: ["사회"],
      103: ["생활", "문화"],
      104: ["세계"],
      105: ["IT", "아이티", "과학"],
    };

    const currentForbiddenWords = forbiddenWords[category];

    if (currentForbiddenWords.includes(guessWord)) {
      toast.error(`뉴스 카테고리인 ${guessWord}은(는) 입력할 수 없습니다.`);
      setGuessWord("");
      return;
    }

    insertWord(insertRequestDto);
    setSendList(insertRequestDto.currentWordList);
    setGuessWord("");
  };

  useEffect(() => {
    if (currentPlayerGameInfo && currentPlayerGameInfo !== null) {
      const sortedRes = currentPlayerGameInfo;
      console.log("sorted ");
      console.log(sortedRes);
      const sorted = currentPlayerGameInfo.sortedWordList;
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

      //득점 성공시!

      if (newScore === score) {
        return;
      } else {
        setTimeout(() => {
          setDeleteList([...sorted.slice(targetWordRank, 4)]);
          setScore(newScore);
        }, 500);
        setTimeout(() => {
          setTargetWord(newTargetWord);
          // setTargetWordIndex(currentWordList.length);
          setCurrentWordList((prev) => [...prev, ...newTargetWord]);
        }, 500);

        // setCurrentWordList(update);
      }
      // console.log(sorted);
      //추가 단어 여부와 추가
      setTimeout(() => {
        if (newSubWordList !== null) {
          setSubWordList(newSubWordList);
        } else {
          setSubWordList(newSubWordList);
        }

        if (newSubWordList && newSubWordList.length > 0) {
          setCurrentWordList((prev) => [...prev, ...newSubWordList]);
          // setSubWordList([]);
        }
      }, 500);

      //정렬 발동 1.2초 후에

      // handleScoring(sorted, newScore, SortedWordResponseDto);
      //효과를 다 하고 쓰세여~

      // 단어 삭제 모션 있고 난 다음에 변경
    }
  }, [currentPlayerGameInfo]);

  return (
    <div>
      <div className="gamecontainer" style={{}}>
        <div className="bglist">
          <div
            className={roomStatus === "FINISHED" ? "m-status over" : "m-status"}
          >
            {roomStatus !== null &&
              roomStatus !== "ONGOING" &&
              roomStatus !== "FINISHED" &&
              data.isMaster && (
                <div
                  className="startbutton"
                  onClick={() => {
                    handleStartGame();
                  }}
                >
                  <Button label="START"></Button>{" "}
                  {/* 시작누를때 서버응답받고 체크해주는 로직 필요 */}
                </div>
              )}
            {roomStatus !== null &&
              roomStatus !== "ONGOING" &&
              roomStatus !== "FINISHED" &&
              !data.isMaster &&
              data.playerStatus === "UNREADY" && (
                <div
                  className="startbutton"
                  onClick={() => {
                    handlePlayerReady();
                  }}
                >
                  <Button label="READY"></Button>{" "}
                  {/* 시작누를때 서버응답받고 체크해주는 로직 필요 */}
                </div>
              )}
            {(data.playerStatus === "OVER" ||
              data.playerStatus === "GAMING") && (
              <>
                {data.playerStatus === "OVER" &&
                  roomStatus !== "PREPARED" &&
                  roomStatus !== "PREPARING" &&
                  roomStatus != null && (
                    <div style={{ fontSize: "48px" }}>GAME OVER</div>
                  )}
                <div className="score">{score}</div>
              </>
            )}
          </div>

          <div className="overlaybox">
            <li className="wordline">&nbsp;</li>
            <li className="wordline">&nbsp;</li>
            <li className="wordline">&nbsp;</li>
            <li className="wordline">&nbsp;</li>
          </div>

          <div className="list-container">
            <ul className="indexlist">
              <li
                className={
                  currentWordList.length <= 17
                    ? "wordline purple"
                    : "wordline red"
                }
              >
                &nbsp;
              </li>
              {listing}
            </ul>
            {!display && (
              <ul className="wordlist" style={{ backgroundColor: "" }}>
                {renderWordList(currentWordList)}
              </ul>
            )}

            {display && (
              // <div className="bglist2 ">
              <>
                {isTarget && (
                  <>
                    <AddWordAnimation
                      bufferList={targetWord}
                      targetWord={targetWord}
                    ></AddWordAnimation>
                    {/* {!sorting && isTarget &&  (
                        <ul className="wordlist dummy" style={{backgroundColor:'blue'}}>
                          {renderWordList(currentWordList)}
                        </ul>
                      )} */}
                  </>
                )}
                {adding && (
                  <>
                    <AddWordAnimation
                      bufferList={levelWord}
                      targetWord={targetWord}
                    ></AddWordAnimation>
                    {!sorting && adding && (
                      <ul
                        className="wordlist dummy"
                        style={{ backgroundColor: "" }}
                      >
                        {renderWordList(currentWordList)}
                      </ul>
                    )}
                  </>
                )}
                {isSub && (
                  <>
                    <AddWordAnimation
                      bufferList={subWordList}
                      targetWord={targetWord}
                    ></AddWordAnimation>
                    {/* {!sorting && isSub && (
                        <ul className="wordlist dummy"  style={{backgroundColor:'blue'}}>
                          {renderWordList(currentWordList)}
                        </ul>
                      )} */}
                  </>
                )}
                <ul className="wordlist">
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
                      {/* {
                        <div sytle={{ backgroundColor: "red" }}>
                          {renderWordList(
                            currentWordList.slice(4, currentWordList.length)
                          )}
                        </div>
                      } */}
                      <DeleteAnimation
                        initialList={currentWordList.slice()}
                        targetIndex={targetWordIndex}
                        targetWord={targetWord}
                      ></DeleteAnimation>
                      {/* {
                        <div sytle={{ backgroundColor: "red" }}>
                          {renderWordList(
                            currentWordList.slice(0, targetWordIndex)
                          )}
                        </div>
                      } */}
                    </>
                  )}
                </ul>
              </>
            )}
          </div>
        </div>

        <input className="guessbox Neo" value={lastGuess} disabled></input>
        <input
          className="inputcase Neo"
          type="text"
          ref={inputRef}
          placeholder="입력하세요"
          value={guessWord}
          onChange={handleInputChange}
          disabled={data.playerStatus === "OVER" || sorting}
          autoFocus
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              e.preventDefault();
              handleInsertWord();
            }
          }}
        ></input>
      </div>
    </div>
  );
};
