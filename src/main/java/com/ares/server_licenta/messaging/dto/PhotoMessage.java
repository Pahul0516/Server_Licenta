package com.ares.server_licenta.messaging.dto;

import lombok.Data;

@Data
public class PhotoMessage{

    private String fileName;
    private byte[] data;

    public PhotoMessage() {}

    public PhotoMessage(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }
}
