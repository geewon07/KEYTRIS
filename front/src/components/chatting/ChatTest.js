import React, { useState, useRef, useEffect } from "react";
import style from "./chat.css";
import { Form, InputGroup } from "react-bootstrap";

function TextChatting() {
  const [inputText, setInputText] = useState("");
  const [chatMessages, setChatMessages] = useState([]);

  // 이 부분 다시 보기 / 채팅을 재참조하는 부분
  const chatAreaRef = useRef();

  useEffect(() => {
    chatAreaRef.current.scrollTop = chatAreaRef.current.scrollHeight;
  }, [chatMessages]);

  const handleInputChange = (event) => {
    setInputText(event.target.value);
  };

  const handleSendMessage = () => {
    if (inputText.trim() !== "") {
      setChatMessages([...chatMessages, { sender: 'user', text: inputText }]);
      setInputText("");
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === "Enter" && !event.shiftKey) {
      handleSendMessage();
      event.preventDefault(); // prevent the default action (newline) from happening
    }
  };

  return (
    <>
      <div>
        <div className={style.chatArea}>
          <div className={style.chatMessages} ref={chatAreaRef}>
            {chatMessages.map((message, index) =>
              message.sender === "admin" ? (
                <p className={style.penaltyChat} key={index}>
                  <strong>
                    플레이어&nbsp;[{message.text.nickName}]&nbsp;::&nbsp;
                    {message.text.penalty}&nbsp; -{message.text.point}점
                  </strong>
                </p>
              ) : (
                <div
                  key={index}
                  className={`${style.messageContainer} `}
                >
                  <p className={`${style.sender}`}>
                  </p>
                  <div className={style.messageBubble}>{message.text}</div>
                </div>
              )
            )}
          </div>
        </div>
        <div className={style.chatInput}>
          <InputGroup>
            <Form.Control
              placeholder="메시지를 입력하세요"
              value={inputText}
              onChange={handleInputChange}
              className={style.inputChat}
              onKeyPress={handleKeyPress}
              style={{
                borderColor: "var(--blue-500)",
                borderTopLeftRadius: "0px",
                borderTopRightRadius: "0px",
                fontSize: "16px",
              }}
            />
            <button
              className={`${style.inputBtn} btn`}
              onClick={handleSendMessage}
            >
              전송
            </button>
          </InputGroup>
        </div>
      </div>
    </>
  );
}

export default TextChatting;