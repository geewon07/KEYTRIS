import "./App.css";
import 'react-toastify/dist/ReactToastify.css';

import React from "react";
import { Routes, Route } from "react-router-dom";
import { Home } from "./pages/home/Home";
import { SingleGame } from "./pages/singleGame/SingleGame";
import { SingleGameTest } from "./pages/singleGame/SingleGameTest";
import { SingleGameResult } from "./pages/singleGame/SingleGameResult";
import { MultiGame } from "./pages/multiGame/MultiGame";
import { MultiGameResult } from "./pages/multiGame/MultiGameResult";
import { QuickMenu } from "./components/quickmenu/QuickMenuTest.js";
import { Star } from "./components/star.js";
import { TestJisoo } from "./pages/test/TestJisoo.js";
import { TestSocket } from "./pages/test/TestSocket.js";
import { ToastContainer } from 'react-toastify';

function App() {
  return (
    <div className="App">
      {/* <div id="stars" />
      <div id="stars2" />
      <div id="stars3" /> */}
      <Star />
      <QuickMenu />
      <ToastContainer
        toastStyle={{minWidth: "400px"}} 
        position="bottom-center"
        autoClose={1500}
        hideProgressBar
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
        />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/test" element={<SingleGameTest />} /><Route path="/SingleGame" element={<SingleGame />} />
        <Route path="/SingleGameResult" element={<SingleGameResult />} />
        <Route path="/MultiGame/:roomId" element={<MultiGame />} />
        <Route path="/MultiGameResult" element={<MultiGameResult />} />
        <Route path="/TestJisoo/:roomId" element={<TestJisoo />} />
        <Route path="/TestSocket/:roomId" element={<TestSocket />} />
      </Routes>
    </div>
  );
}

export default App;
