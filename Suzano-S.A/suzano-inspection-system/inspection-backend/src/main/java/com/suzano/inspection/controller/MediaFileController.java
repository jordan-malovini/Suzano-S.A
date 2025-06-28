package com.suzano.inspection.controller;

import com.suzano.inspection.entity.MediaFile;
import com.suzano.inspection.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaFileController {

    @Autowired
    private MediaFileService mediaFileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMediaFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("inspectionId") Long inspectionId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude) {
        
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            MediaFile mediaFile = mediaFileService.uploadMediaFile(inspectionId, file, description, latitude, longitude);
            return ResponseEntity.ok(mediaFile);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<MediaFile>> getAllMediaFiles() {
        List<MediaFile> mediaFiles = mediaFileService.getAllMediaFiles();
        return ResponseEntity.ok(mediaFiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaFile> getMediaFileById(@PathVariable Long id) {
        return mediaFileService.getMediaFileById(id)
                .map(mediaFile -> ResponseEntity.ok().body(mediaFile))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/inspection/{inspectionId}")
    public ResponseEntity<List<MediaFile>> getMediaFilesByInspection(@PathVariable Long inspectionId) {
        List<MediaFile> mediaFiles = mediaFileService.getMediaFilesByInspection(inspectionId);
        return ResponseEntity.ok(mediaFiles);
    }

    @GetMapping("/inspection/{inspectionId}/type/{mediaType}")
    public ResponseEntity<List<MediaFile>> getMediaFilesByInspectionAndType(
            @PathVariable Long inspectionId,
            @PathVariable MediaFile.MediaType mediaType) {
        List<MediaFile> mediaFiles = mediaFileService.getMediaFilesByInspectionAndType(inspectionId, mediaType);
        return ResponseEntity.ok(mediaFiles);
    }

    @GetMapping("/type/{mediaType}")
    public ResponseEntity<List<MediaFile>> getMediaFilesByType(@PathVariable MediaFile.MediaType mediaType) {
        List<MediaFile> mediaFiles = mediaFileService.getMediaFilesByType(mediaType);
        return ResponseEntity.ok(mediaFiles);
    }

    @GetMapping("/inspection/{inspectionId}/count")
    public ResponseEntity<Long> countMediaFilesByInspection(@PathVariable Long inspectionId) {
        long count = mediaFileService.countMediaFilesByInspection(inspectionId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/inspection/{inspectionId}/type/{mediaType}/count")
    public ResponseEntity<Long> countMediaFilesByInspectionAndType(
            @PathVariable Long inspectionId,
            @PathVariable MediaFile.MediaType mediaType) {
        long count = mediaFileService.countMediaFilesByInspectionAndType(inspectionId, mediaType);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/description")
    public ResponseEntity<?> updateMediaFileDescription(
            @PathVariable Long id,
            @RequestParam String description) {
        try {
            MediaFile mediaFile = mediaFileService.updateMediaFileDescription(id, description);
            return ResponseEntity.ok(mediaFile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating media file description: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMediaFile(@PathVariable Long id) {
        try {
            mediaFileService.deleteMediaFile(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting media file: " + e.getMessage());
        }
    }
}

