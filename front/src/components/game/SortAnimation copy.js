// import Container from 'react-bootstrap/Container';
// import Row from 'react-bootstrap/Row';
// import Col from 'react-bootstrap/Col';
import { useState } from 'react';
import styled, { css, keyframes } from 'styled-components';

const point = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
const startpoint = [8, 7, 6, 9, 0, 4, 3, 5, 2, 1];

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
      // --top: ${startpoint[index]}px; 
      // --bottom: ${-(startpoint[index]-point[index]) * 2 }rem; 
const SortRow = styled.li`
  &.sort-animation {
    ${({ index }) => css`
      animation: ${moveRow} 2s ease-in-out forwards;
      --top: ${-(point[index]-startpoint[index] )*2}rem; 
      --bottom:  ${-(point[index])/100}rem ; 
    `}
  }
`;

export const SortAnimationTest = () => {
  const initialLetterList = ["타겟", "문자1", "문자2", "문자3", "문자4", "문자5", "문자6", "문자7", "문자8", "문자9"];
  const [letterList, setLetterList] = useState(initialLetterList);

  return (
    <>
      {letterList.map((item, index) => (
        <SortRow
          key={index}
          className="sort-animation wordline"
          index={index}
        >
     {item}
        </SortRow>
      ))}
    </>
  );
};
