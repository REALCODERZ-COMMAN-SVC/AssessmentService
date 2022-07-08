/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.AssessmentCreation;
import com.realcoderz.assessmentservice.domain.QuestionMaster;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Aman Bansal
 */
@Repository
public interface AssessmentCreationRepository extends JpaRepository<AssessmentCreation, Long> {

    @Query(nativeQuery = true, value = "SELECT qm.question_id from question_master qm where qm.language_id=:language_id and qm.difficulty_id=:difficulty_id and qm.topic_id=:topic_id and qm.question_type_id=:questionTypeId and qm.active='Y' order by rand() limit :selectedQuestion")
    public List<Long> getRandomQuestions(@Param("language_id") Long language_id, @Param("difficulty_id") Long difficulty_id, @Param("topic_id") Long topic_id, @Param("questionTypeId") Long questionTypeId, @Param("selectedQuestion") Integer selectedQuestion);

    @Query("from QuestionMaster where question_id in :ids ")
    public Set<QuestionMaster> findByIds(@Param("ids") List<Long> ids);

//    @Query(nativeQuery = true, value = "select (case when ua.user_assessment_id is null then 'false' else 'true' end) as status,ac.assessment_id as assessment_id,ac.assessment_time_bound as assessmentTimeBound, ac.assessment_desc as assessment_desc,coalesce((select ac.creation_type), null ,'N/A') as creation_type,ac.active as active From assessment_creation ac LEFT JOIN user_assessment ua on ua.user_assessment_id=(select ua.user_assessment_id from user_assessment ua where ua.assessment_id = ac.assessment_id order by ua.user_assessment_id desc limit 1) where ac.organization_id=:organizationId order by ac.created_date desc")
    @Query(nativeQuery = true, value = "select ac.assessment_id as assessment_id,ac.assessment_time_bound as assessmentTimeBound, ac.assessment_desc as assessment_desc,lm.language_name as skills_name,coalesce((select ac.creation_type), null ,'N/A') as creation_type,ac.active as active From assessment_creation ac inner join language_master lm on lm.language_id =ac.language_id where ac.organization_id=:organizationId order by ac.assessment_id desc")
    public List<LinkedCaseInsensitiveMap> allAssessmentsList(@Param("organizationId") Long organizationId);

    @Query("SELECT ac.assessment_id as assessment_id,ac.organizationId as organizationId ,ac.assessment_desc as rcassessment_desc,ac.codingmarks_id as codingmarks_id,ac.language_id as language_id, ac.difficulty_id as difficulty_id, ac.time as time,ac.active as active from AssessmentCreation ac where ac.assessment_id=:assessment_id")
    public List<LinkedCaseInsensitiveMap> assessmentDetails(@Param("assessment_id") Long assessmentId);

    @Query(nativeQuery = true, value = "SELECT question_id as question_id from assessment_question where assessment_id=:assessmentId")
    public List<Long> findQuestionIds(@Param("assessmentId") Long assessmentId);

//    @Query("SELECT  qm.language_id as topicId,lm.language_name as topicName,qm.question_type_id as question_type_id,qt.question_type_desc as questionType from QuestionMaster qm INNER JOIN LanguageMaster lm on lm.language_id=qm.language_id INNER JOIN QuestionType qt ON qt.question_type_id=qm.question_type_id where qm.question_id=:question_id")
//    public List<LinkedCaseInsensitiveMap> findTnameAndQCount(@Param("question_id") Long qId);
    @Query(nativeQuery = true, value = "select lm.language_name as language_name ,tm.topic_name as topicName, qm.topic_id as topicId,qm.question_type_id as question_type_id ,qt.question_type_desc as questionType from question_master qm INNER JOIN topic_master tm ON tm.topic_id=qm.topic_id  inner join language_master lm on lm.language_id=qm.language_id  INNER JOIN question_type qt ON qt.question_type_id=qm.question_type_id where qm.question_id =:question_id")
    public List<LinkedCaseInsensitiveMap> findTnameAndQCount(@Param("question_id") Long qId);

//    @Query(nativeQuery = true, value = "select count(*) as count,lm.language_id as topicId,lm.language_name as topicName, qt.question_type_name as questionType,qt.question_type_id as questionTypeId From question_master q INNER JOIN language_master lm ON lm.language_id=q.language_id INNER JOIN question_type qt ON q.question_type_id = qt.question_type_id where q.language_id IN(:language_ids) AND q.difficulty_id=:difficulty_id AND q.active = 'Y' AND q.organization_id=:organizationId group by q.question_type_id,q.language_id,lm.language_name having count(*)>0")
//    public List<LinkedCaseInsensitiveMap> getTopicsForRanAssess(@Param("language_ids") List<Long> language_ids, @Param("difficulty_id") Long difficulty_id, @Param("organizationId") Long organizationId);
    @Query(nativeQuery = true, value = "select q.topic_id as topicId, t.topic_name as topicName, count(*) as count, qt.question_type_name as questionType,qt.question_type_id as questionTypeId  From question_master q INNER JOIN topic_master t ON q.topic_id = t.topic_id  INNER JOIN question_type qt ON q.question_type_id = qt.question_type_id where q.language_id in (:language_ids) AND q.difficulty_id=:difficulty_id AND t.active = 'Y' AND q.active = 'Y'  AND q.organization_id=:organizationId group by q.topic_id,q.question_type_id having count(*)>0")
    public List<LinkedCaseInsensitiveMap> getTopicsForRanAssess(@Param("language_ids") List<Long> language_ids, @Param("difficulty_id") Long difficulty_id, @Param("organizationId") Long organizationId);

