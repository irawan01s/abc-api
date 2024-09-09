package com.abc.api.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.Year;

@Component
public class StorageHandler {

    @Value("${storage.path}")
    private String STORAGE_PATH;

    public String uploadFile(MultipartFile file, String filePath) {
        Year year = Year.now();
        String directoryPath = year + File.separator + filePath;

        try {
            Path uploadPath = Paths.get(STORAGE_PATH + File.separator + directoryPath);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = file.getOriginalFilename();
            Path storagePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), storagePath, StandardCopyOption.REPLACE_EXISTING);

            return directoryPath + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    public Resource downloadFile(String filePath) {
        try {
            Path downloadPath = Paths.get(filePath);

            Resource resource = new UrlResource(downloadPath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
