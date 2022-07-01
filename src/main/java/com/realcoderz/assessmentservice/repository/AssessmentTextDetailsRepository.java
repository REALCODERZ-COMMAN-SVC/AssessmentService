/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.AssessmentTextDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedCaseInsensitiveMap;


/**
 *
 * @author Bipul kr Singh
 */

@Repository
public interface AssessmentTextDetailsRepository extends JpaRepository<AssessmentTextDetails,Long>{


    @Transactional
    @Modifying
    @Query("UPDATE AssessmentTextDetails atd SET atd.assessmentCompleted = true where atd.studentId=:studentId AND atd.assessmentId=:assessmentId")
    public void updateAssessmentCompleted(@Param("studentId") Long studentId,@Param("assessmentId") Long assessmentId );

    @Query("Select atd.textDetailsId as textDetailsId ,atd.questionId as questionId,atd.textAnswer as textAnswer FROM AssessmentTextDetails atd where atd.assessmentCompleted = false and atd.studentId=:studentId and atd.assessmentId=:assessmentId order by atd.textDetailsId desc")
    public List<LinkedCaseInsensitiveMap> getTextAnswer(@Param("assessmentId") Long assessmentId, @Param("studentId") Long studentId);

}
