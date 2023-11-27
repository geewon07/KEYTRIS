import "./ButtonTest.css";

export const Button = (props) => {
  const { label, onClick } = props;

  // const { label, action } = props;
  // const [buttonType, setType] = useState("button");
  // const [func, setFunction] = useState();
  // switch (action) {
  //   case "submit":
  //     setType("submit");
  //     break;
  //   case "popup":
  //     setType("button");
  //     bre
  //   case "go":
  //     setType("button");
  //     break;
  //   default:
  //     break;
  // }
  return (
    <div className="large-button-layout">
      <button className="large-button-style" onClick={onClick}>
        <span className="large-button-text">{label}</span>
      </button>
    </div>
  );
};
