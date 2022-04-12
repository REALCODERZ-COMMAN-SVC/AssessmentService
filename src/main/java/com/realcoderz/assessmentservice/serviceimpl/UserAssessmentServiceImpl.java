/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.realcoderz.assessmentservice.domain.AssociateAnswerTrack;
import com.realcoderz.assessmentservice.domain.UserAssessment;
import com.realcoderz.assessmentservice.repository.AssociateAnswerTrackRepository;
import com.realcoderz.assessmentservice.repository.UserAssessmentRepository;
import com.realcoderz.assessmentservice.service.UserAssessmentService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Vineet
 */
@Service
public class UserAssessmentServiceImpl implements UserAssessmentService {

    @Autowired
    private UserAssessmentRepository userAssessmentRepository;
    
    @Autowired
    private AssociateAnswerTrackRepository associateTrackRepository;

    @Override
    public UserAssessment saveUserAssessment(UserAssessment userAssessment) {
        return userAssessmentRepository.save(userAssessment);
    }

    @Override
    public List<UserAssessment> getResultByUserId(Long userId) {
        return userAssessmentRepository.findByUserId(userId);
    }

    @Override
    public Map calculateResult(Long userId, Long assessemntId) {
        Map resultMap = new HashMap();
        List<LinkedCaseInsensitiveMap> rows = userAssessmentRepository.getAssessmentResultByUserAssessmentId(userId, assessemntId);
        if (rows != null) {
            String question = "";
            String assess = "";
            Map<String, String> dataMap = new LinkedHashMap<>();
            List<Map<String, String>> returnList = new ArrayList<>();
            for (LinkedCaseInsensitiveMap mp : rows) {
                if ("".equalsIgnoreCase(assess)) {
                    assess = mp.get("assessment_desc").toString();
                }
                if ("".equalsIgnoreCase(question)) {
                    question = mp.get("question_id").toString();
                    dataMap = new LinkedHashMap<>();
                    dataMap.put("questionId", question);
                    dataMap.put("questionDesc", mp.get("question_desc").toString());
                    dataMap.put("sanswer", mp.get("sanswer").toString());
                    if ("Y".equalsIgnoreCase(mp.get("danswer").toString())) {
                        dataMap.put("danswer", mp.get("option_id").toString());
                    }
                } else {
                    if (!question.equalsIgnoreCase(mp.get("question_id").toString())) {
                        returnList.add(dataMap);
                        dataMap = new LinkedHashMap<>();
                        question = mp.get("question_id").toString();
                        dataMap.put("questionId", question);
                        dataMap.put("questionDesc", mp.get("question_desc").toString());
                        dataMap.put("sanswer", mp.get("sanswer").toString());
                        if ("Y".equalsIgnoreCase(mp.get("danswer").toString())) {
                            dataMap.put("danswer", mp.get("option_id").toString());
                        }
                    } else {
                        if ("Y".equalsIgnoreCase(mp.get("danswer").toString())) {
                            dataMap.put("danswer", (dataMap.get("danswer") != null ? (dataMap.get("danswer") + "," + mp.get("option_id").toString()) : mp.get("option_id").toString()));
                        }
                    }
                }
            }
            returnList.add(dataMap);
            returnList.stream().forEach(a -> {
                if (a.get("sanswer") != null && a.get("danswer") != null) {
                    if (a.get("sanswer").equalsIgnoreCase(a.get("danswer"))) {
                        a.put("status", "Correct");
                    } else {
                        a.put("status", "Incorrect");
                    }
                }else{
                    a.put("status", "Incorrect");
                }
            });
            resultMap.put("questions", returnList);
            Long countCorrectQuestions =Long.parseLong("0");
            if (returnList.size() > 1) {
               countCorrectQuestions =  returnList.parallelStream().filter(a -> a.get("status").equalsIgnoreCase("Correct")).count();

            }
            resultMap.put("correctQuestion", countCorrectQuestions);
            resultMap.put("totalNoOfQuestion", returnList.size());
            resultMap.put("assessment", assess);
            System.out.println(resultMap);
            return resultMap;
        } else {
            return Collections.EMPTY_MAP;
        }
    }

    @Override
    public void saveAnswerDetails(Map map) {
        if(map.containsKey("aid") && map.containsKey("uid") && map.containsKey("qid") && map.containsKey("answer")){
            AssociateAnswerTrack assAnsTrack = associateTrackRepository.findByAssociateIdAndQuestionIdAndAssessmentId(Long.parseLong(map.get("uid").toString()),Long.parseLong(map.get("qid").toString()),Long.parseLong(map.get("aid").toString()));
            if(assAnsTrack==null){
            AssociateAnswerTrack ant = new AssociateAnswerTrack();
            ant.setAssessmentId(Long.parseLong(map.get("aid").toString()));
            ant.setAssociateId(Long.parseLong(map.get("uid").toString()));
            ant.setQuestionId(Long.parseLong(map.get("qid").toString()));
            ant.setAnswer(Long.parseLong(map.get("answer").toString()));
            associateTrackRepository.save(ant);
            }else{
              assAnsTrack.setAnswer(Long.parseLong(map.get("answer").toString()));
              associateTrackRepository.save(assAnsTrack);
            }
        }
    }

}
