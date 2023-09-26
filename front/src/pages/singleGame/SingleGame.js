import React, { useState, useEffect } from "react";
import "./SingleGame.css";
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
  const [sortedIndex, setSortedIndex] = useState([]);
  const [display, setDisplay] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [sorting, setSorting] = useState(false);
  const [adding, setAdding] = useState(false);
  const [count, setCount] = useState(0);
  // const [targetWordSet, setTargetWordSet] = useState(false);
  // const callback = (messageBody) => {
  //   console.log(messageBody);
  // };
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
      setTimeout(() => {
        
      }, 200);

      setTimeout(() => {
        setCurrentWordList((prev) => [...prev, ...levelWord]);
        setDisplay(false);
        setAdding(false);
        
      },300);
      setTimeout(() => {
        // setCurrentWordList((prev) => [...prev, ...levelWord]);
        setLevelWord([]);
      }, 1200);
     
    }
  }, [levelWord]);

  const handleCreate = async () => {
    try {
      const category = 101;
      const res = await createRoom({ category: 101 });
      console.log("category " + category);
      // setSockJS(sock);
      const statusResponseDto = res.data.data.StatusResponse;
      // 게임방 만들어질 때 playerId, roomId 넘겨받음 => 이 api에서는 playerStatus, roomStatus만 변경
      setPlayerStatus(statusResponseDto.playerStatus);
      setRoomStatus(statusResponseDto.roomStatus);
      setPlayerId(statusResponseDto.playerId);
      setRoomId(statusResponseDto.roomId);
      console.log("roomID SET");
    } catch (error) {
      console.error(error);
    }
  };
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

  useEffect(() => {
    const where = currentWordList.findIndex((line) => line[0] === targetWord);
    setTargetWordIndex(where);
  }, [currentWordList, targetWord]);

  // function delayMethod(method, delayInMilliseconds) {
  //   return new Promise((resolve) => {
  //     setTimeout(() => {
  //       method();
  //       resolve(); // Resolve the promise when the delay is complete
  //     }, delayInMilliseconds);
  //   });
  // }

  const handleScoring = (newList, newScore, SortedWordResponseDto) => {
    //여기서 새값 들어오기전에 먼저 효과를 주기
    //TODO: 1 단어정렬, 2 점수 효과, 3 득점X 효과
    if (newScore === score) {
      console.log("did not score");
      //입력창 흔들리는 모션
      // 정렬 모션
      setCurrentWordList([...newList]);
    } else {
      console.log("scored" + newList);

      setCurrentWordList([...newList]);
      setTimeout(() => {
        const toDelete = SortedWordResponseDto.targetWordRank;
        console.log("toDelete " + toDelete);
        setCurrentWordList([
          ...newList.slice(0, toDelete),
          ...newList.slice(4),
        ]);
        setDeleteList([...newList.slice(0, toDelete), ...newList.slice(4)]);
      }, 1000);
      // 삭제 모션
      setTimeout(() => {
        if (SortedWordResponseDto.newSubWordList !== null) {
          setCurrentWordList((prev) => [
            ...prev,
            ...SortedWordResponseDto.newSubWordList,
            ...SortedWordResponseDto.newTargetWord,
            ...levelWord,
          ]);
        } else {
          setCurrentWordList((prev) => [
            ...prev,
            ...SortedWordResponseDto.newTargetWord,
            ...levelWord,
          ]);
        }
      }, 2000);
      setTimeout(() => {
        const where = currentWordList.findIndex(
          (line) => line[0] === SortedWordResponseDto.newTargetWord
        );
        setTargetWordIndex(where);
      }, 2100);
      // setScore(newScore);// 단어 삭제 모션 있고 난 다음에 변경
      setSubWordList(SortedWordResponseDto.newSubWordList);
      setTargetWord(SortedWordResponseDto.newTargetWord);
      console.log("new target " + SortedWordResponseDto.newTargetWord);

      setScore(newScore);
      //새 단어 주기적으로 추가되는 부분-> 실제 리스트에 포함 시키기
    }
    console.log("current " + currentWordList);
  };

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
      
      setSendList(insertRequestDto.currentWordList);
      console.log("insert res ");
      console.log(res);
      if (res.data.success === "fail") {
        alert("no such word in db");
      } else {
        // 정렬 성공시
        const SortedWordResponseDto = res.data.data.SortedWordListResponse;
        console.log("sorted ");
        console.log(SortedWordResponseDto);
        const sorted = SortedWordResponseDto.sortedWordList;
        const sortedIdx = SortedWordResponseDto.sortedIndex;
        const sublist = SortedWordResponseDto.newSubWordList;
        setSubWordList(sublist);
        setSortedIndex(sortedIdx);
        setSortedWordList([...sorted]);
        const newScore = SortedWordResponseDto.newScore;
        if(subWordList.length>0){
          setLevelWord((prev) => [...prev, ...subWordList]);
          setSubWordList([]);
        }

        handleScoring(sorted, newScore, SortedWordResponseDto);
        //효과를 다 하고 쓰세여~

        
        // 단어 삭제 모션 있고 난 다음에 변경
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (sortedWordList.length > 0) {
      setDisplay(true);
      // setAdding(false);
      setSorting(true);
      setTimeout(() => {
        
      }, 100);

      setTimeout(() => {
        
        setDisplay(false);
        // setAdding(false);
        setSorting(false);
        
      },3000);
      setTimeout(() => {
        // setCurrentWordList((prev) => [...prev, ...levelWord]);
        setSortedWordList([]);
      }, 1200);}
  }, [sortedWordList])
  
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
      const res = await overGame(overRequestDto);
      const OverResponseDto = res.data.data.OverResponse;
      setPlayerStatus(OverResponseDto.statusResponse.playerStatus);
      setRoomStatus(OverResponseDto.statusResponse.roomStatus);
      // 페이지 화면 전환
      // 페이지 result로 전환되면서 데이터 넘겨주기? 우선 넘겨줄거를 overResultDto로 만들게요
      // 근데 그냥 위에 overResponseDto 넘겨주는게 나을 것 같아서 그냥 안만들었습니다.
      // stomp.disconnect();
      disconnect();
      console.log(OverResponseDto);
      // subscription.unsubscribe();
    } catch (error) {
      console.error(error);
    }
  };
  useEffect(() => {
    if (currentWordList.length >= 21) {
      handleOverGame();
    } // subWordList가 변경될 때마다 이 로그가 출력
    // eslint-disable-next-line
  }, [currentWordList]);

  // 사라짐...
  // const handleOutRoom = async (statusRequestDto) => { // 위에 dto있음!
  //   try {
  //     const res = await outRoom(statusRequestDto);
  //     const OverResponseDto = res.data.data.OverResponse;
  //   } catch (error) {
  //     console.error(error);
  //   }
  // };
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
          display: "flex",
          flexDirection: "row",
        }}
      >
        <div style={{ width: "35%" }}>
          <h5>
            player :{playerId} , room :{roomId}
          </h5>
          <div>
            <button
              onClick={(e) => {
                e.preventDefault();
                handleCreate();
                // onClickConnectBtn();
              }}
            >
              1인 게임 입장
            </button>
          </div>
          <h1>
            status: player {playerStatus}, room {roomStatus}
          </h1>
          <h1 style={{ flex: "none", position: "sticky", top: 0 }}>
            score:{score}
          </h1>
          {/* <Menu></Menu> */}
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
            {/* <Button
              label="추가모션"
              onClick={() => {
                setLevelWord(["테스트", ""]);
              }}
            >
              추가 모션
            </Button> */}
          </div>
        </div>
        <div className="gamecontainer" style={{}}>
          {/* <div className="bglist displaylayer"> */}

          {/* </div> */}

          <div className="bglist">
            <div className="score">
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
                roomStatus !== null &&
                score}
            </div>
            <div className="overlaybox"></div>

            <ul className="indexlist">{listing}</ul>
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
                      // targetWord={targetWord}
                    ></AddWordAnimation>
                    <ul className="wordlist">
                      {renderWordList(currentWordList)}
                    </ul>
                  </>
                )}
                <ul className="wordlist">
                  {sorting && (
                    <SortAnimation
                      sortedList={sendList.reverse()}
                      beforeIndex={sortedIndex}
                    ></SortAnimation>
                  )}
                  {deleting && (
                    <DeleteAnimation
                      initialList={deleteList.reverse()}
                      targetIndex={2}
                    ></DeleteAnimation>
                  )}
                </ul>
              </>
            )}

            <input className="guessbox Neo" value={lastGuess} disabled></input>
            <input
              className="inputcase Neo"
              type="text"
              placeholder="입력하세요"
              value={guessWord}
              onChange={handleInputChange}
              disabled={playerStatus==="OVER"}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  e.preventDefault();
                  handleInsertWord();
                }
              }}
            ></input>
          </div>
        </div>
        <div style={{ width: "35%" }}>
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
            <li>sorted {sortedWordList}</li>
          </ul>
        </div>
      </div>
    </>
  );
};
