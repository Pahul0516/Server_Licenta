package com.ares.server_licenta.messaging.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SceneLabelResponseTest {

    @Test
    void constructor_ShouldSetFields() {
        SceneLabelResponse response = new SceneLabelResponse("photo.jpg", "beach");

        assertEquals("photo.jpg", response.getFileName());
        assertEquals("beach", response.getLabel());
    }

    @Test
    void noArgsConstructor_ShouldLeaveFieldsNull() {
        SceneLabelResponse response = new SceneLabelResponse();

        assertNull(response.getFileName());
        assertNull(response.getLabel());
    }

    @Test
    void setters_ShouldUpdateFields() {
        SceneLabelResponse response = new SceneLabelResponse();

        response.setFileName("updated.jpg");
        response.setLabel("mountain");

        assertEquals("updated.jpg", response.getFileName());
        assertEquals("mountain", response.getLabel());
    }
}
