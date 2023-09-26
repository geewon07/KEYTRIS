import React, { useState } from "react";
import keytrisLogo from "../../assets/logo_1.svg";
import { Modal } from "../../components/modal/ModalTest";
import "./Home.css";

import {
  connectMultiRoom,
  createMultiRoom,
} from "../../api/multiGame/MultiGameApi.js";
import { createRoom } from "../../api/singleGame/SingleGameApi.js";

export const Home = () => {
  const [modal, setModal] = useState(false);
  const [multigameModal, setMModal] = useState(false);
  const [multiEnterModal, setMMModal] = useState(false);
  const singleDesc = "어떤 분야의 뉴스 키워드로 게임을 진행하시겠어요?";

  const makeSingleGame = async ({ category }) => {
    console.log(category);

    try {
      const response = await createRoom({ category });
      console.log(response);

      if (response.data.data) {
        alert("게임을 만들었습니다");
        // 소켓 연결하기

        // 만들어진 게임으로 이동하기
      } else {
        alert("게임 만들기 실패");
      }
    } catch (error) {
      console.error("오류 발생:", error);
    }
  };

  const makeMultiGame = async ({ category, nickname }) => {
    console.log(category + " " + nickname);

    try {
      const MultiGameCreateRequest = { category, nickname };
      const response = await createMultiRoom(MultiGameCreateRequest);
      console.log(response);

      if (response.data.data) {
        alert("멀티 게임을 만들었습니다");
        // 소켓 연결하기

        // 만들어진 게임으로 이동하기 + 친구 초대 모달 열림
      } else {
        alert("게임 만들기 실패");
      }
    } catch (error) {
      console.error("오류 발생:", error);
    }
  };

  const enterMultiGame = async ({ nickname, gameCode }) => {
    console.log(nickname + " " + gameCode);

    if (!nickname || !gameCode) {
      alert("닉네임과 게임 모드는 필수 항목입니다.");
      return;
    }

    try {
      const MultiGameCreateRequest = { nickname };
      const response = await connectMultiRoom(gameCode, MultiGameCreateRequest);
      console.log(response);

      if (response.data.data) {
        alert("멀티 게임에 입장했습니다");
        // 소켓 연결하기

        // 접속한 페이지로 이동하기
      } else {
        alert("게임 입장 실패");
      }
    } catch (error) {
      console.error("오류 발생:", error);
    }
  };

  return (

    <div className="home-content-container">
      {/* // <div id="keytris_title" className="keytris_main_gradient"> */}
      <div id="keytris_title">
        {/* <div className="main-logo-layout">
      </div> */}
        <img className="main_logo_image" alt="logo_1" src={keytrisLogo} />
        <div>
          <div>
            <div className="main_selection_desc">
              게임으로 배우는 오늘의 기사 키워드
            </div>
          </div>
          <table className="main_selection_table">
            <tbody>
              <tr className="main_selection_table_tr">
                <td className="main_selection_table_td">
                  <div>
                    <div>
                      <div className="main_selection_div">1인모드</div>
                    </div>
                    <div className="button_box">
                      <button
                        className="main_selection_button"
                        onClick={() => {
                          setMMModal(false);
                          setMModal(false);
                          setModal(true);
                        }}
                      >
                        새 게임
                      </button>
                    </div>
                  </div>
                </td>
                <td className="main_selection_table_td">
                  <div>
                    <div>
                      <div className="main_selection_div">친구와 함께</div>
                    </div>
                    <div>
                      <button
                        className="main_selection_button"
                        onClick={() => {
                          setMMModal(false);
                          setMModal(true);
                          setModal(false);
                        }}
                      >
                        새 게임
                      </button>
                    </div>
                    <div>
                      <button
                        className="main_selection_button"
                        onClick={() => {
                          setMMModal(true);
                          setMModal(false);
                          setModal(false);
                        }}
                      >
                        게임 참여
                      </button>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        {/* {modal && <Modal modalShow={title} title={title} setModal={setModal} desc={title} />} */}
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
        </div>
      </div>
    </div>

  );
};
