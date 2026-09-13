package org.example.lab5_3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    @RabbitListener(bindings = @QueueBinding(
            value    = @Queue(value = MessagingConfig.QUEUE, durable = "true"),
            exchange = @Exchange(value = MessagingConfig.EXCHANGE, type = "topic"),
            key      = MessagingConfig.ROUTING_KEY
    ))

    public void consumeMessageFromQueue(MessageGrade grade) {
        System.out.println("Message received from queue : " + grade);
        log.info("Message received from queue : {}", grade);
    }
}