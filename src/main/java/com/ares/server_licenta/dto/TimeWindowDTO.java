package com.ares.server_licenta.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TimeWindowDTO {
    private Set<String> locations = new HashSet<>();
    private Set<String> people = new HashSet<>();
    private Set<String> objects = new HashSet<>();
}