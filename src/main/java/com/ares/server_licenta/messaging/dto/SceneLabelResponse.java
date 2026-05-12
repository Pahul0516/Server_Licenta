package com.ares.server_licenta.messaging.dto;

public class SceneLabelResponse {
    private String fileName;
    private String label;

    public SceneLabelResponse() {}

    public SceneLabelResponse(String fileName, String label) {
        this.fileName = fileName;
        this.label = label;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}