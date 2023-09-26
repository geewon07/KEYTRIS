import React, { useState } from "react";
import styled, { keyframes } from "styled-components";
const fall = keyframes`
  from {
    transform: translateY(0%);
  }
  to {
    transform: translateY(calc(100% - 2rem));
  }
`;
const bottomPosition = `calc(100% - 8rem)`;//var(--bottom)
const FallRow = styled.ul`
  &.add-animation {
    height:100%;
    // --bottom:${bottomPosition};

    animation: ${fall} 0.3s forwards; /* Adjust the animation duration and easing */
  }
`;
export const AddWordAnimation=(props)=> {
  const {bufferList}=props;
  const [words, setWords] = useState(bufferList);

  // 기존 리스트에서 단어가 있는 최상단 y좌표

  return (
    <div style={{height:"500px", backgroundColor:"blue"}}> 
      {words?.map((item, index) => {
        const [word, point] = item;
        return (
          <FallRow className="add-animation wordlist">
            <li key={index} className={"wordline"}>
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
} 
