package com.ares.server_licenta.messaging.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonLabelResponseTest {

    @Test
    void constructor_ShouldSetFields() {
        List<String> labels = List.of("Alice", "Bob");

        PersonLabelResponse response = new PersonLabelResponse("photo.jpg", labels);

        assertEquals("photo.jpg", response.getFileName());
        assertEquals(labels, response.getLabels());
    }

    @Test
    void noArgsConstructor_ShouldLeaveFieldsNull() {
        PersonLabelResponse response = new PersonLabelResponse();

        assertNull(response.getFileName());
        assertNull(response.getLabels());
    }

    @Test
    void setters_ShouldUpdateFields() {
        List<String> labels = List.of("Charlie");
        PersonLabelResponse response = new PersonLabelResponse();

        response.setFileName("updated.jpg");
        response.setLabels(labels);

        assertEquals("updated.jpg", response.getFileName());
        assertEquals(labels, response.getLabels());
    }
}
