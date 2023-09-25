import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';

import { Modal } from "../../components/modal/ModalTest";
import { Button } from "../../components/button/ButtonTest";

import {
  createRoom
} from "../../api/singleGame/singleGameApi.js";
import {
  createMultiRoom, connectMultiRoom
} from "../../api/multiGame/multiGameApi.js";

export const TestJisoo = () => {
  const [modal, setModal] = useState(false);
  const [multigameModal, setMModal] = useState(true);
  const [multiEnterModal, setMMModal ] = useState(false);
  const [inviteModal, setMMMModal ] = useState(false);

  const singleDesc = "어떤 분야의 뉴스 키워드로 게임을 진행하시겠어요?";

  const makeSingleGame = async ({category}) => {
    console.log(category);
    
    try {
      const response = await createRoom({ category });
      console.log(response);

      if(response.data.data) {
        alert("게임을 만들었습니다");
        // 소켓 연결하기

        // 만들어진 게임으로 이동하기       

      } else {
        alert("게임 만들기 실패");
      }
    } catch (error) {
      console.error("오류 발생:", error);
    }    
  }

  const makeMultiGame = async ({category, nickname}) => {
    console.log(category + " " + nickname);

    try {
      const MultiGameCreateRequest = { category, nickname };
      const response = await createMultiRoom(MultiGameCreateRequest);
      console.log(response);

      if(response.data.data) {
        alert("멀티 게임을 만들었습니다");
        // 소켓 연결하기
        
        // 만들어진 게임으로 이동하기 + 친구 초대 모달 열림

      } else {
        alert("게임 만들기 실패");
      }
    } catch (error) {
      console.error("오류 발생:", error);
    } 
    
  }

  const enterMultiGame = async ({nickname, gameCode}) => {
    console.log(nickname + " " + gameCode);

    if(!nickname || !gameCode) {
      alert("닉네임과 게임 모드는 필수 항목입니다.");
      return;
    } 

    try {
      const MultiGameCreateRequest = { nickname };
      const response = await connectMultiRoom(gameCode, MultiGameCreateRequest);
      console.log(response);

      if(response.data.data) {
        alert("멀티 게임에 입장했습니다");
        // 소켓 연결하기

        // 접속한 페이지로 이동하기

      } else {
        alert("게임 입장 실패");
      }
    } catch (error) {
      console.error("오류 발생:", error);
    }
  }

  const copyCode = ({gameCode}) => {
    console.log(gameCode);
    alert("코드를 복사했습니다. 친구에게 전달해주세요.");
  }


  const navigate = useNavigate();

  const handleButtonClickToGO = (path = '/') => {
    console.log("페이지 이동 경로:", path);
    navigate(path);
  };


  return (
    <div style={{ backgroundColor: "#26154A" }}>
      <Modal
        modalShow={modal}
        setModal={setModal}
        title={singleDesc}
        buttonLabel="게임 만들기"
        func={makeSingleGame}
        desc={"뉴스 카테고리"}
        type="createSingle"
      ></Modal>

      <Modal
        modalShow={multigameModal}
        setModal={setMModal}
        title={"게임 만들기"}
        buttonLabel="게임 만들기"
        func={makeMultiGame}
        desc={"닉네임, 뉴스카테고리"}
        type="createMulti"
      ></Modal>

      <Modal
        modalShow={multiEnterModal}
        setModal={setMMModal}
        title={"게임 입장하기"}
        buttonLabel="게임 입장하기"
        func={enterMultiGame}
        desc={"닉네임, 게임코드"}
        type="enterMulti"
      ></Modal>

      <Modal
        modalShow={inviteModal}
        setModal={setMMModal}
        title={"친구 초대하기"}
        buttonLabel="게임 코드 복사하기"
        func={copyCode}
        desc={"게임코드"}
        type="inviteMulti"
      ></Modal>

      <h1>KEYTRIS</h1>
      <button
        onClick={() => {
          setMMModal(false);
          setMModal(false);
          setModal(true);
        }}
      >
        1인모드 새게임
      </button>
      <button
        onClick={() => {
          setMMModal(false);
          setMModal(true);
          setModal(false);
        }}
      >
        친구와 함께 만들기
      </button>

      <button
        onClick={() => {
          setMMModal(true);
          setMModal(false);
          setModal(false);
        }}
      >
        친구와 함께 입장하기
      </button>
      
      <br />

      <button
        onClick={() => {
          setMMMModal(true);
          setMMModal(false);
          setMModal(false);
          setModal(false);
        }}
      >
        친구 초대하기
      </button>

      <div>
        <Button label="시작하기">시작하기</Button>
      </div>

      <div>
        <div>페이지 이동 테스트 버튼</div>
        <Button label="메인으로" onClick={() => handleButtonClickToGO('/')} />
      </div>
    </div>
  );
};
