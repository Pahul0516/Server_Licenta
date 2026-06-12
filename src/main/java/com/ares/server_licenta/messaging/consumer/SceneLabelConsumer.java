package com.ares.server_licenta.messaging.consumer;

import com.ares.server_licenta.domain.Photo;
import com.ares.server_licenta.messaging.dto.SceneLabelResponse;
import com.ares.server_licenta.repository.PhotoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SceneLabelConsumer {

    private final PhotoRepository photoRepository;

    public SceneLabelConsumer (PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @RabbitListener(queues = "scene_response_queue")
    public void receiveLabel(SceneLabelResponse response) {
        String fileName = response.getFileName();
        Photo photo = photoRepository.findFileName(fileName);
        photo.setSceneLabel(response.getLabel());
        photoRepository.save(photo);
    }
}