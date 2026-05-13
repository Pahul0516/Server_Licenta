package com.ares.server_licenta.messaging.consumer;

import com.ares.server_licenta.domain.Photo;
import com.ares.server_licenta.messaging.dto.CocoLabelResponse;
import com.ares.server_licenta.repository.PhotoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PersonLabelConsumer {

    private final PhotoRepository photoRepository;

    public PersonLabelConsumer(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @RabbitListener(queues = "person_response_queue")
    public void receiveLabel(CocoLabelResponse response) {
        String fileName = response.getFileName();
        Photo photo = photoRepository.findFileName(fileName);
        photo.setPersonLabel(response.getLabels().get(0));
        photoRepository.save(photo);
    }
}