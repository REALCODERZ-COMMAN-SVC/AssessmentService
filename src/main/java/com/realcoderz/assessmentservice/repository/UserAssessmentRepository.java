/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.UserAssessment;
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
 * @author Vineet
 */
@Repository
public interface UserAssessmentRepository extends JpaRepository<UserAssessment, Long> {

    @Query("SELECT us.assessment_id as assessment_id,detail.question_id as question_id, ac.assessment_desc as assessment_desc, qm.question_desc as question_desc,options.option_id as option_id, options.isActive as danswer,detail.answer as sanswer FROM UserAssessment us JOIN us.detail_list as detail INNER JOIN QuestionMaster qm ON detail.question_id=qm.question_id JOIN qm.options_list as options INNER JOIN AssessmentCreation ac ON us.assessment_id=ac.assessment_id WHERE us.user_id=:user_id and us.assessment_id=:assessment_id ORDER BY detail.question_id")
    public List<LinkedCaseInsensitiveMap> getAssessmentResultByUserAssessmentId(@Param("user_id") Long userId, @Param("assessment_id") Long assessmentId);

    @Transactional
    @Modifying
    @Query("UPDATE UserAssessment ua SET ua.correct_questions=:correct_questions, ua.total_no_of_questions=:total_no_of_questions WHERE ua.user_id=:user_id AND ua.assessment_id=:assessment_id")
    public void saveAssessmentResultOfAssociate(@Param("user_id") Long userId, @Param("assessment_id") Long assessmentId, @Param("correct_questions") Integer correctQuestions, @Param("total_no_of_questions") Integer totalNoOfQuestions);

    @Query("SELECT ac.assessment_desc as assessment_desc, us.correct_questions as correct_questions,us.total_no_of_questions as total_no_of_questions,um.first_name as first_name,um.last_name as last_name,um.email_id as email_id,um.user_id as user_id,'14-Dec-2020-Java' as batch_id,'Sep-2020-Java' as batch_name from UserAssessment us INNER JOIN AssessmentCreation ac ON us.assessment_id=ac.assessment_id INNER JOIN UserMaster um ON um.user_id=us.user_id  where us.user_id=:user_id ")//INNER JOIN BatchMaster bm ON bm.batch_id=um.batch_id
    public List<LinkedCaseInsensitiveMap> getAssessment(@Param("user_id") Long user_id);

    @Query("SELECT ac.assessment_desc as assessment_desc, us.totalPercentage as total_percentage,um.first_name as first_name,um.last_name as last_name,um.email_id as email_id,um.user_id as user_id from UserAssessment us INNER JOIN AssessmentCreation ac ON ac.assessment_id=us.assessment_id  INNER JOIN UserMaster um ON um.user_id=us.user_id  where us.user_id=:user_id ")//INNER JOIN BatchMaster bm ON bm.batch_id=um.batch_id
    public List<LinkedCaseInsensitiveMap> getAssessmentDetails(@Param("user_id") Long user_id);

    @Query("FROM UserAssessment where user_id=?1")
    public List<UserAssessment> findByUserId(Long userId);

    @Query("SELECT count(*) from UserAssessment us where us.user_id=:user_id")
    public long countByUserId(@Param("user_id") Long user_id);

    @Query(nativeQuery = true, value = "select round(ua.total_percentage,2) as average,ua.created_date as created_date,um.first_name as first_name from user_master um,user_assessment ua where ua.user_id=um.user_id and um.user_id=:user_id and ua.assessment_id IN (:assessmentIds) order by ua.created_date Desc Limit 1;")//INNER JOIN BatchMaster bm ON bm.batch_id=um.batch_id
    public LinkedCaseInsensitiveMap getAssessmentDetailsForAssociates(@Param("user_id") Long user_id, @Param("assessmentIds") List<Long> ids);

//    @Query("SELECT ac.assessment_desc as assessment_desc,tm.topic_name as topic_name, us.correct_questions as correct_questions,us.total_no_of_questions as total_no_of_questions,bm.batch_name as batch_name ,um.first_name as first_name,um.last_name as last_name,um.email_id as email_id,um.user_id as user_id from UserAssessment us INNER JOIN AssessmentCreation ac ON ac.assessment_id=us.assessment_id INNER JOIN TopicMaster tm on tm.topic_id=ac.topic_id INNER JOIN BatchMaster bm on bm.batch_id = ac.batch_id INNER JOIN UserMaster um ON um.user_id=us.user_id where us.user_id=:user_id ")//INNER JOIN BatchMaster bm ON bm.batch_id=um.batch_id
    @Query("SELECT ac.assessment_desc as assessment_desc,lm.language_name as topic_name, us.correct_questions as correct_questions,us.total_no_of_questions as total_no_of_questions,bm.batch_name as batch_name ,um.first_name as first_name,um.last_name as last_name,um.email_id as email_id,um.user_id as user_id from UserAssessment us INNER JOIN AssessmentCreation ac ON ac.assessment_id=us.assessment_id INNER JOIN LanguageMaster lm on lm.language_id=ac.language_id INNER JOIN BatchAssessmentMapping bam on bam.assessment_id = ac.assessment_id INNER JOIN BatchMaster bm on bm.batch_id=bam.batch_id INNER JOIN UserMaster um ON um.user_id=us.user_id where us.user_id=:user_id ")//INNER JOIN BatchMaster bm ON bm.batch_id=um.batch_id
    public List<LinkedCaseInsensitiveMap> getAssessmentDetailsForTopicWise(@Param("user_id") Long user_id);

