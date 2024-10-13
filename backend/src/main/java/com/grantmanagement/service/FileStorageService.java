package com.grantmanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public interface FileStorageService {
    void init();
    String store(MultipartFile file) throws IOException;
    Stream<Path> loadAll();
    Path load(String filename);
    byte[] readFileAsBytes(String filename) throws IOException;
    void deleteAll();
    void delete(String filename) throws IOException;
    String getFileUrl(String filename);
}
