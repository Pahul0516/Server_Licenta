package com.ares.server_licenta.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table (name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "taken_at", nullable = false)
    private OffsetDateTime takenAt;

    @Column(name = "scene_label", length = Integer.MAX_VALUE)
    private String sceneLabel;

    @Column(name = "coco_label", length = Integer.MAX_VALUE)
    private String cocoLabel;

    @Column(name = "person_label", length = Integer.MAX_VALUE)
    private String personLabel;

    @Column(name = "storage_path", length = Integer.MAX_VALUE)
    private String storagePath;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

}