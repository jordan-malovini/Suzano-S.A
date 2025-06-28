package com.suzano.inspection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inspections")
public class Inspection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    @NotNull
    private Equipment equipment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspector_id", nullable = false)
    @NotNull
    private User inspector;
    
    @Enumerated(EnumType.STRING)
    private InspectionStatus status = InspectionStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    private InspectionResult result;
    
    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Size(max = 1000)
    private String observations; // Observações gerais da inspeção
    
    @Size(max = 500)
    private String pendingReason; // Motivo caso a inspeção esteja pendente
    
    private Double inspectionLatitude; // GPS no momento da inspeção
    private Double inspectionLongitude; // GPS no momento da inspeção
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MediaFile> mediaFiles = new ArrayList<>();
    
    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InspectionItem> inspectionItems = new ArrayList<>();
    
    public enum InspectionStatus {
        PENDING, IN_PROGRESS, COMPLETED, NOT_PERFORMED
    }
    
    public enum InspectionResult {
        APPROVED, APPROVED_WITH_OBSERVATIONS, REQUIRES_MAINTENANCE, CRITICAL
    }
    
    // Constructors
    public Inspection() {}
    
    public Inspection(Equipment equipment, User inspector) {
        this.equipment = equipment;
        this.inspector = inspector;
        this.scheduledDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }
    
    public User getInspector() { return inspector; }
    public void setInspector(User inspector) { this.inspector = inspector; }
    
    public InspectionStatus getStatus() { return status; }
    public void setStatus(InspectionStatus status) { this.status = status; }
    
    public InspectionResult getResult() { return result; }
    public void setResult(InspectionResult result) { this.result = result; }
    
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
    
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public String getPendingReason() { return pendingReason; }
    public void setPendingReason(String pendingReason) { this.pendingReason = pendingReason; }
    
    public Double getInspectionLatitude() { return inspectionLatitude; }
    public void setInspectionLatitude(Double inspectionLatitude) { this.inspectionLatitude = inspectionLatitude; }
    
    public Double getInspectionLongitude() { return inspectionLongitude; }
    public void setInspectionLongitude(Double inspectionLongitude) { this.inspectionLongitude = inspectionLongitude; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<MediaFile> getMediaFiles() { return mediaFiles; }
    public void setMediaFiles(List<MediaFile> mediaFiles) { this.mediaFiles = mediaFiles; }
    
    public List<InspectionItem> getInspectionItems() { return inspectionItems; }
    public void setInspectionItems(List<InspectionItem> inspectionItems) { this.inspectionItems = inspectionItems; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

