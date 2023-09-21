import React, { useState } from "react";
import keytrisLogo from '../../assets/logo_1.svg'
import {Modal} from '../../components/modal/ModalTest'
import './Home.css'



export const Home = () => {
  const [modal, setModal] = useState(false);

  return (
    // <div id="keytris_title" className="keytris_main_gradient">
    <div id="keytris_title">
      <img className="main_logo_image" alt="logo_1" src={keytrisLogo}/>
      <div className="main_selection_div">
        <div>
          <text className="main_selection_desc">
            게임으로 배우는 오늘의 기사 키워드
          </text>
        </div>
        <table className="main_selection_table">
          <tr className="main_selection_table_tr">
            <td className="main_selection_table_td">
              <div>
                <div>
                  <text className="main_selection_text">
                    1인모드
                  </text>
                </div>
                <div>
                  <button className="main_selection_button" onClick={() => {setModal(true);}}>
                      새 게임
                  </button>
                </div>
              </div>
            </td>
            <td className="main_selection_table_td">
              <div>
                <div>
                  <text className="main_selection_text">
                    친구와 함께
                  </text>
                </div>
                <div>
                  <button className="main_selection_button">
                    새 게임
                  </button>
                </div>
                <div>
                  <button className="main_selection_button">
                    게임 참여
                  </button>
                </div>
              </div>
            </td>
          </tr>
        </table>
      </div>
      {modal && <Modal modalShow="single" setModal={setModal} title="어떤 분야의 뉴스 키워드로 게임을 진행하시겠어요?" desc="single play!"/>}
    </div>
  );
  
}; 