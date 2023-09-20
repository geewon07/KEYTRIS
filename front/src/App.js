import './App.css';
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Home } from './pages/home/Home';
import { SingleGameResult } from './pages/singleGame/SingleGameResult';
import { MultiGame } from './pages/multiGame/MultiGame';
import { MultiGameResult } from './pages/multiGame/MultiGameResult';
import { SingleGame } from './pages/singleGame/SingleGameTest';
import {Single} from './pages/singleGame/SingleGame';


function App() {
  return (
    <div className="App">
      <Routes>
      <Route path="/game" element={<Single></Single>}></Route>
        <Route path="/test" element={<SingleGame></SingleGame>}></Route>
        <Route path="/" element={<Home />} />
        <Route path="/single-game" element={<SingleGame />}>
          {/* single-game 페이지의 기본 경로인 /single-game에 대한 라우트 */}
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
    </div>
  );
};

export default App;
