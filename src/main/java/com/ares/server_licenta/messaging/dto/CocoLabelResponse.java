package com.ares.server_licenta.messaging.dto;

import java.util.List;

public class CocoLabelResponse {

    private String fileName;
    private List<String> labels;

    public CocoLabelResponse() {}

    public CocoLabelResponse(String fileName, List<String> labels) {
        this.fileName = fileName;
        this.labels = labels;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}