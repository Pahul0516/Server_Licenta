package com.ares.server_licenta.messaging.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CocoLabelResponseTest {

    @Test
    void constructor_ShouldSetFields() {
        List<String> labels = List.of("cat", "dog");

        CocoLabelResponse response = new CocoLabelResponse("photo.jpg", labels);

        assertEquals("photo.jpg", response.getFileName());
        assertEquals(labels, response.getLabels());
    }

    @Test
    void noArgsConstructor_ShouldLeaveFieldsNull() {
        CocoLabelResponse response = new CocoLabelResponse();

        assertNull(response.getFileName());
        assertNull(response.getLabels());
    }

    @Test
    void setters_ShouldUpdateFields() {
        List<String> labels = List.of("person", "car");
        CocoLabelResponse response = new CocoLabelResponse();

        response.setFileName("updated.jpg");
        response.setLabels(labels);

        assertEquals("updated.jpg", response.getFileName());
        assertEquals(labels, response.getLabels());
    }
}
