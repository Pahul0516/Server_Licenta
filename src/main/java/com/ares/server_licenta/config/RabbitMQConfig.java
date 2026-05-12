package com.ares.server_licenta.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange used to broadcast photos
    public static final String EXCHANGE = "photo_exchange";

    // Response queue (Python → Java)
    public static final String SCENE_LABELS_QUEUE = "scene_response_queue";

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue sceneLabelsQueue() {
        return new Queue(SCENE_LABELS_QUEUE, true);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> adminInitializer(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}