package com.ares.server_licenta.service;

import com.ares.server_licenta.domain.Photo;
import com.ares.server_licenta.dto.TimeWindowDTO;
import com.ares.server_licenta.repository.PhotoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StoryService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final PhotoRepository photoRepository;

    public StoryService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public String generateStoryJson(UUID userId) {
        Map<String, List<Photo>> groupedPhotos = new TreeMap<>();

        OffsetDateTime start = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime end = LocalDate.now().atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);
        List<Photo> photos = photoRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtAsc(userId, start, end);

        for (Photo p : photos) {
            groupedPhotos.computeIfAbsent(getWindowKey(p.getCreatedAt()), k -> new ArrayList<>()).add(p);
        }

        Map<String, TimeWindowDTO> timeline = new TreeMap<>();

        groupedPhotos.forEach((windowKey, photoList) -> {
            TimeWindowDTO dto = new TimeWindowDTO();
            int totalPhotos = photoList.size();

            Map<String, Integer> objectCounts = new HashMap<>();
            Map<String, Integer> sceneCounts = new HashMap<>();
            Map<String, Integer> personCounts = new HashMap<>();

            for (Photo p : photoList) {
                if (p.getCocoLabel() != null) {
                    p.getCocoLabel().forEach(label -> objectCounts.merge(label, 1, Integer::sum));
                }
                if (p.getSceneLabel() != null && !p.getSceneLabel().isBlank()) {
                    sceneCounts.merge(p.getSceneLabel(), 1, Integer::sum);
                }
                if (p.getPersonLabel() != null && !p.getPersonLabel().equalsIgnoreCase("Unknown")) {
                    personCounts.merge(p.getPersonLabel(), 1, Integer::sum);
                }
            }

            double threshold = totalPhotos * 0.15;

            objectCounts.forEach((label, count) -> {
                if (count >= threshold) dto.getObjects().add(label);
            });

            personCounts.forEach((name, count) -> {
                if (count >= threshold) dto.getPeople().add(name);
            });

            sceneCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .ifPresent(entry -> dto.getLocations().add(entry.getKey()));

            if (!dto.getObjects().isEmpty() || !dto.getLocations().isEmpty()) {
                timeline.put(windowKey, dto);
            }
        });

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(timeline);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Timeline JSON", e);
        }
    }

    private String getWindowKey(OffsetDateTime time) {
        int minute = time.getMinute();
        int roundedMinute = (minute / 15) * 15;
        return time.withMinute(roundedMinute).format(timeFormatter) + " - " +
                time.withMinute(roundedMinute).plusMinutes(15).format(timeFormatter);
    }
}