package com.abc.api.services;

import com.abc.api.dto.ImageDto;
import com.abc.api.entities.Image;
import com.abc.api.entities.Product;
import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.repositories.ImageRepository;
import com.abc.api.utils.StorageHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    private final ProductService productService;

    private final StorageHandler storageHandler;

    public Image getById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    public void delete(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("Image not found");
        });
    }

    @Transactional
    public List<ImageDto> create(List<MultipartFile> files, Long productId) {
        Product product = productService.getById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setName(file.getOriginalFilename());
                image.setType(file.getContentType());
                image.setSize(files.size());
                image.setProduct(product);

                String tmpFilePath = "/products/" + productId;
                String filePath = storageHandler.uploadFile(file, tmpFilePath);

                image.setPath(filePath);
                Image addedImage = imageRepository.save(image);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(addedImage.getId());
                imageDto.setName(addedImage.getName());
                imageDto.setPath(addedImage.getPath());

                imageDtos.add(imageDto);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return imageDtos;
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
