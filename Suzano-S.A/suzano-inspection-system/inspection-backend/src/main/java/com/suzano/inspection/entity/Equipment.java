package com.suzano.inspection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipments")
public class Equipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    private String code; // Código único do equipamento (QR Code/Barcode)
    
    @NotBlank
    @Size(max = 200)
    private String name;
    
    @Size(max = 500)
    private String description;
    
    @NotBlank
    @Size(max = 100)
    private String location; // Localização física do equipamento
    
    @Size(max = 100)
    private String area; // Área da fábrica
    
    @Size(max = 100)
    private String sector; // Setor
    
    @Enumerated(EnumType.STRING)
    private EquipmentType type;
    
    @Enumerated(EnumType.STRING)
    private EquipmentStatus status = EquipmentStatus.ACTIVE;
    
    private Double latitude; // Coordenada GPS
    private Double longitude; // Coordenada GPS
    
    @Size(max = 100)
    private String manufacturer; // Fabricante
    
    @Size(max = 100)
    private String model; // Modelo
    
    @Size(max = 50)
    private String serialNumber; // Número de série
    
    private Integer installationYear; // Ano de instalação
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inspection> inspections = new ArrayList<>();
    
    public enum EquipmentType {
        MOTOR, PUMP, COMPRESSOR, VALVE, TANK, CONVEYOR, BOILER, TURBINE, GENERATOR, OTHER
    }
    
    public enum EquipmentStatus {
        ACTIVE, INACTIVE, MAINTENANCE, DECOMMISSIONED
    }
    
    // Constructors
    public Equipment() {}
    
    public Equipment(String code, String name, String location) {
        this.code = code;
        this.name = name;
        this.location = location;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    
    public EquipmentType getType() { return type; }
    public void setType(EquipmentType type) { this.type = type; }
    
    public EquipmentStatus getStatus() { return status; }
    public void setStatus(EquipmentStatus status) { this.status = status; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    
    public Integer getInstallationYear() { return installationYear; }
    public void setInstallationYear(Integer installationYear) { this.installationYear = installationYear; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Inspection> getInspections() { return inspections; }
    public void setInspections(List<Inspection> inspections) { this.inspections = inspections; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

