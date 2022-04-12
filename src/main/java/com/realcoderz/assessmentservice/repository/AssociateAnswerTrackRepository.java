/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.AssociateAnswerTrack;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author bipulsingh
 */
public interface AssociateAnswerTrackRepository extends JpaRepository<AssociateAnswerTrack, Long> {
    
    @Transactional
    @Modifying
    @Query("DELETE FROM AssociateAnswerTrack where associateId=:associateId")
    public void deleteByAssociateId(@Param("associateId")Long associateId);

    @Query("FROM AssociateAnswerTrack ant where  ant.associateId=:associateId AND ant.questionId=:questionId AND ant.assessmentId=:assessmentId")
    public AssociateAnswerTrack findByAssociateIdAndQuestionIdAndAssessmentId(@Param("associateId")Long associateId, @Param("questionId")Long questionId, @Param("assessmentId")Long assessmentId);
    
    @Query("SELECT ant.questionId as questionId, ant.answer as answer FROM AssociateAnswerTrack ant where  ant.associateId=:associateId AND ant.assessmentId=:assessmentId")
    public List<LinkedCaseInsensitiveMap> findByAssociateIdAndAssessmentId(@Param("associateId")Long associateId,@Param("assessmentId")Long assessmentId);
}
