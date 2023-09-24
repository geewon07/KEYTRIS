export const Modal4 = ({ isOpen, onClose }) => {

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
    fontSize: "1rem",
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

            <div style={titleStyle}>키트리스 가이드</div>
            <div style={contentStyle}>
              * 1인 모드<br /><br />
              [새 게임] 버튼을 클릭 <br /><br />
              [카테고리]에서 게임의 타겟어가 나올 카테고리 선택 <br /><br />
              [게임 시작]<br /><br />
                주어진 제시어들과 비교해 타겟어에 더 유사한 단어를 이력하세요<br /><br />
                입력단어와 유사도 순으로 제시어들이 재정렬됩니다<br /><br />
                타겟어가 목표 순위내에 들어온다면 단어가 제거되고 점수를 얻을 수 있습니다<br /><br />
                2초마다 새로운 제시어가 추가됩니다<br /><br />
                제시어들이 상한선에 도달하지 않도록 타겟어와 가장 유사한 단어를 입력해 단어를 제거하세요<br /><br />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
