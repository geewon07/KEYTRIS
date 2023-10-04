import React, { useState, useEffect, useRef } from "react";
import { toast } from "react-toastify";
import "./SingleGame.css";
import { startGame, insertWord } from "../../api/singleGame/SingleGameApi.js";
import { connect, disconnect, subscribe } from "../../api/stompClient.js";

import { Button } from "../../components/button/ButtonTest";
import { AddWordAnimation } from "../../components/game/AddWordAnimation";
import { SortAnimation } from "../../components/game/SortAnimation";
import { DeleteAnimation } from "../../components/game/DeleteAnimation";
import { useLocation, useNavigate } from "react-router";

export const SingleGame = (props) => {
  const [playerId, setPlayerId] = useState(null);
  const [playerStatus, setPlayerStatus] = useState(null);
  const [roomId, setRoomId] = useState(null);
  const [roomStatus, setRoomStatus] = useState(null);

  const [subWordList, setSubWordList] = useState([]); //levelword
  const [targetWord, setTargetWord] = useState([]);

  const [guessWord, setGuessWord] = useState("");
  const [lastGuess, setLastGuess] = useState("");

  const [currentWordList, setCurrentWordList] = useState([]);

  const [sortedWordList, setSortedWordList] = useState([]);
  const [score, setScore] = useState(0);

  const [targetWordIndex, setTargetWordIndex] = useState(null);

  const [sendList, setSendList] = useState([]);
  const [levelWord, setLevelWord] = useState([]);
  const [deleteList, setDeleteList] = useState([]);
  // motion display ->  모션이 일어날때
  const [sortedIdx, setSortedIdx] = useState([]);
  const [display, setDisplay] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [sorting, setSorting] = useState(false);
  const [adding, setAdding] = useState(false);
  const [isTarget, setIsTarget] = useState(false);
  const [isSub, setSub] = useState(false);

  const inputRef = useRef(null);
  const location = useLocation();
  const responseData = location.state?.responseData;

  const navigate = useNavigate();
  const category = location.state?.category;
  // console.log(location.state);
  // console.log(category);

  useEffect(() => {
    if (!responseData || responseData === null) {
      alert("잘못된 접근입니다. 메인화면에서 [게임 참여]를 통해 접속해주세요.");
      navigate("/");
    } else {
      // console.log(responseData);
      setPlayerId(responseData.StatusResponse.playerId);
      setRoomId(responseData.StatusResponse.roomId);
      setRoomStatus(responseData.StatusResponse.roomStatus);
      setPlayerStatus(responseData.StatusResponse.playerStatus);
    }
  }, [responseData, navigate]);

  const statusRequestDto = {
    playerId: playerId,
    playerStatus: playerStatus,
    roomId: roomId,
    roomStatus: roomStatus,
  };
  const handleStartGame = async () => {
    try {
      const res = await startGame(statusRequestDto);
      const startResponseDto = res.data.data.StartResponse;
      const { statusResponse, wordListResponse } = startResponseDto;
      // console.log(res);
      // console.log(wordListResponse);
      setTargetWord(wordListResponse.newTargetWord);
      // setTargetWordSet(true);
      setTargetWordIndex(9);
      setPlayerStatus(startResponseDto.statusResponse.playerStatus);
      setRoomId(statusResponse.roomId);
      setRoomStatus(startResponseDto.statusResponse.roomStatus);

      setCurrentWordList([...wordListResponse.sortedWordList.slice(0, -1)]);

      setScore(startResponseDto.wordListResponse.newScore);
    } catch (error) {
      console.error(error);
    }
  };

  const insertRequestDto = {
    roomId: roomId,
    currentWordList: currentWordList,
    guessWord: guessWord,
    targetWord: targetWord,
  };
  const handleInsertWord = async () => {
    // console.log("guess:" + guessWord + ", target:" + targetWord + "끝");

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

    console.log(insertRequestDto);
    try {
      const res = await insertWord(insertRequestDto);
      setGuessWord("");
      setSendList(insertRequestDto.currentWordList);
      // console.log("입력 응답");
      // console.log(res);
      // 정렬 성공시

      const sortedRes = res.data.data.SortedWordListResponse;
      // console.log("sorted ");
      // console.log(sortedRes);
      const sorted = sortedRes.sortedWordList; //정렬단어목록
      // console.log(sorted);
      const {
        newScore,
        newSubWordList,
        newTargetWord,
        sortedIndex,
        targetWordRank,
      } = sortedRes;
      // console.log(sortedIndex);
      //정렬-> sortedWordList useEffect-> 모션
      setSortedIdx(sortedIndex); //0 start
      setSortedWordList(sorted);
      setTargetWordIndex(targetWordRank);
      // console.log("targetRank" + targetWordRank);
      //득점 성공시

      if (newScore === score) {
        //득점 실패 -> 정렬 후 끝
        return;
      } else {
        setTimeout(() => {
          setDeleteList([...sorted.slice(targetWordRank, 4)]);
          setScore(newScore);
        }, 500);
        setTimeout(() => {
          // console.log("new target");
          // console.log(newTargetWord);
          setTargetWord(newTargetWord);
          // setTargetWordIndex(currentWordList.length);
          setCurrentWordList((prev) => [...prev, ...newTargetWord]);
        }, 500);
        // setCurrentWordList(update);
      }
      // console.log(sorted);
      //추가 단어 여부와 추가
      setTimeout(() => {
        setSubWordList(newSubWordList);

        if (newSubWordList && newSubWordList.length > 0) {
          // console.log("new sub words");
          setCurrentWordList((prev) => [...prev, ...newSubWordList]);
          // console.log(subWordList);
          // setLevelWord([]);
        }
      }, 500);
    } catch (error) {
      const { response } = error;
      // 에러 메시지 매핑
      const errorMessages = {
        "GAO2-ERR-404": "찾을 수 없는 플레이어입니다.",
        "GAO3-ERR-404": "찾을 수 없는 게임입니다.",
        "GAO1-ERR-400": "입력할 수 없는 단어입니다.",
        // 필요하다면 다른 에러 코드들도 여기에 추가
      };

      console.log(response);

      // 에러 코드에 따른 메시지 출력
      const errorMessage = errorMessages[response?.data?.errorCode];
      if (errorMessage) {
        toast.error(errorMessage);
      } else {
        toast.error("잘못된 요청입니다."); // 일반적인 에러 메시지
      }
      setGuessWord("");
    }
  };

  useEffect(
    () => {
      const connectAndSubscribe = async () => {
        if (roomId !== null) {
          await connect("SINGLE", roomId, playerId);
          const callback = (messageBody) => {
            // console.log(messageBody);
            const toTwoD = [messageBody, ""];
            setLevelWord((prev) => [...prev, toTwoD]);
          };
          subscribe(`/topic/room/level-word/${roomId}`, callback);
        }
      };

      connectAndSubscribe();

      return () => {
        disconnect();
      };
    },
    [roomId],
    [playerId]
  );
  useEffect(() => {
    if (levelWord.length > 0) {
      setDisplay(true);
      setAdding(true);
      // setTimeout(() => {}, 200);
      //소켓으로
      setTimeout(() => {
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
    setDisplay(true);
    setIsTarget(true);
    // setAdding(true);
    setTimeout(() => {}, 200);
    //소켓으로
    setTimeout(() => {
      // console.log("add target word");
      // console.log(targetWord);

      setCurrentWordList((prev) => [...prev, ...targetWord]);
      // setTargetWordIndex(currentWordList.length);
      setDisplay(false);
      setIsTarget(false);
    }, 200);
    setTimeout(() => {
      // setCurrentWordList((prev) => [...prev, ...levelWord]);
    }, 400);
  }, [targetWord]);
  useEffect(() => {
    if (subWordList && subWordList.length < 1) {
    } else {
      setDisplay(true);
      setSub(true);
      // setAdding(true);
      setTimeout(() => {}, 200);
      setTimeout(() => {
        // console.log("add sub word");
        // console.log(subWordList);

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
  }, [sortedWordList]);

  const handleInputChange = (e) => {
    const { value } = e.target;
    setGuessWord(value);
  };

  const overRequestDto = {
    roomId: roomId,
    lastWord: targetWord,
    score: score,
  };
  const handleOverGame = async () => {
    try {
      disconnect();
      setPlayerStatus("OVER");
      setRoomStatus("FINISHED");
    } catch (error) {
      console.error(error);
    }
  };
  useEffect(() => {
    if (currentWordList.length >= 21) {
      handleOverGame();
    }
    // eslint-disable-next-line
  }, [currentWordList]);

  const handleOutRoom = async () => {
    try {
      console.log(overRequestDto);
      navigate("/SingleGameResult", {
        state: { overRequestDto: overRequestDto },
      });
    } catch (error) {
      console.error(error);
    }
  };
  const renderWordList = (list) => {
    return list
      .slice()
      .reverse()
      .map((item, index) => {
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
      });
  };
  const listIndexStandard = [
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1,
  ];
  const listing = listIndexStandard?.slice().map((value, index) => (
    <li
      key={index}
      // className={20 - index - 1 === targetWordIndex ? "wordline" : "wordline"}
      className={"wordline"}
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
          display: "grid",
          // flexDirection: "column",
          gridTemplateColumns: "repeat(12,1fr)",
          // grid-template-columns: repeat(12, [col-start] 1fr);
        }}
      >
        <div
          className="sidenav-left"
        >
          <button className="nav-button" onClick={() => navigate("/")}>
            홈
          </button>
        </div>
        <div className="gamecontainer" style={{}}>
          <div className="bglist">
            <div
              className={roomStatus === "FINISHED" ? "status over" : "status"}
            >
              {roomStatus === "PREPARED" && (
                <div
                  className="startbutton"
                  onClick={() => {
                    handleStartGame();
                  }}
                >
                  <Button label="START"></Button>
                </div>
              )}
              {roomStatus !== "PREPARED" &&
                roomStatus !== "UNPREPARED" &&
                roomStatus !== null && (
                  <div className="score">
                    {score}
                    {roomStatus === "FINISHED" &&
                      playerStatus === "OVER" &&
                      roomStatus !== null && (
                        <div className="gameover" style={{ fontSize: "48px" }}>
                          <br />
                          GAME OVER
                          <br />
                          <button
                            onClick={handleOutRoom}
                            className="large-button-style Neo"
                            style={{
                              WebkitTextStroke: "0",
                              fontSize: "26px",
                              color: "white",
                              padding: "5px",
                            }}
                          >
                            결과 보기
                          </button>
                        </div>
                      )}
                  </div>
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
                <ul className="wordlist" style={{ backgroundColor: "", listStyle:"none" }}>
                  {renderWordList(currentWordList)}
                </ul>
              )}

              {display && (
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
                          <div sytle={{ backgroundColor: "" }}>
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
            placeholder="단어를 입력해주세요."
            value={guessWord}
            onChange={handleInputChange}
            disabled={playerStatus === "OVER" || sorting}
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
    </>
  );
};
