import React, { useState } from "react";
import { Modal } from "../../components/modal/ModalTest";

export const Home = () => {
  const [modal, setModal] = useState(false);
  const [multigameModal, setMModal] = useState(false);

  return (
    <div>
      <Modal
        modalShow={modal}
        setModal={setModal}
        title={"카테고리 선택"}
        desc={"뉴스 카테고리"}
        children={<CategorySelect />}
      ></Modal>
      <Modal
        modalShow={multigameModal}
        setModal={setMModal}
        title={"게임 만들기"}
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
    </div>
  );
};
export const CategorySelect = () => {
  return (
    <>
      <form>
        <select form="category" style={{b}}>
          <option value={100}>정치</option>
          <option value={101}>경제</option>
          <option value={102}>사회</option>
          <option value={103}>생활/문화</option>
          <option value={104}>세계</option>
          <option value={105}>IT/과학</option>
        </select>
      </form>
    </>
  );
};
