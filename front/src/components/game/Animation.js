import React, { useEffect, useState } from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

export const Animation = () => {
  const numRows = 20;
  const [letterList, setLetterList] = useState([]);
  let distance;

  useEffect(() => {
    const letters = ["문자1", "문자2", "문자3", "문자4", "문자5", "문자6", "문자7", "문자8", "문자9"];
    const targetLetter = "타겟";

    const sortList = ["타겟", "문자1", "문자2", "문자3", "문자4", "문자5", "문자6", "문자7", "문자8", "문자9"];
    const tempList = [...letters, targetLetter];
    while (tempList.length < numRows) {
      tempList.push("");
    }
    tempList.reverse();
    setLetterList(tempList);

    setTimeout(() => {
      const rows = document.querySelectorAll('.row-animation');
      const lastRow = rows[rows.length - 1];
      const lastRowRect = lastRow.getBoundingClientRect();
      const distanceToMove = lastRowRect.top - lastRowRect.bottom;
      rows.forEach((row, index) => {
        distance = distanceToMove * (numRows - index);
        row.style.transform = `translateY(${-distanceToMove * (numRows - index)}px)`;
        row.style.position = "relative";
      });

      setTimeout(() => {
        while (sortList.length < numRows) {
          sortList.push("");
        }
        sortList.reverse();
        setLetterList(sortList);

        rows.forEach((row, index) => {
          row.style.transform = `translateY(${distance+24}px)`;
          row.style.position = "relative";
        });
      }, 1000);
    }, 1000);
  }, []);

  return (
    <Container>
      {letterList.map((item, index) => (
        <Row key={index} className="row-animation">
          <Col>{index} 1 of 3</Col>
          <Col xs={6}>{item}</Col>
          <Col>3 of 3</Col>
        </Row>
      ))}
    </Container>
  );
};
