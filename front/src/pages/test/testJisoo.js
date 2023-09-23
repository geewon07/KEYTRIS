import React, { useState } from "react";
import { Modal } from "../../components/modal/ModalTest";
import { Button } from "../../components/button/buttonTest";

export const TestJisoo = () => {
  const [modal, setModal] = useState(false);
  const [multigameModal, setMModal] = useState(true);
  const [multiEnterModal, setMMModal ] = useState(false);
  const [inviteModal, setMMMModal ] = useState(false);

  const singleDesc = "어떤 분야의 뉴스 키워드로 게임을 진행하시겠어요?";

  const makeSingleGame = ({category}) => {
    console.log("게임 시작 버튼 클릭");
    console.log(category);
    alert("게임을 시작합니다");
  }

  const makeMultiGame = ({category, nickname}) => {
    console.log("멀티 게임 시작 버튼 클릭");
    console.log(category + " " + nickname);
    alert("멀티 게임 방을 만듭니다");
  }

  const enterMultiGame = ({nickname, gameCode}) => {
    console.log("멀티 게임 입장 버튼 클릭");
    console.log(nickname + " " + gameCode);
    alert("멀티 게임에 입장합니다");
  }

  const copyCode = ({gameCode}) => {
    console.log("멀티 게임 초대 코드 복사");
    console.log(gameCode);
    alert("코드를 복사했습니다. 친구에게 전달해주세요.");
  }

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
    </div>
  );
};
