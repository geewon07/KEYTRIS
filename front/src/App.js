import "./App.css";
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

function App() {
  return (
    <div className="App">
      {/* <div id="stars" />
      <div id="stars2" />
      <div id="stars3" /> */}
      <Star />
      <QuickMenu />

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/test" element={<SingleGameTest />} /><Route path="/SingleGame" element={<SingleGame />} />
        <Route path="/SingleGameResult" element={<SingleGameResult />} />
        <Route path="/MultiGame/:roomId" element={<MultiGame />} />
        <Route path="/MultiGameResult" element={<MultiGameResult />} />
        <Route path="/TestJisoo/:roomId" element={<TestJisoo />} />
      </Routes>
    </div>
  );
}

export default App;
