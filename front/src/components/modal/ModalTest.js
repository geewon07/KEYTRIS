export const Modal = (props) => {
  let { modalShow, setModal, title, buttonLabel, desc, children } = props;

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
            <div style={contentStyle}>
              <CategorySelect></CategorySelect>
              <ModalButton label={buttonLabel}></ModalButton>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export const CategorySelect = () => {
  return (
    <>
      <div>
        <label id="category">뉴스 카테고리</label>
        <form>
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
          >
            <option value={100}>정치</option>
            <option value={101}>경제</option>
            <option value={102}>사회</option>
            <option value={103}>생활/문화</option>
            <option value={104}>세계</option>
            <option value={105}>IT/과학</option>
          </select>
        </form>
      </div>
    </>
  );
};

export const ModalButton = (props) => {
  const { label } = props;
  return (
    <>
      <div className="modal-button-layout">
        {/* <button className="modal-button-style" type={buttonType} onClick={func}> */}
        <button className="modal-button-style">
          <span className="modal-button-text">{label}</span>
        </button>
      </div>
    </>
  );
};
