import React from "react";
import { Link } from 'react-router-dom';

export const Home = () => {
  return (
    <div>
      Home
      <Link to="/single-game">Go to Single Game</Link>
      <Link to="/multi-game">Go to Multi Game</Link>
    </div>
  );
}; 