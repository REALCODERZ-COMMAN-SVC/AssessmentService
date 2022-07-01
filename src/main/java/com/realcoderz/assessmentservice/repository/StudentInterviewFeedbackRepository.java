/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.StudentInterviewFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author admin
 */
@Repository
public interface StudentInterviewFeedbackRepository extends JpaRepository<StudentInterviewFeedBack, Long> {

    @Query(nativeQuery = true, value = "Select interviews from student_master sm INNER JOIN job_portal jp on jp.job_portal_id=sm.job_portal_id WHERE sm.student_id=:student_id")
    public Long getInterviewRounds(@Param("student_id") Long student_id);

    @Query(value = "SELECT organizationId FROM Organization WHERE lower(organizationName)=lower(:organizationName)")
    public Long findOrganizationIdByName(@Param("organizationName") String organizationName);

    @Query(value = "select * from student_interview_feed_back sif where sif.student_id=:student_id order by sif.student_feedback_id desc limit 1", nativeQuery = true)
    public StudentInterviewFeedBack findByStudentId(@Param("student_id") Long student_id);

    @Query(nativeQuery = true, value = "select round((sa.correct_questions/sa.total_no_of_questions) * 100,2) as assScore from student_assessment sa where sa.student_id =:student_id order by sa.created_date desc limit 0,1")
    public LinkedCaseInsensitiveMap getAssScoreByStdntId(@Param("student_id") Long student_id);

}