    @Query("select count(*) as count,lm.language_id as topicId,lm.language_name as topicName, qt.question_type_name as questionType,qt.question_type_id as questionTypeId From QuestionMaster q INNER JOIN LanguageMaster lm ON lm.language_id=q.language_id INNER JOIN QuestionType qt ON q.question_type_id = qt.question_type_id where q.language_id =:language_id AND q.difficulty_id=:difficulty_id AND q.active = 'Y' AND q.organizationId=:organizationId group by q.question_type_id having count(*)>0")
    public List<LinkedCaseInsensitiveMap> getTopicsForRanAssessment(@Param("language_id") Long language_id, @Param("difficulty_id") Long difficulty_id, @Param("organizationId") Long organizationId);

    @Query(nativeQuery = true, value = "select count(aq.question_id) as topicCount from assessment_question aq JOIN question_master qm ON qm.question_id=aq.question_id and qm.language_id=:languageId and qm.question_type_id=1 where aq.assessment_id=:assessmentId")
    public List<LinkedCaseInsensitiveMap> totalQuestionOfTopic(@Param("languageId") Long topicId, @Param("assessmentId") Long assessmentId);

    @Query(nativeQuery = true, value = "select uad.question_id as question_id from user_assessment_details uad where uad.user_assessment_id=:userAssessmentId")
    public List<LinkedCaseInsensitiveMap> totalQuestionsIds(@Param("userAssessmentId") Long userAssessmentId);

    @Query("select lm.language_name as topicName,qt.question_type_id as questionTypeId,qm.question_id as questionId from QuestionMaster qm INNER JOIN LanguageMaster lm ON lm.language_id=qm.language_id INNER JOIN QuestionType qt ON qt.question_type_id=qm.question_type_id where qm.question_id=:question_id")
    public LinkedCaseInsensitiveMap findTopicName(@Param("question_id") Long qId);

//    @Query("select ac.assessment_desc as assessment_desc, ac.assessment_id as assessment_id,lm.language_name as language_name, (Select 'Y'||' '||floor(totalPercentage) from UserAssessment ua where ua.user_id=:user_id and ua.assessment_id=ac.assessment_id) as attended From AssessmentCreation ac INNER JOIN BatchAssessmentMapping bam ON bam.batch_id=:batch_id and bam.assessment_id=ac.assessment_id  INNER JOIN LanguageMaster lm ON ac.language_id=lm.language_id where bam.batch_id=:batch_id AND ac.active = 'Y' order by ac.assessment_id ")
    @Query("select ac.assessment_desc as assessment_desc, ac.assessment_id as assessment_id, (Select 'Y'||' '||floor(totalPercentage) from UserAssessment ua where ua.user_id=:user_id and ua.assessment_id=ac.assessment_id) as attended From AssessmentCreation ac INNER JOIN BatchAssessmentMapping bam ON bam.batch_id=:batch_id and bam.assessment_id=ac.assessment_id  where bam.batch_id=:batch_id AND ac.active = 'Y' order by ac.assessment_id ")
    public List<LinkedCaseInsensitiveMap> getAssessmentByBatchAssociateId(@Param("batch_id") Long batchId, @Param("user_id") Long associateId);

    @Query(nativeQuery = true, value = "Select count(av.assessment_submit) as count from user_master um inner join associate_validate av on um.user_id =:associateId where av.assessment_id=:assessment_id and av.assessment_submit=0")
    public Integer countForResumeTest(@Param("assessment_id") Long assessment_id, @Param("associateId") Long associateId);

