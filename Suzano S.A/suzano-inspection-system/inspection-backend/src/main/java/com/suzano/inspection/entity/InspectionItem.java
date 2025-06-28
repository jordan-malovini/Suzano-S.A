package com.suzano.inspection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_items")
public class InspectionItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_id", nullable = false)
    @NotNull
    private Inspection inspection;
    
    @NotBlank
    @Size(max = 200)
    private String itemName; // Nome do item verificado
    
    @Size(max = 500)
    private String description; // Descrição do que deve ser verificado
    
    @Enumerated(EnumType.STRING)
    private ItemStatus status;
    
    @Size(max = 1000)
    private String observations; // Observações específicas do item
    
    @Size(max = 100)
    private String measuredValue; // Valor medido (se aplicável)
    
    @Size(max = 50)
    private String unit; // Unidade de medida
    
    @Size(max = 100)
    private String expectedValue; // Valor esperado
    
    private Boolean isCompliant; // Se está conforme
    
    @Column(name = "checked_at")
    private LocalDateTime checkedAt = LocalDateTime.now();
    
    public enum ItemStatus {
        NOT_CHECKED, APPROVED, REQUIRES_ATTENTION, CRITICAL, NOT_APPLICABLE
    }
    
    // Constructors
    public InspectionItem() {}
    
    public InspectionItem(Inspection inspection, String itemName) {
        this.inspection = inspection;
        this.itemName = itemName;
        this.status = ItemStatus.NOT_CHECKED;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Inspection getInspection() { return inspection; }
    public void setInspection(Inspection inspection) { this.inspection = inspection; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public String getMeasuredValue() { return measuredValue; }
    public void setMeasuredValue(String measuredValue) { this.measuredValue = measuredValue; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public String getExpectedValue() { return expectedValue; }
    public void setExpectedValue(String expectedValue) { this.expectedValue = expectedValue; }
    
    public Boolean getIsCompliant() { return isCompliant; }
    public void setIsCompliant(Boolean isCompliant) { this.isCompliant = isCompliant; }
    
    public LocalDateTime getCheckedAt() { return checkedAt; }
    public void setCheckedAt(LocalDateTime checkedAt) { this.checkedAt = checkedAt; }
}

