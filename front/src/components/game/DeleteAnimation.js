// npm install styled-components 설치 필요


import React, { useState } from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import styled, { keyframes } from 'styled-components';

const flashAndGrowAndShrink = keyframes`
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
  100% {
    transform: scale(1);
    opacity: 0;
  }
`;

const AnimatedRow = styled(Row)`
  &.row-animation {
    animation: ${flashAndGrowAndShrink} 2s ease-in-out forwards;
  }
`;

export const DeleteAnimation = () => {
  const initialLetterList = ["타겟", "문자1", "문자2", "문자3", "문자4", "문자5", "문자6", "문자7", "문자8", "문자9"];
  const [letterList, setLetterList] = useState(initialLetterList.reverse());

  return (
    <Container>                                                               
      {letterList.map((item, index) => (
        <AnimatedRow
          key={index}
          className= "row-animation"
        >
          <Col>{index} </Col>
          <Col xs={6}>{item}</Col>
          <Col>3 of 3</Col>
        </AnimatedRow>
      ))}
    </Container>
  );
};
