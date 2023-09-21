import { Stomp } from "@stomp/stompjs";
import axios from "axios";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";

export const SingleGameTest = () => {
  const AXIOS_BASE_URL = "http://localhost:8765/api";
  const [sockJS, setSockJS] = useState();
  const [player, setPlayer] = useState();
  const [room, setRoom] = useState();
  const [words, setWords] = useState([]);
  const [playerStatus, setPStatus] = useState();
  const [roomStatus, setRStatus] = useState();
  const [guess, setGuess] = useState("");
  const [wordList, setWordList] = useState();
  const [score, setScore] = useState();
  const [target, setTarget] = useState("");
  const listing = words
    ?.slice()
    .reverse()
    .map((value, index) => (
      <li key={index} style={{lineHeight: "1.5rem",
      }} className={value === target ? "targetWord" : ""}>
        {value}+{words.length - index}
      </li>
    ));
  console.log(target);
  const sock = new SockJS("http:localhost:8765/keytris");
  const stomp = Stomp.over(sock);
  const handleCreate = () => {
    setSockJS(sock);
    axios.post(`${AXIOS_BASE_URL}/games`, { category: 101 }).then((res) => {
      const statusResponse = res.data.data.StatusResponse;
      console.log(res.data);
      console.log(res.data.data);
      setPlayer(statusResponse.playerId);
      setRoom(statusResponse.roomId);
      setPStatus(statusResponse.playerStatus);
      setRStatus(statusResponse.roomStatus);
      console.log(room);
    });
    console.log(room);
  };

  useEffect(() => {
    if (room != null)
      stomp.connect({}, () => {
        stomp.send("/app/games/room/" + room);
        stomp.subscribe("/topic/room/level-word/" + room, (message) => {
          console.log(message.body);
          const msg =message.body; //JSON.parse(message.body);
          setPStatus(msg.playerStatus);
          setRStatus(msg.roomStatus);
        });
      });
  }, [room]);

  const afterCreate = () => {};
  const handleStart = () => {
    axios
      .post(`${AXIOS_BASE_URL}/games/start`, {
        playerId: player,
        playerStatus: playerStatus,
        roomId: room,
        roomStatus: roomStatus,
      })
      .then((res) => {
        const startResponse = res.data.data.StartResponse;
        const statusResponse = startResponse.statusResponse;
        console.log(res.data);
        console.log(res.data.data);
        setWordList(startResponse.wordListResponse);
        setPStatus(statusResponse.playerStatus);
        setRStatus(statusResponse.roomStatus);
        setWords(startResponse.wordListResponse.subWordList);
        setTarget(startResponse.wordListResponse.targetWord);
        setWords((prev) => [
          ...prev,
          startResponse.wordListResponse.targetWord,
        ]);
        setScore(startResponse.wordListResponse.score);
        // stomp.subscribe("/topic/games/level-word/"+room)
      });
  };
  //   sockJS.onmessage = function (e) {
  //     //   setReceivedData(e.data)
  //     console.log(e.data);
  //   };
  const handleOver = () => {
    // sockJS.close();
    stomp.disconnect();
  };
  // useEffect(() => {
  //   const interval = setInterval(() => {
  //     if(words.length<20){
  //     // Generate a random word and add it to the list
  //     const timestamp = new Date().getSeconds();
  //     const randomWord = timestamp;
  //     // setWords((prevWords) => [ randomWord,...prevWords]);}
  //     setWords((prevWords) => [...prevWords, randomWord]);}

  //   }, 2000); // Add a random word every 2 seconds}

  //   return () => clearInterval(interval); // Cleanup on unmount
  // }, [words.length]);
  const enterWord = () => {
    console.log("시도" + guess);
    getList();
  };
  const getList = () => {
    axios.post(`${AXIOS_BASE_URL}/guess-word`).then((res) => {
      console.log(res.data);
      setWords((prev) => [...prev, res.data]);
    });
  };

  const handleInputChange = (e) => {
    const { value } = e.target;
    setGuess(value);
  };
  return (
    <>
      <div
        style={{
          textAlign: "center",
          alignItems: "center",
          display: "flex",
          flexDirection: "row",
        }}
      >
        <div style={{ width: "45%" }}>
          <h5>
            player :{player} , room :{room}
          </h5>
          <div>
            <button
              onClick={(e) => {
                e.preventDefault();
                handleCreate();
                // onClickConnectBtn();
              }}
            >
              1인 게임 입장
            </button>
          </div>
          <h1>
            status: player {playerStatus}, room {roomStatus}
          </h1>
          <h1 style={{ flex: "none", position: "sticky", top: 0 }}>score:{score}</h1>
          {/* <Menu></Menu> */}
          <div>
            <button
              onClick={(e) => {
                e.preventDefault();
                handleStart();
              }}
            >
              게임 시작
            </button>
            <button onClick={handleOver}>게임 종료/소켓 종료</button>
          </div>
        </div>

        <div
          className="wordlist"
          style={{
            height: "100vh",
            display: "flex",
            width: "25%",
            flexDirection: "column",
            // alignItems:"center",
            justifyContent: "flex-end",
            textAlign: "start",
          }}
        >
   
          <ul style={{ listStyle: "none" }}>{listing}</ul>
          <input
            type="text"
            placeholder="입력하세요"
            value={guess}
            onChange={handleInputChange}
          ></input>
        </div>

        <div>
          <form
            onKeyDown={(e) => {
              if (e.key === "Enter") e.preventDefault();
              enterWord();
            }}
            // onSubmit={enterWord}
          >
            {/* <button type="submit">버튼</button> */}
          </form>
        </div>
      </div>
    </>
  );
};
