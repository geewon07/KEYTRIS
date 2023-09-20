export const Modal = (props) => {
    let { modalShow, setModal, title, desc, children } = props;
  
    const titleStyle = {
      color: "#FFF", // Note: Color should be enclosed in quotes
      textAlign: "center",
      fontSize: "30px",
      fontStyle: "normal",
      fontWeight: 400,
      lineHeight: "50px",
      letterSpacing: "3px",
      alignItems: "center", // This doesn't apply to text elements
      width: "60%",
      wordBreak: "break-all",
      marginBottom: "90px",
    };
    return (
      <>
        <div hidden={!modalShow}>
          <div className="modal">
            <div className="modal-content">
              <div style={{ alignSelf: "end" }}>
                <button onClick={() => setModal(false)} style={{}}>
                  X
                </button>
              </div>
  
              <div style={titleStyle}>{title}</div>
  
              <CategorySelect></CategorySelect>
            </div>
          </div>
        </div>
      </>
    );
  };
  export const CategorySelect = () => {
    return (
      <>
        <div style={{ textAlign: "start", width: "70%" }}>
          <label for="category">뉴스 카테고리</label>
          <form>
            <select
              form="category"
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
  