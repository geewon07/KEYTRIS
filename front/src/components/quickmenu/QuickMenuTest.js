
import { useMatch } from 'react-router-dom';

import './QuickMenu.css';
import { Modal3 } from '../modal/Modal3';
import { Modal4 } from '../modal/Modal4';
import { Modal5 } from '../modal/Modal5';
import { useState } from 'react';
// import { useLocation } from 'react-router-dom';
// import MuteIcon from '../../assets/mute.svg';
// import {Modal} from '../modal/ModalTest'

export const QuickMenu = () => {
  const [isModalOpen3, setIsModalOpen3] = useState(false);
  const handleOpenModal3 = () => {
    setIsModalOpen3(true);
  };
  const handleCloseModal3 = () => {
    setIsModalOpen3(false);
  };

  const [isModalOpen4, setIsModalOpen4] = useState(false);
  const handleOpenModal4 = () => {
    setIsModalOpen4(true);
  };
  const handleCloseModal4 = () => {
    setIsModalOpen4(false);
  };

  const openLink = () => {
    window.open('https://www.notion.so/87701a296e5240e69af7bb6fac4a6937', '_blank');
  }

  const match = useMatch('/MultiGame/:gameId');
  const isMultiPage = !!match;

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
              onClick={handleOpenModal3}
            >
              초대
            </button>
            <Modal3 isOpen={isModalOpen3} onClose={handleCloseModal3} />
          </div>
        )}
        <div>
          <button
            className="nav-button"
            onClick={handleOpenModal4}
          >
            가이드
          </button>
          <Modal4 isOpen={isModalOpen4} onClose={handleCloseModal4} />
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
