package pt.ua.claudino.backend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SCHOOL_EXCHANGE = "school-exchange";
    
    public static final String STUDENT_QUEUE = "students-queue";
    public static final String GRADE_QUEUE = "grades-queue";

    // Routing patterns
    // student.new
    // course.<COURSE_NAME>.grade
    public static final String STUDENT_ROUTING_KEY = "student.#";
    public static final String GRADE_ROUTING_KEY = "course.#.grade";

    @Bean
    public TopicExchange schoolExchange() {
        return new TopicExchange(SCHOOL_EXCHANGE);
    }

    @Bean
    public Queue studentQueue() {
        return new Queue(STUDENT_QUEUE);
    }

    @Bean
    public Queue gradeQueue() {
        return new Queue(GRADE_QUEUE);
    }

    @Bean
    public Binding studentBinding(Queue studentQueue, TopicExchange schoolExchange) {
        return BindingBuilder.bind(studentQueue).to(schoolExchange).with(STUDENT_ROUTING_KEY);
    }

    @Bean
    public Binding gradeBinding(Queue gradeQueue, TopicExchange schoolExchange) {
        return BindingBuilder.bind(gradeQueue).to(schoolExchange).with(GRADE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
