package com.ares.server_licenta.controller;

import com.ares.server_licenta.service.GptService;
import com.ares.server_licenta.service.PhotoService;
import com.ares.server_licenta.service.StoryService;
import com.ares.server_licenta.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Add this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);

    @Autowired
    private PhotoService photoService;

    @Autowired
    private StoryService storyService;

    @Autowired
    private GptService gptStoryService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Upload a photo")
    @PostMapping(value = "/lablePhotos")
    public ResponseEntity<String> uploadPhoto() {
        // Keeps the hardcoded UUID as requested for now
        photoService.processPhotos(UUID.fromString("750838d8-f761-46b9-a564-f58a5c687a2e"));
        return ResponseEntity.ok("Ceva");
    }

    @Operation(
            summary = "Get the aggregated timeline JSON for GPT",
            security = @SecurityRequirement(name = "bearerAuth") // Locks only this endpoint
    )
    @GetMapping(value = "/story")
    public ResponseEntity<String> getStoryJson(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        UUID userId = userService.getUserIdFromToken(token);
        String timelineJson = storyService.generateStoryJson(userId);
        return ResponseEntity.ok(timelineJson);
    }

    @Operation(
            summary = "Get the day narrative text from GPT",
            security = @SecurityRequirement(name = "bearerAuth") // Locks only this endpoint
    )
    @GetMapping(value = "/story/narrative", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getDayNarrative(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        UUID userId = userService.getUserIdFromToken(token);

        String timelineJson = storyService.generateStoryJson(userId);
        String story = gptStoryService.generateNarrative(timelineJson);

        return ResponseEntity.ok(story);
    }
}