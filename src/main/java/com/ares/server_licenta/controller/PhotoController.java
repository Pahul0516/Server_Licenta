package com.ares.server_licenta.controller;

import com.ares.server_licenta.domain.Photo;
import com.ares.server_licenta.messaging.producer.PhotoProducer;
import com.ares.server_licenta.service.PhotoService;
import com.ares.server_licenta.service.StoryService; // Added this
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);

    @Autowired
    private PhotoService photoService;

    @Autowired
    private StoryService storyService; // Injecting the new service

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
}