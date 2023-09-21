import './quickMenu.css'
import MuteIcon  from '../../assets/mute.svg'
import {Modal} from '../modal/ModalTest'
import { useState } from 'react';

export const QuickMenu = () => {
    const [modal, setModal] = useState(false);
    const [title, setTitle] = useState(false);
    const [desc, setDesc] = useState(false);
    return (
        <nav>
            <div>
                <button className='nav-button' onClick={() => {
                    console.log('음소거')
                    }}>
                    <img src={MuteIcon}/>
                </button>
           </div>
           <div>
            <button className='nav-button' onClick={() => {
                setModal(true)
                setTitle('게임 코드를 복사하여 친구에게 공유해주세요!');
                setDesc('invite');
                }}>
                초대
            </button>
           </div>
           <div>
            <button className='nav-button' onClick={() => {
                setModal(true);
                setTitle('가이드가 들어가는데 바뀐다.')
                }}>
                가이드
            </button>
           </div>
           <div>
            <button className='nav-button' onClick={() => {
                setModal(true)
                setTitle('여기는 소개모달이 들어간다.')
                }}>
                소개
            </button>
           </div>
           {modal && <Modal modalShow={title} title={title} setModal={setModal} desc={title} />}
        </nav>
    );
    
  }; 