import { useState } from "react";
import "./ButtonTest.css";

export const Button = (props) => {
  const { label } = props;
  // const { label, action } = props;
  // const [buttonType, setType] = useState("button");
  // const [func, setFunction] = useState();
  // switch (action) {
  //   case "submit":
  //     setType("submit");
  //     break;
  //   case "popup":
  //     setType("button");
  //     break;
  //   case "go":
  //     setType("button");
  //     break;
  //   default:
  //     break;
  // }
  return (
    <div className="large-button-layout">
      {/* <button className="modal-button-style" type={buttonType} onClick={func}> */}
      <button className="large-button-style">
        <span className="large-button-text">{label}</span>
      </button>
    </div>
  );
};
