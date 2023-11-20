package com.druzynav.controllers;

import com.druzynav.models.photo.Photo;
import com.druzynav.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping(value = "/api/v1/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhoto(@RequestParam("image") MultipartFile file, @RequestParam("id") Integer id) {
        try {
            photoService.savePhoto(file, id);
            return ResponseEntity.ok().body("File uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file!");
        }
    }

    @GetMapping("/api/v1/download/{id}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable Integer id) {
        Optional<Photo> photo = photoService.getPhotoById(id);

        if (photo.isPresent()) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photo.get().getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/v1/delete/{id}")
    public ResponseEntity<byte[]> deletePhotoById(@PathVariable Integer id) {
        photoService.deletePhoto(id);

        return ResponseEntity.noContent().build();

    }
}

