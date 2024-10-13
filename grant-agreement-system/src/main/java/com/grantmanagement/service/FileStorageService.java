package com.grantmanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Service
public interface FileStorageService {
    String storeFile(MultipartFile file) throws IOException;
    byte[] getFile(String fileName) throws IOException;
    void deleteFile(String fileName) throws IOException;
    Path getFilePath(String fileName);
}
