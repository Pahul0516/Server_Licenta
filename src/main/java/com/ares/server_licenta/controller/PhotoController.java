package com.ares.server_licenta.controller;

import com.ares.server_licenta.service.GptService;
import com.ares.server_licenta.service.PhotoService;
import com.ares.server_licenta.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Upload a photo")
    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadPhoto() {
        photoService.processPhotos(UUID.fromString("750838d8-f761-46b9-a564-f58a5c687a2e"));
        return ResponseEntity.ok("Ceva");
    }

    @Operation(summary = "Get the aggregated timeline JSON for GPT")
    @GetMapping(value = "/story")
    public ResponseEntity<String> getStoryJson() {


        String timelineJson = storyService.generateStoryJson(UUID.fromString("750838d8-f761-46b9-a564-f58a5c687a2e"));

        return ResponseEntity.ok(timelineJson);
    }

    @GetMapping(value = "/story/narrative", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getDayNarrative() {
        // 1. Get the aggregated JSON from our existing StoryService
        String timelineJson = storyService.generateStoryJson(UUID.fromString("750838d8-f761-46b9-a564-f58a5c687a2e"));

        // 2. Send it to GPT
        String story = gptStoryService.generateNarrative(timelineJson);

        return ResponseEntity.ok(story);
    }
}