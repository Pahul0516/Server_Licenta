package com.ares.server_licenta.messaging.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhotoMessageTest {

    @Test
    void constructor_ShouldSetFields() {
        byte[] data = "image".getBytes();

        PhotoMessage message = new PhotoMessage("photo.jpg", data);

        assertEquals("photo.jpg", message.getFileName());
        assertArrayEquals(data, message.getData());
    }

    @Test
    void noArgsConstructor_ShouldLeaveFieldsNull() {
        PhotoMessage message = new PhotoMessage();

        assertNull(message.getFileName());
        assertNull(message.getData());
    }

    @Test
    void setters_ShouldUpdateFields() {
        byte[] data = "updated".getBytes();
        PhotoMessage message = new PhotoMessage();

        message.setFileName("updated.jpg");
        message.setData(data);

        assertEquals("updated.jpg", message.getFileName());
        assertArrayEquals(data, message.getData());
    }
}
