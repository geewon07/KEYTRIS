import React, { useState } from 'react';
import home from "../../assets/imgs/home.PNG";
import single from "../../assets/imgs/single.PNG";
import singleGame from "../../assets/imgs/singleGame.PNG";
import singleResult from "../../assets/imgs/singleResult.PNG";
import multi from "../../assets/imgs/multi.PNG";
import multiCode from "../../assets/imgs/multiCode.PNG";
import multiGame from "../../assets/imgs/multiGame.PNG";
import multiInvite from "../../assets/imgs/multiInvite.PNG";
import multiResult from "../../assets/imgs/multiResult.PNG";

export const Modal4 = ({ isOpen, onClose }) => {
  const [activeIndex, setActiveIndex] = useState(0);

  const images = [
    home,
    single,
    singleGame,
    singleResult,
    multi,
    multiCode,
    multiInvite,
    multiGame,
    multiResult,
  ];

  const captions = [
    "모드 선택",
    "1인 모드 카테고리 선택",
    "1인 모드 게임 화면",
    "1인 모드 결과창",
    "멀티 모드 게임 생성",
    "멀티 모드 게임 초대 링크",
    "멀티 모드 초대 참여", 
    "멀티 모드 게임 화면",
    "멀티 모드 게임 결과창",
  ];

  const descriptions = `1인 모드 혹은 멀티 모드 게임을 선택하세요.
  1인 모드 새 게임의 타겟어가 나올 카테고리를 선택하고 게임에 입장하세요.
  [Start] 버튼을 클릭해 게임을 시작하세요<br /> 주어진 제시어들과 비교해 타겟어에 더 유사한 단어를 입력하세요<br /> 입력단어와 유사도 순으로 제시어들이 재정렬됩니다<br /> 타겟어가 목표 순위내에 들어온다면 단어가 제거되고 점수를 얻을 수 있습니다<br /> 2초마다 새로운 제시어가 추가됩니다<br /> 제시어들이 상한선에 도달하지 않도록 타겟어와 가장 유사한 단어를 입력해 단어를 제거하세요.
  1인 모드 게임 결과를 기록하고 마지막 타겟어가 나오는 뉴스를 읽어보세요.
  새 게임의 타겟어가 나올 카테고리를 선택하고 게임을 생성하세요.
  게임 코드를 복사해 친구들을 초대하세요.
  게임 코드를 입력해 게임에 입장하세요.
  친구들과 게임을 함께 즐겨보세요.
  내 순위를 확인하고 마지막 타겟어가 나오는 뉴스를 읽어보세요.`;

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
    width: "500px",
    height: "300px",
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
                <button className="slide-button" onClick={handlePrev} style={{ margin: "5%", }}>
                  &#9664;
                </button>
                <img src={images[activeIndex]} alt={captions[activeIndex]} style={imgStyle} />
                <button className="slide-button" onClick={handleNext} style={{ margin: "5%", }}>
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
