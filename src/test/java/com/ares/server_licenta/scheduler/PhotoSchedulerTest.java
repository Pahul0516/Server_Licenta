package com.ares.server_licenta.scheduler;

import com.ares.server_licenta.domain.Profile;
import com.ares.server_licenta.repository.ProfileRepository;
import com.ares.server_licenta.service.PhotoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoSchedulerTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PhotoService photoService;

    @InjectMocks
    private PhotoScheduler photoScheduler;

    @Test
    void labelPhotosForAllUsers_ShouldCallProcessPhotosForEachUser() {
        // Arrange
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        Profile profile1 = new Profile();
        profile1.setId(userId1);

        Profile profile2 = new Profile();
        profile2.setId(userId2);

        when(profileRepository.findAll()).thenReturn(List.of(profile1, profile2));

        // Act
        photoScheduler.labelPhotosForAllUsers();

        // Assert
        verify(photoService).processPhotos(userId1);
        verify(photoService).processPhotos(userId2);
        verifyNoMoreInteractions(photoService);
    }

    @Test
    void labelPhotosForAllUsers_ShouldDoNothing_WhenNoUsersExist() {
        // Arrange
        when(profileRepository.findAll()).thenReturn(List.of());

        // Act
        photoScheduler.labelPhotosForAllUsers();

        // Assert
        verifyNoInteractions(photoService);
    }
}
