package com.ares.server_licenta.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SCENE_QUEUE = "scene_queue";
    public static final String EXCHANGE = "photo_exchange";
    public static final String ROUTING_KEY = "photo.routingKey";

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue sceneQueue(){
        return new Queue(SCENE_QUEUE);
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue sceneQueue, DirectExchange exchange){
        return BindingBuilder
                .bind(sceneQueue)
                .to(exchange)
                .with(ROUTING_KEY);
    }
}