# Lab 5.4 

## System Architecture

The system consists of three main components:
1.  **Frontend (React):** A dashboard displaying students, their courses, and grades in real-time.
2.  **Backend (Spring Boot):** Consumes messages from RabbitMQ, persists data in PostgreSQL, and broadcasts updates via WebSockets (STOMP).
3.  **Generator (Python):** Simulates external processes by generating new students and grades.

## RabbitMQ Strategy (Topic Exchange)

To support multiple courses effectively, I implemented a **Topic Exchange** (`school-exchange`) in RabbitMQ.

### Justification:
-   **Granularity:** Routing keys follow patterns like `student.new` and `course.<COURSE_NAME>.grade`. This allows different parts of the system to subscribe only to specific information (e.g., a specific department could subscribe to `course.MATH.#`).
-   **Flexibility:** Topic exchanges support wildcards (`*` for one word, `#` for zero or more). This makes it easy to add new types of messages or routing rules without changing the exchange type.
-   **Scalability:** If the system grows to handle thousands of courses, the architecture supports distributing consumers across different queues based on course categories or priorities.

### Routing Key Examples:
-   `student.new`: Triggers student creation/retrieval.
-   `course.Mathematics.grade`: Routes a grade update specifically for the Mathematics course.
-   `course.Physics.grade`: Routes a grade update specifically for the Physics course.

## How to Run

1.  **Infrastructure:**
    ```bash
    docker-compose up -d
    ```
2.  **Generator:**
    Ensure `pika` is installed (`pip install pika`), then:
    ```bash
    python generator.py
    ```
3.  **Access:**
    - Frontend: `http://localhost:3001`
    - Backend API: `http://localhost:8080/api/students`
    - RabbitMQ Management: `http://localhost:15672` (guest/guest)
