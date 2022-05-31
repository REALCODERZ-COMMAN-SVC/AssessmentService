/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.realcoderz.assessmentservice.domain.AssessmentCreation;
import com.realcoderz.assessmentservice.domain.CandidateStatus;
import com.realcoderz.assessmentservice.domain.LanguageMaster;
import com.realcoderz.assessmentservice.domain.QuestionMaster;
import com.realcoderz.assessmentservice.domain.StudentAnswerTrack;
import com.realcoderz.assessmentservice.domain.StudentAssessment;
import com.realcoderz.assessmentservice.domain.StudentAssessmentDetails;
import com.realcoderz.assessmentservice.domain.StudentInterviewFeedBack;
import com.realcoderz.assessmentservice.domain.StudentMaster;
import com.realcoderz.assessmentservice.domain.StudentTopicScores;
import com.realcoderz.assessmentservice.exceptions.EntiryNotFoundException;
import com.realcoderz.assessmentservice.repository.AssessmentCreationRepository;
import com.realcoderz.assessmentservice.repository.CandidateStatusRepository;
import com.realcoderz.assessmentservice.repository.LanguageMasterRepository;
import com.realcoderz.assessmentservice.repository.OrganizationRepository;
import com.realcoderz.assessmentservice.repository.QuestionMasterRepository;
import com.realcoderz.assessmentservice.repository.QuestionOptionMappingRepository;
import com.realcoderz.assessmentservice.repository.StudentAnswerTrackRepository;
import com.realcoderz.assessmentservice.repository.StudentAssessmentRepository;
import com.realcoderz.assessmentservice.repository.StudentInterviewFeedbackRepository;
import com.realcoderz.assessmentservice.repository.StudentMasterRepository;
import com.realcoderz.assessmentservice.repository.StudentTopicScoresRepo;
import com.realcoderz.assessmentservice.repository.UserMasterRepository;
import com.realcoderz.assessmentservice.service.AssessmentCreationService;
import com.realcoderz.assessmentservice.service.StudentAssessmentService;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Aman Bansal
 */
@Service
public class AssessmentCreationServiceImpl implements AssessmentCreationService {

    static final Logger logger = LoggerFactory.getLogger(AssessmentCreationServiceImpl.class);

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    AssessmentCreationRepository assessmentCreationRepository;

    @Autowired
    LanguageMasterRepository languageMasterRepository;

    @Autowired
    StudentAssessmentRepository studentAssessmentRepo;

    @Autowired
    QuestionMasterRepository questionMasterRepository;

    @Autowired
    StudentInterviewFeedbackRepository studentInterviewFeedbackRepository;

    @Autowired
    QuestionOptionMappingRepository questionOptionMappingRepository;

    @Autowired
    private StudentTopicScoresRepo scoresService;

    @Autowired
    private StudentAssessmentService studentAssessmentService;

    @Autowired
    private StudentAnswerTrackRepository studentAnswerTrackRepository;

    @Autowired
    private StudentMasterRepository studentMasterRepository;

    @Autowired
    private CandidateStatusRepository candidateStatusRepository;

    @Autowired
    private StudentInterviewFeedbackRepository stdntFdbckrepo;

    @Override
    public Map add(Map map) {
        Map resultSet = new HashMap();
        AssessmentCreation assessmentCreation = new AssessmentCreation();
        Set<QuestionMaster> questions = new HashSet();
        Set<AssessmentCreation> assessmentCreations = new HashSet<>();
        assessmentCreation.setLanguage_id(Long.parseLong(map.get("language_id").toString()));
        assessmentCreation.setDifficulty_id(Long.parseLong(map.get("difficulty_id").toString()));
        assessmentCreation.setCodingmarks_id(Long.parseLong(map.get("codingmarks_id").toString()));
        assessmentCreation.setAssessment_desc((String) map.get("rcassessment_desc"));
        assessmentCreation.setTime(Integer.parseInt(map.get("time").toString()));
        assessmentCreation.setCreation_type(map.get("creation_type").toString());
        assessmentCreation.setActive(map.get("active").toString().charAt(0));
        assessmentCreation.setOrganizationId(Long.parseLong(map.get("organizationId").toString()));
        List<LinkedHashMap> topicWiseData = (List<LinkedHashMap>) map.get("randomTopics");
        for (LinkedHashMap topic : topicWiseData) {
            if (topic.containsKey("selectedMCQQuestion")) {
                if (Integer.parseInt(topic.get("selectedMCQQuestion").toString()) > 0) {
                    List<Long> ids = assessmentCreationRepository.getRandomQuestions(Long.parseLong(topic.get("topicId").toString()), Long.parseLong(topic.get("questionTypeId").toString()), Integer.parseInt(topic.get("selectedMCQQuestion").toString()));
                    questions.addAll(assessmentCreationRepository.findByIds(ids));
                }
            }
        }
        assessmentCreation.setQuestion_list(questions);
        assessmentCreations.add(assessmentCreation);
        questions.forEach(a -> {
            a.setAssessmentCreation(assessmentCreations);
        });

        assessmentCreation = assessmentCreationRepository.save(assessmentCreation);
        resultSet.put("status", "success");
        resultSet.put("data", assessmentCreation);
        return resultSet;
    }

