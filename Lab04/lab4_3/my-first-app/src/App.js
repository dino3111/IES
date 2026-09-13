import React, { useState } from "react";

function App() {
  const [tasks, setTasks] = useState([]);
  const [input, setInput] = useState("");

  const addTask = () => {
    if (input.trim() === "") return;
    setTasks([...tasks, input]);
    setInput("");
  };

  const removeTask = (index) => {
    const newTasks = tasks.filter((_, i) => i !== index);
    setTasks(newTasks);
  };

  return (
      <div style={{ padding: "20px" }}>
        <h1>ToDo List</h1>

        <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Nova tarefa"
        />

        <button onClick={addTask}>Adicionar</button>

        <ul>
          {tasks.map((task, index) => (
              <li key={index}>
                {task}
                <button onClick={() => removeTask(index)}>X</button>
              </li>
          ))}
        </ul>
      </div>
  );
}

export default App;