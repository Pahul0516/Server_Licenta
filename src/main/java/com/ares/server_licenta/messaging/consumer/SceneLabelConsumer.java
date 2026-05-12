package com.ares.server_licenta.messaging.consumer;

import com.ares.server_licenta.messaging.dto.SceneLabelResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SceneLabelConsumer {

    @RabbitListener(queues = "scene_response_queue")
    public void receiveLabel(SceneLabelResponse response) {
        System.out.println("File: " + response.getFileName());
        System.out.println("Scene Label: " + response.getLabel());
    }
}
