package com.abc.api.services;

import com.abc.api.dto.ImageDto;
import com.abc.api.entities.Image;
import com.abc.api.entities.Product;
import com.abc.api.entities.User;
import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.payload.response.images.ImageResponse;
import com.abc.api.payload.response.products.ProductResponse;
import com.abc.api.repositories.ImageRepository;
import com.abc.api.repositories.ProductRepository;
import com.abc.api.utils.StorageHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    private final ProductRepository productRepository;

    private final StorageHandler storageHandler;

    public Image getById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    public Image getByName(String filename) {
        try {
            return imageRepository.findByName(filename);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Transactional
    public List<ImageDto> create(User user, List<MultipartFile> files, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        Long userAuth = user.getId();
        List<ImageDto> imageDtos = new ArrayList<>();

        int i = 1;
        for (MultipartFile file : files) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                Image image = new Image();
                image.setName(fileName);
                image.setSize(file.getSize()); // Size in sytes
                image.setType(file.getContentType());
                image.setSequence(i);
                image.setProduct(product);
                image.setCreatedBy(userAuth);

                String filePath = "products" +  File.separator + productId;
                String filePathDb = storageHandler.uploadFile(file, fileName, filePath);

                image.setPath(filePathDb);
                Image addedImage = imageRepository.save(image);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(addedImage.getId());
                imageDto.setName(addedImage.getName());
                imageDto.setPath(addedImage.getPath());

                imageDtos.add(imageDto);
                i++;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return imageDtos;
    }

    @Transactional
    public ImageResponse update(User user, MultipartFile file, Long id) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Image image = getById(id);

        image.setName(fileName);
        image.setSize(file.getSize()); //size in byte
        image.setType(file.getContentType());
        image.setUpdatedBy(user.getId());

        storageHandler.deleteFile(image.getPath());
        String filePath = "products" +  File.separator + image.getProduct().getId();
        String filePathDb = storageHandler.uploadFile(file, fileName, filePath);
        image.setPath(filePathDb);

        imageRepository.save(image);

        return ImageResponse.builder()
                .id(id)
                .name(image.getName())
                .size(image.getSize())
                .type(image.getType())
                .sequence(image.getSequence())
                .build();
    }

    @Transactional
    public void delete(Long id) {
        Image image = getById(id);
        storageHandler.deleteFile(image.getPath());
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        });
    }
}