    @Override
    public void save(AssessmentCreation assessmentCreation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveRanAssess(Map map) {

    }

    @Override
    public AssessmentCreation findById(Long id) {
        return assessmentCreationRepository.findById(id).orElseThrow(() -> new EntiryNotFoundException("Assessment not exist with id :" + id));
    }

    @Override
    public void delete(AssessmentCreation ac) {
        assessmentCreationRepository.delete(ac);
    }

    @Override
    public void update(Long id, AssessmentCreation assessmentCreation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LinkedCaseInsensitiveMap> assessments(Map map) {
        return assessmentCreationRepository.assessments();
    }

    @Override
    public Set<QuestionMaster> findQuestionsByTopicAndQuestionId(List<String> ids) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LinkedCaseInsensitiveMap> getQuestionsForAssessment(Map map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AssessmentCreation> assessmentList(String assessment_desc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LinkedCaseInsensitiveMap> getUserAssessmentByUserAssessmentId(Long user_id, Long assessment_id) {
        return assessmentCreationRepository.getAssociatesAssessmentName(user_id, assessment_id);
    }

    @Override
    public List<LinkedCaseInsensitiveMap> getTopicsForRanAssess(Map map) {
        List<Long> language_ids = new ArrayList<>();
        if (map.containsKey("language_id") && map.get("language_id") != null) {
            language_ids = (List<Long>) map.get("language_id");
        }

        Long difficulty_id = Long.parseLong(map.get("difficulty_id").toString());
        Long organizationId = Long.parseLong(map.get("organizationId").toString());
        List<LinkedCaseInsensitiveMap> resultList = new LinkedList<>();
        if (language_ids.size() > 0 && difficulty_id > 0 && organizationId > 0) {
            List<LinkedCaseInsensitiveMap> returnList = assessmentCreationRepository.getTopicsForRanAssess(language_ids, difficulty_id, organizationId);
            returnList.stream().forEach(value -> {
                if (value.get("questionType").toString().equalsIgnoreCase("MCQ")) {
                    LinkedCaseInsensitiveMap list = new LinkedCaseInsensitiveMap();
                    list.put("topicId", value.get("topicId"));
                    list.put("topicName", value.get("topicName"));
                    list.put("questionTypeId", value.get("questionTypeId"));
                    list.put("questionType", value.get("questionType"));
                    list.put("count", value.get("count"));
                    resultList.add(list);
                } else if (value.get("questionType").toString().equalsIgnoreCase("Coding")) {
                    LinkedCaseInsensitiveMap list = new LinkedCaseInsensitiveMap();
                    list.put("topicId", value.get("topicId"));
                    list.put("topicName", value.get("topicName"));
                    list.put("questionTypeId", value.get("questionTypeId"));
                    list.put("questionType", value.get("questionType"));
                    list.put("count", value.get("count"));
                    resultList.add(list);
                }
            });
        } else {
            throw new NullPointerException("Language or Topic id is null");
        }
        return resultList;
    }

    @Override
    public Map findRanAssess(Map map) {
        Long assessmentId = Long.parseLong(map.get("id").toString());
        Map<String, List<String>> returnList1 = new HashMap<>();
        List<LinkedCaseInsensitiveMap> returnList2 = new LinkedList<>();
        LinkedCaseInsensitiveMap skills = new LinkedCaseInsensitiveMap();
        Map returnMap = new HashMap();

        List<LinkedCaseInsensitiveMap> assessmentDetails = assessmentCreationRepository.assessmentDetails(assessmentId);
        Long language_id = Long.parseLong(assessmentDetails.get(0).get("language_id").toString());
        List<Long> languages = Arrays.asList(language_id);
        LanguageMaster language = languageMasterRepository.findById(language_id).get();
        if (language != null) {
            skills.put("label", language.getLanguage_name());
            skills.put("value", language_id);
        }
        Long difficulty_id = Long.parseLong(assessmentDetails.get(0).get("difficulty_id").toString());
        Long organizationId = Long.parseLong(assessmentDetails.get(0).get("organizationId").toString());
        List<Long> ids = assessmentCreationRepository.findQuestionIds(assessmentId);
        Map<String, LinkedCaseInsensitiveMap> skillsIdAndName = new HashMap<>();
        ids.stream().forEach(qId -> {
            List<LinkedCaseInsensitiveMap> list = assessmentCreationRepository.findTnameAndQCount(qId);
            if (!returnList1.containsKey(list.get(0).get("topicName") + " "
                    + list.get(0).get("questionType").toString())) {
                List<String> questionCount = new ArrayList<>();
                questionCount.add(list.get(0).get("topicId").toString());
                returnList1.put(list.get(0).get("topicName").toString() + " "
                        + list.get(0).get("questionType").toString(), questionCount);
            } else {
                returnList1.get(list.get(0).get("topicName").toString() + " "
                        + list.get(0).get("questionType").toString()).add(list.get(0).get("topicId").toString());
            }

        });
        List<Long> skillsId = new ArrayList<>();

        List<LinkedCaseInsensitiveMap> allRandomTopics = assessmentCreationRepository.getTopicsForRanAssess(languages, difficulty_id, organizationId);
        allRandomTopics.stream().forEach(allTopic -> {
            final StringBuffer sb = new StringBuffer();
            LinkedCaseInsensitiveMap mp = new LinkedCaseInsensitiveMap();
            returnList1.entrySet().stream().forEach(selectedTopics -> {
                if (selectedTopics.getKey().toString().equalsIgnoreCase(allTopic.get("topicName").toString() + " "
                        + allTopic.get("questionType").toString())) {
                    mp.put("topicId", selectedTopics.getValue().get(0));
                    mp.put("topicName", allTopic.get("topicName").toString());
                    mp.put("questionTypeId", allTopic.get("questionTypeId"));
                    mp.put("questionType", allTopic.get("questionType").toString());
                    mp.put("count", allTopic.get("count"));
                    mp.put("selectedMCQQuestion", selectedTopics.getValue().size());
                    returnList2.add(mp);
                    sb.append("false");
                }
            });
            String str1 = "false";
            String str2 = sb.toString();
            if (!str1.equalsIgnoreCase(str2)) {
                mp.put("topicId", allTopic.get("topicId"));
                mp.put("topicName", allTopic.get("topicName"));
                mp.put("questionTypeId", allTopic.get("questionTypeId"));
                mp.put("questionType", allTopic.get("questionType").toString());
                mp.put("count", allTopic.get("count"));
                returnList2.add(mp);
            }
        });
        returnMap.put("randomTopics", returnList2);
        returnMap.put("assessmentDetails", assessmentDetails.get(0));
        returnMap.put("skills", skills);
        return returnMap;
    }

    @Override
    public AssessmentCreation update(Map map) {

        AssessmentCreation assessmentCreation = assessmentCreationRepository.findById(Long.parseLong(map.get("rcassessment_id").toString())).orElseThrow(() -> new EntiryNotFoundException("Assessment not exist with id :" + Long.parseLong(map.get("rcassessment_id").toString())));

        Set<QuestionMaster> questions = new HashSet();
        Set<AssessmentCreation> assessmentCreations = new HashSet<>();
        assessmentCreation.setLanguage_id(Long.parseLong(map.get("language_id").toString()));
        assessmentCreation.setDifficulty_id(Long.parseLong(map.get("difficulty_id").toString()));
        assessmentCreation.setCodingmarks_id(Long.parseLong(map.get("codingmarks_id").toString()));
        assessmentCreation.setAssessment_desc((String) map.get("rcassessment_desc"));
        assessmentCreation.setTime(Integer.parseInt(map.get("time").toString()));
//        assessmentCreation.setCreation_type(map.get("creation_type").toString());
        assessmentCreation.setActive(map.get("active").toString().charAt(0));
        List<LinkedHashMap> topicWiseData = (List<LinkedHashMap>) map.get("randomTopics");
        for (LinkedHashMap topic : topicWiseData) {
            if (topic.containsKey("selectedMCQQuestion")) {
                if (Integer.parseInt(topic.get("selectedMCQQuestion").toString()) > 0) {
                    List<Long> ids = assessmentCreationRepository.getRandomQuestions(Long.parseLong(topic.get("topicId").toString()), Long.parseLong(topic.get("questionTypeId").toString()), Integer.parseInt(topic.get("selectedMCQQuestion").toString()));
                    if (!ids.isEmpty() && ids.size() > 0) {
                        questions.addAll(assessmentCreationRepository.findByIds(ids));
                    } else {
                        return new AssessmentCreation();
                    }
                }
            }
        }
        assessmentCreation.setQuestion_list(questions);
        assessmentCreations.add(assessmentCreation);
        questions.forEach(a -> {
            a.setAssessmentCreation(assessmentCreations);
        });
        AssessmentCreation saveAss = assessmentCreationRepository.save(assessmentCreation);
        return saveAss;
    }

    @Override
    public List<LinkedCaseInsensitiveMap> allAssessmentsList(Map<String, Object> map) {
        return assessmentCreationRepository.allAssessmentsList(Long.parseLong(map.get("organizationId").toString()));
    }

    @Override
    public Map verifyCode(Map map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map assessmentList(Map map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<LinkedCaseInsensitiveMap> getTopicWiseScoresForAssociates(Long userId, List<LinkedCaseInsensitiveMap> assessmentIds) {
        List<LinkedCaseInsensitiveMap> topicWiseAssessmentsScores = new ArrayList<>();
        assessmentIds.stream().forEach((data) -> {
            LinkedCaseInsensitiveMap assessmentTopicWiseAss = new LinkedCaseInsensitiveMap();
            assessmentTopicWiseAss.put("havingCoding", "0");
            List<LinkedCaseInsensitiveMap> userAssessments = new LinkedList<>();
            LinkedCaseInsensitiveMap details = new LinkedCaseInsensitiveMap();

            List<LinkedCaseInsensitiveMap> correctQuestions = userMasterRepository.getCorrectQuestions(Long.parseLong(data.get("user_assessment_id").toString()));
            details.put("user_assessment_id", data.get("user_assessment_id").toString());
            details.put("correctQuestionId", correctQuestions);
            details.put("totalQuestion", data.get("total_questions").toString());
            details.put("assessment_id", data.get("assessment_id").toString());

            userAssessments.add(details);

            Map<String, Integer> topicTotalCount = new HashMap();
            Map<String, Integer> topicCorrectTotalCount = new HashMap();
            userAssessments.stream().forEach(userAss -> {
                List<LinkedCaseInsensitiveMap> correctQuestionIds = (List<LinkedCaseInsensitiveMap>) userAss.get("correctQuestionId");
                Map<String, List<String>> topicWithIds = new HashMap<>();
                correctQuestionIds.stream().forEach(qId -> {
                    List<LinkedCaseInsensitiveMap> list = assessmentCreationRepository.findTnameAndQCount(Long.parseLong(qId.get("question_id").toString()));
                    if (!topicWithIds.containsKey(list.get(0).get("topicName").toString())) {
                        List<String> questionCount = new ArrayList<>();
                        questionCount.add(list.get(0).get("topicId").toString());
                        topicWithIds.put(list.get(0).get("topicName").toString(), questionCount);
                    } else {
                        topicWithIds.get(list.get(0).get("topicName").toString()).add(list.get(0).get("topicId").toString());
                    }
                });
                topicWithIds.entrySet().stream().forEach(topic -> {
                    List<LinkedCaseInsensitiveMap> totalCount = assessmentCreationRepository.totalQuestionOfTopic(Long.parseLong(topic.getValue().get(0)), Long.parseLong(userAss.get("assessment_id").toString()));

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
                            average.add(map);
                        }

                    }
                });
            });
            List<LinkedCaseInsensitiveMap> totalQuestionsIds = assessmentCreationRepository.totalQuestionsIds(Long.parseLong(data.get("user_assessment_id").toString()));
            Set<String> wrongTopics = new HashSet<>();
            totalQuestionsIds.stream().forEach(wt -> {
                LinkedCaseInsensitiveMap value = assessmentCreationRepository.findTopicName(Long.parseLong(wt.get("question_id").toString()));
                if (value.containsKey("questionId") && value.containsKey("topicName") && value.containsKey("questionTypeId")) {
                    if (value.get("questionTypeId") != null) {
                        if (value.get("questionTypeId").toString().equals("1")) {
                            if (value.get("topicName") != null && value.get("topicName") != "") {
                                wrongTopics.add(value.get("topicName").toString());
                            }
                        } else if (value.get("questionTypeId").toString().equals("2")) {
                            assessmentTopicWiseAss.put("haveCoding", 1);
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
                mp.put("topicName", wt);
                mp.put("average", "0");
                returnList.add(mp);
            });

            assessmentTopicWiseAss.put("assessmentId", data.get("assessment_id").toString());
            assessmentTopicWiseAss.put("topicWiseScore", returnList);
            topicWiseAssessmentsScores.add(assessmentTopicWiseAss);
        });
        return topicWiseAssessmentsScores;
    }

    @Override
    public String getLangName(Long language_id) {
        return languageMasterRepository.getLangName(language_id);
    }

    @Override
    public Map getAssessmentByBatchAssociateId(Long batchId, Long associateId) {
        Map map = new HashMap();
        List<LinkedCaseInsensitiveMap> allAssessments = assessmentCreationRepository.getAssessmentByBatchAssociateId(batchId, associateId);
        List<LinkedCaseInsensitiveMap> learningRecommendAndLanguage = new ArrayList<>();

        if (allAssessments != null && allAssessments.size() > 0) {
            allAssessments.stream().forEach(assessment -> {
                Integer assessResumeCount = assessmentCreationRepository.countForResumeTest(Long.parseLong(assessment.get("assessment_id").toString()), associateId);
                Map<String, LinkedCaseInsensitiveMap> topicNames = new HashMap<>();
                String attendedValue = assessment.get("attended") != null && assessment.get("attended") != "" ? assessment.get("attended").toString() : "N";
                String[] a = attendedValue.split(" ");
                List<Long> ids = assessmentCreationRepository.findQuestionIds(Long.parseLong(assessment.get("assessment_id").toString()));
                ids.parallelStream().forEach(qId -> {

                    LinkedCaseInsensitiveMap totalQuestionAndCorrectQuestion = new LinkedCaseInsensitiveMap();
                    List<LinkedCaseInsensitiveMap> list = assessmentCreationRepository.findTnameAndQCount(qId);
                    String correctQuestions = "";

                    if (assessment.get("attended") != null) {
                        correctQuestions = assessmentCreationRepository.findQuestionCorrectOrNot(Long.parseLong(assessment.get("assessment_id").toString()), associateId, qId);
                        if (correctQuestions != null && correctQuestions.equalsIgnoreCase("Y")) {
                            if (!topicNames.containsKey(list.get(0).get("topicName"))) {
                                totalQuestionAndCorrectQuestion.put("correct_questions", 1);
                                totalQuestionAndCorrectQuestion.put("total_questions", 1);
                                totalQuestionAndCorrectQuestion.put("attended", a[0]);
                                topicNames.put(list.get(0).get("topicName").toString(), totalQuestionAndCorrectQuestion);
                            } else {
                                totalQuestionAndCorrectQuestion.put("correct_questions", Integer.parseInt(topicNames.get(list.get(0).get("topicName")).get("correct_questions").toString()) + 1);
                                totalQuestionAndCorrectQuestion.put("total_questions", Integer.parseInt(topicNames.get(list.get(0).get("topicName")).get("total_questions").toString()) + 1);
                                totalQuestionAndCorrectQuestion.put("attended", a[0]);
                                topicNames.put(list.get(0).get("topicName").toString(), totalQuestionAndCorrectQuestion);
                            }
                        } else {
                            if (!topicNames.containsKey(list.get(0).get("topicName"))) {
                                totalQuestionAndCorrectQuestion.put("correct_questions", 0);
                                totalQuestionAndCorrectQuestion.put("total_questions", 1);
                                totalQuestionAndCorrectQuestion.put("attended", a[0]);
                                topicNames.put(list.get(0).get("topicName").toString(), totalQuestionAndCorrectQuestion);
                            } else {
                                totalQuestionAndCorrectQuestion.put("correct_questions", Integer.parseInt(topicNames.get(list.get(0).get("topicName")).get("correct_questions").toString()));
                                totalQuestionAndCorrectQuestion.put("total_questions", Integer.parseInt(topicNames.get(list.get(0).get("topicName")).get("total_questions").toString()) + 1);
                                totalQuestionAndCorrectQuestion.put("attended", a[0]);
                                topicNames.put(list.get(0).get("topicName").toString(), totalQuestionAndCorrectQuestion);
                            }
                        }
                    } else {
                        if (!topicNames.containsKey(list.get(0).get("topicName"))) {
                            totalQuestionAndCorrectQuestion.put("correct_questions", 0);
                            totalQuestionAndCorrectQuestion.put("total_questions", 1);
                            totalQuestionAndCorrectQuestion.put("attended", "N");

                            topicNames.put(list.get(0).get("topicName").toString(), totalQuestionAndCorrectQuestion);
                        } else {
                            totalQuestionAndCorrectQuestion.put("total_questions", Integer.parseInt(topicNames.get(list.get(0).get("topicName")).get("total_questions").toString()) + 1);
                            totalQuestionAndCorrectQuestion.put("correct_questions", Integer.parseInt(topicNames.get(list.get(0).get("topicName")).get("correct_questions").toString()));
                            totalQuestionAndCorrectQuestion.put("attended", "N");
                            topicNames.put(list.get(0).get("topicName").toString(), totalQuestionAndCorrectQuestion);
                        }
                    }

                });
                List<LinkedCaseInsensitiveMap> topicNameAndScore = new ArrayList<>();
                List<String> topics = new ArrayList<>();
                topicNames.entrySet().forEach(data -> {
                    LinkedCaseInsensitiveMap details = new LinkedCaseInsensitiveMap();
                    details.put("topic", data.getKey());
                    details.put("correct_questions", data.getValue().get("correct_questions"));
                    details.put("total_questions", data.getValue().get("total_questions"));
                    float percentage = Float.parseFloat(data.getValue().get("correct_questions").toString()) / Float.parseFloat(data.getValue().get("total_questions").toString()) * 100;
                    details.put("percenatge", percentage);
                    details.put("attended", data.getValue().get("attended"));
                    topicNameAndScore.add(details);
                    topics.add(data.getKey().toString());
                });
                if (topicNameAndScore.size() > 0) {
                    topicNameAndScore.parallelStream().forEach(data -> {
                        float scores = 0;
                        scores = (Float.parseFloat(data.get("correct_questions").toString()) / Float.parseFloat(data.get("total_questions").toString())) * 100;
                        if (scores <= 33 && data.get("attended").toString().equalsIgnoreCase("Y")) {
                            LinkedCaseInsensitiveMap details = new LinkedCaseInsensitiveMap();
                            details.put("topic", data.get("topic").toString());
                            details.put("language", "");
//                                learningRecommendation.add(data.get("topic").toString());
                            learningRecommendAndLanguage.add(details);
                        }
                    });
                }
//                assessment.put("learning_recommendation", learningRecommendation);
                assessment.put("topicAndScores", topicNameAndScore);
//                assessment.put("language_name", assessment.get("language_name").toString());
                assessment.put("topics", topics);
                assessment.put("resumeCount", assessResumeCount);
                if (assessment.get("attended") != null) {
                    String attended = assessment.get("attended").toString();
                    String[] a1 = attended.split(" ");
                    assessment.put("attended", a1[0]);
                    assessment.put("total_percentage", a1[1]);
                } else {
                    assessment.put("attended", "N");
                    assessment.put("total_percentage", "0");
                }
            });
        }
        map.put("assessments", allAssessments);
        map.put("learning_recommendation", learningRecommendAndLanguage.parallelStream().distinct().collect(Collectors.toList()));
        return map;
    }

    public List<LinkedCaseInsensitiveMap> getQuesWithOptByRcAssId(Long rcAssId) {
        List<LinkedCaseInsensitiveMap> fetchQuestionList = questionMasterRepository.findByRcAssId(rcAssId);
        List<LinkedCaseInsensitiveMap> questionListWithOptions = new ArrayList<>();
        fetchQuestionList.stream().forEach(qList -> {
            List<LinkedCaseInsensitiveMap> options = questionOptionMappingRepository.findByQuesId(Long.parseLong(qList.get("question_id").toString()));
            qList.put("options", options);
            questionListWithOptions.add(qList);
        });
        return questionListWithOptions;
    }

    @Override
    public LinkedCaseInsensitiveMap getQuiz(Long user_id, Long jobportalId, Long organizationId) {
        LinkedCaseInsensitiveMap resultMap = new LinkedCaseInsensitiveMap();
        if (user_id != null && !"".equalsIgnoreCase(user_id.toString())) {

            List<LinkedCaseInsensitiveMap> list = assessmentCreationRepository.getQuizByName(jobportalId);

            if (list != null && !list.isEmpty()) {
                LinkedCaseInsensitiveMap assessment = list.get(0);
                List<LinkedCaseInsensitiveMap> questionListWithOptions = this.getQuesWithOptByRcAssId(Long.parseLong(assessment.get("assessment_id").toString()));
                List<LinkedCaseInsensitiveMap> questions = new ArrayList<>();
                LinkedCaseInsensitiveMap result = new LinkedCaseInsensitiveMap();
                Set topicList = new HashSet<>();
                result.put("assessment_id", assessment.get("assessment_id"));
                result.put("assessment_desc", (assessment.get("assessment_desc")));
                result.put("time", assessment.get("Time"));
//                result.put("language", assessmentCreationRepository.getLangName(Long.parseLong(assessment.get("language_id").toString())).get("language").toString());
                questionListWithOptions.stream().forEach(que -> {
                    LinkedCaseInsensitiveMap quest = new LinkedCaseInsensitiveMap();
                    LinkedCaseInsensitiveMap topics = new LinkedCaseInsensitiveMap();
                    quest.put("question_id", que.get("question_id"));
                    quest.put("question_type_id", que.get("question_type_id"));
                    quest.put("question_desc", que.get("question_desc"));
                    quest.put("codingTemplate", que.get("coding_template"));
                    quest.put("no_of_answer", que.get("no_of_answer"));
                    quest.put("topic_id", que.get("topic_id"));
                    quest.put("topic_Name", assessmentCreationRepository.topicNameById(Long.parseLong(que.get("topic_id").toString())));
                    topics.put("topic_id", que.get("topic_id"));
                    topics.put("topic_Name", assessmentCreationRepository.topicNameById(Long.parseLong(que.get("topic_id").toString())));
                    topics.put("count_no_of_question", assessmentCreationRepository.countNoOfQuestion(Long.parseLong(que.get("topic_id").toString()), organizationId, Long.parseLong(assessment.get("assessment_id").toString())));
                    topicList.add(topics);
//                    List<LinkedCaseInsensitiveMap> options = new ArrayList<>();
//                    if (que.get("options") != null) {
//                        que.getOptions_list().stream().forEach(opt -> {
//                            LinkedCaseInsensitiveMap option = new LinkedCaseInsensitiveMap();
//                            option.put("option_id", opt.getOption_id());
//                            option.put("option_desc", opt.getOption_desc());
//                            options.add(option);
//                        });
//                    }
//                    Collections.shuffle(options);
                    quest.put("options_list", que.get("options"));
                    questions.add(quest);
                });
                Timer timer = new Timer("Assessment_Timer_For_" + user_id + "+" + assessment.get("assessment_id").toString());
                final Long userId = user_id;
                final Long assessmentId = Long.parseLong(assessment.get("assessment_id").toString());
                final Date startTime = new Date();
                final Integer totalQue = questionListWithOptions.size();
                final AssessmentCreation stdntAssessment = assessmentCreationRepository.findById(assessmentId).get();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        List<LinkedCaseInsensitiveMap> list = studentAssessmentRepo.getAssessmentDetailsByUserAssessmentId(userId, assessmentId);
                        System.out.println("List" + list);
                        if ((list == null) || (list.isEmpty())) {
                            StudentAssessment assessment = new StudentAssessment();

                            Set<StudentAssessmentDetails> detailList = new HashSet<>();

                            Set<QuestionMaster> questionList = stdntAssessment.getQuestion_list();
                            assessment.setStudent_id(userId);
                            assessment.setAssessment_id(assessmentId);
                            assessment.setStartTime(startTime);
                            assessment.setEndTime(new Date());
                            assessment.setCreatedDate(new Date());
                            assessment.setAi_score(0.0);
                            assessment.setTotal_no_of_questions(totalQue);
                            assessment.setCorrect_questions(0);
                            assessment.setRemarks("Window closed forcefully");
                            assessment.setCreatedBy(userId.toString());

                            assessment.setLastModifiedBy(userId.toString());

                            List<LinkedCaseInsensitiveMap> selectedQuestions = studentAnswerTrackRepository.findByStudentIdAndAssessmentId(userId, assessmentId);
                            questionList.stream().forEach(question -> {
                                Optional<LinkedCaseInsensitiveMap> present = selectedQuestions.stream().filter(sq -> sq.get("questionId").toString().equalsIgnoreCase(question.getQuestion_id().toString())).findFirst();
                                StudentAssessmentDetails details = new StudentAssessmentDetails();
                                details.setStudentAssessment(assessment);
                                details.setQuestion_id(question.getQuestion_id());
                                if (present.isPresent()) {
                                    String answer = present.get().get("answer").toString();
                                    details.setAnswer(answer);
                                } else {
                                    details.setAnswer("");
                                }
                                detailList.add(details);
                            });
                            assessment.setDetail_list(detailList);
                            studentAssessmentRepo.save(assessment);
                            System.out.println("List2" + list);

                            Map result = calculateResult(userId, assessmentId);
                            int totalMcqMarks = Integer.parseInt(result.get("totalNoOfQuestion").toString());
                            int totalMcqScore = Integer.parseInt(result.get("correctQuestion").toString());
                            assessment.setTotal_no_of_questions(totalMcqMarks);
                            assessment.setCorrect_questions(totalMcqScore);
                            if (totalMcqScore != 0) {
                                DecimalFormat df = new DecimalFormat("#.00");
                                float mcqPercentage = Float.valueOf(df.format((totalMcqScore * 100) / (float) totalMcqMarks));
                                assessment.setMcqPercentage(mcqPercentage);
                                assessment.setTotalPercentage(mcqPercentage);
                            }
//                            UserAssessment userAss = takeAssessmentService.saveUserAssessment(userAssessment);
                            assessment.setCreatedBy(userId.toString());
                            assessment.setLastModifiedBy(userId.toString());
                            StudentAssessment stdntAss = save(assessment);
                            System.out.println("List3" + list);

                            studentAnswerTrackRepository.deleteByStudentId(userId);
                            try {
                                LinkedCaseInsensitiveMap assess = new LinkedCaseInsensitiveMap();
                                assess.put("student_assessment_id", stdntAss.getStudent_assessment_id());
                                assess.put("assessment_id", stdntAss.getAssessment_id());
                                assess.put("student_id", stdntAss.getStudent_id());
                                assess.put("total_questions", stdntAss.getTotal_no_of_questions());
                                new Thread(() -> {
                                    Map topics = studentAssessmentService.getTopicScores(assess);
                                    if (topics.get("status").toString().equals("success")) {
                                        List<LinkedCaseInsensitiveMap> topicsList = (List<LinkedCaseInsensitiveMap>) topics.get("report");
                                        List<StudentTopicScores> sts = new ArrayList<>();
                                        for (LinkedCaseInsensitiveMap topic : topicsList) {
                                            StudentTopicScores s = new StudentTopicScores(stdntAss.getStudent_id(), stdntAss.getAssessment_id(), topic.get("topicName").toString(), Float.parseFloat(topic.get("average").toString()));
                                            s.setCreatedDate(new Date());
                                            s.setCreatedBy(stdntAss.getStudent_id().toString());
                                            s.setLastModifiedBy(stdntAss.getStudent_id().toString());
                                            s.setLastModifiedDate(new Date());
                                            sts.add(s);
                                        }
                                        scoresService.saveAll(sts);
                                    } else {
                                        logger.error("Problem in saveAssessment() :: getTopicScores does not return success");
                                    }
                                }).start();
                                StudentMaster userEmail = studentMasterRepository.findById(userId).get();
                                CandidateStatus status = new CandidateStatus();
                                LinkedCaseInsensitiveMap newUser = candidateStatusRepository.findByEmailAndOrganizationId(userEmail.getEmail_id(), organizationId).get(0);
                                if (newUser.containsKey("id") && newUser.get("id") != null
                                        && newUser.containsKey("email") && newUser.get("email") != null
                                        && newUser.containsKey("organization_name")
                                        && newUser.get("organization_name") != null && newUser.containsKey("organization_id") && newUser.get("organization_id") != null) {
                                    status.setId(Long.parseLong(newUser.get("id").toString()));
                                    status.setEmail(newUser.get("email").toString());
                                    status.setOrganizationId(Long.parseLong(newUser.get("organization_id").toString()));
                                    status.setOrganizationName(newUser.get("organization_name").toString());
                                    status.setStatus("Test Submitted");
                                    candidateStatusRepository.save(status);
                                }
                                //Send assessment notification
                            } catch (Exception ex) {
                                logger.error("Problem in saveAssessment() :: While saving topic wise scores => " + ex);
                            }

//                            StudentInterviewFeedBack stdntFdbck = new StudentInterviewFeedBack();
//                            stdntFdbck.setStudent_id(userId);
//                            stdntFdbck.setStatus("Assessment Completed");
//                            stdntFdbck.setProgress_percentage(Long.parseLong("25"));
//                            studentInterviewFeedbackRepository.save(stdntFdbck);
                            StudentInterviewFeedBack stdntFdbck = new StudentInterviewFeedBack();
                            stdntFdbck.setStudent_id(userId);
                            stdntFdbck.setStatus("Assessment Completed");
                            Long no_of_round = stdntFdbckrepo.getInterviewRounds(userId);
                            if (no_of_round == Long.parseLong("1")) {
                                stdntFdbck.setProgress_percentage(Long.parseLong("50"));
                            } else if (no_of_round == Long.parseLong("3")) {
                                stdntFdbck.setProgress_percentage(Long.parseLong("20"));
                            } else {
                                stdntFdbck.setProgress_percentage(Long.parseLong("25"));
                            }
                            studentInterviewFeedbackRepository.save(stdntFdbck);
                        }
                        timer.cancel();
                    }
                };
                timer.schedule(task, ((Integer.parseInt(assessment.get("time").toString()) + 1) * 60 * 1000));
                Collections.shuffle(questions);
                result.put("question_list", questions);
                result.put("topic_list", topicList);
                result.put("topic_wise_question", questions);
                resultMap.put("assessment", result);
                resultMap.put("status", "success");
            }

        } else {
            resultMap.put("msg", "User not exist.");
            resultMap.put("status", "error");
        }
        return resultMap;
    }

    private StudentAssessment save(StudentAssessment studentAssessment) {
        return studentAssessmentRepo.save(studentAssessment);
    }
    //Student Assessment

    @Override
    @Transactional
    public CompletableFuture<LinkedCaseInsensitiveMap> saveAssessment(Map map) {
        LinkedCaseInsensitiveMap result = new LinkedCaseInsensitiveMap();
        result.put("status", "success");
        Map assessmentData = (Map) map.get("assessment");
        String status = "false";
        String remarks = null;
        if (map.containsKey("closeWindow") && map.get("closeWindow") != null) {
            if (map.get("closeWindow").toString().equalsIgnoreCase("true")) {
                status = "true";
            }
        }
        if (map.containsKey("remarks") && map.get("remarks") != null) {
            remarks = map.get("remarks").toString();
        }
        int counter = Integer.parseInt(map.get("counter").toString());
        Long jobPortalId = Long.parseLong(map.get("jobPortalId").toString());
        Long rcAssId = Long.parseLong(assessmentData.get("assessment_id").toString());
        List<LinkedCaseInsensitiveMap> questWithOpt = this.getQuesWithOptByRcAssId(rcAssId);
        if (questWithOpt != null && !questWithOpt.isEmpty()) {
            StudentAssessment studentAssessment = new StudentAssessment();
            Set<StudentAssessmentDetails> detailList = new HashSet<>();
            List<LinkedHashMap> selectedQuestions = (List<LinkedHashMap>) assessmentData.get("question_list");
            studentAssessment.setStudent_id(Long.parseLong(map.get("user_id").toString()));
            studentAssessment.setAssessment_id(rcAssId);
            studentAssessment.setStartTime(null);
            studentAssessment.setEndTime(new Date());
            studentAssessment.setCreatedBy(map.get("user_id").toString());
            studentAssessment.setCreatedDate(new Date());
            studentAssessment.setJobPortalId(jobPortalId);

            if (counter == 0 && status.equalsIgnoreCase("true")) {
                studentAssessment.setRemarks("Window closed forcefully");
            } else if (remarks != null) {
                studentAssessment.setRemarks(remarks);
            } else {
                studentAssessment.setRemarks(counter == 4 ? "Assessment submitted automatically, as user exceeded the window switch limit." : "Tried to switch window for " + counter + " " + "times");
            }
//                assessmentDetailsRepo.updateAssessmentCompleted(Long.parseLong(map.get("user_id").toString()), assessment.getRcassessment_id());
            questWithOpt.stream().forEach(question -> {
                Optional<LinkedHashMap> present = selectedQuestions.stream().filter(sq -> sq.get("question_id").toString().equalsIgnoreCase(question.get("question_id").toString())).findFirst();
                if (present.isPresent()) {
                    StudentAssessmentDetails details = new StudentAssessmentDetails();
                    List<LinkedHashMap> options = (List<LinkedHashMap>) ((LinkedHashMap) present.get()).get("options_list");
                    List<LinkedHashMap> selectedOptions = options.stream().filter(op -> op.containsKey("isAnswer") && "Y".equalsIgnoreCase(op.get("isAnswer").toString())).collect(Collectors.toList());
                    String answer = selectedOptions.stream().map(s -> s.get("option_id").toString()).collect(Collectors.joining(","));
                    details.setStudentAssessment(studentAssessment);
                    details.setQuestion_id(Long.parseLong(question.get("question_id").toString()));
                    details.setAnswer(answer);
                    details.setCreatedBy(map.get("user_id").toString());
                    details.setCreatedDate(new Date());
                    detailList.add(details);
                }
            });
            studentAssessment.setDetail_list(detailList);
            this.save(studentAssessment);
            Map calResult = calculateResult(studentAssessment.getStudent_id(), studentAssessment.getAssessment_id());
            int totalMcq = Integer.parseInt(calResult.get("totalNoOfQuestion").toString() != null ? calResult.get("totalNoOfQuestion").toString() : "0");
            int totalMcqScore = Integer.parseInt(calResult.get("correctQuestion").toString() != null ? calResult.get("correctQuestion").toString() : "0");
            studentAssessment.setTotal_no_of_questions(totalMcq);
            studentAssessment.setCorrect_questions(totalMcqScore);
            if (totalMcqScore != 0) {
                DecimalFormat df = new DecimalFormat("#.00");
                float mcqPercentage = Float.valueOf(df.format((totalMcqScore * 100) / (float) totalMcq));
                studentAssessment.setMcqPercentage(mcqPercentage);
                studentAssessment.setTotalPercentage(mcqPercentage);
            }
            StudentAssessment stAssess = save(studentAssessment);
//Save Student Feedback           
            StudentInterviewFeedBack stdntFdbck = new StudentInterviewFeedBack();
            stdntFdbck.setStatus("Assessment Completed");
            stdntFdbck.setScholarship("In Process");
            Long no_of_round = stdntFdbckrepo.getInterviewRounds(Long.parseLong(map.get("user_id").toString()));
            if (no_of_round == Long.parseLong("1")) {
                stdntFdbck.setProgress_percentage(Long.parseLong("50"));
            } else if (no_of_round == Long.parseLong("3")) {
                stdntFdbck.setProgress_percentage(Long.parseLong("20"));
            } else {
                stdntFdbck.setProgress_percentage(Long.parseLong("25"));

            }
            stdntFdbck.setStudent_id(Long.parseLong(map.get("user_id").toString()));
            stdntFdbck.setScholarship("In Process");
            stdntFdbck.setCreatedDate(new Date());
            stdntFdbck.setCreatedBy(map.get("user_id").toString());
            stdntFdbck.setLastModifiedBy(map.get("user_id").toString());
            stdntFdbck.setLastModifiedDate(new Date());
            stdntFdbck.setJob_portal_id(jobPortalId);
            if (map.containsKey("organization_name") && map.get("organization_name") != null) {
                Long organizationId = stdntFdbckrepo.findOrganizationIdByName(map.get("organization_name").toString());
                if (organizationId != null) {
                    stdntFdbck.setOrganizationId(organizationId);
                }
            }
            stdntFdbckrepo.save(stdntFdbck);
            //save topic wise scores
            try {
                LinkedCaseInsensitiveMap assess = new LinkedCaseInsensitiveMap();
                assess.put("student_assessment_id", stAssess.getStudent_assessment_id());
                assess.put("assessment_id", stAssess.getAssessment_id());
                assess.put("student_id", stAssess.getStudent_id());
                assess.put("total_questions", stAssess.getTotal_no_of_questions());
                new Thread(() -> {
                    Map topics = studentAssessmentService.getTopicScores(assess);
                    if (topics.get("status").toString().equals("success")) {
                        List<LinkedCaseInsensitiveMap> topicsList = (List<LinkedCaseInsensitiveMap>) topics.get("report");
                        List<StudentTopicScores> sts = new ArrayList<>();
                        for (LinkedCaseInsensitiveMap topic : topicsList) {
                            StudentTopicScores s = new StudentTopicScores(stAssess.getStudent_id(), stAssess.getAssessment_id(), topic.get("topicName").toString(), Float.parseFloat(topic.get("average").toString()));
                            s.setCreatedDate(new Date());
                            s.setCreatedBy(stAssess.getStudent_id().toString());
                            s.setLastModifiedBy(stAssess.getStudent_id().toString());
                            s.setLastModifiedDate(new Date());
                            sts.add(s);
                        }
                        scoresService.saveAll(sts);
                    } else {
                        logger.error("Problem in saveAssessment() :: getTopicScores does not return success");
                    }
                }).start();

                //Send assessment notification
//                this.assessmentNotification(map);
            } catch (Exception ex) {
                logger.error("Problem in saveAssessment() :: While saving topic wise scores => " + ex);
            }
        } else {
            result.put("msg", "Assessment not exist.");
            result.put("status", "error");
        }

        return CompletableFuture.completedFuture(result);

    }

    private Map calculateResult(Long studentId, Long assessemntId) {
        Map resultMap = new HashMap();
        Set<LinkedCaseInsensitiveMap> rows = studentAssessmentRepo.getAssessmentResultByUserAssessmentId(studentId, assessemntId);
        if (rows != null) {
            Long countCorrectQuestions = rows.stream().filter(a -> a.get("sanswer").toString().equalsIgnoreCase(a.get("danswer").toString())).count();

            resultMap.put("correctQuestion", countCorrectQuestions);
            resultMap.put("totalNoOfQuestion", rows.size());
            return resultMap;
        } else {
            return Collections.EMPTY_MAP;
        }
    }

    @Override
    public void saveAnswerDetails(Map map) {
        if (map.containsKey("aid") && map.containsKey("uid") && map.containsKey("qid") && map.containsKey("answer")) {
            StudentAnswerTrack assAnsTrack = studentAnswerTrackRepository.findByStudentIdAndQuestionIdAndAssessmentId(Long.parseLong(map.get("uid").toString()), Long.parseLong(map.get("qid").toString()), Long.parseLong(map.get("aid").toString()));
            if (assAnsTrack == null) {
                StudentAnswerTrack ant = new StudentAnswerTrack();
                ant.setAssessmentId(Long.parseLong(map.get("aid").toString()));
                ant.setStudentId(Long.parseLong(map.get("uid").toString()));
                ant.setQuestionId(Long.parseLong(map.get("qid").toString()));
                ant.setAnswer(Long.parseLong(map.get("answer").toString()));

                studentAnswerTrackRepository.save(ant);
            } else {
                assAnsTrack.setAnswer(Long.parseLong(map.get("answer").toString()));
                studentAnswerTrackRepository.save(assAnsTrack);
            }
        }
    }

//    private void assessmentNotification(Map map) {
//        try {
//            String stdEmail = map.get("email").toString();
//            LinkedCaseInsensitiveMap cm = cmr.recEmailByStdEmail(stdEmail);
//            if (cm != null) {
//                JSONObject recMsg = notifications.getMessage(2);
//                recMsg.replace("message", recMsg.get("message").toString().replaceAll("email", stdEmail));
//                String sender = cm.get("sender") != null ? cm.get("sender").toString() : null;
//                String encrypted = EncryptDecryptUtils.encrypt(notifications.template(recMsg, sender, "RC", true).toString());
//                notifications.saveNotification(encrypted, map.get("accessToken").toString());
//            }
//            JSONObject studentMsg = notifications.getMessage(1);
//            String encrypted = EncryptDecryptUtils.encrypt(notifications.template(studentMsg, stdEmail, null, true).toString());
//            notifications.saveNotification(encrypted, map.get("accessToken").toString());
//        } catch (Exception ex) {
//            logger.error("Problem in StudentAssessmentServiceImpl :: assessmentNotification() => " + ex);
//        }
//    }
}
