package com.ares.server_licenta.service;

import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    public String savePhoto(byte[] photoData, String filename) {
        // Here you would implement the logic to save the photo to disk or a database
        // For demonstration, we'll just return a success message
        return "Photo saved successfully with filename: " + filename;
    }

}
