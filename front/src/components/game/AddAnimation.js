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
  &.add-animation {
    animation: ${fall} 2s
    background-color: lightblue;
  }
`;
export const AddAnimation = (props) => {
  const [words, setWords] = useState([]);
  const { wordsToAdd, targetWord } = props;
  const bufferList = [
    ["강아지", ""],
    // ["고양이", ""],
    // ["책", ""],
    // ["휴대폰", ""],
    // ["친구", ""],
  ];
  // 기존 리스트에서 단어가 있는 최상단 y좌표
  const endIdx = 500;

  const handleAddWord = () => {
    const newWord = "추가단어";
    setWords((prevWords) => [...prevWords, newWord]);
  };

  return (
    <>

      {bufferList.map((item, index) => {
        const [word, point] = item;
        return (
          <FallRow className="add-animation wordline"key={wordsToAdd.length - index - 1}>
            {/* <li  className={"wordline"}> */}
              <div
                className={
                  "집" === word ? "targetWord wordline left" : "wordline left"
                }
              >
                {word}
              </div>
              <div className="right points">{point}</div>
            {/* </li> */}
          </FallRow>
        );
      })}
       </>
  );
};
