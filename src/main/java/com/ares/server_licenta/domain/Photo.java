package com.ares.server_licenta.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
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
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "scene_label", length = Integer.MAX_VALUE)
    private String sceneLabel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "coco_label")
    private List<String> cocoLabel = new java.util.ArrayList<>();

    @Column(name = "person_label", length = Integer.MAX_VALUE)
    private String personLabel;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

}