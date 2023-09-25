import React, { useState, useEffect } from "react";
import Swal from "sweetalert2";

export const Modal3 = ({ isOpen, onClose }) => {
  const [address, setAddress] = useState("");

  useEffect(() => {
    setAddress(window.location.href);
  }, []);

  if (!isOpen) return null;

  const copyAddress = () => {
    navigator.clipboard.writeText(address);
    Swal.fire({
      icon: "success",
      title: "주소 복사 완료",
      text: "주소가 클립보드에 복사되었습니다.",
    });
  };

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
  };

  return (
    <>
      <div>
        <div className="modal">
          <div className="modal-content">
            <div style={{ alignSelf: "end" }}>
              <button className="modal-close-button" onClick={onClose}>
                X
              </button>
            </div>

            <div style={titleStyle}>
              게임 코드를 복사하여<br></br> 친구들에게 공유해주세요!
            </div>
            <div style={contentStyle}>
              <label htmlFor="gamecode">게임 코드</label>
              <input
                type="text"
                id="gamecode"
                name="gamecode"
                defaultValue={address}
                readOnly
                style={{
                  backgroundColor: "#5523BD",
                  color: "white",
                  fontSize: "30px",
                  height: "40px",
                  width: "100%",
                }}
                className="Neo"
              />
              <ModalButton onClick={copyAddress}></ModalButton>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export const ModalButton = ({ onClick }) => {
  return (
    <>
      <div className="modal-button-layout">
        <button className="modal-button-style" onClick={onClick}>
          <span className="modal-button-text">게임 코드 복사</span>
        </button>
      </div>
    </>
  );
};
