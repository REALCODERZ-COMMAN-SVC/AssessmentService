/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.realcoderz.assessmentservice.repository.StudentMasterRepository;
import com.realcoderz.assessmentservice.service.StudentAssessmentService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Aman Bansal, Bipul Kr Singh, Shubham Mishra
 */
@Service
public class StudentAssessmentServiceImpl implements StudentAssessmentService {

    @Autowired
    private StudentMasterRepository studentMasterRepository;

    @Override
    public Map getTopicScores(LinkedCaseInsensitiveMap givenAssessments) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("haveCoding", 0);
        if (givenAssessments.containsKey("student_assessment_id")) {
            LinkedCaseInsensitiveMap details = new LinkedCaseInsensitiveMap();
            List<LinkedCaseInsensitiveMap> userAssessments = new LinkedList<>();
            List<LinkedCaseInsensitiveMap> correctQuestions = studentMasterRepository.correctQuestions(Long.parseLong(givenAssessments.get("student_assessment_id").toString()));
            details.put("user_assessment_id", givenAssessments.get("student_assessment_id").toString());
            details.put("correctQuestionId", correctQuestions);
            details.put("totalQuestion", givenAssessments.get("total_questions").toString());
            details.put("assessment_id", givenAssessments.get("assessment_id").toString());
            userAssessments.add(details);
            Map<String, Integer> topicTotalCount = new HashMap();
            Map<String, Integer> topicCorrectTotalCount = new HashMap();
            userAssessments.stream().forEach(userAss -> {
                List<LinkedCaseInsensitiveMap> correctQuestionIds = (List<LinkedCaseInsensitiveMap>) userAss.get("correctQuestionId");
                Map<String, List<String>> topicWithIds = new HashMap<>();
                correctQuestionIds.stream().forEach(qId -> {
                    List<LinkedCaseInsensitiveMap> list = studentMasterRepository.findTnameAndQCount(Long.parseLong(qId.get("question_id").toString()));
                    if (!topicWithIds.containsKey(list.get(0).get("topicName").toString())) {
                        List<String> questionCount = new ArrayList<>();
                        questionCount.add(list.get(0).get("topicId").toString());
                        topicWithIds.put(list.get(0).get("topicName").toString(), questionCount);
                    } else {
                        topicWithIds.get(list.get(0).get("topicName").toString()).add(list.get(0).get("topicId").toString());
                    }
                });
                topicWithIds.entrySet().stream().forEach(topic -> {
                    List<LinkedCaseInsensitiveMap> totalCount = studentMasterRepository.totalQuestionOfTopic(Long.parseLong(topic.getValue().get(0)), Long.parseLong(userAss.get("assessment_id").toString()));

                    if (!topicCorrectTotalCount.containsKey(topic.getKey())) {
                        topicCorrectTotalCount.put(topic.getKey(), topic.getValue().size());
                    } else {
                        topicCorrectTotalCount.put(topic.getKey(), (topicCorrectTotalCount.get(topic.getKey()) + topic.getValue().size()));
                    }

                    if (!topicTotalCount.containsKey(topic.getKey())) {
                        topicTotalCount.put(topic.getKey(), Integer.parseInt(totalCount.get(0).get("topicCount").toString()));
                    } else {
                        topicTotalCount.put(topic.getKey(), (topicTotalCount.get(topic.getKey()) + Integer.parseInt(totalCount.get(0).get("topicCount").toString())));
                    }
                });

            });
            List<LinkedCaseInsensitiveMap> average = new LinkedList<>();
            topicCorrectTotalCount.entrySet().stream().forEach(correct -> {
                topicTotalCount.entrySet().stream().forEach(total -> {
                    LinkedCaseInsensitiveMap map = new LinkedCaseInsensitiveMap();
                    if (correct.getKey().equalsIgnoreCase(total.getKey())) {
                        map.put(total.getKey(), total.getKey());
                        if (Float.parseFloat(total.getValue().toString()) != 0) {
                            map.put("average", Math.round(((Float.parseFloat(correct.getValue().toString()) / Float.parseFloat(total.getValue().toString())) * 100)));
                            map.put("total_questions", total.getValue().toString());
                            map.put("correct_questions", correct.getValue().toString());
                            average.add(map);
                        }

                    }
                });
            });
            List<LinkedCaseInsensitiveMap> totalQuestionsIds = studentMasterRepository.totalQuestionsIds(Long.parseLong(givenAssessments.get("student_assessment_id").toString()));
            Set<String> wrongTopics = new HashSet<>();
            totalQuestionsIds.stream().forEach(wt -> {
                LinkedCaseInsensitiveMap value = studentMasterRepository.findTopicName(Long.parseLong(wt.get("question_id").toString()));
                if (value.containsKey("questionId") && value.containsKey("topicName") && value.containsKey("questionTypeId")) {
                    if (value.get("questionTypeId") != null) {
                        if (value.get("questionTypeId").toString().equals("1")) {
                            if (value.get("topicName") != null && value.get("topicName") != "") {
                                wrongTopics.add(value.get("topicName").toString());
                            }
                        } else if (value.get("questionTypeId").toString().equals("2")) {
                            resultMap.put("haveCoding", 1);
                        }
                    }
                }
            });
            List<LinkedCaseInsensitiveMap> returnList = new LinkedList<>();
            average.stream().forEach(avg -> {
                topicTotalCount.entrySet().stream().forEach(t -> {
                    LinkedCaseInsensitiveMap mp = new LinkedCaseInsensitiveMap();
                    mp.put("topicName", avg.get(t.getKey()));
                    mp.put("average", avg.get("average") != null ? avg.get("average").toString() : "0");
                    mp.put("total_questions", avg.get("total_questions") != null ? avg.get("total_questions").toString() : "0");
                    mp.put("correct_questions", avg.get("correct_questions") != null ? avg.get("correct_questions").toString() : "0");
                    if (mp.get("topicName") != null) {
                        returnList.add(mp);
                        if (wrongTopics.contains(mp.get("topicName").toString())) {
                            wrongTopics.remove(mp.get("topicName").toString());
                        }

                    }
                });
            });
            wrongTopics.stream().forEach(wt -> {
                LinkedCaseInsensitiveMap mp = new LinkedCaseInsensitiveMap();
                Integer total_questions = studentMasterRepository.findTotalQuestion(Long.parseLong(givenAssessments.get("assessment_id").toString()), wt);
                mp.put("topicName", wt);
                mp.put("average", "0");
                mp.put("correct_questions", "0");
                mp.put("total_questions", total_questions);
                returnList.add(mp);
            });
            resultMap.put("status", "success");
            resultMap.put("msg", "Fetched topic wise performance for a particular candidate successfully.!");
            resultMap.put("report", returnList);
        }
        return resultMap;
    }

}
