package com.ares.server_licenta.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleStorageService {

    private final Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public GoogleStorageService(Storage storage) {
        this.storage = storage;
    }

    public byte[] downloadFile(String fileName) {
        Blob blob = storage.get(bucketName, fileName);
        if (blob == null) {
            throw new RuntimeException("File not found in GCS: " + fileName);
        }
        return blob.getContent();
    }
}