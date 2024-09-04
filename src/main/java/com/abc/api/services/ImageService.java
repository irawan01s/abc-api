package com.abc.api.services;

import com.abc.api.entities.Image;
import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private ImageRepository imageRepository;

    private final ProductService productService;

    public Image getById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    public void delete(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("Image not found");
        });
    }

    public void update(MultipartFile file, Long id) {
        Image image = getById(id);
        try {
            image.setName(file.getOriginalFilename());
            image.setType(file.getContentType());
            imageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
