import { Stomp } from "@stomp/stompjs";
import axios from "axios";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";

export const SingleGame = () => {
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
  const listing = words
    ?.slice()
    .reverse()
    .map((value, index) => (
      <li key={index}>
        {value}+{words.length - index}
      </li>
    ));
    
    const sock = new SockJS("http:localhost:8765/keytris");
    const stomp = Stomp.over(sock);
  const handleCreate = () => {
   
    setSockJS(sock);
    axios
      .post(`${AXIOS_BASE_URL}/games`, { category: 101 })
      .then((res) => {
        const statusResponse = res.data.data.StatusResponse;
        console.log(res.data);
        console.log(res.data.data);
        setPlayer(statusResponse.playerId);
        setRoom(statusResponse.roomId);
        setPStatus(statusResponse.playerStatus);
        setRStatus(statusResponse.roomStatus);     
        console.log(room)
       
      })
        console.log(room);
    
      

    sock.onmessage = function (e) {
      //   setReceivedData(e.data)
      console.log(e.data);
    };
    
  };

  useEffect(() => {
    if(room!=null)
    stomp.connect({}, () => {
         stomp.send("/app/games/room/"+room);
         stomp.subscribe("/topic/games/room/"+room,(message)=>{
           console.log(message.body)
           const msg = JSON.parse(message.body)
           setPStatus(msg.playerStatus);
           setRStatus(msg.roomStatus);
         });
       })
  }, [room])

  

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
        setWords((prev) => [
          ...prev,
          startResponse.wordListResponse.targetWord,
        ]);
        setScore(startResponse.wordListResponse.score);
        // stomp.subscribe("/topic/games/level-word/"+room)
      });
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
      {/* <Stest></Stest> */}
      {/* <div>
          <a href="https://vitejs.dev" target="_blank">
            <img src={viteLogo} className="logo" alt="Vite logo" />
          </a>
          <a href="https://react.dev" target="_blank">
            <img src={reactLogo} className="logo react" alt="React logo" />
          </a>
        </div>
        <h1>Vite + React</h1>
        <div className="card">
          <button onClick={() => setCount((count) => count + 1)}>
            count is {count}
          </button>
          <p>
            Edit <code>src/App.jsx</code> and save to test HMR
          </p>
        </div>
        <p className="read-the-docs">
          Click on the Vite and React logos to learn more
        </p> */}
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
      </div>
      <div
        className="wordlist"
        style={{
          height: "400px",
          display: "flex",
          flexDirection: "column-reverse",
          alignItems: "start",
        }}
      >
        <ul>{listing}</ul>
      </div>
      <div>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            enterWord();
          }}
          // onSubmit={enterWord}
        >
          <input
            type="text"
            placeholder="입력하세요"
            value={guess}
            onChange={handleInputChange}
          ></input>
          <button type="submit">버튼</button>
        </form>
      </div>
    </>
  );
};
