import React, { useState, useEffect, useRef } from "react";
import "./SingleGame.css";

// import "./SingleGame copy.css";

import {
  startGame,
  createRoom,
  insertWord,
  overGame,
} from "../../api/singleGame/SingleGameApi.js";
import { connect, disconnect, subscribe } from "../../api/stompClient.js";

import { Button } from "../../components/button/ButtonTest";
import { AddWordAnimation } from "../../components/game/AddWordAnimation";
import { SortAnimation } from "../../components/game/SortAnimation";
import { DeleteAnimation } from "../../components/game/DeleteAnimation";
import { useLocation, useNavigate } from "react-router";

//TODO: 입력시 입력창 리셋
export const SingleGame = (props) => {
  // const { category } = props;
  // const [streak, setStreak] = useState(null);
  const [playerId, setPlayerId] = useState(null);
  const [playerStatus, setPlayerStatus] = useState(null);
  const [roomId, setRoomId] = useState(null);
  const [roomStatus, setRoomStatus] = useState(null);

  const [subWordList, setSubWordList] = useState([]); //levelword
  const [targetWord, setTargetWord] = useState("");

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
  const [count, setCount] = useState(0);
  const inputRef = useRef(null);
  const location = useLocation();
  const responseData = location.state?.responseData;

  const navigate = useNavigate();

  useEffect(() => {
    if (!responseData) {
      alert("잘못된 접근입니다. 메인화면에서 [게임 참여]를 통해 접속해주세요.");
      navigate("/");
    } else {
      console.log(responseData);
      setPlayerId(responseData.StatusResponse.playerId);
      setRoomId(responseData.StatusResponse.roomId);
      setRoomStatus(responseData.StatusResponse.roomStatus);
      setPlayerStatus(responseData.StatusResponse.playerStatus);
    }
  }, [responseData, navigate]);
  // const handleCreate = async () => {
  //   try {
  //     const category = 101;
  //     const res = await createRoom({ category: 101 });
  //     console.log("category " + category);
  //     // setSockJS(sock);
  //     const statusResponseDto = res.data.data.StatusResponse;
  //     // 게임방 만들어질 때 playerId, roomId 넘겨받음 => 이 api에서는 playerStatus, roomStatus만 변경
  //     setPlayerStatus(statusResponseDto.playerStatus);
  //     setRoomStatus(statusResponseDto.roomStatus);
  //     setPlayerId(statusResponseDto.playerId);
  //     setRoomId(statusResponseDto.roomId);
  //     console.log("roomID SET");
  //   } catch (error) {
  //     console.error(error);
  //   }
  // };
  const statusRequestDto = {
    playerId: playerId,
    playerStatus: playerStatus,
    roomId: roomId,
    roomStatus: roomStatus,
  };
  const handleStartGame = async () => {
    //게임시작api
    //newScore:0
    //newTargetWord
    //subWordList null
    //sortedIndex null
    //targetRank 9
    try {
      const res = await startGame(statusRequestDto);
      const startResponseDto = res.data.data.StartResponse;
      const statusResponse = startResponseDto.statusResponse;
      const wordListResponse = startResponseDto.wordListResponse;
      // console.log(res);
      console.log(wordListResponse);
      setTargetWord(wordListResponse.newTargetWord);
      // setTargetWordSet(true);
      setTargetWordIndex(9);
      setPlayerStatus(startResponseDto.statusResponse.playerStatus);
      setRoomId(statusResponse.roomId);
      setRoomStatus(startResponseDto.statusResponse.roomStatus);

      setCurrentWordList([...wordListResponse.sortedWordList]);

      setScore(startResponseDto.wordListResponse.newScore);
    } catch (error) {
      console.error(error);
    }
  };

  // function delayMethod(method, delayInMilliseconds) {
  //   return new Promise((resolve) => {
  //     setTimeout(() => {
  //       method();
  //       resolve(); // Resolve the promise when the delay is complete
  //     }, delayInMilliseconds);
  //   });
  // }

  // const handleScoring = (newList, newScore, SortedWordResponseDto) => {
  //   //여기서 새값 들어오기전에 먼저 효과를 주기
  //   //TODO: 1 단어정렬, 2 점수 효과, 3 득점X 효과
  //   setCurrentWordList([...newList]);

  //   console.log("scored" + newList);
  //   const toDelete = SortedWordResponseDto.targetWordRank;
  //   console.log("toDelete " + toDelete);

  //   setTimeout(() => {
  //     setCurrentWordList([...newList.slice(0, toDelete), ...newList.slice(4)]);
  //     // setDeleteList([...newList.slice(0, toDelete), ...newList.slice(4)]);
  //   }, 500);
  //   // 삭제 모션
  //   setTimeout(() => {
  //     setCurrentWordList((prev) => [
  //       ...prev,
  //       ...SortedWordResponseDto.newTargetWord,
  //     ]);
  //   }, 1000);
  //   setTimeout(() => {
  //     const where = currentWordList.findIndex(
  //       (line) => line[0] === SortedWordResponseDto.newTargetWord
  //     );
  //     setTargetWordIndex(where);
  //   }, 2100);
  //   // setScore(newScore);// 단어 삭제 모션 있고 난 다음에 변경
  //   setSubWordList(SortedWordResponseDto.newSubWordList);
  //   setTargetWord(SortedWordResponseDto.newTargetWord);
  //   console.log("new target " + SortedWordResponseDto.newTargetWord);

  //   setScore(newScore);
  //   //새 단어 주기적으로 추가되는 부분-> 실제 리스트에 포함 시키기

  //   console.log("current " + currentWordList);
  // };

  // useEffect(() => {

  //   handleStartGame(statusRequestDto);
  //   console.log(subWordList);

  // },[statusRequestDto]);
  const insertRequestDto = {
    roomId: roomId,
    currentWordList: currentWordList,
    guessWord: guessWord, // 입력한 단어 넣어주기
    targetWord: targetWord,
  };
  const handleInsertWord = async () => {
    if (guessWord === "") {
      alert("cant guess blank");
      return;
    }
    setLastGuess(guessWord);
    if (guessWord === targetWord) {
      alert("target cant be guessed");
      return;
    }
    console.log("guess " + guessWord);
    console.log(insertRequestDto);
    try {
      const res = await insertWord(insertRequestDto);
      setGuessWord("");
      setSendList(insertRequestDto.currentWordList);
      console.log("입력 응답");
      console.log(res);
      if (res.data.success === "fail") {
        alert("no such word in db");
      } else {
        // 정렬 성공시

        const sortedRes = res.data.data.SortedWordListResponse;
        console.log("sorted ");
        console.log(sortedRes);
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
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const connectAndSubscribe = async () => {
      if (roomId !== null) {
        await connect(); // Wait for the connect function to complete
        const callback = (messageBody) => {
          console.log(messageBody);
          const toTwoD = [messageBody, ""];
          setLevelWord((prev) => [...prev, toTwoD]);
        };
        subscribe(`/topic/room/level-word/${roomId}`, callback);
      }
    };

    connectAndSubscribe();
  }, [roomId]);
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
      }, 200);
      setTimeout(() => {
        // setCurrentWordList((prev) => [...prev, ...levelWord]);
      }, 400);
    }
  }, [levelWord]);
  useEffect(() => {
    if (!sorting) {
      inputRef.current.focus();
    }
  }, [sorting]);

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

      // 페이지 화면 전환
      // 페이지 result로 전환되면서 데이터 넘겨주기? 우선 넘겨줄거를 overResultDto로 만들게요
      // 근데 그냥 위에 overResponseDto 넘겨주는게 나을 것 같아서 그냥 안만들었습니다.
      // stomp.disconnect();

      // console.log(OverResponseDto);
      // subscription.unsubscribe();
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

  // 사라짐...
  const handleOutRoom = async () => {
    // 위에 dto있음!
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
        // if (Array.isArray(item)) {
        // This is a 2D array with points
        const [word, point] = item;
        return (
          <li key={currentWordList.length - index - 1} className={"wordline"}>
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
        {/* <div style={{ gridColumn: "1 /span 2" }}>
          <h5>
            player :{playerId} , room :{roomId}
          </h5>
          <div></div>
          <h1>
            status: player {playerStatus}, room {roomStatus}
          </h1>
          <h1 style={{ flex: "none", position: "sticky", top: 0 }}>
            score:{score}
          </h1>

          <div>
            <button
              onClick={(e) => {
                e.preventDefault();
                handleStartGame();
              }}
            >
              게임 시작
            </button>
            <button onClick={handleOverGame}> 게임 종료/소켓 종료 </button>
            <Button
              label="레이어 토글"
              onClick={() => {
                setDisplay((prev) => !prev);
              }}
            >
              레이어 토글
            </Button>
            <br />
            <Button
              label="삭제모션"
              onClick={() => {
                setDeleting(true);
                setTimeout(() => setDeleting(false), 300);
              }}
            >
              삭제 모션
            </Button>
            <br />
            <Button
              label="정렬 모션"
              onClick={() => {
                setSorting((prev) => !prev);
              }}
            >
              정렬 모션
            </Button>
            <br />
            <Button
              label="추가토글"
              onClick={() => {
                setAdding((prev) => !prev);
              }}
            >
              추가 토글 {adding.toString()}
            </Button>
            <br />
          </div>
        </div> */}
        <div className="gamecontainer" style={{}}>
          <div className="bglist">
            <div className={roomStatus==="FINISHED"?"status over":"status"} >
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
                    currentWordList.length <= 15
                      ? "wordline purple"
                      : "wordline red"
                  }
                >
                  &nbsp;
                </li>
                {listing}
              </ul>
              {!display && (
                <ul className="wordlist">{renderWordList(currentWordList)}</ul>
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
                        <ul className="wordlist dummy">
                          {renderWordList(currentWordList)}
                        </ul>
                      )}
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

          <input className="guessbox Neo" value={lastGuess} disabled></input>
          <input
            className="inputcase Neo"
            type="text"
            ref={inputRef}
            placeholder="입력하세요"
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

        {/* <div style={{ width: "35%" }}>
          <ul>
            <li sytle={{ display: "flex", flexDirection: "row" }}>
              <div style={{}}>
                타겟 단어: {targetWordIndex}
                {targetWord}
              </div>
            </li>
            <li>levelWord: {levelWord}</li>
            <li>next up</li>
            <li>{subWordList}</li>
            <li>deleteList</li>
            <li>{deleteList}</li>
            <li style={{ color: "white" }}>sorted {sortedWordList}</li>
          </ul>
          <ul className="wordlist">{renderWordList(currentWordList)}</ul>
        </div>  */}
      </div>
    </>
  );
};
