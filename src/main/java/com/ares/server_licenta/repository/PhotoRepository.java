package com.ares.server_licenta.repository;

import com.ares.server_licenta.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

    @Query(value = "SELECT * FROM photos p WHERE p.user_id = :userId " +
            "AND p.created_at >= :startOfDay " +
            "AND p.created_at <= :endOfDay " +
            "AND (" +
            "  p.coco_label IS NULL OR jsonb_array_length(p.coco_label) = 0 " + // JSONB check
            "  OR p.scene_label IS NULL OR trim(p.scene_label) = '' " +         // TEXT check
            "  OR p.person_label IS NULL OR trim(p.person_label) = ''" +        // TEXT check
            ")", nativeQuery = true)
    List<Photo> findByUserIdAndCurrentDayAndMissingLabels(
            @Param("userId") UUID userId,
            @Param("startOfDay") OffsetDateTime startOfDay,
            @Param("endOfDay") OffsetDateTime endOfDay
    );

    @Query("SELECT p FROM Photo p WHERE p.fileName = :fileName")
    Photo findFileName(
            @Param("fileName") String fileName
    );
}