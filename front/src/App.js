import "./App.css";
import React, { useState } from "react";
import { Routes, Route } from "react-router-dom";
import { Home } from "./pages/home/Home";
import { SingleGame } from "./pages/singleGame/singleGame.js";
import { SingleGameResult } from "./pages/singleGame/singleGameResult.js";
import { MultiGame } from "./pages/multiGame/multiGame.js";
import { MultiGameResult } from "./pages/multiGame/multiGameResult.js";
import { QuickMenu } from "./components/quickmenu/quickMenuTest.js";
import { Star } from "./components/star.js";
import { TestJisoo } from "./pages/test/testJisoo.js";

function App() {
  return (
    <div className="App">
      <Star />
      <QuickMenu />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/SingleGame" element={<SingleGame />} />
        <Route path="/SingleGameResult" element={<SingleGameResult />} />
        <Route path="/MultiGame" element={<MultiGame />} />
        <Route path="/MultiGameResult" element={<MultiGameResult />} />

        <Route path="/testJisoo" element={<TestJisoo />} />
      </Routes>
    </div>
  );
}

export default App;
