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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonLabelConsumerTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PersonLabelConsumer personLabelConsumer;

    @Test
    void receiveLabel_ShouldUpdatePhotoWithPersonLabel() {
        // Arrange
        String fileName = "person.jpg";
        String label = "John Doe";
        CocoLabelResponse response = new CocoLabelResponse(fileName, List.of(label));
        
        Photo photo = new Photo();
        photo.setFileName(fileName);
        
        when(photoRepository.findFileName(fileName)).thenReturn(photo);

        // Act
        personLabelConsumer.receiveLabel(response);

        // Assert
        verify(photoRepository).findFileName(fileName);
        verify(photoRepository).save(photo);
        assertEquals(label, photo.getPersonLabel());
    }
}
