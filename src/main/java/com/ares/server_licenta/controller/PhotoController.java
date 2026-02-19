package com.ares.server_licenta.controller;

import com.ares.server_licenta.messaging.producer.PhotoProducer;
import com.ares.server_licenta.messaging.dto.PhotoMessage;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoProducer photoProducer;

    @Operation(summary = "Upload a photo")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("file") MultipartFile file) {

        try {
            PhotoMessage msg = new PhotoMessage(
                    file.getOriginalFilename(),
                    file.getBytes()
            );


            photoProducer.send(msg);
        }catch (Exception e){

        }

        return ResponseEntity.ok("Uploaded: " + file.getOriginalFilename());
    }
}