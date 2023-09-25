import React, { useState } from "react";
import styled, { keyframes } from "styled-components";
// import "./AddWordAnimation.css";
const fall = keyframes`
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(var(--bottom));
  }
`;

const FallRow = styled.ul`
  &.row-animation {
    animation: ${fall} 2s;
    list-style: none;
  }
`;
export const AddAnimation = (props) => {
  const [words, setWords] = useState([]);
  const { wordsToAdd, targetWord } = props;

  // 기존 리스트에서 단어가 있는 최상단 y좌표
  const endIdx = 500;

  const handleAddWord = () => {
    const newWord = "추가단어";
    setWords((prevWords) => [...prevWords, newWord]);
  };

  return (
    <div className="App">
      {wordsToAdd.map((item, index) => {
        const [word, point] = item;
        return (
            <FallRow>
          <li key={wordsToAdd.length - index - 1} className={"wordline"}>
            <div
              className={
                "집" === word ? "targetWord wordline left" : "wordline left"
              }
            >
              {word}
            </div>
            <div className="right points">{point}</div>
          </li>
          </FallRow>
        );
      })}
    </div>
  );
};
