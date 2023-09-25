import "./App.css";
import React from "react";
import { Routes, Route } from "react-router-dom";
import { Home } from "./pages/home/Home";
// import { SingleGame } from "./pages/singleGame/SingleGame";
import { SingleGameResult } from "./pages/singleGame/singleGameResult";
import { MultiGame } from "./pages/multiGame/multiGame";
import { MultiGameResult } from "./pages/multiGame/multiGameResult";
import { QuickMenu } from "./components/quickmenu/quickMenuTest.js";
import { Star } from "./components/star.js";
import { TestJisoo } from "./pages/test/testJisoo.js";

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
        {/* <Route path="/SingleGame" element={<SingleGame />} /> */}
        <Route path="/SingleGameResult" element={<SingleGameResult />} />
        <Route path="/MultiGame" element={<MultiGame />} />
        <Route path="/MultiGameResult" element={<MultiGameResult />} />
        <Route path="/TestJisoo" element={<TestJisoo />} />
      </Routes>
    </div>
  );
}

export default App;
