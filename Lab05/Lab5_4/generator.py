import pika
import json
import time
import random
import sys

# Configuration
RABBITMQ_HOST = 'localhost'
EXCHANGE = 'school-exchange'

# Sample data
NAMES = ["Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy"]
SURNAMES = ["Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"]

# Course data
COURSES = [
    {"name": "Mathematics", "code": "MATH101"},
    {"name": "Physics", "code": "PHYS202"},
    {"name": "Chemistry", "code": "CHEM303"},
    {"name": "History", "code": "HIST404"},
    {"name": "Computer Science", "code": "CS505"}
]

# State
students_emails = []

def get_connection():
    try:
        connection = pika.BlockingConnection(pika.ConnectionParameters(host=RABBITMQ_HOST))
        return connection
    except Exception as e:
        print(f"Error connecting to RabbitMQ: {e}")
        return None

def main():
    print(f"Starting generator. Connecting to {RABBITMQ_HOST}...")
    connection = get_connection()
    if not connection:
        sys.exit(1)
        
    channel = connection.channel()
    
    # Declare the exchange
    channel.exchange_declare(exchange=EXCHANGE, exchange_type='topic')
    
    print("Generator started. Press Ctrl+C to stop.")
    
    try:
        while True:
            # Decide what to generate: a student or a grade
            if not students_emails or random.random() < 0.2:
                # Generate a student
                name = random.choice(NAMES)
                surname = random.choice(SURNAMES)
                full_name = f"{name} {surname}"
                email = f"{name.lower()}.{surname.lower()}.{random.randint(100, 999)}@ua.pt"
                
                student = {
                    "name": full_name,
                    "email": email
                }
                
                # Routing key: student.new
                channel.basic_publish(exchange=EXCHANGE, routing_key='student.new', body=json.dumps(student))
                print(f"Generated student: {full_name} ({email}) -> student.new")
                
                if email not in students_emails:
                    students_emails.append(email)
            else:
                # Generate a grade for an existing student and a random course
                email = random.choice(students_emails)
                course = random.choice(COURSES)
                value = round(random.uniform(0, 20), 1)
                
                grade = {
                    "studentEmail": email,
                    "courseName": course["name"],
                    "courseCode": course["code"],
                    "value": value
                }
                
                # Routing key: course.<NAME>.grade
                routing_key = f"course.{course['name'].replace(' ', '_')}.grade"
                channel.basic_publish(exchange=EXCHANGE, routing_key=routing_key, body=json.dumps(grade))
                print(f"Generated grade for {email}: {course['name']} = {value} -> {routing_key}")
            
            sleep_time = random.uniform(2, 5)
            time.sleep(sleep_time)
            
    except KeyboardInterrupt:
        print("Stopping generator...")
    finally:
        connection.close()

if __name__ == "__main__":
    main()
