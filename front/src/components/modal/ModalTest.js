import React, { useState, useEffect } from 'react';

export const Modal = (props) => {
  let { modalShow, setModal, title, buttonLabel, func, type } = props;
  // console.log(props);

  const [category, setCategory] = useState(100);
  const [nickname, setNickname] = useState("");
  const [gameCode, setGamecode] = useState("");

  useEffect(() => {
    if (modalShow && type === "enterMulti") {
      setGamecode("");  // 모달이 enterMulti 타입으로 열릴 때마다 gameCode 초기화
    }
  }, [modalShow, type]); // modalShow 또는 type이 변경될 때마다 useEffect 실행
  

  const titleStyle = {
    color: "#FFF", // Note: Color should be enclosed in quotes
    textAlign: "center",
    fontSize: "30px",
    fontStyle: "normal",
    fontWeight: 400,
    lineHeight: "50px",
    letterSpacing: "3px",
    alignItems: "center", // This doesn't apply to text elements
    width: "70%",
    wordBreak: "break-all",
    marginBottom: "3rem",
  };

  const contentStyle = {
    display: "flex",
    flexDirection: "column",
    textAlign: "start",
    width: "70%",
    gap: "3rem",
    marginBottom: "2rem",
  };

  return (
    <>
      <div hidden={!modalShow}>
        <div className="modal">
          <div className="modal-content">
            <div style={{ alignSelf: "end" }}>
              <button
                className="modal-close-button"
                onClick={() => setModal(false)}
                style={{}}
              >
                X
              </button>
            </div>

            <div style={titleStyle}>{title}</div>

            <form 
              onSubmit={(e) => {
                e.preventDefault();
                func({ category, nickname, gameCode });
              }}
              style={contentStyle}
            >

              { (type === "createSingle" || type === "createMulti") && 
              <CategorySelect
                selectedCategory = {category}
                onChange={(e) => setCategory(e.target.value)}
              />
              }

              { (type === "createMulti" || type === "enterMulti") && 
                <NicknameInput 
                  value={nickname}
                  onChange={(e) => setNickname(e.target.value)}
                />
              }

              {type === "enterMulti" &&
                <GameCodeInput 
                  value={gameCode}
                  readOnly={false}
                  onChange={(e) => setGamecode(e.target.value)}
                />
              }

              {type === 'inviteMulti' &&
                <GameCodeInput 
                  value={gameCode}
                  readOnly={true}
                />
              }

              <ModalButton 
                label={buttonLabel}
                buttonType="submit"
              />
            </form>
          </div>
        </div>
      </div>
    </>
  );
};

function getByteLength(str) {
  return new Blob([str]).size;
}

export const CategorySelect = (props) => {
  const { selectedCategory, onChange } = props;
  return (
    <>
      <div>
        <label id="category">뉴스 카테고리</label>
          <select
            id="category"
            style={{
              backgroundColor: "#5523BD",
              color: "white",
              fontSize: "30px",
              height: "40px",
              width: "100%",
            }}
            className="Neo"
            value={selectedCategory}
            onChange={onChange}
          >
            <option value={100}>정치</option>
            <option value={101}>경제</option>
            <option value={102}>사회</option>
            <option value={103}>생활/문화</option>
            <option value={104}>세계</option>
            <option value={105}>IT/과학</option>
          </select>
      </div>
    </>
  );
};

export const NicknameInput = (props) => {
  const { value, onChange } = props;

  const handleInputChange = (e) => {
    const inputValue = e.target.value;
    if(getByteLength(inputValue) <= 15) {
      onChange(e);
    }
  }

  return (
    <>
      <div>
        <label id="nicknameInput">닉네임</label>
          <input 
            style={{              
              backgroundColor: "#5523BD",
              color: "white",
              fontSize: "30px",
              height: "40px",
              width: "100%",
            }}
            className="Neo"
            type="text" 
            id="nicknameInput"
            placeholder="닉네임을 입력해주세요."
            value={value} 
            onChange={handleInputChange} 
          />
      </div>
    </>
  );
};

export const GameCodeInput = (props) => {
  const { value, onChange, readOnly } = props;
  return (
    <>
      <div>
        <label id="gameCodeInput">게임 코드</label>
          <input 
            style={{              
              backgroundColor: "#5523BD",
              color: "white",
              fontSize: "30px",
              height: "40px",
              width: "100%",
            }}
            className="Neo"
            type="text" 
            id="gameCodeInput"
            placeholder="게임 코드를 입력해주세요."
            value={value} 
            readOnly={readOnly}
            onChange={readOnly ? null : onChange}
          />
      </div>
    </>
  );
};

export const ModalButton = (props) => {
  const { label, buttonType = "button" } = props;

  return (
    <>
      <div className="modal-button-layout">
        <button className="modal-button-style" type={buttonType}>
          <span className="modal-button-text">{label}</span>
        </button>
      </div>
    </>
  );
};

