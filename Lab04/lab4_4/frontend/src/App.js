import React, { useEffect, useState } from "react";

function App() {
  const [items, setItems] = useState([]);
  const [input, setInput] = useState("");
  const [error, setError] = useState("");

  const [file, setFile] = useState(null);
  const [importResult, setImportResult] = useState(null);

  const API = process.env.REACT_APP_API_URL || "http://localhost:8080/api/items";

  // GET
  const loadItems = () => {
    fetch(API)
        .then(res => res.json())
        .then(data => setItems(data));
  };

  useEffect(() => {
    loadItems();
  }, []);

  // Validation
  const validateInput = () => {
    if (input.trim() === "") {
      setError("A tarefa não pode estar vazia");
      return false;
    }

    if (input.trim().length < 3) {
      setError("Mínimo 3 caracteres");
      return false;
    }

    setError("");
    return true;
  };

  // POST
  const addItem = () => {
    if (!validateInput()) return;

    fetch(API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: input.trim() })
    }).then(() => {
      setInput("");
      loadItems();
    });
  };

  // DELETE
  const deleteItem = (id) => {
    fetch(`${API}/${id}`, { method: "DELETE" })
        .then(() => loadItems());
  };

  // UPDATE
  const updateItem = (id) => {
    const newName = prompt("Novo nome:");

    if (!newName || newName.trim() === "") {
      alert("Nome inválido");
      return;
    }

    if (newName.trim().length < 3) {
      alert("Mínimo 3 caracteres");
      return;
    }

    fetch(`${API}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: newName.trim() })
    }).then(() => loadItems());
  };

  // CSV upload
  const uploadFile = () => {
    if (!file) {
      alert("Seleciona um ficheiro primeiro");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    fetch(`${API}/import`, {
      method: "POST",
      body: formData
    })
        .then(res => res.json())
        .then(data => {
          setImportResult(data);
          loadItems();
        })
        .catch(err => console.error(err));
  };

  return (
      <div style={{ padding: "20px" }}>
        <h1>ToDo (React + Spring)</h1>

        <input
            value={input}
            onChange={(e) => {
              setInput(e.target.value);
              setError("");
            }}
            placeholder="Nova tarefa"
        />

        <button onClick={addItem}>Adicionar</button>

        {error && <p style={{ color: "red" }}>{error}</p>}

        <hr />

        <h2>Importar CSV</h2>

        <input
            type="file"
            accept=".csv"
            onChange={(e) => setFile(e.target.files[0])}
        />

        <button onClick={uploadFile}>Upload CSV</button>

        {importResult && (
            <div>
              <h3>Resultado da Importação</h3>
              <p>Importados: {importResult.imported}</p>
              <p>Erros: {importResult.errors}</p>
            </div>
        )}

        <hr />

        <ul>
          {items.map(item => (
              <li key={item.id}>
                {item.name}
                <button onClick={() => updateItem(item.id)}>Editar</button>
                <button onClick={() => deleteItem(item.id)}>Eliminar</button>
              </li>
          ))}
        </ul>
      </div>
  );
}

export default App;