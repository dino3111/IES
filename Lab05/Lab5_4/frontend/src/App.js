import React, { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import LatestGrades from "./LatestGrades";

function App() {
  const [students, setStudents] = useState([]);
  const [latestGrades, setLatestGrades] = useState([]);
  const [connected, setConnected] = useState(false);
  const API_BASE = "http://localhost:8080/api/students";
  const WS_ENDPOINT = "http://localhost:8080/ws-api";

  const loadStudents = () => {
    fetch(API_BASE)
        .then(res => res.json())
        .then(data => setStudents(data));
  };

  useEffect(() => {
    loadStudents();

    const socket = new SockJS(WS_ENDPOINT);
    const stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, () => {
      setConnected(true);
      console.log("Connected to WebSocket");

      stompClient.subscribe("/topic/students", (message) => {
        const newStudent = JSON.parse(message.body);
        setStudents(prev => {
          if (!prev.some(s => s.id === newStudent.id)) {
            return [...prev, newStudent];
          }
          return prev;
        });
      });

      stompClient.subscribe("/topic/grades", (message) => {
        const update = JSON.parse(message.body);
        const { studentId, grade } = update;
        
        setStudents(prev => {
          const student = prev.find(s => s.id === studentId);
          const studentName = student ? student.name : "Unknown Student";

          setLatestGrades(prevLatest => {
            const newEntry = {
              studentName,
              courseName: grade.course.name, // Access course name from Grade object
              value: grade.value
            };
            return [newEntry, ...prevLatest].slice(0, 5);
          });

          return prev.map(s => {
            if (s.id === studentId) {
              if (!s.grades.some(g => g.id === grade.id)) {
                  return { ...s, grades: [...s.grades, grade] };
              }
            }
            return s;
          });
        });
      });
    }, (error) => {
        setConnected(false);
    });

    return () => {
      if (stompClient) {
        stompClient.disconnect();
      }
    };
  }, []);

  return (
      <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <h1>Multi-Course Dashboard</h1>
            <div>
                Status: <span style={{ 
                    padding: "5px 10px", 
                    borderRadius: "15px", 
                    backgroundColor: connected ? "#28a745" : "#dc3545",
                    color: "white",
                    fontSize: "0.8em",
                    fontWeight: "bold"
                }}>
                    {connected ? "LIVE" : "DISCONNECTED"}
                </span>
            </div>
        </div>
        
        <LatestGrades grades={latestGrades} />

        <hr />
        
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(350px, 1fr))", gap: "20px", marginTop: "20px" }}>
          {students.map(student => (
              <div key={student.id} style={{ border: "1px solid #ccc", padding: "15px", borderRadius: "8px", boxShadow: "2px 2px 5px rgba(0,0,0,0.1)" }}>
                <h3>{student.name}</h3>
                <p><strong>Email:</strong> {student.email}</p>
                <h4>Grades per Course:</h4>
                {student.grades && student.grades.length > 0 ? (
                    <ul style={{ paddingLeft: "20px" }}>
                      {student.grades.map(grade => (
                          <li key={grade.id} style={{ marginBottom: "5px" }}>
                            <strong>{grade.course.name} ({grade.course.code}):</strong> <span style={{ fontWeight: "bold", color: grade.value >= 10 ? "green" : "red" }}>{grade.value}</span>
                          </li>
                      ))}
                    </ul>
                ) : (
                    <p>No grades yet.</p>
                )}
              </div>
          ))}
        </div>
        
        {students.length === 0 && <p>No students found. Waiting for updates...</p>}
      </div>
  );
}

export default App;
