package com.ares.server_licenta.messaging.producer;

import com.ares.server_licenta.config.RabbitMQConfig;
import com.ares.server_licenta.messaging.dto.PhotoMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class PhotoProducer {

    private final RabbitTemplate rabbitTemplate;

    public PhotoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(PhotoMessage message) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }
}