    @Query(nativeQuery = true, value = "select coalesce((qom.is_active),null,'N') as answer from user_assessment ua INNER JOIN user_assessment_details uad on  uad.user_assessment_id=ua.user_assessment_id and uad.question_id=:questionId INNER JOIN question_option_mapping qom on qom.option_id=uad.answer where ua.assessment_id=:assessmentId and ua.user_id=:associateId ")
    public String findQuestionCorrectOrNot(@Param("assessmentId") Long assessmentId, @Param("associateId") Long associateId, @Param("questionId") Long qId);

    @Query("Select ua.assessment_id as assessment_id ,ua.correct_questions as correct_questions from UserAssessment ua where ua.user_id=:user_id and ua.assessment_id=:assessment_id")
    public List<LinkedCaseInsensitiveMap> getAssociatesAssessmentName(@Param("user_id") Long user_id, @Param("assessment_id") Long assessment_id);

    @Query(nativeQuery = true, value = "SELECT language_name as language from language_master where language_id=:language_id")
    public String getSkillsNameById(@Param("language_id") Long language_id);

//    @Query(nativeQuery = true, value = "SELECT ac.assessment_id,ac.assessment_desc,ac.time FROM assessment_creation ac INNER JOIN job_assessment_mapping jam on  (jam.job_portal_id=:jobPortalId and jam.assessment_id=ac.assessment_id)  Where ac.active ='Y' AND ac.creation_type='Random' order by RAND()")
    @Query(nativeQuery = true, value = "SELECT ac.assessment_id,ac.assessment_desc,ac.time,ac.language_id FROM assessment_creation ac INNER JOIN job_assessment_mapping jam on  (jam.job_portal_id=:jobPortalId and jam.assessment_id=ac.assessment_id)  Where ac.active ='Y' order by RAND()")
    public List<LinkedCaseInsensitiveMap> getQuizByName(@Param("jobPortalId") Long jobPortalId);

    @Query(nativeQuery = true, value = "SELECT language_name as language from language_master where language_id=:language_id")
    public LinkedCaseInsensitiveMap getLangName(@Param("language_id") Long language_id);

//    @Query(nativeQuery = true, value = "SELECT count(*) as count FROM question_master qm, language_master lm where question_id in ( SELECT question_id FROM assessment_question  where assessment_id=:assessment_id and qm.organization_id=:organizationId) and qm.organization_id=:organizationId and lm.language_id=qm.language_id and lm.language_id=:language_id")
//    @Query(nativeQuery = true, value = "SELECT count(*) FROM question_master where topic_id=:topic_id and organization_id=:organizationId")
    @Query(nativeQuery = true, value = "SELECT count(*) as count FROM question_master qm, topic_master tm where question_id in ( SELECT question_id FROM assessment_question  where assessment_id=:assessment_id and qm.organization_id=:organizationId) and qm.organization_id=:organizationId and tm.topic_id=qm.topic_id and tm.topic_id=:language_id and qm.active='Y'")
    public String countNoOfQuestion(@Param("language_id") Long topic_id, @Param("organizationId") Long organizationId, @Param("assessment_id") Long assessment_id);

    @Query(nativeQuery = true, value = "select topic_name from topic_master where topic_id=:topic_id")
    public String topicNameById(@Param("topic_id") Long topic_id);

    @Query("select ac.assessment_id as assessment_id,ac.assessmentTimeBound as assessmentTimeBound, ac.assessment_desc as assessment_desc, bm.batch_name as batch_name,ac.creation_type as creation_type From AssessmentCreation ac INNER JOIN BatchAssessmentMapping bam  ON bam.assessment_id=ac.assessment_id INNER JOIN BatchMaster bm on bm.batch_id=bam.batch_id ")
    public List<LinkedCaseInsensitiveMap> assessments();

    @Query("select codingmarks_id from AssessmentCreation where assessment_id=:assessment_id")
    public Long findCodingMarksIdByAssessmentId(@Param("assessment_id") Long assessment_id);

    @Query("select ac.codingmarks_id as codingmarks_id,ac.assessment_desc as assessment_desc,ac.language_id as language_id FROM AssessmentCreation ac where ac.assessment_id=:assessment_id")
    public LinkedCaseInsensitiveMap findByAssessmentId(@Param("assessment_id") Long assessment_id);

    @Query(nativeQuery = true, value = "select aq.question_id from assessment_question aq,question_master qm where aq.rcassessment_id=:assessment_id and qm.question_id=aq.question_id and qm.question_type_id=2")
    public List<Long> findQuestionIdByAssessmentId(@Param("assessment_id") Long assessment_id);

}
