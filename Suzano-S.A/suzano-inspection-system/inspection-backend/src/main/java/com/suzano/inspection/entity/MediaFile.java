package com.suzano.inspection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_files")
public class MediaFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_id", nullable = false)
    @NotNull
    private Inspection inspection;
    
    @NotBlank
    @Size(max = 255)
    private String fileName; // Nome original do arquivo
    
    @NotBlank
    @Size(max = 500)
    private String filePath; // Caminho no S3 ou storage
    
    @NotBlank
    @Size(max = 500)
    private String fileUrl; // URL pública do arquivo
    
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;
    
    @Size(max = 100)
    private String mimeType; // image/jpeg, video/mp4, etc.
    
    private Long fileSize; // Tamanho em bytes
    
    @Size(max = 500)
    private String description; // Descrição da mídia
    
    private Double captureLatitude; // GPS no momento da captura
    private Double captureLongitude; // GPS no momento da captura
    
    @Column(name = "captured_at")
    private LocalDateTime capturedAt = LocalDateTime.now();
    
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();
    
    public enum MediaType {
        PHOTO, VIDEO, AUDIO, DOCUMENT
    }
    
    // Constructors
    public MediaFile() {}
    
    public MediaFile(Inspection inspection, String fileName, String filePath, String fileUrl, MediaType mediaType) {
        this.inspection = inspection;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileUrl = fileUrl;
        this.mediaType = mediaType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Inspection getInspection() { return inspection; }
    public void setInspection(Inspection inspection) { this.inspection = inspection; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    
    public MediaType getMediaType() { return mediaType; }
    public void setMediaType(MediaType mediaType) { this.mediaType = mediaType; }
    
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getCaptureLatitude() { return captureLatitude; }
    public void setCaptureLatitude(Double captureLatitude) { this.captureLatitude = captureLatitude; }
    
    public Double getCaptureLongitude() { return captureLongitude; }
    public void setCaptureLongitude(Double captureLongitude) { this.captureLongitude = captureLongitude; }
    
    public LocalDateTime getCapturedAt() { return capturedAt; }
    public void setCapturedAt(LocalDateTime capturedAt) { this.capturedAt = capturedAt; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}

