package com.ares.server_licenta.repository;

import com.ares.server_licenta.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

    @Query("SELECT p FROM Photo p WHERE p.userId = :userId AND CAST(p.createdAt AS date) = :targetDate")
    List<Photo> findByUserIdAndDate(
            @Param("userId") UUID userId,
            @Param("targetDate") LocalDate targetDate
    );
}