package com.suzano.inspection.service;

import com.suzano.inspection.entity.Equipment;
import com.suzano.inspection.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    public Equipment createEquipment(Equipment equipment) {
        if (equipmentRepository.existsByCode(equipment.getCode())) {
            throw new RuntimeException("Equipment code already exists");
        }
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(Long id, Equipment equipmentDetails) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        equipment.setName(equipmentDetails.getName());
        equipment.setDescription(equipmentDetails.getDescription());
        equipment.setLocation(equipmentDetails.getLocation());
        equipment.setArea(equipmentDetails.getArea());
        equipment.setSector(equipmentDetails.getSector());
        equipment.setType(equipmentDetails.getType());
        equipment.setStatus(equipmentDetails.getStatus());
        equipment.setLatitude(equipmentDetails.getLatitude());
        equipment.setLongitude(equipmentDetails.getLongitude());
        equipment.setManufacturer(equipmentDetails.getManufacturer());
        equipment.setModel(equipmentDetails.getModel());
        equipment.setSerialNumber(equipmentDetails.getSerialNumber());
        equipment.setInstallationYear(equipmentDetails.getInstallationYear());

        return equipmentRepository.save(equipment);
    }

    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    public Optional<Equipment> getEquipmentByCode(String code) {
        return equipmentRepository.findByCode(code);
    }

    public List<Equipment> getEquipmentsByStatus(Equipment.EquipmentStatus status) {
        return equipmentRepository.findByStatus(status);
    }

    public List<Equipment> getEquipmentsByLocation(String location) {
        return equipmentRepository.findByLocation(location);
    }

    public List<Equipment> getEquipmentsByArea(String area) {
        return equipmentRepository.findByArea(area);
    }

    public List<Equipment> getEquipmentsBySector(String sector) {
        return equipmentRepository.findBySector(sector);
    }

    public List<Equipment> getEquipmentsByType(Equipment.EquipmentType type) {
        return equipmentRepository.findByType(type);
    }

    public List<Equipment> searchEquipments(String searchTerm) {
        return equipmentRepository.findByNameOrCodeContaining(searchTerm);
    }

    public List<String> getDistinctAreas() {
        return equipmentRepository.findDistinctAreas();
    }

    public List<String> getDistinctSectors() {
        return equipmentRepository.findDistinctSectors();
    }

    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    public Equipment changeEquipmentStatus(Long id, Equipment.EquipmentStatus status) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setStatus(status);
        return equipmentRepository.save(equipment);
    }
}

