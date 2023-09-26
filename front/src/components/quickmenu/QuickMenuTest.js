
import './QuickMenu.css';
import { ModalGameCode } from '../modal/ModalGameCode';
import { ModalGuide } from '../modal/ModalGuide';
import { useState } from 'react';
import { useLocation } from 'react-router-dom';
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

  const [isModalOpenGuide, setIsModalOpenGuide] = useState(false);
  const handleOpenModalGuide = () => {
    setIsModalOpenGuide(true);
  };
  const handleCloseModalGuide = () => {
    setIsModalOpenGuide(false);
  };

  const openLink = () => {
    window.open('https://www.notion.so/87701a296e5240e69af7bb6fac4a6937', '_blank');
  }

  const location = useLocation();
  const isMultiPage = location.pathname === '/MultiGame';

  return (
    <div className='sidenav-container'>
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
            <button
              className="nav-button"
              onClick={handleOpenModalGameCode}
            >
              초대
            </button>
            <ModalGameCode isOpen={isModalOpenGameCode} onClose={handleCloseModalGameCode} />
          </div>
        )}
        <div>
          <button
            className="nav-button"
            onClick={handleOpenModalGuide}
          >
            가이드
          </button>
          <ModalGuide isOpen={isModalOpenGuide} onClose={handleCloseModalGuide} />
        </div>
        <div>
          <button
            className="nav-button"
            onClick={openLink}
          >
            소개
          </button>
        </div>
        {/* {modal && <Modal modalShow={title} title={title} setModal={setModal} desc={title} />} */}
      </nav>
    </div>
  );
};
