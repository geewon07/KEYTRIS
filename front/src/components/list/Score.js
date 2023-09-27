import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Score.css";
import { overGame, rankPlayer } from "../../api/singleGame/SingleGameApi.js";
import { Button } from "../button/ButtonTest";

function Score({ playerId, playerResultList }) {
  const [overResponse, setOverResponse] = useState(null);
  const [rankList, setRankList] = useState([]);
  const [nickName, setNickName] = useState("");
  const [isSaving, setIsSaving] = useState(false);
  const score = 3000;


  useEffect(() => {
    setOverResponse({
      recordList: [
          {
              nickname: "dfdf",
              score: 10000
          },
          {
              nickname: "dddd",
              score: 3000
          },
          {
              nickname: "dgdgd",
              score: 400
          },
          {
              nickname: "ㅎㅇㅎㅇ",
              score: 30
          },
          {
              "nickname": "ㅎㅎㅎㅎ",
              "score": 40
          }
      ],
      statusResponse: {
          playerId: "8b16bae2-87ee-4c8a-95dc-32ee7f62986e",
          playerStatus: "OVER",
          roomId: "891cd6fd-e7f2-4b67-a14c-80704c2eb59c",
          roomStatus: "FINISHED"
      },
      record: true
  })
    // handleOverGame(overRequestDto);
     // eslint-disable-next-line
  }, []);

  useEffect(() => {
    if(overResponse !== null) {
    setRankList(overResponse.recordList);
    }

  }, [overResponse]);

  const navigate = useNavigate();

  const handleButtonClickToGO = (path = "/") => {
    console.log("페이지 이동 경로:", path);
    navigate(path);
  };

  const overRequestDto = {
    roomId: "2fd4e586-a402-418c-916d-38f291758b72",
    lastWord: "초콜릿",
    score: 0,
  };

  const handleOverGame = async (overRequestDto) => {
    try {
      const res = await overGame(overRequestDto);
      const overResponseDto = res.data.data.OverResponse;
      setOverResponse(overResponseDto);
      setRankList(overResponseDto.recordList);
    } catch (error) {
      console.error(error);
    }
  };

  const rankRequestDto = {
    roomId: "2fd4e586-a402-418c-916d-38f291758b72",
    nickname: nickName,
  };

  const handleInsertRank = async (rankRequestDto) => {
    try {
      const res = await rankPlayer(rankRequestDto);
      const rankResponseDto = res.data.data.RankingResponse;
      setRankList(rankResponseDto);
    } catch (error) {
      console.error(error);
    }
  };

  // 닉네임 입력 핸들러
  const handleNicknameChange = (event) => {
    setNickName(event.target.value);
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
    <div key={index} style={{ lineHeight: "1.5rem" }} className="rank-index">
      {index + 1}위&nbsp;&nbsp;{value.score}&nbsp;&nbsp;
      {value.nickname}
    </div>
  ));

  return (
    <>
      <div className="my-score-display">
        <div className="list-title">점수</div>
        <hr />
        {overResponse && <div className="rank-list" style={{ lineHeight: "1.5rem", wordSpacing: '1.5rem', }} >{listing}</div>}
        {/* 내 점수 */}
        <div className="record" style={{ lineHeight: "2rem", marginTop: '1rem', marginBottom: '1rem', fontSize: '1.5rem', color: '#CCFF00', WebkitTextStroke: 'initial', wordSpacing: '1.7rem', }}>
          내 점수 &nbsp;{score}&nbsp;&nbsp;
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