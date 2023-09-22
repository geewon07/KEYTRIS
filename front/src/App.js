import "./App.css";
import React from "react";
import { Routes, Route } from "react-router-dom";
import { Home } from "./pages/home/Home";
import { SingleGame } from "./pages/singleGame/singleGame";
import { SingleGameResult } from "./pages/singleGame/singleGameResult";
import { MultiGame } from "./pages/multiGame/multiGame"
import { MultiGameResult } from "./pages/multiGame/multiGameResult";
import { QuickMenu } from "./components/quickmenu/quickMenuTest";

import { GameTest } from "./components/game/GameTest";

function App() {
  return (
    <div className="App">
      <div id="stars" />
      <div id="stars2" />
      <div id="stars3" />
      <QuickMenu />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/singleGame" element={<SingleGame />}>
          <Route index element={<SingleGame />} />
          {/* single-game-result 페이지에 대한 라우트 */}
          <Route path="result" element={<SingleGameResult />} />
        </Route>
        <Route path="/multi-game" element={<MultiGame />}>
          {/* multi-game 페이지의 기본 경로인 /multi-game에 대한 라우트 */}
          <Route index element={<MultiGame />} />
          {/* multi-game-result 페이지에 대한 라우트 */}
          <Route path="result" element={<MultiGameResult />} />
        </Route>
      </Routes>
      <GameTest />
    </div>
  );
}

export default App;
