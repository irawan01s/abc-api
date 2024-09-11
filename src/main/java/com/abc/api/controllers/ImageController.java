package com.abc.api.controllers;

import com.abc.api.dto.ImageDto;
import com.abc.api.entities.Image;
import com.abc.api.entities.User;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.images.ImageResponse;
import com.abc.api.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final ImageService imageService;

    @Value("${storage.path}")
    private String STORAGE_PATH;


    @GetMapping("/preview/{fileName}")
    public ResponseEntity<Resource> previewImage(@PathVariable String fileName) {

        try {
            Image image = imageService.getByName(fileName);
            Path uriPath = Paths.get(STORAGE_PATH + File.separator + image.getPath());
            Resource resource = new UrlResource(uriPath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(image.getType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<WebResponse<?>> createImage(@AuthenticationPrincipal User user, @RequestParam List<MultipartFile> files, @RequestParam Long productId) {
            List<ImageDto> imageDtos = imageService.create(user, files, productId);

            return ResponseEntity.ok(WebResponse.builder()
                    .status(true)
                    .data("Images " + imageDtos)
                    .build());
    }

    @PutMapping("/{id}")
    public WebResponse<ImageResponse> updateImage(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody MultipartFile file) {
            ImageResponse response = imageService.update(user, file, id);
            return WebResponse.<ImageResponse>builder()
                    .status(true)
                    .data(response)
                    .build();
    }

    @DeleteMapping("/{id}")
    public WebResponse<String> deleteImage(@PathVariable Long id) {
        imageService.delete(id);
        return WebResponse.<String>builder()
                .status(true)
                .build();
    }
}
