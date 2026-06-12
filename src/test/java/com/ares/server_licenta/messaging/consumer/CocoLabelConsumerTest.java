package com.ares.server_licenta.messaging.consumer;

import com.ares.server_licenta.domain.Photo;
import com.ares.server_licenta.messaging.dto.CocoLabelResponse;
import com.ares.server_licenta.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CocoLabelConsumerTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private CocoLabelConsumer cocoLabelConsumer;

    @Test
    void receiveLabel_ShouldUpdatePhotoWithLabels() {
        // Arrange
        String fileName = "test.jpg";
        List<String> labels = List.of("dog", "park");
        CocoLabelResponse response = new CocoLabelResponse(fileName, labels);
        
        Photo photo = new Photo();
        photo.setFileName(fileName);
        
        when(photoRepository.findFileName(fileName)).thenReturn(photo);

        // Act
        cocoLabelConsumer.receiveLabel(response);

        // Assert
        verify(photoRepository).findFileName(fileName);
        verify(photoRepository).save(photo);
        assert photo.getCocoLabel().equals(labels);
    }
}
