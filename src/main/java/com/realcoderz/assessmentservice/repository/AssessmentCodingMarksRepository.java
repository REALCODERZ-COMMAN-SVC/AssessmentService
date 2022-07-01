/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.AssessmentCodingMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bipul kr Singh
 */

@Repository
public interface AssessmentCodingMarksRepository extends JpaRepository<AssessmentCodingMarks, Long>{
  
}
