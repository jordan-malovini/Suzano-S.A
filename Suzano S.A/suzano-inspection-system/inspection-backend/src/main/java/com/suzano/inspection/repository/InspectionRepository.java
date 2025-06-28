package com.suzano.inspection.repository;

import com.suzano.inspection.entity.Inspection;
import com.suzano.inspection.entity.Equipment;
import com.suzano.inspection.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    
    List<Inspection> findByEquipment(Equipment equipment);
    
    List<Inspection> findByInspector(User inspector);
    
    List<Inspection> findByStatus(Inspection.InspectionStatus status);
    
    List<Inspection> findByResult(Inspection.InspectionResult result);
    
    @Query("SELECT i FROM Inspection i WHERE i.inspector = :inspector AND i.status = :status")
    List<Inspection> findByInspectorAndStatus(@Param("inspector") User inspector, @Param("status") Inspection.InspectionStatus status);
    
    @Query("SELECT i FROM Inspection i WHERE i.equipment = :equipment AND i.status = :status")
    List<Inspection> findByEquipmentAndStatus(@Param("equipment") Equipment equipment, @Param("status") Inspection.InspectionStatus status);
    
    @Query("SELECT i FROM Inspection i WHERE i.scheduledDate BETWEEN :startDate AND :endDate")
    List<Inspection> findByScheduledDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT i FROM Inspection i WHERE i.completedAt BETWEEN :startDate AND :endDate")
    List<Inspection> findByCompletedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT i FROM Inspection i WHERE i.inspector = :inspector AND i.scheduledDate BETWEEN :startDate AND :endDate")
    List<Inspection> findByInspectorAndScheduledDateBetween(@Param("inspector") User inspector, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(i) FROM Inspection i WHERE i.status = :status")
    long countByStatus(@Param("status") Inspection.InspectionStatus status);
    
    @Query("SELECT COUNT(i) FROM Inspection i WHERE i.inspector = :inspector AND i.status = :status")
    long countByInspectorAndStatus(@Param("inspector") User inspector, @Param("status") Inspection.InspectionStatus status);
}

