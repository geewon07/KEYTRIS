import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Score.css";
import { overGame, rankPlayer } from "../../api/singleGame/SingleGameApi.js";
import { Button } from "../button/ButtonTest";

function Score({ overRequestDto }) {
  const [overResponse, setOverResponse] = useState(null);
  const [rankList, setRankList] = useState([]);
  const [nickName, setNickName] = useState("");
  const [isSaving, setIsSaving] = useState(false);
  const [score, setScore] = useState("");
  const [roomId, setRoomId] = useState("");

  useEffect(() => {
    if (overRequestDto) {
      setRoomId(overRequestDto.roomId);
      setScore(overRequestDto.score);
      handleOverGame(overRequestDto);
    }
  }, [overRequestDto]);

  const navigate = useNavigate();

  const handleButtonClickToGO = (path = "/") => {
    //console.log("페이지 이동 경로:", path);
    navigate(path);
  };

  const handleOverGame = async (overRequestDto) => {
    try {
      const res = await overGame(overRequestDto);
      const overResponseDto = res.data.data.OverResponse;
      setOverResponse(overResponseDto);
      setRankList(overResponseDto.recordList);
      // //console.log("overRequestDto", overRequestDto);
    } catch (error) {
      console.error(error);
    }
  };

  const rankRequestDto = {
    roomId: roomId,
    nickname: nickName,
  };

  const handleInsertRank = async (rankRequestDto) => {
    // //console.log(rankRequestDto);
    try {
      const res = await rankPlayer(rankRequestDto);
      const rankResponseDto = res.data.data.RankingResponse;
      setRankList(rankResponseDto);
    } catch (error) {
      console.error(error);
    }
  };


  function getByteLength(str) {
    return new Blob([str]).size;
  }

  // 닉네임 입력 핸들러
  const handleNicknameChange = (event) => {
    const inputValue = event.target.value;
    if (getByteLength(inputValue) <= 15) {
      setNickName(event.target.value);
    }
  };

  // 저장 버튼 클릭 핸들러
  const handleSaveClick = () => {
    setIsSaving(true);
    handleInsertRank(rankRequestDto);
  };

  const handleKeyPress = (event) => {
    if (event.key === "Enter" && !event.shiftKey) {
      handleSaveClick();
      event.preventDefault();
    }
  };

  const listing = rankList?.map((value, index) => (
    <div key={index} className="rank-item">
      <span className="rank-index" style={{ textAlign: "right" }} >{index + 1}위</span>
      <span className="rank-score" style={{ textAlign: "right" }} >{value.score}점</span>
      <span className="rank-nickname" style={{ textAlign: "right" }} >{value.nickname}</span>
    </div>
  ));

  return (
    <>
      <div className="my-score-display">
        <div className="list-title">점수
          <hr />
        </div>
        {overResponse && <div className="rank-list">{listing}</div>}
        {/* 내 점수 */}
        <div className="record">
          내 점수 &nbsp;&nbsp;{score}점&nbsp;&nbsp;
          {/* 닉네임 입력란 */}
          {typeof overResponse?.record === "boolean" &&
            overResponse.record &&
            !isSaving && (
              <>
                <input
                  className="score-nickname-input"
                  type="text"
                  value={nickName}
                  onChange={handleNicknameChange}
                  onKeyPress={handleKeyPress}
                  placeholder="닉네임 입력"
                />
                <button className="score-save-btn" onClick={handleSaveClick}>
                  저장
                </button>
              </>
            )}
        </div>

        <Button
          label="다시하기"
          onClick={() => handleButtonClickToGO("/")}
        ></Button>
      </div>
    </>
  );
}

export default Score;
