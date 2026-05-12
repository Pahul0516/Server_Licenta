package com.ares.server_licenta.messaging.consumer;

import com.ares.server_licenta.messaging.dto.CocoLabelResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CocoLabelConsumer {

        @RabbitListener(queues = "coco_response_queue")
        public void receiveLabel(CocoLabelResponse response) {
            System.out.println("File: " + response.getFileName());
            System.out.println("Items Labels: " + response.getLabels());
        }

}
