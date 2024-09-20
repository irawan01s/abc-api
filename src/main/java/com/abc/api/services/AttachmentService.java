package com.abc.api.services;

import com.abc.api.entities.Attachment;
import com.abc.api.entities.User;
import com.abc.api.payload.request.attachments.AttachmentCreateRequest;
import com.abc.api.payload.response.AttachmentResponse;
import com.abc.api.repositories.AttachmentRepository;
import com.abc.api.utils.StorageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final StorageHandler storageHandler;

    @Transactional
    public List<AttachmentResponse> create(User user, List<MultipartFile> files, AttachmentCreateRequest request) {
        List<AttachmentResponse> responses = new ArrayList<>();

        int i = 1;
        for (MultipartFile file : files) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                Attachment attachment = new Attachment();
                attachment.setName(fileName);
                attachment.setSize(file.getSize()); // Size in bytes
                attachment.setType(file.getContentType());
                attachment.setRefId(user.getId());
                attachment.setRefTable("users");
                attachment.setCreatedBy(user.getId());

                String filePath = "attachments" +  File.separator + user.getId();
                String filePathDb = storageHandler.uploadFile(file, fileName, filePath);

                attachment.setPath(filePathDb);
                Attachment addedImage = attachmentRepository.save(attachment);

                AttachmentResponse response = new AttachmentResponse();
                response.setId(addedImage.getId());
                response.setName(addedImage.getName());
                response.setPath(addedImage.getPath());

                responses.add(response);
                i++;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return responses;
    }
}
