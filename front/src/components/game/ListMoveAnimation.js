// npm install styled-components 설치 필요





import React, { useState, useEffect } from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import styled, { keyframes } from 'styled-components';

const moveTable = keyframes`
  from {
    transform: translateY(0px);
  }
  to {
    transform: translateY(80px);
  }
`;

const WordContainer = styled(Container)`
  &.word-container {
    animation: ${moveTable} 2s ease-in-out forwards;
  }
`;

const AnimatedRow = styled(Row)`
  animation: none;
`;

export const ListMoveAnimation = () => {
  const initialLetterList = ["타겟", "문자1", "문자2", "문자3", "문자4", "문자5", "문자6", "문자7", "문자8", "문자9"];
  const [letterList, setLetterList] = useState(initialLetterList.reverse());

  useEffect(() => {
    setTimeout(() => {
      AnimatedRow.defaultProps = {
        ...AnimatedRow.defaultProps,
        'data-testid': 'animated-row',
      };
    }, 1);
  }, []);

  return (
    <WordContainer className="word-container">
      {letterList.map((item, index) => (
        <AnimatedRow key={index}>
          <Col>{index}</Col>
          <Col xs={6}>{item}</Col>
          <Col>3 of 3</Col>
        </AnimatedRow>
      ))}
    </WordContainer>
  );
};
