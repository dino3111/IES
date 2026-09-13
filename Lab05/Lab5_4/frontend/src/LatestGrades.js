import React from "react";

function LatestGrades({ grades }) {
  return (
    <div style={{ marginTop: "20px", padding: "15px", border: "1px solid #007bff", borderRadius: "8px", backgroundColor: "#f0f8ff" }}>
      <h3>Latest 5 Updated Grades</h3>
      {grades.length === 0 ? (
        <p>No updates yet.</p>
      ) : (
        <ul style={{ listStyleType: "none", padding: 0 }}>
          {grades.map((item, index) => (
            <li key={index} style={{ padding: "5px 0", borderBottom: "1px solid #ddd" }}>
              <strong>{item.studentName}</strong>: {item.courseName} &rarr; <span style={{ fontWeight: "bold", color: item.value >= 10 ? "green" : "red" }}>{item.value}</span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default LatestGrades;
