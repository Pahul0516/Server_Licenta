package com.ares.server_licenta;

import com.ares.server_licenta.repository.ProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerLicentaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerLicentaApplication.class, args);
    }

    @Bean
    public CommandLineRunner testRepository(ProfileRepository repo) {
        return args -> {
            // This code runs AFTER the application starts
            var allProfiles = repo.findAll();
            System.out.println("Found " + allProfiles.size() + " profiles in Supabase.");
            allProfiles.forEach(p -> System.out.println(p.getEmail()));
        };
    }

}
