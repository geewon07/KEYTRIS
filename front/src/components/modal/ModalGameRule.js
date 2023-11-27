import React, { useState } from "react";
import singleGame from "../../assets/imgs/singleGame.png";
import game_rule_0 from "../../assets/imgs/game_rule_0.gif";
import game_rule_1 from "../../assets/imgs/game_rule_1.gif";
import game_rule_2 from "../../assets/imgs/game_rule_2.gif";
import game_rule_3 from "../../assets/imgs/game_rule_3.gif";

export const ModalGameRule = ({ isOpen, onClose }) => {
  const [activeIndex, setActiveIndex] = useState(0);

  const images = [
    // singleGame,
    game_rule_0,
    game_rule_1,
    game_rule_2,
    game_rule_3,
  ];

  const captions = [
    // "1. 게임 시작",
    "1. 단어 입력",
    "2. 유사도 순으로 정렬",
    "3. 점수 획득",
    "4. 게임 종료 조건",
  ];

  const descriptions = `연두색 타겟어와 맥락이 유사한 새로운 단어를 입력해주세요.
    화면의 단어들이 입력 단어와 가장 유사한 순으로 정렬됩니다.
    타겟 단어가 4위 이내 이면 단어가 제거되고 점수를 얻을 수 있어요.
    3초 마다 새로운 단어가 추가됩니다.<br /> 단어가 천장에 닿으면 게임이 종료됩니다.`;

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
    setActiveIndex(
      (prevIndex) => (prevIndex - 1 + images.length) % images.length
    );
  };

  const paragraphs = descriptions.split("\n");

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
            <div style={titleStyle}>게임 규칙</div>
            <div style={contentStyle}>
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  height: "300px",
                }}
              >
                <button
                  className="slide-button"
                  onClick={handlePrev}
                  style={{ margin: "5%", cursor: "pointer" }}
                >
                  &#9664;
                </button>
                <img
                  src={images[activeIndex]}
                  alt={captions[activeIndex]}
                  style={imgStyle}
                />
                <button
                  className="slide-button"
                  onClick={handleNext}
                  style={{ margin: "5%", cursor: "pointer" }}
                >
                  &#9654;
                </button>
              </div>
              <div style={{ margin: "5%" }}>
                <h3>{captions[activeIndex]}</h3>
                {paragraphs[activeIndex].split("<br />").map((line, index) => (
                  <p key={index}>{line}</p>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