    @Query(nativeQuery = true, value = "select ac.assessment_id from assessment_creation ac where ac.batch_id=:batch_id order by created_date desc limit 0,1")
    public Long getRecentAssessment(@Param("batch_id") Long batch_id);

    @Query(nativeQuery = true, value = "SELECT um.first_name as first_name, ac.assessment_desc as assessment_desc ,concat(round(ua.total_percentage,2),'%') as percentage  FROM user_assessment ua inner join assessment_creation ac on ac.assessment_id =ua.assessment_id  inner join user_master um on um.user_id =ua.user_id where ua.user_id=:user_id and ua.assessment_id=:assessment_id")
    public List<LinkedCaseInsensitiveMap> getRecentAssessmentOfAssociates(@Param("user_id") Long user_id, @Param("assessment_id") Long assessment_id);

    @Query("select um.first_name  as first_name,um.user_id as user_id, ac.assessment_desc as assessment_desc from UserMaster um inner join AssessmentCreation ac on ac.assessment_id=:assessment_id where um.user_id=:user_id")
    public List<LinkedCaseInsensitiveMap> getAssociatesWithoutAssessment(@Param("user_id") Long user_id, @Param("assessment_id") Long assessment_id);

    @Query("SELECT ac.assessment_id as assessment_id from AssessmentCreation ac INNER JOIN BatchAssessmentMapping bam on bam.batch_id=:batch_id")
    public List<Long> getAllAssessments(@Param("batch_id") Long batch_id);

    @Query("Select ua.assessment_id as assessment_id ,ua.correct_questions as correct_questions from UserAssessment ua where ua.user_id=:user_id and ua.assessment_id=:assessment_id")
    public List<LinkedCaseInsensitiveMap> getAssociatesAssessmentName(@Param("user_id") Long user_id, @Param("assessment_id") Long assessment_id);

    @Query("select round(us.correct_questions/us.total_no_of_questions*100,2) as score from UserAssessment us where us.user_id=:user_id")
    public List<Long> getAssociateScoreForPrediction(@Param("user_id") Long user_id);

    @Query("Select ac.assessment_desc , um.email_id from AssessmentCreation ac inner join UserAssessment ua on ac.assessment_id=ua.assessment_id inner join UserMaster um on ua.user_id=um.user_id inner join BatchAssessmentMapping bam on (bam.batch_id=:batch_id and bam.assessment_id=ac.assessment_id)")
    public List<LinkedCaseInsensitiveMap> getHistoricPerformance(@Param("batch_id") Long batch_id);

    @Query(nativeQuery = true, value = "SELECT um.first_name as first_name, ac.assessment_desc as assessment_desc ,concat(round(ua.total_percentage,2),'%') as percentage  FROM user_assessment ua inner join assessment_creation ac on ac.assessment_id =ua.assessment_id  inner join user_master um on um.user_id =ua.user_id where ua.user_id=:id and ua.assessment_id=:assessmentId order by ua.user_assessment_id desc limit 0,1")
    public LinkedCaseInsensitiveMap checkAssociatesScores(@Param("id") Long id, @Param("assessmentId") Long assessmentId);

    @Query(nativeQuery = true, value = "select um.first_name  as first_name,um.user_id as user_id, ac.assessment_desc as assessment_desc from user_master um , assessment_creation ac Where ac.assessment_id=:assessmentId and um.user_id=:id")
    public LinkedCaseInsensitiveMap checkAssWithoutAssessment(@Param("id") Long id, @Param("assessmentId") Long assessmentId);

}
