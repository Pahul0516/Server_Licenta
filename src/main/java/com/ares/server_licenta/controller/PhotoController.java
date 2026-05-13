package com.ares.server_licenta.controller;

import com.ares.server_licenta.messaging.producer.PhotoProducer;
import com.ares.server_licenta.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;


@RestController
@RequestMapping("/photos")
public class PhotoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoProducer.class);
    @Autowired
    private PhotoService photoService;

    @Operation(summary = "Upload a photo")
    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadPhoto(){

        photoService.processPhotos(UUID.fromString("750838d8-f761-46b9-a564-f58a5c687a2e"));
        return ResponseEntity.ok("Ceva");
    }
}