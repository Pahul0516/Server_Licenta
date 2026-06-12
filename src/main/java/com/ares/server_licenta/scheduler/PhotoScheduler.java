package com.ares.server_licenta.scheduler;

import com.ares.server_licenta.repository.ProfileRepository;
import com.ares.server_licenta.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PhotoScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoScheduler.class);

    private final ProfileRepository profileRepository;
    private final PhotoService photoService;

    public PhotoScheduler(ProfileRepository profileRepository, PhotoService photoService) {
        this.profileRepository = profileRepository;
        this.photoService = photoService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void labelPhotosForAllUsers() {
        LOGGER.info("Scheduled photo labeling started");
        profileRepository.findAll().forEach(profile -> photoService.processPhotos(profile.getId()));
    }
}
