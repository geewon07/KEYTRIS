import React, { useState, useRef, useEffect } from "react";
import "./Chat.css";
import { Form, InputGroup } from "react-bootstrap";

function TextChatting({ onSendMessage, chatContent, playerList, myplayerId }) {
  const [inputText, setInputText] = useState("");
  const [chatMessages, setChatMessages] = useState([]);

  const chatAreaRef = useRef();

  useEffect(() => {
    chatAreaRef.current.scrollTop = chatAreaRef.current.scrollHeight;
  }, [chatMessages]);

  useEffect(() => {
    chatAreaRef.current.scrollTop = chatAreaRef.current.scrollHeight;
    if (chatContent && chatContent.playerId !== "notification") {
      const matchedPlayer = playerList.find(
        (player) => player.playerId === chatContent.playerId
      );
      const getNickName = matchedPlayer ? matchedPlayer.nickname : "null";

      setChatMessages((prevMessages) => [
        ...prevMessages,
        {
          sender: getNickName,
          text: chatContent.content,
          playerId: chatContent.playerId,
        },
      ]);
    } else {
      setChatMessages((prevMessages) => [
        ...prevMessages,
        { sender: "notification", text: chatContent.content },
      ]);
    }
    // eslint-disable-next-line
  }, [chatContent]);

  const handleInputChange = (event) => {
    setInputText(event.target.value);
  };

  const handleSendMessage = () => {
    if (inputText.trim() !== "") {
      onSendMessage(inputText); // 입력된 메시지 전달
      // setChatMessages([...chatMessages, { sender: "user", text: inputText }]);
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
        <div className="chatWrapper">
          <div className="chatArea">
            <div className="chatMessages" ref={chatAreaRef}>
              {chatMessages.map((message, index) =>
                message.sender === "notification" ? (
                  <p className="NotificationChat" key={index}>
                    <strong>{message.text}</strong>
                  </p>
                ) : (
                  <div
                    key={index}
                    className={`${
                      message.playerId === myplayerId
                        ? "messageContainer userMessage"
                        : "messageContainer otherMessage"
                    }`}
                  >
                    <div>
                      {message.playerId === myplayerId ? "나" : message.sender}
                    </div>
                    <div>{message.text}</div>
                  </div>
                )
              )}
            </div>
          </div>
          <div className="chatInput">
            <InputGroup style={{ flexGrow: 1 }}>
              {" "}
              {/* Changed */}
              <Form.Control
                placeholder="메시지를 입력하세요"
                value={inputText}
                onChange={handleInputChange}
                className={"inputChat"}
                onKeyPress={handleKeyPress}
                style={{
                  borderColor: "var(--blue-500)",
                  borderTopLeftRadius: "0px",
                  borderTopRightRadius: "0px",
                  fontSize: "16px",
                }}
              />
            </InputGroup>

            <button className={"input"} onClick={handleSendMessage}>
              전송
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default TextChatting;
