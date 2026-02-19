package com.ares.server_licenta.messaging.producer;

import com.ares.server_licenta.config.RabbitMQConfig;
import com.ares.server_licenta.messaging.dto.PhotoMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PhotoProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoProducer.class);

    public PhotoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(PhotoMessage message) {

        LOGGER.info("Sending message to RabbitMQ");
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }
}
