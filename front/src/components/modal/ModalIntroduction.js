export const ModalIntroduction = ({ isOpen, onClose }) => {

  if (!isOpen) return null;

  const titleStyle = {
    color: "#FFF", 
    textAlign: "center",
    fontSize: "30px",
    fontStyle: "normal",
    fontWeight: 400,
    lineHeight: "50px",
    letterSpacing: "3px",
    alignItems: "center", 
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
    fontSize: "1.5rem",
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

            <div style={titleStyle}>키트리스 소개</div>
            <div style={contentStyle}>
              [키트리스]<br /><br />
                제시어들 중 타겟어와 가장 유사한 단어를 입력해 단어를 제거하고 점수를 획득하세요!
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
