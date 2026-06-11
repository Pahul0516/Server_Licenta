package com.ares.server_licenta.messaging.consumer;

import com.ares.server_licenta.domain.Photo;
import com.ares.server_licenta.messaging.dto.SceneLabelResponse;
import com.ares.server_licenta.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SceneLabelConsumerTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private SceneLabelConsumer sceneLabelConsumer;

    @Test
    void receiveLabel_ShouldUpdatePhotoWithSceneLabel() {
        // Arrange
        String fileName = "scene.jpg";
        String label = "beach";
        SceneLabelResponse response = new SceneLabelResponse(fileName, label);
        
        Photo photo = new Photo();
        photo.setFileName(fileName);
        
        when(photoRepository.findFileName(fileName)).thenReturn(photo);

        // Act
        sceneLabelConsumer.receiveLabel(response);

        // Assert
        verify(photoRepository).findFileName(fileName);
        verify(photoRepository).save(photo);
        assertEquals(label, photo.getSceneLabel());
    }
}
