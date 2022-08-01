/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

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
 * @author Prateek
 */
@Repository
public interface QuestionMasterRepository extends JpaRepository<QuestionMaster, Long> {

//    @Query("SELECT q.question_id as questionId, q.topic_id as topicId, t.topic_name as topicName, qt.question_type_name as questionType, q.question_desc as questionDesc From QuestionMaster q INNER JOIN TopicMaster t ON q.topic_id = t.topic_id INNER JOIN QuestionType qt ON q.question_type_id = qt.question_type_id where q.language_id=?1 AND q.topic_id=?2 AND q.difficulty_id=?3 AND q.organizationId=?4 AND q.active = 'Y'")
//    public List<LinkedCaseInsensitiveMap> findQuestionsByTopicId(@Param("language_id")Long language_id,@Param("topic_id") Long topic_id,@Param("difficulty_id") Long difficulty_id,@Param("organizationId") Long organizationId);
//
//    @Query("SELECT q From QuestionMaster q where q.question_id in :questions AND q.topic_id in :topics ")
//    public Set<QuestionMaster> findByTopicAndQuestionId(@Param("questions")List<Long> questions,@Param("topics") List<Long> topics);
//
    @Query("SELECT count(*) from QuestionMaster qm where lower(qm.question_desc)=lower(:question_desc) AND qm.language_id=:language_id  AND qm.difficulty_id=:difficulty_id")
    public Integer questionHavingSameDesc(@Param("question_desc") String question_desc, @Param("language_id") Long language_id, @Param("difficulty_id") Long difficulty_id);

    @Query("SELECT count(*) from QuestionMaster qm where lower(qm.question_desc)=lower(:question_desc) AND qm.question_id NOT IN (:question_id) AND qm.language_id=:language_id AND qm.difficulty_id=:difficulty_id")
    public Integer checkQuestionHavingSameDescWithQuestionId(@Param("question_desc") String question_desc, @Param("question_id") Long question_id, @Param("language_id") Long language_id, @Param("difficulty_id") Long difficulty_id);

    @Query("SELECT qm.question_id as question_id,qm.questionTime as questionTime,qm.language_id as language_id,qm.difficulty_id as difficulty_id,qm.question_type_id as question_type_id,qm.codingTemplate as codingTemplate,qm.question_desc as question_desc,qm.active as active,qm.expectedOutput as expectedOutput from QuestionMaster qm where qm.question_id=:id")
    public LinkedCaseInsensitiveMap getCodingQues(@Param("id") Long id);

    @Query("SELECT qm.question_id as question_id,qm.questionTime as questionTime,qm.language_id as language_id,qm.difficulty_id as difficulty_id,qm.question_type_id as question_type_id,qm.no_of_answer as no_of_answer,qm.question_desc as question_desc,qm.active as active,qm.parameterForTestCases as parameterForTestCases,qm.expectedOutput as expectedOutput,ol.option_id as option_id,ol.option_desc as option_desc,ol.isActive as isActive  FROM QuestionMaster qm JOIN qm.options_list ol where qm.question_id=:id order by ol.option_id")
    public List<LinkedCaseInsensitiveMap> getMcqQues(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select qm.topic_id,tm.topic_name, qm.shuffle as shuffle , qm.question_id,qm.question_type_id,qm.question_desc,qm.coding_template as codingTemplate,qm.no_of_answer,qm.language_id from assessment_question rcq,question_master qm,topic_master tm where tm.topic_id=qm.topic_id AND qm.active='Y' AND qm.question_id=rcq.question_id and rcq.assessment_id=?1")
    public List<LinkedCaseInsensitiveMap> findByRcAssId(Long rcAssId);

    @Query(nativeQuery = true, value = "SELECT  qm.question_id as question_id,qm.difficulty_id as difficulty_id,qm.language_id as language_id  , qm.topic_id as topic_id from assessment_question aq inner join question_master qm on qm.question_id=aq.question_id Where (aq.assessment_id=:assessmentId)")
    public Set<LinkedCaseInsensitiveMap> getQuestionsByAssId(@Param("assessmentId") Long assessmentid);

    @Query("select expectedOutput from QuestionMaster where question_id=:question_id")
    public String findExpectedOutputById(@Param("question_id") Long question_id);

    @Query(nativeQuery = true, value = "select lm.language_name from question_master qm  inner join language_master lm on lm.language_id=qm.language_id Where qm.question_id=:question_id")
    public String getLanguageNameById(@Param("question_id") Long question_id);

    @Query(nativeQuery = true, value = "SELECT qm.question_desc , tm.topic_name,qm.expected_output FROM question_master qm inner join topic_master tm on tm.topic_id=qm.topic_id where qm.question_type_id=2 and qm.language_id=:language_id and qm.organization_id=:organizationId order by rand() ")
    public List<LinkedCaseInsensitiveMap> findQuestionBylangId(@Param("language_id") Long language_id, @Param("organizationId") Long organizationId);

}
