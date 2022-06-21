/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.FaceDetectionFlags;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author rohini
 */
@Repository
public interface FaceDetectionFlagsRepository extends JpaRepository<FaceDetectionFlags, Long>
{
    public FaceDetectionFlags findByStudentId(@Param("studentId") Long studentId);
    
    @Query(nativeQuery = true,value = "select webcam from job_portal where job_portal_id=:jobPortalId")
    public Boolean findWebcamOnByJobPortalId(@Param("jobPortalId") Long jobPortalId);
}