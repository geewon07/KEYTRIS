import React, { useState } from "react";
import "./AddWordAnimation.css";

export function AddWordAnimation() {
  const [words, setWords] = useState([]);

  // 기존 리스트에서 단어가 있는 최상단 y좌표
  const endIdx = 500;

  const handleAddWord = () => {
    const newWord = "추가단어";
    setWords((prevWords) => [...prevWords, newWord]);
  };

  return (
    <div className="App">
      <button onClick={handleAddWord}>단어 추가</button>

      {words.map((word, index) => (
        <div
          key={index}
          className="word"
          style={{
            animation: "2s ease forwards slideInAnimation",
            // bottom값이 0보다 작아지면 게임 종료(상단에 도달하는 시점)
            "--bottom": `${endIdx - 24 * index * 2}px`,
          }}
        >
          {word} {endIdx}
          {index}
        </div>
      ))}
    </div>

  );
} 
