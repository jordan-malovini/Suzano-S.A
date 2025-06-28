package com.suzano.inspection.repository;

import com.suzano.inspection.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    
    Optional<Equipment> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<Equipment> findByStatus(Equipment.EquipmentStatus status);
    
    List<Equipment> findByLocation(String location);
    
    List<Equipment> findByArea(String area);
    
    List<Equipment> findBySector(String sector);
    
    List<Equipment> findByType(Equipment.EquipmentType type);
    
    @Query("SELECT e FROM Equipment e WHERE e.name LIKE %:name% OR e.code LIKE %:name%")
    List<Equipment> findByNameOrCodeContaining(@Param("name") String name);
    
    @Query("SELECT e FROM Equipment e WHERE e.area = :area AND e.status = :status")
    List<Equipment> findByAreaAndStatus(@Param("area") String area, @Param("status") Equipment.EquipmentStatus status);
    
    @Query("SELECT DISTINCT e.area FROM Equipment e WHERE e.area IS NOT NULL ORDER BY e.area")
    List<String> findDistinctAreas();
    
    @Query("SELECT DISTINCT e.sector FROM Equipment e WHERE e.sector IS NOT NULL ORDER BY e.sector")
    List<String> findDistinctSectors();
}

