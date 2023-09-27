import { useNavigate } from "react-router-dom";
import "./Score.css";
import { overGame, rankPlayer } from "../../api/singleGame/SingleGameApi.js";
import { Button } from "../button/ButtonTest";

function Score() {
  const [overResponse, setOverResponse] = useState(null);
  const [rankList, setRankList] = useState([]);
  const [nickName, setNickName] = useState("");
  const [isSaving, setIsSaving] = useState(false);
  const score = 3000;


  useEffect(() => {
    handleOverGame(overRequestDto);
     // eslint-disable-next-line
  }, []);

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
        {overResponse && <div className="rank-list">{listing}</div>}
        {/* 내 점수 */}
        {typeof overResponse?.record === "boolean" &&
          overResponse.record &&
          !isSaving && (
            <>
              <div className="record">
                내 점수 &nbsp;{score}&nbsp;&nbsp;
                {/* 닉네임 입력란 */}
                <input className="score-nickname-input"
                  type="text"
                  value={nickName}
                  onChange={handleNicknameChange}
                  onKeyPress={handleKeyPress}
                  placeholder="닉네임 입력"
                />
                <button className="score-save-btn" onClick={handleSaveClick}>저장</button>
              </div>
            </>
          )}
        <Button
          label="다시하기"
          onClick={() => handleButtonClickToGO("/")}
        ></Button>
      </div>
    </>
  );
}

export default Score;