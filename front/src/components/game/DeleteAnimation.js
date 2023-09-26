// npm install styled-components 설치 필요

import React, { useState } from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import styled, { keyframes } from "styled-components";
import "../../pages/singleGame/SingleGame.css";
const flashAndGrowAndShrink = keyframes`
  0% {
    transform:translateX(0) scale(1);
    opacity: 1;
  }
  50% {
    // transform:translateX(100%) scale(1.1);
    opacity: 0.3;
  }
  
  100% {
    transform:translateX(100%) scale(2);
    opacity: 0;
  }
`;

const AnimatedRow = styled.li`
  &.delete-animation {
    animation: ${flashAndGrowAndShrink} 0.3s;
    list-style: none;
    font-size:24px;
  }
`;

export const DeleteAnimation = (props) => {
  const { initialList, targetWord , targetIndex } = props;
  const [letterList, setLetterList] = useState(initialList);


  return (
    <>
      {letterList?.map((item, index) => {
        const [word, point] = item;
        return (
          <AnimatedRow key={index} className="delete-animation wordline">
            {/* <li key={letterList.length - index - 1} className={"wordline"}> */}
            <div
              className={
                targetWord === word
                  ? "targetWord wordline left"
                  : "wordline left"
              }
            >
              {word}
            </div>
            <div className="right points">{point}</div>
            {/* </li> */}
            {/* <li style={{ color: "white" }}>{item[0]}</li> */}
          </AnimatedRow>
        );
      })}
    </>
  );
};
