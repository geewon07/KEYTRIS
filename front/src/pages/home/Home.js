import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import keytrisLogo from "../../assets/logo_1.svg";
import { Modal } from "../../components/modal/ModalTest";
import "./Home.css";

import {
  connectMultiRoom,
  createMultiRoom,
} from "../../api/multiGame/MultiGameApi.js";
import { createRoom } from "../../api/singleGame/SingleGameApi.js";

export const Home = () => {
  const navigate = useNavigate();

  const [modal, setModal] = useState(false);
  const [multigameModal, setMModal] = useState(false);
  const [multiEnterModal, setMMModal] = useState(false);
  const singleDesc = "어떤 카테고리의 뉴스로 게임을 하시겠어요?";

  const makeSingleGame = async ({ category }) => {
    console.log(category);

    try {
      const response = await createRoom({ category });
      console.log(response);

      if (response.data.success === "success") {
        console.log("요청 success");
        navigate("/SingleGame", {
          state: { responseData: response.data.data },
        });
      } else {
        alert("게임 만들기를 실패했습니다.");
      }
    } catch (error) {
      const { response } = error;

      // 에러 메시지 매핑
      const errorMessages = {
        "CMO4-ERR-400": "잘못된 요청입니다.",
        // 필요하다면 다른 에러 코드들도 여기에 추가
      };

      console.log(response);

      // 에러 코드에 따른 메시지 출력
      const errorMessage = errorMessages[response?.data?.errorCode];
      if (errorMessage) {
        alert(errorMessage);
      } else {
        alert("게임 입장에 실패했습니다."); // 일반적인 에러 메시지
      }
    }
  };

  const makeMultiGame = async ({ category, nickname }) => {
    console.log(category + " " + nickname);

    if (!nickname) {
      alert("닉네임을 입력해주세요.");
      return;
    }

    try {
      const MultiGameCreateRequest = { category, nickname };
      const response = await createMultiRoom(MultiGameCreateRequest);
      console.log(response);

      if (response.data.success === "success") {
        console.log("요청 success");
        const roomId = response.data.data.gameInfo.roomId;
        navigate(`/MultiGame/${roomId}`, {
          state: { responseData: response.data.data },
        });
      } else {
        alert("게임 만들기 실패");
      }
    } catch (error) {
      const { response } = error;

      // 에러 메시지 매핑
      const errorMessages = {
        "CMO4-ERR-400": "잘못된 요청입니다.",
        // 필요하다면 다른 에러 코드들도 여기에 추가
      };

      console.log(response);

      // 에러 코드에 따른 메시지 출력
      const errorMessage = errorMessages[response?.data?.errorCode];
      if (errorMessage) {
        alert(errorMessage);
      } else {
        alert("게임 입장에 실패했습니다."); // 일반적인 에러 메시지
      }
    }
  };

  const enterMultiGame = async ({ nickname, gameCode }) => {
    console.log("닉네임: " + nickname + " " + gameCode);

    if (!nickname || !gameCode) {
      alert("닉네임과 게임 코드는 필수 항목입니다.");
      return;
    }

    try {
      const MultiGameCreateRequest = { nickname };
      const response = await connectMultiRoom(gameCode, MultiGameCreateRequest);
      console.log(response);

      if (response.data.success === "success") {
        console.log("요청 success");
        const roomId = response.data.data.gameInfo.roomId;
        navigate(`/MultiGame/${roomId}`, {
          state: { responseData: response.data.data },
        });
      } else {
        alert("게임 입장에 실패했습니다.");
      }
    } catch (error) {
      const { response } = error;

      // 에러 메시지 매핑
      const errorMessages = {
        "GA03-ERR-404": "잘못된 게임 코드입니다.",
        "GA04-ERR-403": "입장할 수 없는 게임입니다.",
        // 필요하다면 다른 에러 코드들도 여기에 추가
      };

      console.log(response);

      // 에러 코드에 따른 메시지 출력
      const errorMessage = errorMessages[response?.data?.errorCode];
      if (errorMessage) {
        alert(errorMessage);
      } else {
        alert("게임 입장에 실패했습니다."); // 일반적인 에러 메시지
      }
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
            title={"친구 모드 게임 만들기"}
            buttonLabel="게임 만들기"
            func={makeMultiGame}
            desc={"닉네임, 뉴스카테고리"}
            type="createMulti"
          ></Modal>

          <Modal
            modalShow={multiEnterModal}
            setModal={setMMModal}
            title={"친구 모드 게임 입장하기"}
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
