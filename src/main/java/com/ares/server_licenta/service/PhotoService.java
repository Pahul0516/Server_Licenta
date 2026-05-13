package com.ares.server_licenta.service;

import com.ares.server_licenta.domain.Photo;
import com.ares.server_licenta.messaging.dto.PhotoMessage;
import com.ares.server_licenta.messaging.producer.PhotoProducer;
import com.ares.server_licenta.repository.PhotoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoProducer photoProducer;
    private final GoogleStorageService storageService;

    public PhotoService(PhotoRepository photoRepository, PhotoProducer photoProducer, GoogleStorageService storageService) {
        this.photoRepository = photoRepository;
        this.photoProducer = photoProducer;
        this.storageService = storageService;
    }

    public void processPhotos(UUID userId) {
        OffsetDateTime start = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime end = LocalDate.now().atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);

        List<Photo> photos = photoRepository.findByUserIdAndCurrentDayAndMissingLabels(userId, start, end);

        for (Photo photo : photos) {
            byte[] imageBytes = storageService.downloadFile(photo.getFileName());
            photoProducer.send(new PhotoMessage(photo.getFileName(), imageBytes));
        }
    }
}