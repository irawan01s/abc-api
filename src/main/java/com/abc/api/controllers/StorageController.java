package com.abc.api.controllers;

import com.abc.api.entities.Attachment;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload")
    public WebResponse<String> uploadFile(@RequestParam("file") MultipartFile[] files) {
        String message = "";

        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.asList(files).stream().forEach(file -> {
                String filePath = "/report" ;
                storageService.uploadFile(file, filePath);
                fileNames.add(file.getOriginalFilename());
            });


            message = "Uploaded the file successfully: " + fileNames;
            return WebResponse.<String>builder()
                    .data(message)
                    .build();
        } catch (Exception e) {
            message = "Could not upload the file, Error: " + e.getMessage();
            return WebResponse.<String>builder()
                    .errors(message)
                    .build();
        }
    };

    @GetMapping("/download/{name}")
    public ResponseEntity<?> downloadFile(@PathVariable(value = "name") String filename) {
        try {
            var fileToDownload = storageService.downloadFile(filename);
            System.out.println(fileToDownload);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileToDownload);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/files")
    public WebResponse<List<Attachment>> getAll() {
        List<Attachment> files = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(StorageController.class,
                    "getFile",
                    path.getFileName().toString()).build().toString();
            return new Attachment(filename, url);
        }).collect(Collectors.toList());

        return  WebResponse.<List<Attachment>>builder()
                .data(files)
                .build();
    }
}
