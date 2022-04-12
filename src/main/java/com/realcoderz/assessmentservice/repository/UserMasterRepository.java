/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.UserMaster;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author vineet, Shubham Mishra
 */
@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster, Long> {

    @Query(nativeQuery = true, value = "select uad.question_id as question_id from user_assessment_details uad INNER JOIN question_option_mapping qom ON (qom.option_id=uad.answer) and (qom.question_id=uad.question_id)and (qom.is_active='Y') where uad.user_assessment_id=:userAssessmentId")
    public List<LinkedCaseInsensitiveMap> getCorrectQuestions(@Param("userAssessmentId") Long userAssessmentId);

    @Query(" from UserMaster um where um.user_id=:user_id")
    public UserMaster findByUserId(@Param("user_id") Long user_id);

    @Query("select sm.student_id from StudentMaster sm where sm.email_id IN (select um.email_id from UserMaster um where um.user_id =:user_id)")
    public Long getStudentIdFromUserId(@Param("user_id") Long user_id);

}
