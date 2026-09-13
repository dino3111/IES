import pika
import json
import sys

def callback(ch, method, properties, body):
    message = json.loads(body)
    print(f"Received: {message}")

def main():
    nmec = "127368"
    
    queue_name = "myQueue"

    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()

    channel.queue_declare(queue=queue_name, durable=True)

    channel.basic_consume(
        queue=queue_name,
        on_message_callback=callback,
        auto_ack=True
    )

    print(f"Waiting for messages in {queue_name}. To exit press CTRL+C")
    channel.start_consuming()

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        sys.exit(0)