export const Modal = (props) => {
  let { modalShow, setModal, title, desc, children } = props;
  return (
    <>
      <div hidden={!modalShow}>
        <div className="modal">
          <div className="modal-content">

            <button onClick={() => setModal(false)}>X</button>
      
            <div>
              <h3 style={{alignItems:"center", color:"white"}}>{title}</h3>
              {desc} 모달입니다.
              {children}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
