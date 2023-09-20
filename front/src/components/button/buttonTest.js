import { useState } from "react";

export const Button = (props) => {
  const { label, action } = props;
  const [buttonType, setType] = useState("button");
  const [func, setFunction] = useState();
  switch (action) {
    case "submit":
      setType("submit");
      break;
    case "popup":
      setType("button");
      break;
    case "go":
      setType("button");
      break;
    default:
      break;
  }
  return (
    <>
      <button type={buttonType} onClick={func}>
        {label}
      </button>
    </>
  );
};
