package com.ares.server_licenta.controller;

import com.ares.server_licenta.service.GptService;
import com.ares.server_licenta.service.PhotoService;
import com.ares.server_licenta.service.StoryService;
import com.ares.server_licenta.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoControllerTest {

    @Mock
    private PhotoService photoService;

    @Mock
    private StoryService storyService;

    @Mock
    private GptService gptStoryService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PhotoController photoController;

    @Test
    void uploadPhoto_ShouldCallPhotoService() {
        // Arrange
        String token = "Bearer test-token";
        UUID userId = UUID.randomUUID();
        when(userService.getUserIdFromToken(token)).thenReturn(userId);

        // Act
        ResponseEntity<String> response = photoController.uploadPhoto(token);

        // Assert
        assertEquals(202, response.getStatusCode().value());
        assertEquals("Photo labeling started", response.getBody());
        verify(photoService).processPhotos(userId);
    }

    @Test
    void getStoryJson_ShouldReturnTimeline() {
        // Arrange
        String token = "Bearer test-token";
        UUID userId = UUID.randomUUID();
        String expectedJson = "{\"timeline\": []}";
        
        when(userService.getUserIdFromToken(token)).thenReturn(userId);
        when(storyService.generateStoryJson(userId)).thenReturn(expectedJson);

        // Act
        ResponseEntity<String> response = photoController.getStoryJson(token);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedJson, response.getBody());
    }

    @Test
    void getDayNarrative_ShouldReturnStory() {
        // Arrange
        String token = "Bearer test-token";
        UUID userId = UUID.randomUUID();
        String timelineJson = "{\"timeline\": []}";
        String expectedStory = "Once upon a time...";
        
        when(userService.getUserIdFromToken(token)).thenReturn(userId);
        when(storyService.generateStoryJson(userId)).thenReturn(timelineJson);
        when(gptStoryService.generateNarrative(timelineJson)).thenReturn(expectedStory);

        // Act
        ResponseEntity<String> response = photoController.getDayNarrative(token);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedStory, response.getBody());
    }
}
