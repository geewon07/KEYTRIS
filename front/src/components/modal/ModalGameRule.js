import React, { useState } from 'react';
import singleGame from "../../assets/imgs/singleGame.png";
import singleGame1 from "../../assets/imgs/singleGame1.png";
import GameSort from "../../assets/imgs/GameSort.png";
import singleGame2 from "../../assets/imgs/singleGame2.png";
import singleGameOver from "../../assets/imgs/singleGameOver.png";

export const ModalGameRule = ({ isOpen, onClose }) => {
  const [activeIndex, setActiveIndex] = useState(0);

  const images = [
    singleGame,
    singleGame1,
    GameSort,
    singleGame2,
    singleGameOver,
  ];

  const captions = [
    "1. 게임 시작",
    "2. 유사 단어 입력",
    "3. 유사도에 따른 재정렬",
    "4. 점수 획득",
    "5. 게임 종료 조건",
  ];

  const descriptions =`[시작하기] 버튼을 클릭해 게임을 시작하세요.
    주어진 제시어들과 비교해 타겟어에 더 유사한 단어를 입력하세요.
    입력단어와 유사도 순으로 제시어들이 재정렬됩니다.
    타겟어가 목표 순위내에 들어온다면 단어가 제거되고 점수를 얻을 수 있습니다.
    2초마다 새로운 제시어가 추가됩니다<br /> 제시어들이 상한선에 도달하지 않도록 타겟어와 가장 유사한 단어를 입력해 단어를 제거하세요.`;

  if (!isOpen) return null;

  const titleStyle = {
    color: "#FFF",
    textAlign: "center",
    fontSize: "30px",
    fontStyle: "normal",
    fontWeight: 400,
    lineHeight: "50px",
    letterSpacing: "3px",
    alignItems: "center",
    width: "70%",
    wordBreak: "break-all",
    marginBottom: "2rem",
  };

  const contentStyle = {
    display: "flex",
    flexDirection: "column",
    textAlign: "start",
    width: "70%",
    gap: "1rem",
    marginBottom: "1rem",
    fontSize: "1rem",
  };

  const imgStyle = {
    width: "400px",
    height: "350px",
  };

  const handleNext = () => {
    setActiveIndex((prevIndex) => (prevIndex + 1) % images.length);
  };

  const handlePrev = () => {
    setActiveIndex((prevIndex) => (prevIndex - 1 + images.length) % images.length);
  };

  const paragraphs = descriptions.split('\n');

  return (
    <>
      <div>
        <div className="modal">
          <div className="modal-content">
            <div style={{ alignSelf: "end" }}>
              <button
                className="modal-close-button"
                onClick={onClose}
                style={{}}
              >
                X
              </button>
            </div>
            <div style={titleStyle}>키트리스 가이드</div>
            <div style={contentStyle}>
              <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "300px", }}>
                <button className="slide-button" onClick={handlePrev} style={{ margin: "5%", cursor: "pointer", }}>
                  &#9664;
                </button>
                <img src={images[activeIndex]} alt={captions[activeIndex]} style={imgStyle} />
                <button className="slide-button" onClick={handleNext} style={{ margin: "5%", cursor: "pointer", }}>
                  &#9654;
                </button>
              </div>
              <div style={{ margin: "5%", }}>
                <h3>{captions[activeIndex]}</h3>
                {paragraphs[activeIndex].split('<br />').map((line, index) => (
                  <p key={index} >
                    {line}
                  </p>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
