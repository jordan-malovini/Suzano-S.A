package com.suzano.inspection.controller;

import com.suzano.inspection.entity.Inspection;
import com.suzano.inspection.service.InspectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inspections")
@CrossOrigin(origins = "*")
public class InspectionController {

    @Autowired
    private InspectionService inspectionService;

    @GetMapping
    public ResponseEntity<List<Inspection>> getAllInspections() {
        List<Inspection> inspections = inspectionService.getAllInspections();
        return ResponseEntity.ok(inspections);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inspection> getInspectionById(@PathVariable Long id) {
        return inspectionService.getInspectionById(id)
                .map(inspection -> ResponseEntity.ok().body(inspection))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<Inspection>> getInspectionsByEquipment(@PathVariable Long equipmentId) {
        List<Inspection> inspections = inspectionService.getInspectionsByEquipment(equipmentId);
        return ResponseEntity.ok(inspections);
    }

    @GetMapping("/inspector/{inspectorId}")
    public ResponseEntity<List<Inspection>> getInspectionsByInspector(@PathVariable Long inspectorId) {
        List<Inspection> inspections = inspectionService.getInspectionsByInspector(inspectorId);
        return ResponseEntity.ok(inspections);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Inspection>> getInspectionsByStatus(@PathVariable Inspection.InspectionStatus status) {
        List<Inspection> inspections = inspectionService.getInspectionsByStatus(status);
        return ResponseEntity.ok(inspections);
    }

    @GetMapping("/result/{result}")
    public ResponseEntity<List<Inspection>> getInspectionsByResult(@PathVariable Inspection.InspectionResult result) {
        List<Inspection> inspections = inspectionService.getInspectionsByResult(result);
        return ResponseEntity.ok(inspections);
    }

    @GetMapping("/my-pending")
    public ResponseEntity<List<Inspection>> getMyPendingInspections(Authentication authentication) {
        // Get current user ID from authentication
        // This would need to be implemented based on your User entity structure
        // For now, assuming we can get user ID from authentication
        Long inspectorId = getCurrentUserId(authentication);
        List<Inspection> inspections = inspectionService.getPendingInspectionsForInspector(inspectorId);
        return ResponseEntity.ok(inspections);
    }

    @GetMapping("/my-in-progress")
    public ResponseEntity<List<Inspection>> getMyInProgressInspections(Authentication authentication) {
        Long inspectorId = getCurrentUserId(authentication);
        List<Inspection> inspections = inspectionService.getInProgressInspectionsForInspector(inspectorId);
        return ResponseEntity.ok(inspections);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Inspection>> getInspectionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Inspection> inspections = inspectionService.getInspectionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(inspections);
    }

    @GetMapping("/completed/date-range")
    public ResponseEntity<List<Inspection>> getCompletedInspectionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Inspection> inspections = inspectionService.getCompletedInspectionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(inspections);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> createInspection(@Valid @RequestBody Inspection inspection) {
        try {
            Inspection createdInspection = inspectionService.createInspection(inspection);
            return ResponseEntity.ok(createdInspection);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating inspection: " + e.getMessage());
        }
    }

    @PostMapping("/schedule")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> scheduleInspection(
            @RequestParam Long equipmentId,
            @RequestParam Long inspectorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledDate) {
        try {
            Inspection inspection = inspectionService.scheduleInspection(equipmentId, inspectorId, scheduledDate);
            return ResponseEntity.ok(inspection);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error scheduling inspection: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<?> startInspection(
            @PathVariable Long id,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        try {
            Inspection inspection = inspectionService.startInspection(id, latitude, longitude);
            return ResponseEntity.ok(inspection);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error starting inspection: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeInspection(
            @PathVariable Long id,
            @RequestParam Inspection.InspectionResult result,
            @RequestParam(required = false) String observations) {
        try {
            Inspection inspection = inspectionService.completeInspection(id, result, observations);
            return ResponseEntity.ok(inspection);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error completing inspection: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/not-performed")
    public ResponseEntity<?> markInspectionAsNotPerformed(
            @PathVariable Long id,
            @RequestParam String reason) {
        try {
            Inspection inspection = inspectionService.markInspectionAsNotPerformed(id, reason);
            return ResponseEntity.ok(inspection);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error marking inspection as not performed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteInspection(@PathVariable Long id) {
        try {
            inspectionService.deleteInspection(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting inspection: " + e.getMessage());
        }
    }

    // Helper method to get current user ID from authentication
    private Long getCurrentUserId(Authentication authentication) {
        // This is a simplified implementation
        // In a real application, you would extract the user ID from the JWT token or user details
        return 1L; // Placeholder - implement based on your authentication setup
    }
}

