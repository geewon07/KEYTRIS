import React, { useState, useEffect } from "react";
import "./SingleGame.css";
import {
  startGame,
  createRoom,
  insertWord,
  overGame,
} from "../../api/singleGame/SingleGameApi.js";
import { connect, disconnect, subscribe } from "../../api/stompClient.js";
import { QuickMenu } from "../../components/quickmenu/quickMenuTest";

export const SingleGame = (props) => {
  // const { category } = props;
  // const [streak, setStreak] = useState(null);
  const [playerId, setPlayerId] = useState(null);
  const [playerStatus, setPlayerStatus] = useState(null);
  const [roomId, setRoomId] = useState(null);
  const [roomStatus, setRoomStatus] = useState(null);

  const [subWordList, setSubWordList] = useState([]); //levelword
  const [targetWord, setTargetWord] = useState(null);

  const [guessWord, setGuessWord] = useState("");
  const [lastGuess, setLastGuess] = useState("");
  const [currentWordList, setCurrentWordList] = useState([]);
  const [deleteList, setDeleteList] = useState([]);

  const [sortedWordList, setSortedWordList] = useState([]);
  const [score, setScore] = useState(0);

  const [levelWord, setLevelWord] = useState([]);

  const [targetWordSet, setTargetWordSet] = useState(false);
  // const callback = (messageBody) => {
  //   console.log(messageBody);
  // };
  const callback = (messageBody) => {
    console.log(messageBody);
    const toTwoD = [messageBody, ""];
    setLevelWord((prev) => [...prev, ...toTwoD]);
  };
  const subscription = useEffect(() => {
    if (roomId !== null) {
      connect();
    }

    // subscribe(`/topic/room/level-word/${roomId}`, callback);
    return () => {
      if (subscription) {
        subscription.unsubscribe();
      }
      if (roomId != null) disconnect();
    };
  }, [roomId]);

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
    try {
      const res = await startGame(statusRequestDto);
      const startResponseDto = res.data.data.StartResponse;
      const statusResponse = startResponseDto.statusResponse;
      const wordListResponse = startResponseDto.wordListResponse;
      // console.log(res);
      console.log(wordListResponse);
      setTargetWord(wordListResponse.newTargetWord);
      setTargetWordSet(true);
      setPlayerStatus(startResponseDto.statusResponse.playerStatus);
      setRoomStatus(startResponseDto.statusResponse.roomStatus);

      // setSubWordList(wordListResponse.newSubWordList);
      setCurrentWordList([...wordListResponse.sortedWordList]);

      setScore(startResponseDto.wordListResponse.newScore);

      console.log(currentWordList);
      // currentWordList 저장해야함 -> 어떻게 저장?? subWordList에 TargetWord뒤에 추가???
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (targetWordSet) {
      // Render the word list with the appropriate class names
      renderWordList(currentWordList);
    }
  }, [targetWordSet, currentWordList]);
  

  function delayMethod(method, delayInMilliseconds) {
    return new Promise((resolve) => {
      setTimeout(() => {
        method();
        resolve(); // Resolve the promise when the delay is complete
      }, delayInMilliseconds);
    });
  }

  const handleScoring = (newList, newScore, SortedWordResponseDto) => {
    //여기서 새값 들어오기전에 먼저 효과를 주기
    //TODO: 1 단어정렬, 2 점수 효과, 3 득점X 효과
    if (newScore === score) {
      console.log("did not score");
      //흔들리는 모션
      setCurrentWordList([...newList]);
    } else {
      console.log("scored" + newList);
      setScore(newScore);
      setCurrentWordList([...newList]);
      setTimeout(() => {
        const toDelete = newList.findIndex((line) => line[0] === targetWord);
        console.log("toDelete " + toDelete);
        setCurrentWordList([
          ...newList.slice(0, toDelete),
          ...newList.slice(4),
        ]);
      }, 5000);
      setTimeout(() => {
        setCurrentWordList((prev) => [
          ...prev,
          ...SortedWordResponseDto.newSubWordList,
          ...SortedWordResponseDto.newTargetWord,
          ...levelWord,
        ]);
      }, 6000);
      setSubWordList(SortedWordResponseDto.newSubWordList);
      setTargetWord(SortedWordResponseDto.newTargetWord);
      console.log("new target " + SortedWordResponseDto.newTargetWord);
      //새 단어 주기적으로 추가되는 부분-> 실제 리스트에 포함 시키기
    }
    console.log("current " + currentWordList);
  };

  // useEffect(() => {
  //   console.log(subWordList); // subWordList가 변경될 때마다 이 로그가 출력
  // }, [subWordList]);

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
      console.log("insert res ");
      console.log(res);
      if (res.data.success === "fail") {
        alert("no such word in db");
      } else {
        const SortedWordResponseDto = res.data.data.SortedWordListResponse;
        console.log("sorted ");
        console.log(SortedWordResponseDto);
        const sorted = SortedWordResponseDto.sortedWordList;
        const newScore = SortedWordResponseDto.newScore;
        handleScoring(sorted, newScore, SortedWordResponseDto);
        //효과를 다 하고 쓰세여~

        setSortedWordList([...sorted]);
        setScore(newScore);

        // setSubWordList(SortedWordResponseDto.newSubWordList);
        // setTargetWord(SortedWordResponseDto.newTargetWord);
      }
      // setCurrentWordList((prev) => [...prev, subWordList, targetWord]);
    } catch (error) {
      console.error(error);
    }
  };

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
      // const res = await overGame(overRequestDto);
      // const OverResponseDto = res.data.data.OverResponse;
      // 페이지 화면 전환
      // 페이지 result로 전환되면서 데이터 넘겨주기? 우선 넘겨줄거를 overResultDto로 만들게요
      // 근데 그냥 위에 overResponseDto 넘겨주는게 나을 것 같아서 그냥 안만들었습니다.
      // stomp.disconnect();
      disconnect();
      if (subscription !== null) {
        subscription.unsubscribe();
      }
    } catch (error) {
      console.error(error);
    }
  };

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
          <li
            key={currentWordList.length - index - 1}
            className={word === targetWord[0] ? "targetWord wordline" : "wordline"}
          >
            <div className="left">{word}</div>
            <div className="right">
              {point} index {currentWordList.length - index - 1}
            </div>
          </li>
        );
        // }
      });
  };
  const listing = currentWordList
    ?.slice()
    .reverse()
    .map((value, index) => (
      <li
        key={index}
        style={{ lineHeight: "1.5rem", color: "white" }}
        className={value === targetWord ? "targetWord wordline" : "wordline"}
      >
        {value}+{currentWordList.length - index}
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
            <button onClick={handleOverGame}>게임 종료/소켓 종료</button>
          </div>
        </div>
        <div className="gamecontainer" style={{}}>
          <div className="bglist">
            <ul className="wordlist" style={{ listStyle: "none" }}>
              {renderWordList(currentWordList)}
            </ul>
            <div className="overlaybox"></div>
            <input className="guessbox Neo" value={lastGuess} disabled></input>
            <input
              className="inputcase Neo"
              type="text"
              placeholder="입력하세요"
              value={guessWord}
              onChange={handleInputChange}
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
          <QuickMenu />
          <ul>
            <li sytle={{ display: "flex", flexDirection: "row" }}>
              <div style={{}}>타겟 단어: {targetWord}</div>
              <div>유사도</div>
            </li>
            <li>{sortedWordList}</li>
            <li>next up</li>
            <li>{subWordList}</li>
          </ul>
          <ul>{renderWordList(deleteList)}</ul>
        </div>
      </div>
    </>
  );
};
