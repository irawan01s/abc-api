package com.abc.api.controllers;

import com.abc.api.dto.ImageDto;
import com.abc.api.entities.Image;
import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.services.ImageService;
import com.abc.api.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final ImageService imageService;

    private final StorageService storageService;

    @Value("${storage.path}")
    private String STORAGE_PATH;

    @PostMapping("/upload")
    public ResponseEntity<WebResponse> createImage(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.create(files, productId);

            return ResponseEntity.ok(WebResponse.builder()
                    .data("Upload Success" + imageDtos).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WebResponse.builder()
                            .errors("Upload failed " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@RequestParam Long imageId) {
        Image image = imageService.getById(imageId);

        Resource resource = storageService.downloadFile(image.getPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/preview/{filepath}")
    public ResponseEntity<Resource> previewImage(@PathVariable String filepath) {
        try {
            Path filePath = Paths.get(STORAGE_PATH + File.separator + filepath);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse> updateImage(@PathVariable Long id, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getById(id);
            if (image != null) {
                imageService.update(file, id);
                return ResponseEntity.ok(WebResponse.builder()
                        .data("Update Success")
                        .build());
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WebResponse.builder()
                            .data(e.getMessage())
                            .build());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.builder()
                        .data("Update Failed")
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse> deleteImage(@PathVariable Long id) {
        try {
            Image image = imageService.getById(id);
            if (image != null) {
                imageService.delete(id);
                return ResponseEntity.ok(WebResponse.builder()
                        .data("Delete Success")
                        .build());
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WebResponse.builder()
                            .data(e.getMessage())
                            .build());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.builder()
                        .data("Delete Failed")
                        .build());
    }
}
