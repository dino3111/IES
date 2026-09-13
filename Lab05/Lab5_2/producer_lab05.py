import pika
import json

def main():
    nmec = "127368"
    
    # Lab5_3 MessagingConfig
    exchange_name = 'myExchange'
    routing_key = 'myRoutingKey'

    # Connection
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()

    channel.exchange_declare(exchange=exchange_name, exchange_type='topic')

    # Examples
    grades = [
        {'studentId': nmec, 'grade': 15.5, 'subject': 'IES'},
        {'studentId': nmec, 'grade': 18.0, 'subject': 'CBD'},
        {'studentId': nmec, 'grade': 12.0, 'subject': 'MPEI'}
    ]

    for message in grades:
        channel.basic_publish(
            exchange=exchange_name,
            routing_key=routing_key,
            body=json.dumps(message)
        )
        print(f"Sent to {exchange_name}/{routing_key}: {message}")

    connection.close()

if __name__ == "__main__":
    main()