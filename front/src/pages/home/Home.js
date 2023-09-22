import React, { useState } from "react";
import keytrisLogo from "../../assets/logo_1.svg";
import { Modal } from "../../components/modal/ModalTest";
import "./Home.css";
import { QuickMenu } from "../../components/quickmenu/quickMenuTest";

export const Home = () => {
  const [modal, setModal] = useState(false);
  const [multigameModal, setMModal] = useState(false);
  const singleDesc = "어떤 분야의 뉴스 키워드로 게임을 진행하시겠어요?";

  return (
    // <div id="keytris_title" className="keytris_main_gradient">
    <div id="keytris_title">
      {/* <div className="main-logo-layout">
      </div> */}
        <img className="main_logo_image" alt="logo_1" src={keytrisLogo} />
      <div className="main_selection_div">
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
                  <div>
                    <button
                      className="main_selection_button"
                      onClick={() => {
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
                    <div
                      className="main_selection_div"
                      onClick={() => {
                        setMModal(true);
                        setModal(false);
                      }}
                    >
                      친구와 함께
                    </div>
                  </div>
                  <div>
                    <button
                      className="main_selection_button"
                      onClick={() => {
                        setMModal(false);
                        setModal(true);
                      }}
                    >
                      새 게임
                    </button>
                  </div>
                  <div>
                    <button className="main_selection_button">게임 참여</button>
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      {/* <div style={{ backgroundColor: "#26154A" }}>
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
      </div> */}
    </div>
  );
};
