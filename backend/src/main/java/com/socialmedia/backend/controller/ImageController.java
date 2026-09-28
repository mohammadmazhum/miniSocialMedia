package com.socialmedia.backend.controller;

import com.socialmedia.backend.service.ImageUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageUploadService imageUploadService;

    public ImageController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file) {

        try {

            String imageUrl =
                    imageUploadService.uploadImage(file);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Image uploaded successfully",
                            "url", imageUrl
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Image upload failed: " + e.getMessage());
        }
    }
}