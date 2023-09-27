import "./QuickMenu.css";
import { ModalGameCode } from "../modal/ModalGameCode";
import { ModalGameRule } from "../modal/ModalGameRule";
import { ModalGuide } from "../modal/ModalGuide";
import { useState } from "react";
import { useMatch } from "react-router-dom";
import { useLocation } from "react-router-dom";
// import MuteIcon from '../../assets/mute.svg';
// import {Modal} from '../modal/ModalTest'

export const QuickMenu = () => {
  const [isModalOpenGameCode, setIsModalOpenGameCode] = useState(false);
  const handleOpenModalGameCode = () => {
    setIsModalOpenGameCode(true);
  };
  const handleCloseModalGameCode = () => {
    setIsModalOpenGameCode(false);
  };

  const [isModalOpenGameRule, setIsModalOpenGameRule] = useState(false);
  const handleOpenModalGameRule = () => {
    setIsModalOpenGameRule(true);
  };
  const handleCloseModalGameRule = () => {
    setIsModalOpenGameRule(false);
  };

  const [isModalOpenGuide, setIsModalOpenGuide] = useState(false);
  const handleOpenModalGuide = () => {
    setIsModalOpenGuide(true);
  };
  const handleCloseModalGuide = () => {
    setIsModalOpenGuide(false);
  };

  const openLink = () => {
    window.open(
      "https://www.notion.so/87701a296e5240e69af7bb6fac4a6937",
      "_blank"
    );
  };

  const location = useLocation();
  // const isGamePage = [
  //   "/MultiGame",
  //   "/multiGame",
  //   "/multigame",
  //   "/SingleGame",
  //   "/singleGame",
  //   "/singlegame",
  // ].includes(location.pathname.toLowerCase());
  // const isMultiPage = ["/MultiGame", "/multiGame", "/multigame"].includes(
  //   location.pathname.toLowerCase()
  // );
  const isGamePage = ["/SingleGame", "/singleGame", "/singlegame"].includes(
    location.pathname.toLowerCase()
  );
  const multiMatch = useMatch("/MultiGame/:gameId");
  const isMultiPage = !!multiMatch;

  return (
    <div className="sidenav-container">
      <nav>
        {/* <div>
        <button className='nav-button' onClick={() => {
          console.log('음소거')
        }}>
          <img src={MuteIcon} alt='muteIcon'/>
        </button>
      </div> */}
        {isMultiPage && (
          <div>
            <button className="nav-button" onClick={handleOpenModalGameCode}>
              초대
            </button>
            <ModalGameCode
              isOpen={isModalOpenGameCode}
              onClose={handleCloseModalGameCode}
            />
          </div>
        )}
        {isGamePage || isMultiPage ? (
          <div>
            <button className="nav-button" onClick={handleOpenModalGameRule}>
              게임 규칙
            </button>
            <ModalGameRule
              isOpen={isModalOpenGameRule}
              onClose={handleCloseModalGameRule}
            />
          </div>
        ) : (
          <div>
            <button className="nav-button" onClick={handleOpenModalGuide}>
              가이드
            </button>
            <ModalGuide
              isOpen={isModalOpenGuide}
              onClose={handleCloseModalGuide}
            />
          </div>
        )}
        <div>
          <button className="nav-button" onClick={openLink}>
            소개
          </button>
        </div>
        {/* {modal && <Modal modalShow={title} title={title} setModal={setModal} desc={title} />} */}
      </nav>
    </div>
  );
};
