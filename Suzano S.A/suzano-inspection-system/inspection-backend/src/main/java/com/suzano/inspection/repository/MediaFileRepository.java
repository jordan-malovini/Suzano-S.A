package com.suzano.inspection.repository;

import com.suzano.inspection.entity.MediaFile;
import com.suzano.inspection.entity.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    
    List<MediaFile> findByInspection(Inspection inspection);
    
    List<MediaFile> findByMediaType(MediaFile.MediaType mediaType);
    
    @Query("SELECT m FROM MediaFile m WHERE m.inspection = :inspection AND m.mediaType = :mediaType")
    List<MediaFile> findByInspectionAndMediaType(@Param("inspection") Inspection inspection, @Param("mediaType") MediaFile.MediaType mediaType);
    
    @Query("SELECT COUNT(m) FROM MediaFile m WHERE m.inspection = :inspection")
    long countByInspection(@Param("inspection") Inspection inspection);
    
    @Query("SELECT COUNT(m) FROM MediaFile m WHERE m.inspection = :inspection AND m.mediaType = :mediaType")
    long countByInspectionAndMediaType(@Param("inspection") Inspection inspection, @Param("mediaType") MediaFile.MediaType mediaType);
}

