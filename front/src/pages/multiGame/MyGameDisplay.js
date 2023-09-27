import React, { useEffect, useState } from "react";
import "./GameDisplay.css";
import { Button } from "../../components/button/ButtonTest";

export const MyGameDisplay = ({
  data,
  roomStatus,
  handleStartGame,
  handlePlayerReady,
  wordListResponse,
  insertWord,
  currentPlayerGameInfo,
  newlevelWord,
}) => {
  const [subWordList, setSubWordList] = useState([]);
  const [targetWord, setTargetWord] = useState("");

  const [guessWord, setGuessWord] = useState("");
  const [lastGuess, setLastGuess] = useState("");
  const [currentWordList, setCurrentWordList] = useState([]);
  const [deleteList, setDeleteList] = useState([]);

  const [sortedWordList, setSortedWordList] = useState([]);

  const [levelWord, setLevelWord] = useState([]);

  const [targetWordIndex, setTargetWordIndex] = useState(null);

  const [score, setScore] = useState(null);

  const levelWordInfo = (messageBody) => {
    console.log(messageBody);
    const toTwoD = [messageBody, ""];
    setLevelWord((prev) => [...prev, toTwoD]);
  };

  useEffect(() => {
    if (levelWord.length > 0) {
      setCurrentWordList((prev) => [...prev, ...levelWord]);
      setLevelWord([]);
    }
  }, [levelWord]);

  useEffect(() => {
    if (data !== null && data.playerStatus === "GAMING") {
      console.log(wordListResponse);
      console.log(data);
      setTargetWord(wordListResponse.newTargetWord);
      setTargetWordIndex(9);
      setCurrentWordList([...wordListResponse.sortedWordList]);
      setScore(wordListResponse.newscore);
    }
  }, [data]);

  useEffect(() => {
    const where = currentWordList.findIndex((line) => line[0] === targetWord);
    setTargetWordIndex(where);
  }, [currentWordList, targetWord]);

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

  const handleInputChange = (e) => {
    const { value } = e.target;
    setGuessWord(value);
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

  const insertRequestDto = {
    currentWordList: currentWordList,
    guessWord: guessWord,
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

    insertWord(insertRequestDto);
  };

  useEffect(() => {
    if (currentPlayerGameInfo && currentPlayerGameInfo !== null) {
      const sorted = currentPlayerGameInfo.sortedWordList;
      const newScore = currentPlayerGameInfo.newScore;

      handleScoring(sorted, newScore, currentPlayerGameInfo);

      setSortedWordList([...sorted]);
    }
  }, [currentPlayerGameInfo]);

  return (
    <div>
      <div className="gamecontainer" style={{}}>
        <div className="bglist">
          <div className="multi-score">
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
              data.playerStatus ===
                "UNREADY"(
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
            {roomStatus !== "PREPARED" &&
              roomStatus !== "PREPARING" &&
              roomStatus !== null &&
              data.score}
          </div>
          <div className="overlaybox"></div>

          <ul className="indexlist">{listing}</ul>
          <ul className="wordlist">{renderWordList(currentWordList)}</ul>

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
    </div>
  );
};
