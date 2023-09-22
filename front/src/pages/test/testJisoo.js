import React, { useState } from "react";
import { Modal } from "../../components/modal/ModalTest";
import { Button } from "../../components/button/buttonTest";

export const TestJisoo = () => {
  const [modal, setModal] = useState(true);
  const [multigameModal, setMModal] = useState(false);
  const singleDesc = "어떤 분야의 뉴스 키워드로 게임을 진행하시겠어요?";

  return (
    <div style={{ backgroundColor: "#26154A" }}>
      <Modal
        modalShow={modal}
        setModal={setModal}
        title={singleDesc}
        buttonLabel="게임 만들기"
        desc={"뉴스 카테고리"}
      ></Modal>
      <Modal
        modalShow={multigameModal}
        setModal={setMModal}
        title={"게임 만들기"}
        buttonLabel="게임 만들기"
        desc={"닉네임, 뉴스카테고리"}
      ></Modal>
      <h1>KEYTRIS</h1>
      <button
        onClick={() => {
          setMModal(false);
          setModal(true);
        }}
      >
        1인모드 새게임
      </button>
      <button
        onClick={() => {
          setMModal(true);
          setModal(false);
        }}
      >
        친구와 함께
      </button>

      <div>
        <Button label="시작하기">시작하기</Button>
      </div>
    </div>
  );
};
