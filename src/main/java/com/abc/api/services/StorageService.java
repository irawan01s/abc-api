package com.abc.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class StorageService {

    @Value("${storage.path}")
    private String STORAGE_PATH;

    public String uploadFile(MultipartFile file,String filePath) {
        Year year = Year.now();
        String directoryPath = year + File.separator + filePath;

        try {
            Path uploadPath = Paths.get(STORAGE_PATH + File.separator + directoryPath);
            Files.copy(file.getInputStream(), uploadPath.resolve(filePath + File.separator + file.getOriginalFilename()));

            return directoryPath;
        } catch (IOException e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    public Resource downloadFile(String filePath) {
        try {
            Path downloadPath = Paths.get(STORAGE_PATH + File.separator + filePath).normalize();
            System.out.println(downloadPath);
            Resource resource = new UrlResource(downloadPath.toUri());
            System.out.println(resource);

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

//    public void deleteAll() {
//        FileSystemUtils.deleteRecursively(root.toFile());
//    }
//
//    public Stream<Path> loadAll() {
//        try {
//            return  Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
//        } catch (IOException e) {
//            throw new RuntimeException("Could not load the files!, " + e);
//        }
//    }
}
