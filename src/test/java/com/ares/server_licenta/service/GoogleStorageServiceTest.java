package com.ares.server_licenta.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleStorageServiceTest {

    @Mock
    private Storage storage;

    @Mock
    private Blob blob;

    @InjectMocks
    private GoogleStorageService googleStorageService;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(googleStorageService, "bucketName", bucketName);
    }

    @Test
    void downloadFile_ShouldReturnBytes_WhenBlobExists() {
        // Arrange
        String fileName = "test.jpg";
        byte[] content = "test content".getBytes();
        when(storage.get(bucketName, fileName)).thenReturn(blob);
        when(blob.getContent()).thenReturn(content);

        // Act
        byte[] result = googleStorageService.downloadFile(fileName);

        // Assert
        assertArrayEquals(content, result);
        verify(storage).get(bucketName, fileName);
    }

    @Test
    void downloadFile_ShouldThrowRuntimeException_WhenBlobDoesNotExist() {
        // Arrange
        String fileName = "missing.jpg";
        when(storage.get(bucketName, fileName)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            googleStorageService.downloadFile(fileName)
        );
        assertTrue(exception.getMessage().contains("File not found in GCS"));
    }
}
