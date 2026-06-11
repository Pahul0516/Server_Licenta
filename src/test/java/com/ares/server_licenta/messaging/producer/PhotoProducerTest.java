package com.ares.server_licenta.messaging.producer;

import com.ares.server_licenta.config.RabbitMQConfig;
import com.ares.server_licenta.messaging.dto.PhotoMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PhotoProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PhotoProducer photoProducer;

    @Test
    void send_ShouldCallConvertAndSend() {
        // Arrange
        PhotoMessage message = new PhotoMessage();
        message.setFileName("test.jpg");

        // Act
        photoProducer.send(message);

        // Assert
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq(""),
                eq(message)
        );
    }
}
