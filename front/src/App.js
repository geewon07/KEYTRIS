import './App.css';
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Home } from './pages/home/Home';
import { SingleGame } from './pages/singleGame/singleGame';
import { SingleGameResult } from './pages/singleGame/singleGameResult';
import { MultiGame } from './pages/multiGame/multiGame';
import { MultiGameResult } from './pages/multiGame/multiGameResult';
import styled, {keyframes} from 'styled-components';

function App() {

  return (
    <div className="App">
      <div id='stars' />
      <div id='stars2' />
      <div id='stars3' />
      <Routes>
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
