/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.AssessmentCodingMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bipul kr Singh
 */

@Repository
public interface AssessmentCodingMarksRepository extends JpaRepository<AssessmentCodingMarks, Long>{

    @Query(nativeQuery = true , value = "select ac.marks_id from assessment_coding_marks ac Where ac.organization_id=:organizationId order by ac.marks_id desc  limit 0,1")
    public Long findByOrganizationId(@Param("organizationId")Long organizationId);
  
}
