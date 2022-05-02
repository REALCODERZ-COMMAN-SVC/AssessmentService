/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.StudentAnswerTrack;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Aman Bansal
 */
@Repository
public interface StudentAnswerTrackRepository extends JpaRepository<StudentAnswerTrack, Long>  {
    
    @Query("FROM StudentAnswerTrack ant where  ant.studentId=:studentId AND ant.questionId=:questionId AND ant.assessmentId=:assessmentId")
    public StudentAnswerTrack findByStudentIdAndQuestionIdAndAssessmentId(@Param("studentId")Long studentId, @Param("questionId")Long questionId, @Param("assessmentId")Long assessmentId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM StudentAnswerTrack where studentId=:studentId")
    public void deleteByStudentId(@Param("studentId")Long studentId);

    @Query("SELECT ant.questionId as questionId, ant.answer as answer FROM StudentAnswerTrack ant where  ant.studentId=:studentId AND ant.assessmentId=:assessmentId")
    public List<LinkedCaseInsensitiveMap> findByStudentIdAndAssessmentId(@Param("studentId")Long studentId,@Param("assessmentId")Long assessmentId);
}
