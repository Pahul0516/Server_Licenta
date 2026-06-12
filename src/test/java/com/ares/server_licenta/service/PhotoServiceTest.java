package com.ares.server_licenta.service;

import com.ares.server_licenta.domain.Photo;
import com.ares.server_licenta.messaging.dto.PhotoMessage;
import com.ares.server_licenta.messaging.producer.PhotoProducer;
import com.ares.server_licenta.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private PhotoProducer photoProducer;

    @Mock
    private GoogleStorageService storageService;

    @InjectMocks
    private PhotoService photoService;

    @Test
    void processPhotos_ShouldDownloadAndSendPhotos() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Photo photo1 = new Photo();
        photo1.setFileName("photo1.jpg");
        Photo photo2 = new Photo();
        photo2.setFileName("photo2.jpg");
        
        List<Photo> photos = List.of(photo1, photo2);
        when(photoRepository.findByUserIdAndCurrentDayAndMissingLabels(eq(userId), any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(photos);
        
        byte[] content1 = "content1".getBytes();
        byte[] content2 = "content2".getBytes();
        when(storageService.downloadFile("photo1.jpg")).thenReturn(content1);
        when(storageService.downloadFile("photo2.jpg")).thenReturn(content2);

        // Act
        photoService.processPhotos(userId);

        // Assert
        verify(photoProducer, times(2)).send(any(PhotoMessage.class));
        verify(storageService).downloadFile("photo1.jpg");
        verify(storageService).downloadFile("photo2.jpg");
    }
}
