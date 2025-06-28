package com.suzano.inspection.service;

import com.suzano.inspection.entity.MediaFile;
import com.suzano.inspection.entity.Inspection;
import com.suzano.inspection.repository.MediaFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MediaFileService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name:suzano-inspection-media}")
    private String bucketName;

    @Value("${aws.s3.region:us-east-1}")
    private String region;

    public MediaFile uploadMediaFile(Long inspectionId, MultipartFile file, String description, 
                                   Double latitude, Double longitude) throws IOException {
        
        Inspection inspection = inspectionService.getInspectionById(inspectionId)
                .orElseThrow(() -> new RuntimeException("Inspection not found"));

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + extension;
        String s3Key = "inspections/" + inspectionId + "/" + uniqueFilename;

        // Upload to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        // Generate public URL
        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, s3Key);

        // Determine media type
        MediaFile.MediaType mediaType = determineMediaType(file.getContentType());

        // Create MediaFile entity
        MediaFile mediaFile = new MediaFile(inspection, originalFilename, s3Key, fileUrl, mediaType);
        mediaFile.setMimeType(file.getContentType());
        mediaFile.setFileSize(file.getSize());
        mediaFile.setDescription(description);
        mediaFile.setCaptureLatitude(latitude);
        mediaFile.setCaptureLongitude(longitude);
        mediaFile.setCapturedAt(LocalDateTime.now());

        return mediaFileRepository.save(mediaFile);
    }

    private MediaFile.MediaType determineMediaType(String mimeType) {
        if (mimeType.startsWith("image/")) {
            return MediaFile.MediaType.PHOTO;
        } else if (mimeType.startsWith("video/")) {
            return MediaFile.MediaType.VIDEO;
        } else if (mimeType.startsWith("audio/")) {
            return MediaFile.MediaType.AUDIO;
        } else {
            return MediaFile.MediaType.DOCUMENT;
        }
    }

    public List<MediaFile> getMediaFilesByInspection(Long inspectionId) {
        Inspection inspection = inspectionService.getInspectionById(inspectionId)
                .orElseThrow(() -> new RuntimeException("Inspection not found"));
        return mediaFileRepository.findByInspection(inspection);
    }

    public List<MediaFile> getMediaFilesByInspectionAndType(Long inspectionId, MediaFile.MediaType mediaType) {
        Inspection inspection = inspectionService.getInspectionById(inspectionId)
                .orElseThrow(() -> new RuntimeException("Inspection not found"));
        return mediaFileRepository.findByInspectionAndMediaType(inspection, mediaType);
    }

    public Optional<MediaFile> getMediaFileById(Long id) {
        return mediaFileRepository.findById(id);
    }

    public List<MediaFile> getAllMediaFiles() {
        return mediaFileRepository.findAll();
    }

    public List<MediaFile> getMediaFilesByType(MediaFile.MediaType mediaType) {
        return mediaFileRepository.findByMediaType(mediaType);
    }

    public long countMediaFilesByInspection(Long inspectionId) {
        Inspection inspection = inspectionService.getInspectionById(inspectionId)
                .orElseThrow(() -> new RuntimeException("Inspection not found"));
        return mediaFileRepository.countByInspection(inspection);
    }

    public long countMediaFilesByInspectionAndType(Long inspectionId, MediaFile.MediaType mediaType) {
        Inspection inspection = inspectionService.getInspectionById(inspectionId)
                .orElseThrow(() -> new RuntimeException("Inspection not found"));
        return mediaFileRepository.countByInspectionAndMediaType(inspection, mediaType);
    }

    public void deleteMediaFile(Long id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media file not found"));
        
        // Delete from S3
        try {
            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(mediaFile.getFilePath()));
        } catch (Exception e) {
            // Log error but continue with database deletion
            System.err.println("Error deleting file from S3: " + e.getMessage());
        }
        
        // Delete from database
        mediaFileRepository.deleteById(id);
    }

    public MediaFile updateMediaFileDescription(Long id, String description) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media file not found"));
        
        mediaFile.setDescription(description);
        return mediaFileRepository.save(mediaFile);
    }
}

