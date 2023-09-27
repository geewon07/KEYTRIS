export const ModalInvite = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

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
      <div>
        <div className="modal">
          <div className="modal-content">
            <div style={{ alignSelf: "end" }}>
              <button
                className="modal-close-button"
                onClick={onClose}
                style={{}}
              >
                X
              </button>
            </div>

            <div style={titleStyle}></div>
            <div style={contentStyle}>
              <label for="nickname">닉네임</label>
              <input
                type="text"
                id="nickname"
                name="nickname"
                placeholder="닉네임을 입력하세요"
                style={{
                  backgroundColor: "#5523BD",
                  color: "white",
                  fontSize: "30px",
                  height: "40px",
                  width: "100%",
                }}
                className="Neo"
              />
              <label for="gamecode">게임 코드</label>
              <input
                type="text"
                id="gamecode"
                name="gamecode"
                placeholder="게임 코드를 입력하세요"
                style={{
                  backgroundColor: "#5523BD",
                  color: "white",
                  fontSize: "30px",
                  height: "40px",
                  width: "100%",
                }}
                className="Neo"
              />
              <ModalButton></ModalButton>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export const ModalButton = () => {
  return (
    <>
      <div className="modal-button-layout">
        {/* <button className="modal-button-style" type={buttonType} onClick={func}> */}
        <button className="modal-button-style">
          <span className="modal-button-text">입장하기</span>
        </button>
      </div>
    </>
  );
};
