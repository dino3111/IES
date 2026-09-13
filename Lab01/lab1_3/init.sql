CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    num_mec INT UNIQUE NOT NULL,
    first_name TEXT,
    last_name TEXT
);

CREATE TABLE grades (
    id SERIAL PRIMARY KEY,
    student_id INT REFERENCES students(id),
    exam INT,
    grade FLOAT
);