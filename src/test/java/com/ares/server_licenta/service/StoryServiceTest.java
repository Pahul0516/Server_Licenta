package com.ares.server_licenta.service;

import com.ares.server_licenta.domain.Photo;
import com.ares.server_licenta.repository.PhotoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoryServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private StoryService storyService;

    @Test
    void generateStoryJson_ShouldThrowRuntimeException_WhenSerializationFails() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(photoRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtAsc(eq(userId), any(), any()))
                .thenReturn(List.of());

        ObjectMapper mockMapper = mock(ObjectMapper.class);
        ObjectWriter mockWriter = mock(ObjectWriter.class);
        
        ReflectionTestUtils.setField(storyService, "objectMapper", mockMapper);
        when(mockMapper.writerWithDefaultPrettyPrinter()).thenReturn(mockWriter);
        when(mockWriter.writeValueAsString(any())).thenThrow(new RuntimeException("Serialization error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            storyService.generateStoryJson(userId)
        );
        assertTrue(exception.getMessage().contains("Failed to generate Timeline JSON"));
    }

    @Test
    void generateStoryJson_ShouldHandleEmptyList() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(photoRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtAsc(eq(userId), any(), any()))
                .thenReturn(List.of());

        // Act
        String json = storyService.generateStoryJson(userId);

        // Assert
        assertEquals("{ }", json.trim(), "Should return an empty JSON object");
    }

    @Test
    void generateStoryJson_ShouldFilterOutWindowsWithOnlyPeople() {
        // Arrange
        UUID userId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.of(2026, 5, 21, 10, 5, 0, 0, ZoneOffset.UTC);
        
        // Window with only a person, no objects or locations
        Photo p1 = createPhoto(now, null, null, "John");
        
        when(photoRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtAsc(eq(userId), any(), any()))
                .thenReturn(List.of(p1));

        // Act
        String json = storyService.generateStoryJson(userId);

        // Assert
        assertEquals("{ }", json.trim(), "Should be empty because objects and locations are empty");
    }

    @Test
    void generateStoryJson_ShouldHandleMultipleWindowsAndNullLabels() {
        // Arrange
        UUID userId = UUID.randomUUID();
        OffsetDateTime win1 = OffsetDateTime.of(2026, 5, 21, 10, 5, 0, 0, ZoneOffset.UTC);
        OffsetDateTime win2 = OffsetDateTime.of(2026, 5, 21, 11, 5, 0, 0, ZoneOffset.UTC);

        Photo p1 = createPhoto(win1, List.of("keys"), "home", "Unknown");
        Photo p2 = createPhoto(win2, null, "office", "Alice"); // Null coco
        Photo p3 = createPhoto(win2, List.of("laptop"), "", "Alice"); // Blank scene

        when(photoRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtAsc(eq(userId), any(), any()))
                .thenReturn(List.of(p1, p2, p3));

        // Act
        String json = storyService.generateStoryJson(userId);

        // Assert
        assertTrue(json.contains("10:00 - 10:15"));
        assertTrue(json.contains("11:00 - 11:15"));
        assertTrue(json.contains("\"keys\""));
        assertTrue(json.contains("\"home\""));
        assertTrue(json.contains("\"office\""));
        assertTrue(json.contains("\"Alice\""));
        assertTrue(!json.contains("\"Unknown\""));
    }

    @Test
    void generateStoryJson_ShouldGroupAndFilterLabels() {
        // Arrange
        UUID userId = UUID.randomUUID();
        // Use a fixed time to avoid window crossing issues
        OffsetDateTime now = OffsetDateTime.of(2026, 5, 21, 10, 5, 0, 0, ZoneOffset.UTC);
        
        // 10 photos in the same 15-min window (10:00 - 10:15)
        // 15% threshold of 10 is 1.5. So 2 or more occurrences are needed.
        List<Photo> photos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> coco = (i < 2) ? List.of("dog") : (i == 2 ? List.of("cat") : List.of());
            photos.add(createPhoto(now.plusMinutes(i), coco, "beach", "Unknown"));
        }
        
        when(photoRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtAsc(eq(userId), any(), any()))
                .thenReturn(photos);

        // Act
        String json = storyService.generateStoryJson(userId);

        // Assert
        assertTrue(json.contains("10:00 - 10:15"), "Should contain the correct time window");
        assertTrue(json.contains("\"dog\""), "Should contain 'dog' as it is above 15% threshold");
        assertTrue(!json.contains("\"cat\""), "Should NOT contain 'cat' as it is below 15% threshold");
        assertTrue(json.contains("\"beach\""), "Should contain 'beach' as the primary location");
    }

    private Photo createPhoto(OffsetDateTime createdAt, List<String> coco, String scene, String person) {
        Photo p = new Photo();
        p.setCreatedAt(createdAt);
        p.setCocoLabel(coco);
        p.setSceneLabel(scene);
        p.setPersonLabel(person);
        return p;
    }
}
