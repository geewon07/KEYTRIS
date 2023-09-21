import "./App.css";
import React from "react";
import { Routes, Route } from "react-router-dom";
import { Home } from "./pages/home/Home";
import { SingleGame } from "./pages/singleGame/singleGame";
import { SingleGameResult } from "./pages/singleGame/singleGameResult";
import { MultiGame } from "./pages/multiGame/multiGame";
import { MultiGameResult } from "./pages/multiGame/multiGameResult";

import { TestJisoo } from "./pages/test/testJisoo";

function App() {
  return (
    <div className="App">
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
        <Route path="/testJisoo" element={<TestJisoo />} />
      </Routes>
    </div>
  );
}

export default App;
