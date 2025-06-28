package com.suzano.inspection.controller;

import com.suzano.inspection.entity.Equipment;
import com.suzano.inspection.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
@CrossOrigin(origins = "*")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipments() {
        List<Equipment> equipments = equipmentService.getAllEquipments();
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        return equipmentService.getEquipmentById(id)
                .map(equipment -> ResponseEntity.ok().body(equipment))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Equipment> getEquipmentByCode(@PathVariable String code) {
        return equipmentService.getEquipmentByCode(code)
                .map(equipment -> ResponseEntity.ok().body(equipment))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Equipment>> getEquipmentsByStatus(@PathVariable Equipment.EquipmentStatus status) {
        List<Equipment> equipments = equipmentService.getEquipmentsByStatus(status);
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/area/{area}")
    public ResponseEntity<List<Equipment>> getEquipmentsByArea(@PathVariable String area) {
        List<Equipment> equipments = equipmentService.getEquipmentsByArea(area);
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/sector/{sector}")
    public ResponseEntity<List<Equipment>> getEquipmentsBySector(@PathVariable String sector) {
        List<Equipment> equipments = equipmentService.getEquipmentsBySector(sector);
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Equipment>> getEquipmentsByType(@PathVariable Equipment.EquipmentType type) {
        List<Equipment> equipments = equipmentService.getEquipmentsByType(type);
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Equipment>> searchEquipments(@RequestParam String q) {
        List<Equipment> equipments = equipmentService.searchEquipments(q);
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/areas")
    public ResponseEntity<List<String>> getDistinctAreas() {
        List<String> areas = equipmentService.getDistinctAreas();
        return ResponseEntity.ok(areas);
    }

    @GetMapping("/sectors")
    public ResponseEntity<List<String>> getDistinctSectors() {
        List<String> sectors = equipmentService.getDistinctSectors();
        return ResponseEntity.ok(sectors);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> createEquipment(@Valid @RequestBody Equipment equipment) {
        try {
            Equipment createdEquipment = equipmentService.createEquipment(equipment);
            return ResponseEntity.ok(createdEquipment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating equipment: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> updateEquipment(@PathVariable Long id, @Valid @RequestBody Equipment equipmentDetails) {
        try {
            Equipment updatedEquipment = equipmentService.updateEquipment(id, equipmentDetails);
            return ResponseEntity.ok(updatedEquipment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating equipment: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> changeEquipmentStatus(@PathVariable Long id, @RequestParam Equipment.EquipmentStatus status) {
        try {
            Equipment updatedEquipment = equipmentService.changeEquipmentStatus(id, status);
            return ResponseEntity.ok(updatedEquipment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error changing equipment status: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        try {
            equipmentService.deleteEquipment(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting equipment: " + e.getMessage());
        }
    }
}

