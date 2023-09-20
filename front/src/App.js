import './App.css';
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Home } from './pages/home/Home';
import { SingleGame } from './pages/singleGame/SingleGame';
import { SingleGameResult } from './pages/singleGame/SingleGameResult';
import { MultiGame } from './pages/multiGame/MultiGame';
import { MultiGameResult } from './pages/multiGame/MultiGameResult';
import { GameTest } from './components/game/GameTest';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/singleGame" element={<SingleGame />} />
        <Route path="/singleGameResult" element={<SingleGameResult />} />
        <Route path="/multiGame" element={<MultiGame />} />
        <Route path="/multiGameResult" element={<MultiGameResult />} />
      </Routes>
      <GameTest />
    </div>
  );
};

export default App;
