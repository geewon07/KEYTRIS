import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import styled, { css, keyframes } from "styled-components";
import { useState } from "react";

const startpoint = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
const endpoint = [8, 7, 6, 9, 0, 4, 3, 5, 2, 1];

// 리스트 간격 계산
const rowGap = 24;

const moveRow = keyframes`
  from {
    transform: translateY(var(--top));
  }
  to {
    transform: translateY(var(--bottom));
  }
`;

const AnimatedRow = styled.ul`
  &.row-animation {
    ${({ index, endpoint }) => css`
      animation: ${moveRow} 2s ease-in-out forwards;
      --top: ${startpoint[index]}px;
      --bottom: ${-(startpoint[index] - endpoint[index]) * 2}rem;
    `}
  }
`;

export const SortAnimation = (props) => {
    const{sortedIndex, sendList}=props;
  const initialLetterList = [
    "타겟",
    "문자1",
    "문자2",
    "문자3",
    "문자4",
    "문자5",
    "문자6",
    "문자7",
    "문자8",
    "문자9",
  ];
  
  const [letterList, setLetterList] = useState(sendList);

  return (
    <Container>
      {letterList.map((item, index) => {
        const [word,point] =item;
        return(
        <AnimatedRow key={index} className="row-animation" index={index} endpoint={endpoint}>
    
          <div
            className={
              "집" === word ? "targetWord wordline left" : "wordline left"
            }
          >
            {word}
          </div>
          <div className="right points">{point}</div>
        </AnimatedRow>
      )})}
    </Container>
  );
};
