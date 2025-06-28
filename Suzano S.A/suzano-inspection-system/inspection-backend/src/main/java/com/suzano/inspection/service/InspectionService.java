package com.suzano.inspection.service;

import com.suzano.inspection.entity.Inspection;
import com.suzano.inspection.entity.Equipment;
import com.suzano.inspection.entity.User;
import com.suzano.inspection.repository.InspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InspectionService {

    @Autowired
    private InspectionRepository inspectionRepository;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private UserService userService;

    public Inspection createInspection(Inspection inspection) {
        return inspectionRepository.save(inspection);
    }

    public Inspection scheduleInspection(Long equipmentId, Long inspectorId, LocalDateTime scheduledDate) {
        Equipment equipment = equipmentService.getEquipmentById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        
        User inspector = userService.getUserById(inspectorId)
                .orElseThrow(() -> new RuntimeException("Inspector not found"));

        Inspection inspection = new Inspection(equipment, inspector);
        inspection.setScheduledDate(scheduledDate);
        
        return inspectionRepository.save(inspection);
    }

    public Inspection startInspection(Long inspectionId, Double latitude, Double longitude) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new RuntimeException("Inspection not found"));

        if (inspection.getStatus() != Inspection.InspectionStatus.PENDING) {
            throw new RuntimeException("Inspection cannot be started");
        }

        inspection.setStatus(Inspection.InspectionStatus.IN_PROGRESS);
        inspection.setStartedAt(LocalDateTime.now());
        inspection.setInspectionLatitude(latitude);
        inspection.setInspectionLongitude(longitude);

        return inspectionRepository.save(inspection);
    }

    public Inspection completeInspection(Long inspectionId, Inspection.InspectionResult result, String observations) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new RuntimeException("Inspection not found"));

        if (inspection.getStatus() != Inspection.InspectionStatus.IN_PROGRESS) {
            throw new RuntimeException("Inspection is not in progress");
        }

        inspection.setStatus(Inspection.InspectionStatus.COMPLETED);
        inspection.setResult(result);
        inspection.setObservations(observations);
        inspection.setCompletedAt(LocalDateTime.now());

        return inspectionRepository.save(inspection);
    }

    public Inspection markInspectionAsNotPerformed(Long inspectionId, String reason) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new RuntimeException("Inspection not found"));

        inspection.setStatus(Inspection.InspectionStatus.NOT_PERFORMED);
        inspection.setPendingReason(reason);

        return inspectionRepository.save(inspection);
    }

    public List<Inspection> getAllInspections() {
        return inspectionRepository.findAll();
    }

    public Optional<Inspection> getInspectionById(Long id) {
        return inspectionRepository.findById(id);
    }

    public List<Inspection> getInspectionsByEquipment(Long equipmentId) {
        Equipment equipment = equipmentService.getEquipmentById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        return inspectionRepository.findByEquipment(equipment);
    }

    public List<Inspection> getInspectionsByInspector(Long inspectorId) {
        User inspector = userService.getUserById(inspectorId)
                .orElseThrow(() -> new RuntimeException("Inspector not found"));
        return inspectionRepository.findByInspector(inspector);
    }

    public List<Inspection> getInspectionsByStatus(Inspection.InspectionStatus status) {
        return inspectionRepository.findByStatus(status);
    }

    public List<Inspection> getInspectionsByResult(Inspection.InspectionResult result) {
        return inspectionRepository.findByResult(result);
    }

    public List<Inspection> getInspectionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return inspectionRepository.findByScheduledDateBetween(startDate, endDate);
    }

    public List<Inspection> getCompletedInspectionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return inspectionRepository.findByCompletedAtBetween(startDate, endDate);
    }

    public List<Inspection> getPendingInspectionsForInspector(Long inspectorId) {
        User inspector = userService.getUserById(inspectorId)
                .orElseThrow(() -> new RuntimeException("Inspector not found"));
        return inspectionRepository.findByInspectorAndStatus(inspector, Inspection.InspectionStatus.PENDING);
    }

    public List<Inspection> getInProgressInspectionsForInspector(Long inspectorId) {
        User inspector = userService.getUserById(inspectorId)
                .orElseThrow(() -> new RuntimeException("Inspector not found"));
        return inspectionRepository.findByInspectorAndStatus(inspector, Inspection.InspectionStatus.IN_PROGRESS);
    }

    public long countInspectionsByStatus(Inspection.InspectionStatus status) {
        return inspectionRepository.countByStatus(status);
    }

    public long countInspectionsByInspectorAndStatus(Long inspectorId, Inspection.InspectionStatus status) {
        User inspector = userService.getUserById(inspectorId)
                .orElseThrow(() -> new RuntimeException("Inspector not found"));
        return inspectionRepository.countByInspectorAndStatus(inspector, status);
    }

    public void deleteInspection(Long id) {
        inspectionRepository.deleteById(id);
    }
}

