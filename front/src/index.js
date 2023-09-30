import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom';
import './assets/fonts/font.css'
import ReactGA from 'react-ga4'


const root = ReactDOM.createRoot(document.getElementById('root'));
const TRAKING_ID = process.env.REACT_APP_GOOGLE_ANALYTICS_TRAKING_ID;
ReactGA.initialize(TRAKING_ID);
root.render(
  
  <React.StrictMode>
    <BrowserRouter> 
      <App />
    </BrowserRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
