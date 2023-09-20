import './App.css';
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Home } from './pages/home/Home';
import { SingleGameResult } from './pages/singleGame/SingleGameResult';
import { MultiGame } from './pages/multiGame/MultiGame';
import { MultiGameResult } from './pages/multiGame/MultiGameResult';
import { SingleGame } from './pages/singleGame/SingleGame';


function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/game" element={<SingleGame></SingleGame>}></Route>
        <Route path="/" element={<Home />} />
        <Route path="/singleGame" element={<SingleGame />}>
          <Route index element={<SingleGame />} />
          <Route path="singleGameResult" element={<SingleGameResult />} />
        </Route>
        <Route path="/multiGame" element={<MultiGame />}>
          <Route index element={<MultiGame />} />
          <Route path="multiGameResult" element={<MultiGameResult />} />
        </Route>
      </Routes>
    </div>
  );
};

export default App;
