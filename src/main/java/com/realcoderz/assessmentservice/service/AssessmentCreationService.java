/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.service;

import com.realcoderz.assessmentservice.domain.AssessmentCreation;
import com.realcoderz.assessmentservice.domain.QuestionMaster;
import com.realcoderz.assessmentservice.exceptions.InvalidKey;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Prateek
 */
public interface AssessmentCreationService {

    public void save(AssessmentCreation assessmentCreation);

    public AssessmentCreation findById(Long id);

    public void delete(AssessmentCreation ac);

    public void update(Long id, AssessmentCreation assessmentCreation);

    public List<LinkedCaseInsensitiveMap> assessments(Map map);

    public Set<QuestionMaster> findQuestionsByTopicAndQuestionId(List<String> ids);

    public List<LinkedCaseInsensitiveMap> getQuestionsForAssessment(Map map);

    public List<LinkedCaseInsensitiveMap> getUserAssessmentByUserAssessmentId(Long user_id, Long assessment_id);

    public List<LinkedCaseInsensitiveMap> getTopicsForRanAssess(Map map);

    public void saveRanAssess(Map map);

    public Map findRanAssess(Map map);

    public AssessmentCreation update(Map map);

    public List<LinkedCaseInsensitiveMap> allAssessmentsList(Map<String, Object> map);

    public Map verifyCode(Map map);

    public Map assessmentList(Map map);

    public Map add(Map map);

    public String getLangName(Long language_id);

    public Map getAssessmentByBatchAssociateId(Long batchId, Long userId);

    public LinkedCaseInsensitiveMap getQuiz(Long user_id, Long jobportalId, Long organizationId);

    public CompletableFuture<LinkedCaseInsensitiveMap> saveAssessment(Map map);

    public void saveAnswerDetails(Map map);

    public LinkedCaseInsensitiveMap getTopicWiseScoresForAssociates(LinkedCaseInsensitiveMap userAssessments);

    public Map getAssociateTopicScores(Map map);

    public void saveStudentFeedBack(Map map);

    public Map getCodingQuestion(String data) throws NullPointerException;

    public Map saveAssessmentCodingDetails(String data) throws InvalidKey;

    public Map getCodingDetailsBasedOnAssId(String data);

    public Map<String, Object> saveTextAssessment(Map<String, Object> map);

    public Map<String, Object> getTextAnswer(Map map);

    public LinkedCaseInsensitiveMap getResultByUserId(Map map);
}
