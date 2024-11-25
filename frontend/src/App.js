import './App.css';
import { ChartComponent } from "./ChartComponent";
import React, { useState } from 'react';

function App() {
  const title = "Weather Comparison";
  const [counter, setCounter] = useState(0);
  const handleClick = () => {
    setCounter(counter + 1);
    ChartComponent();
  };

  return (
      <div className="App">
        <h1>{ title }</h1>
        <button onClick={handleClick}>graph</button>
        <h2>{counter}</h2>
        <ChartComponent />
      </div>
  );
}

export default App;
