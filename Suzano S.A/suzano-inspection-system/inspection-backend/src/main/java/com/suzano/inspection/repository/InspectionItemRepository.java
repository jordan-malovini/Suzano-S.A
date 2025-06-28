package com.suzano.inspection.repository;

import com.suzano.inspection.entity.InspectionItem;
import com.suzano.inspection.entity.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionItemRepository extends JpaRepository<InspectionItem, Long> {
    
    List<InspectionItem> findByInspection(Inspection inspection);
    
    List<InspectionItem> findByStatus(InspectionItem.ItemStatus status);
    
    @Query("SELECT i FROM InspectionItem i WHERE i.inspection = :inspection AND i.status = :status")
    List<InspectionItem> findByInspectionAndStatus(@Param("inspection") Inspection inspection, @Param("status") InspectionItem.ItemStatus status);
    
    @Query("SELECT COUNT(i) FROM InspectionItem i WHERE i.inspection = :inspection")
    long countByInspection(@Param("inspection") Inspection inspection);
    
    @Query("SELECT COUNT(i) FROM InspectionItem i WHERE i.inspection = :inspection AND i.status = :status")
    long countByInspectionAndStatus(@Param("inspection") Inspection inspection, @Param("status") InspectionItem.ItemStatus status);
    
    @Query("SELECT COUNT(i) FROM InspectionItem i WHERE i.inspection = :inspection AND i.isCompliant = false")
    long countNonCompliantByInspection(@Param("inspection") Inspection inspection);
}

