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

    @Query(nativeQuery = true, value = "Select ua.assessment_id as assessment_id,qm.question_type_id as  question_type_id , (CASE When qom.option_desc is null then 'N' ELSE 'Y' END)  as answer, qm.question_id as question_id , tm.topic_name as topic_name from  user_assessment ua inner join  user_assessment_details uad on uad.user_assessment_id=ua.user_assessment_id left join question_master qm on qm.question_id=uad.question_id left join topic_master tm on tm.topic_id=qm.topic_id left join question_option_mapping qom on qom.option_id=uad.answer and qom.is_active='Y'  Where  ua.user_assessment_id=:user_assessment_id")
    public List<LinkedCaseInsensitiveMap> associatesQuestionStatus(@Param("user_assessment_id") Long user_assessment_id);

    @Query(nativeQuery = true, value = "Select ua.user_assessment_id as user_assessment_id , ua.assessment_id as assessment_id from user_assessment ua Where ua.user_id=:userId")
    public List<LinkedCaseInsensitiveMap> getAssociateAssessment(@Param("userId") Long userId);

}
