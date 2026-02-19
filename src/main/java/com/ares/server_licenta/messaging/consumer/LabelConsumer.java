package com.ares.server_licenta.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LabelConsumer {


    @RabbitListener(queues = "scene_queue")
    public void receive(String message) {
        System.out.println("Received: " + message);
    }
}
