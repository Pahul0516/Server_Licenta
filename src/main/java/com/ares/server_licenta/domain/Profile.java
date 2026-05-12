package com.ares.server_licenta.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "profiles", schema = "public")
public class Profile {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", insertable = false, updatable = false)
    private String email;

    @Column(name = "first_name", insertable = false, updatable = true)
    private String firstName;

    @Column(name = "last_name", insertable = false, updatable = true)
    private String lastName;

    @Column(name = "avatar_url", insertable = false, updatable = true)
    private String avatarUrl;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

}