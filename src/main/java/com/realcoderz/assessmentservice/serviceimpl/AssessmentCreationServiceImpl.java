/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.realcoderz.assessmentservice.domain.AssessmentCodingDetails;
import com.realcoderz.assessmentservice.domain.AssessmentCodingIssues;
import com.realcoderz.assessmentservice.domain.AssessmentCodingMarks;
import com.realcoderz.assessmentservice.domain.AssessmentCreation;
import com.realcoderz.assessmentservice.domain.AssessmentTextDetails;
import com.realcoderz.assessmentservice.domain.CandidateStatus;
import com.realcoderz.assessmentservice.domain.CodingQuestionTestCases;
import com.realcoderz.assessmentservice.domain.LanguageMaster;
import com.realcoderz.assessmentservice.domain.QuestionMaster;
import com.realcoderz.assessmentservice.domain.StudentAnswerTrack;
import com.realcoderz.assessmentservice.domain.StudentAssessment;
import com.realcoderz.assessmentservice.domain.StudentAssessmentDetails;
import com.realcoderz.assessmentservice.domain.StudentInterviewFeedBack;
import com.realcoderz.assessmentservice.domain.StudentMaster;
import com.realcoderz.assessmentservice.domain.StudentTopicScores;
import com.realcoderz.assessmentservice.exceptions.EntiryNotFoundException;
import com.realcoderz.assessmentservice.exceptions.InvalidKey;
import com.realcoderz.assessmentservice.repository.AssessmentCodingDetailsRepository;
import com.realcoderz.assessmentservice.repository.AssessmentCodingMarksRepository;
import com.realcoderz.assessmentservice.repository.AssessmentCreationRepository;
import com.realcoderz.assessmentservice.repository.AssessmentTextDetailsRepository;
import com.realcoderz.assessmentservice.repository.CandidateStatusRepository;
import com.realcoderz.assessmentservice.repository.CodingQuestionTestCaseRepository;
import com.realcoderz.assessmentservice.repository.LanguageMasterRepository;
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
import com.realcoderz.assessmentservice.service.StudentFeedbackService;
import static com.realcoderz.assessmentservice.serviceimpl.StudentAssessmentServiceImpl.logger;
import com.realcoderz.assessmentservice.util.CommonCompiler;
import com.realcoderz.assessmentservice.util.EncryptDecryptUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
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
import jdk.nashorn.internal.objects.Global;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.wsclient.SonarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Autowired
    private CodingQuestionTestCaseRepository testCaseRepo;
    
    @Autowired
    AssessmentCodingMarksRepository codeMarksRepository;
    
    ObjectMapper mapper = new ObjectMapper();
    
    @Value("${compiler_url}")
    private String compilerUrl;
    
    @Value("${sonar_url}")
    private String sonarUrl;
    
    @Value("${sonar_login}")
    private String sonarLogin;
    
    @Value("${sonar_password}")
    private String sonarPassword;
    
    @Autowired
    private StudentFeedbackService studentFeedBackService;
    
    @Autowired
    AssessmentTextDetailsRepository assessmentDetailsRepo;
    
    @Autowired
    AssessmentCodingDetailsRepository codeDetailsRepository;
    
    @Autowired
    StudentTopicScoresRepo studentTopicScoresRepo;
    
    @Override
    public Map add(Map map) {
        Map resultSet = new HashMap();
        try {
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
            assessmentCreation.setOnScreen(Boolean.parseBoolean(map.get("onScreen").toString()));
            assessmentCreation.setOnMail(Boolean.parseBoolean(map.get("onMail").toString()));
            assessmentCreation.setDetailForm(Boolean.parseBoolean(map.get("detailForm").toString()));
            assessmentCreation.setMediaUpload(Boolean.parseBoolean(map.get("mediaUpload").toString()));
            assessmentCreation.setWebcam(Boolean.parseBoolean(map.get("webcam").toString()));
            
            List<LinkedHashMap> topicWiseData = (List<LinkedHashMap>) map.get("randomTopics");
            
            for (LinkedHashMap topic : topicWiseData) {
                if (topic.containsKey("selectedMCQQuestion")) {
                    if (Integer.parseInt(topic.get("selectedMCQQuestion").toString()) > 0) {
                        List<Long> ids = assessmentCreationRepository.getRandomQuestions(Long.parseLong(map.get("language_id").toString()), Long.parseLong(map.get("difficulty_id").toString()), Long.parseLong(topic.get("topicId").toString()), Long.parseLong(topic.get("questionTypeId").toString()), Integer.parseInt(topic.get("selectedMCQQuestion").toString()));
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultSet;
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
    public List<LinkedCaseInsensitiveMap> assessments(Map map) {
        return assessmentCreationRepository.assessments();
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
                    List<Long> ids = assessmentCreationRepository.getRandomQuestions(Long.parseLong(map.get("language_id").toString()), Long.parseLong(map.get("difficulty_id").toString()), Long.parseLong(topic.get("topicId").toString()), Long.parseLong(topic.get("questionTypeId").toString()), Integer.parseInt(topic.get("selectedMCQQuestion").toString()));
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
            if (qList.containsKey("shuffle")) {
                if (qList.containsValue("y") || qList.containsValue("Y")) {
                    Collections.shuffle(options);
                }
            }
            qList.put("options_list", options);
            questionListWithOptions.add(qList);
        });
        return questionListWithOptions;
    }
    
    @Override
    public LinkedCaseInsensitiveMap getQuiz(Long user_id, Long jobportalId, Long organizationId, Long assessmntId) {
        LinkedCaseInsensitiveMap resultMap = new LinkedCaseInsensitiveMap();
        if (user_id != null && !"".equalsIgnoreCase(user_id.toString())) {
            List<LinkedCaseInsensitiveMap> list = new ArrayList<>();
            if (assessmntId != null && jobportalId == null) {
                list = assessmentCreationRepository.getAssessmentById(assessmntId);
            } else {
                list = assessmentCreationRepository.getQuizByName(jobportalId);
            }
            if (list != null && !list.isEmpty()) {
                LinkedCaseInsensitiveMap assessment = list.get(0);
                List<LinkedCaseInsensitiveMap> questions = this.getQuesWithOptByRcAssId(Long.parseLong(assessment.get("assessment_id").toString()));
                LinkedCaseInsensitiveMap result = new LinkedCaseInsensitiveMap();
                Set topicList = new HashSet<>();
                result.put("assessment_id", assessment.get("assessment_id"));
                result.put("assessment_desc", (assessment.get("assessment_desc")));
                result.put("time", assessment.get("Time"));
                result.put("language", assessmentCreationRepository.getLangName(Long.parseLong(assessment.get("language_id").toString())).get("language").toString());
                questions.stream().forEach(que -> {
                    LinkedCaseInsensitiveMap topics = new LinkedCaseInsensitiveMap();
                    topics.put("topic_id", que.get("topic_id"));
                    topics.put("topic_Name", que.get("topic_name").toString());
                    topics.put("count_no_of_question", assessmentCreationRepository.countNoOfQuestion(Long.parseLong(que.get("topic_id").toString()), organizationId, Long.parseLong(assessment.get("assessment_id").toString())));
                    topicList.add(topics);
                });
                Timer timer = new Timer("Assessment_Timer_For_" + user_id + "+" + assessment.get("assessment_id").toString());
                final Long userId = user_id;
                final Long assessmentId = Long.parseLong(assessment.get("assessment_id").toString());
                final Date startTime = new Date();
                final Integer totalQue = questions.size();
                final AssessmentCreation stdntAssessment = assessmentCreationRepository.findById(assessmentId).get();
                
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        List<LinkedCaseInsensitiveMap> list = studentAssessmentRepo.getAssessmentDetailsByUserAssessmentId(userId, assessmentId);
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

// 
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

    //Save Student FeedBack
    @Override
    public void saveStudentFeedBack(Map map) {
        //Save Student Feedback
        try {
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
            stdntFdbck.setJob_portal_id(Long.parseLong(map.get("jobPortalId").toString()));
            if (map.containsKey("organization_name") && map.get("organization_name") != null) {
                Long organizationId = stdntFdbckrepo.findOrganizationIdByName(map.get("organization_name").toString());
                if (organizationId != null) {
                    stdntFdbck.setOrganizationId(organizationId);
                }
            }
            stdntFdbckrepo.save(stdntFdbck);
        } catch (Exception ex) {
            logger.error("Error While Saving FeedBack !!!");
        }
        
    }

    //Save Student Assessment
    @Override
    @Transactional
    public CompletableFuture<LinkedCaseInsensitiveMap> saveAssessment(Map map) {
        LinkedCaseInsensitiveMap result = new LinkedCaseInsensitiveMap();
        result.put("status", "success");
//        String status = "false";
        String remarks = null;
        if (map.containsKey("remarks") && map.get("remarks") != null) {
            remarks = map.get("remarks").toString();
        }
        Long jobPortalId = 0l;
        int counter = Integer.parseInt(map.get("counter").toString());
        if (map.containsKey("jobPortalId") && map.get("jobPortalId") != null && map.get("jobPortalId") != Global.undefined) {
            jobPortalId = Long.parseLong(map.get("jobPortalId").toString());
        }
        
        Long assessmentId = Long.parseLong(map.get("assessmentId").toString());
        Long studentId = Long.parseLong(map.get("user_id").toString());
        List<LinkedCaseInsensitiveMap> questWithOptions = this.getQuesWithOptByRcAssId(assessmentId);
        if (questWithOptions != null && !questWithOptions.isEmpty()) {
            StudentAssessment studentAssessment = new StudentAssessment();
            Set<StudentAssessmentDetails> detailList = new HashSet<>();
//            List<LinkedHashMap> selectedQuestions = (List<LinkedHashMap>) assessmentData.get("question_list");
            List<LinkedCaseInsensitiveMap> selectedQuestions = studentAnswerTrackRepository.findByStudentIdAndAssessmentId(studentId, assessmentId);
            studentAssessment.setStudent_id(studentId);
            studentAssessment.setAssessment_id(assessmentId);
            studentAssessment.setStartTime(null);
            studentAssessment.setEndTime(new Date());
            studentAssessment.setCreatedBy(map.get("user_id").toString());
            studentAssessment.setCreatedDate(new Date());
            if (jobPortalId != 0) {
                studentAssessment.setJobPortalId(jobPortalId);
            }
            if (remarks != null) {
                studentAssessment.setRemarks(remarks);
            } else {
                studentAssessment.setRemarks(counter == 4 ? "Assessment submitted automatically, as user exceeded the window switch limit." : "Tried to switch window for " + counter + " " + "times");
            }
//                assessmentDetailsRepo.updateAssessmentCompleted(Long.parseLong(map.get("user_id").toString()), assessment.getRcassessment_id());
            questWithOptions.stream().forEach(question -> {
                Optional<LinkedCaseInsensitiveMap> present = selectedQuestions.stream().filter(sq -> sq.get("questionId").toString().equalsIgnoreCase(question.get("question_id").toString())).findFirst();
                StudentAssessmentDetails details = new StudentAssessmentDetails();
                details.setStudentAssessment(studentAssessment);
                details.setQuestion_id(Long.parseLong(question.get("question_id").toString()));
                if (present.isPresent()) {
                    String answer = present.get().get("answer").toString();
                    details.setAnswer(answer);
                } else {
                    details.setAnswer("");
                }
                detailList.add(details);
            });
            studentAssessment.setDetail_list(detailList);
            save(studentAssessment);
            studentAnswerTrackRepository.deleteByStudentId(studentId);
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
            if (map.containsKey("directAss") && map.get("directAss") != null) {
                studentAssessment.setDirectAss(Boolean.parseBoolean(map.get("directAss").toString()));
            } else {
                studentAssessment.setDirectAss(Boolean.FALSE);
            }
            StudentAssessment stAssess = save(studentAssessment);
            
            result.put("correct_questions", stAssess.getCorrect_questions());
            result.put("total_questions", stAssess.getTotal_no_of_questions());
            //save topic wise scores
            List<LinkedCaseInsensitiveMap> topicScores = new ArrayList<>();
            try {
                LinkedCaseInsensitiveMap assess = new LinkedCaseInsensitiveMap();
                assess.put("student_assessment_id", stAssess.getStudent_assessment_id());
                assess.put("assessment_id", stAssess.getAssessment_id());
                assess.put("total_questions", stAssess.getTotal_no_of_questions());
                LinkedCaseInsensitiveMap topicWiseScores = this.getTopicWiseScoresForStudent(assess);
                List<StudentTopicScores> scores = new ArrayList<>();
                if (topicWiseScores != null) {
                    if (topicWiseScores.containsKey("topicWiseScore") && topicWiseScores.get("topicWiseScore") != null) {
                        topicScores = (List<LinkedCaseInsensitiveMap>) topicWiseScores.get("topicWiseScore");
                        
                        for (LinkedCaseInsensitiveMap topic : topicScores) {
                            scores.add(new StudentTopicScores(Long.parseLong(map.get("user_id").toString()), stAssess.getAssessment_id(), topic.get("topicName").toString(), Float.parseFloat(topic.get("average").toString())));
                        }
                        studentTopicScoresRepo.saveAll(scores);
                    }
                }
                //Send assessment notification
//                this.assessmentNotification(map);
            } catch (Exception ex) {
                logger.error("Problem in saveAssessment() :: While saving topic wise scores => " + ex);
            }
            result.put("topicScores", topicScores);
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
    
    @Override
    public LinkedCaseInsensitiveMap getTopicWiseScoresForAssociates(LinkedCaseInsensitiveMap userAssessment) {
        LinkedCaseInsensitiveMap associateTopicScores = new LinkedCaseInsensitiveMap();
        List<LinkedCaseInsensitiveMap> candidateTopicWiseScores = new ArrayList<>();
        if (userAssessment != null) {
            associateTopicScores.put("haveCoding", 0);
            Map<String, LinkedCaseInsensitiveMap> topicWiseScores = new HashMap<>();
            if (userAssessment.containsKey("user_assessment_id") && userAssessment.get("user_assessment_id") != null
                    && userAssessment.containsKey("assessment_id") && userAssessment.get("assessment_id") != null) {
                List<LinkedCaseInsensitiveMap> associatesQuestionStatus = userMasterRepository.associatesQuestionStatus(Long.parseLong(userAssessment.get("user_assessment_id").toString()));
                Long checkCodingCount = associatesQuestionStatus.stream().filter(data -> Long.parseLong(data.get("question_type_id").toString()) == 2).count();
                if (checkCodingCount > 0) {
                    associateTopicScores.put("haveCoding", 1);
                }
                if (!associatesQuestionStatus.isEmpty() && associatesQuestionStatus.size() > 0) {
                    associatesQuestionStatus.stream().forEach(data -> {
                        if (!topicWiseScores.containsKey(data.get("topic_name").toString())) {
                            LinkedCaseInsensitiveMap topicCount = new LinkedCaseInsensitiveMap();
                            if (data.get("answer") != null && data.get("answer").toString().equalsIgnoreCase("Y")) {
                                topicCount.put("correct_questions", 1);
                            } else {
                                topicCount.put("correct_questions", 0);
                            }
                            topicCount.put("total_questions", 1);
                            topicWiseScores.put(data.get("topic_name").toString(), topicCount);
                        } else {
                            LinkedCaseInsensitiveMap topicWiseCount = topicWiseScores.get(data.get("topic_name").toString());
                            if (topicWiseCount != null) {
                                if (data.get("answer") != null && data.get("answer").toString().equalsIgnoreCase("Y")) {
                                    topicWiseCount.put("correct_questions", Integer.parseInt(topicWiseCount.get("correct_questions").toString()) + 1);
                                }
                                topicWiseCount.put("total_questions", Integer.parseInt(topicWiseCount.get("total_questions").toString()) + 1);
                            }
                            topicWiseScores.put(data.get("topic_name").toString(), topicWiseCount);
                        }
                    });
                }
            }
            if (!topicWiseScores.isEmpty()) {
                topicWiseScores.forEach((key, value) -> {
                    LinkedCaseInsensitiveMap questionsCount = new LinkedCaseInsensitiveMap();
                    LinkedCaseInsensitiveMap questions = (LinkedCaseInsensitiveMap) value;
                    questionsCount.put("topicName", key);
                    questionsCount.put("correct_questions", questions.get("correct_questions").toString());
                    questionsCount.put("total_questions", questions.get("total_questions").toString());
                    questionsCount.put("average", Math.round(((Float.parseFloat(questions.get("correct_questions").toString()) / Float.parseFloat(questions.get("total_questions").toString())) * 100)));
                    
                    candidateTopicWiseScores.add(questionsCount);
                });
            }
            associateTopicScores.put("assessmentId", userAssessment.get("assessment_id").toString());
            associateTopicScores.put("topicWiseScore", candidateTopicWiseScores);
        }
        return associateTopicScores;
    }
    
    @Override
    public Map getAssociateTopicScores(Map map) {
        Map resultSet = new HashMap();
        if (map.containsKey("user_id") && map.get("user_id") != null) {
            List<LinkedCaseInsensitiveMap> userTopicWiseScores = new ArrayList<>();
            List<LinkedCaseInsensitiveMap> userAssessment = userMasterRepository.getAssociateAssessment(Long.parseLong(map.get("user_id").toString()));
            if (!userAssessment.isEmpty() && userAssessment.size() > 0) {
                userAssessment.stream().forEach(assessments -> {
                    LinkedCaseInsensitiveMap scores = this.getTopicWiseScoresForAssociates(assessments);
                    userTopicWiseScores.add(scores);
                });
            }
            resultSet.put("status", "success");
            resultSet.put("topicWiseScores", userTopicWiseScores);
        } else {
            throw new NullPointerException("Please provide a valid key or value !!");
        }
        return resultSet;
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
    @Override
    public void save(AssessmentCreation assessmentCreation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void update(Long id, AssessmentCreation assessmentCreation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void saveRanAssess(Map map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Map getCodingQuestion(String data) throws NullPointerException {
        logger.info("StudentAssessmentService getCodingQuestion :: Method execution started.");
        Map resultMap = new HashMap<>();
        
        try {
            Map<String, Object> map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("StudentAssessmentServiceImpl->getCodingQuestion() with request data :: " + map);
            List<LinkedCaseInsensitiveMap> list = null;
            if (map.get("student_email") != null ? true : false) {
                String email = map.get("student_email") != null ? map.get("student_email").toString() : "";
                Long organizationId = map.get("organizationId") != null ? Long.parseLong(map.get("organizationId").toString()) : 0l;
                if (email.length() > 0) {
                    Long assessmentId = null;
                    Long studentId = studentMasterRepository.findByEmailAndOrgId(email, organizationId);
                    if (studentId != null) {
                        assessmentId = studentAssessmentRepo.findByStudentIdAndOrg(studentId, organizationId);
                        if (assessmentId != null) {
                            resultMap.put("coding", testCaseRepo.getCodingQuestion(assessmentId, organizationId));
                        }
                    } else {
                        resultMap.put("status", "student not exist");
                        throw new NullPointerException("Student Id Not exist .");
                    }
                    resultMap.put("status", "success");
                } else {
                    resultMap.put("status", "Please provide student email");
                    logger.info("StudentAssessmentServiceImpl->getCodingQuestion() :: Please provide student email");
                    throw new NullPointerException("Email not exist .");
                }
            }
        } catch (IOException io) {
            resultMap.clear();
            resultMap.put("status", "Input formate exception");
            logger.info("StudentAssessmentServiceImpl->getCodingQuestion() :: Input formate exception" + io);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.clear();
            resultMap.put("status", "exception Occured");
            logger.info("Exception Occured");
        }
        logger.info("StudentAssessmentServiceImpl->getCodingQuestion()" + resultMap);
        logger.info("StudentAssessmentService getCodingQuestion :: Method execution completed.");
        return resultMap;
    }
    
    @Override
    public Map saveAssessmentCodingDetails(String data) throws InvalidKey {
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Map<String, Object> map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("StudentAssessmentServiceImpl-> saveAssessmentCodingDetails ()" + map);
            resultMap = this.saveAssessmentCodingQuestion(map);
        } catch (IOException e) {
            resultMap.clear();
            resultMap.put("status", "inputexception");
            logger.info("StudentAssessmentServiceImpl->saveAssessmentCodingDetails () :: Input formate Exception" + e);
        } catch (Exception e) {
            resultMap.clear();
            //e.printStackTrace();
            resultMap.put("status", "exception");
            logger.info("StudentAssessmentServiceImpl->saveAssessmentCodingDetails () ::Exception Occured!!" + e);
            
        }
        
        logger.info("StudentAssessmentServiceImpl->saveAssessmentCodingDetails () :: resultMap is " + resultMap);
        return resultMap;
    }
    
    private AssessmentCodingDetails validatingTestCases(Long qid, AssessmentCodingDetails acd, String lang, AssessmentCodingMarks acm) {
        List<CodingQuestionTestCases> list = testCaseRepo.findByQuestionMaster(qid);
        int testCaseSquence = 0;
        LinkedHashMap map = new LinkedHashMap();
        String sourceCode = new String(acd.getCode_source()).replaceAll("\u00a0", " ");
        map.put("source_code", sourceCode);
        map.put("language_id", getLanguageIdByName(lang));
        CommonCompiler compiler = new CommonCompiler();
        if (!list.isEmpty()) {
            for (CodingQuestionTestCases a : list) {
                map.put("stdin", a.getInput());
                map.put("expected_output", a.getExpectedOutput());
                LinkedHashMap response = compiler.compilerAndRun(map, compilerUrl);
                if (!response.isEmpty()) {
                    if (response.get("description").toString().equalsIgnoreCase("accepted")) {
                        if (testCaseSquence < 1) {
                            testCaseSquence++;
                            acd.setTestCase1Score(acm.getTestCase1Marks());
                        } else if (testCaseSquence < 2 && testCaseSquence > 0) {
                            testCaseSquence++;
                            acd.setTestCase2Score(acm.getTestCase2Marks());
                        } else if (testCaseSquence < 3 && testCaseSquence > 1) {
                            acd.setTestCase3Score(acm.getTestCase3Marks());
                        }
                    } else {
                        testCaseSquence++;
                    }
                }
            }
        } else {
            acd.setTestCase1Score(acm.getTestCase1Marks());
            acd.setTestCase2Score(acm.getTestCase2Marks());
            acd.setTestCase3Score(acm.getTestCase3Marks());
        }
        return acd;
    }
    
    private String getLanguageIdByName(String language) {
        String id = null;
        if (language.equalsIgnoreCase("java")) {
            return id = "62";
        } else if (language.equalsIgnoreCase("python")) {
            id = "71";
        } else if (language.equalsIgnoreCase("php")) {
            id = "68";
        } else if (language.equalsIgnoreCase("javascript")) {
            id = "63";
        } else if (language.equalsIgnoreCase("c#")) {
            id = "51";
        } else if (language.equalsIgnoreCase("kotlin")) {
            id = "78";
        } else if (language.equalsIgnoreCase("objective c")) {
            id = "79";
        } else if (language.equalsIgnoreCase("r")) {
            id = "80";
        } else if (language.equalsIgnoreCase("rust")) {
            id = "73";
        } else if (language.equalsIgnoreCase("scala")) {
            id = "81";
        }
        return id;
        
    }
    
    @Override
    public Map getCodingDetailsBasedOnAssId(String data) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Map<String, Object> map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("StudentAssessmentServiceImpl->getCodingDetailsBasedOnAssId ()" + map);
            if (map.containsKey("student_email") && map.containsKey("organization_id")) {
                if (map.get("student_email") != null && map.get("organization_id") != null) {
                    String email = map.get("student_email").toString();
                    Long organizationId = Long.parseLong(map.get("organization_id").toString());
                    long studentId = 0l;
                    if (email.length() > 0 && organizationId != 0) {
                        studentId = studentMasterRepository.findByEmailAndOrgId(email, organizationId);
                        if (studentId != 0) {
                            Long assessmentId = studentAssessmentRepo.findByStudentIdAndOrg(studentId, organizationId);
                            Optional<AssessmentCodingDetails> details = codeDetailsRepository.findById(assessmentId);
                            if (details.isPresent()) {
                                resultMap.put("status", "success");
                                logger.info("StudentAssessmentServiceImpl->getCodingDetailsBasedOnAssId :: Coding details are present " + details.get().getAssessmentCodingDetails_id());
                                resultMap.put("data", details);
                            } else {
                                resultMap.put("status", "No coding details are present");
                                logger.info("StudentAssessmentServiceImpl->getCodingDetailsBasedOnAssId :: No coding details are present ");
                            }
                        } else {
                            resultMap.put("status", "Student email are not exist.");
                            logger.info("StudentAssessmentServiceImpl->getCodingDetailsBasedOnAssId :: Student email are not exist ");
                        }
                    } else {
                        throw new NullPointerException("Student email and organization can't be null");
                    }
                }
            } else {
                throw new InvalidKey("Please enter valid key.");
            }
        } catch (InvalidKey iv) {
            resultMap.clear();
            logger.info(" StudentAssessmentServiceImpl->getCodingDetailsBasedOnAssId () :: Please Enter valid key.");
            resultMap.put("status", "Please enter valid key.");
            
        } catch (NullPointerException iv) {
            resultMap.clear();
            logger.info(" StudentAssessmentServiceImpl->getCodingDetailsBasedOnAssId () :: Please provide some value.");
            resultMap.put("status", "Student email and organization can't be null");
            
        } catch (IOException io) {
            io.printStackTrace();
            resultMap.clear();
            logger.info(" StudentAssessmentServiceImpl->getCodingDetailsBasedOnAssId () :: Input Formate Exception.");
            resultMap.put("status", "Input Formate Exception");
            
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.clear();
            logger.info("StudentAssessmentServiceImpl->getCodingDetailsBasedOnAssId () :: Exception Occured !!.");
            resultMap.put("status", "exception");
        }
        logger.info("StudentAssessmentServiceImpl->getCodingDetailsBasedOnAssId () :: resultMap is " + resultMap);
        return resultMap;
        
    }
    
    @Override
    public Map<String, Object> saveTextAssessment(Map<String, Object> map) {
        logger.info("StudentAssessmentServiceImpl -> saveTextAssessment() :: Method execution start with request data " + map);
        Map<String, Object> result = new LinkedCaseInsensitiveMap();
        Map assessmentData = (Map) map.get("assessment");
        Optional<AssessmentCreation> list = assessmentCreationRepository.findById(Long.parseLong(assessmentData.get("assessment_id").toString()));
        if (list != null && list.isPresent()) {
            AssessmentCreation assessment = (AssessmentCreation) list.get();
            if (assessment != null) {
                List<LinkedHashMap> selectedQuestions = (List<LinkedHashMap>) assessmentData.get("question_list");
                Set<QuestionMaster> questionList = assessment.getQuestion_list();
                if (Integer.parseInt(map.get("mcq").toString()) == 0) {
                    studentFeedBackService.saveStdntFeedback(assessmentData);
                    saveAssessment(map);
                }
                questionList.stream().forEach(question -> {
                    Optional<LinkedHashMap> present = selectedQuestions.stream().filter(sq -> sq.get("question_id").toString().equalsIgnoreCase(question.getQuestion_id().toString())).findFirst();
                    if (present.isPresent() && question.getQuestion_type_id() == 3) {
                        AssessmentTextDetails atd = new AssessmentTextDetails();
                        atd.setStudentId(Long.parseLong(map.get("user_id").toString()));
                        atd.setAssessmentId(assessment.getAssessment_id());
                        atd.setQuestionId(question.getQuestion_id());
                        atd.setTextAnswer((present.get()).get("template") != null ? (present.get()).get("template").toString() : "");
                        atd.setCreatedTime(LocalDateTime.now());
                        atd.setAssessmentCompleted(Integer.parseInt(map.get("mcq").toString()) == 0);
                        assessmentDetailsRepo.save(atd);
                        logger.error("AssessmentCreationServiceImpl -> saveTextAssessment() :: " + map.get("user_id").toString());
                    }
                });
                result.put("status", "success");
                logger.info("AssessmentCreationServiceImpl -> saveTextAssessment() :: Method execution start with request data " + map);
            } else {
                result.put("msg", "Assessment not exist.");
                result.put("status", "error");
                logger.error("AssessmentCreationServiceImpl -> saveTextAssessment() :: Assessment not exist.");
            }
        } else {
            result.put("msg", "Assessment not exist.");
            result.put("status", "error");
            logger.error("AssessmentCreationServiceImpl -> saveTextAssessment() ::  Assessment not exist. ");
            
        }
        logger.info("AssessmentCreationServiceImpl -> saveTextAssessment() :: Method execution completed with response data " + result);
        return result;
    }
    
    @Override
    public Map<String, Object> getTextAnswer(Map map) {
        Map<String, Object> result = new HashMap<>();
        List<LinkedCaseInsensitiveMap> data = assessmentDetailsRepo.getTextAnswer(Long.parseLong(map.get("assessment_id").toString()), Long.parseLong(map.get("student_id").toString()));
        if (!data.isEmpty()) {
            result.put("status", "success");
            result.put("data", data);
        } else {
            result.put("status", "error");
            result.put("msg", "No data available!");
        }
        return result;
    }
    
    @Override
    public LinkedCaseInsensitiveMap getResultByUserId(Map map) {
        logger.info("StudentAssessmentServiceImpl -> getResultByUserId() ::  Method execution start with request data  ::  " + map);
        LinkedCaseInsensitiveMap result = new LinkedCaseInsensitiveMap();
        if (map.containsKey("user_id") && map.get("user_id") != null) {
            List<StudentAssessment> user = studentAssessmentRepo.getAssessmentByUserId(Long.parseLong(map.get("user_id").toString()));
            if (user != null && !user.isEmpty()) {
                result.put("status", "success");
                result.put("user", "yes");
            } else {
                result.put("status", "error");
                logger.error("StudentAssessmentServiceImpl -> getResultByUserId() :: Error Occured!!");
            }
        } else {
            result.put("msg", "User not found.");
            result.put("status", "error");
            logger.info("StudentAssessmentServiceImpl -> getResultByUserId() :: User not found ");
        }
        logger.info("StudentAssessmentServiceImpl -> getResultByUserId() ::  Method execution complete with response data ::  " + result);
        return result;
    }
    
    private Map saveAssessmentCodingQuestion(Map map) {
        Map resultMap = new HashMap();
        if (map.containsKey("student_id") && map.containsKey("question_id") && map.containsKey("code_source") && map.containsKey("marks_id")) {
            if (map.get("question_id") != null && map.get("student_id") != null && map.get("code_source") != null) {
                Long studentId = Long.parseLong(map.get("student_id").toString());
                Long questionId = Long.parseLong(map.get("question_id").toString());
                String marksId = map.get("marks_id") != null ? map.get("marks_id").toString() : "";
                String language = map.get("language") != null ? map.get("language").toString() : "";
                String souceCode = map.get("code_source") != null ? map.get("code_source").toString() : "";
                if (studentId != 0 && questionId != 0 && souceCode.length() > 0 && language.length() > 0 && marksId.length() > 0) {
                    AssessmentCodingDetails details = new AssessmentCodingDetails();
                    byte[] blob = souceCode.getBytes();
                    details.setQuestion_id(questionId);
                    details.setCode_source(blob);
                    details.setUser_id(studentId);
                    AssessmentCodingDetails assCodingDetail = codeDetailsRepository.save(details);
                    logger.info("Student Assessment Coding details are save successfully" + details.getAssessmentCodingDetails_id());
                    if (assCodingDetail != null) {
                        Long assCodingDetailsId = assCodingDetail.getAssessmentCodingDetails_id();
                        AssessmentCodingMarks acm = null;
                        Optional<AssessmentCodingMarks> acms = codeMarksRepository.findById(Long.parseLong(marksId));
                        if (acms.isPresent()) {
                            acm = acms.get();
                            if (acm != null) {
                                AssessmentCodingDetails updateAssessment = this.validatingTestCases(questionId, assCodingDetail, language, acm);
                                boolean isExistAssessmentId = codeDetailsRepository.existsById(assCodingDetailsId);
                                if (isExistAssessmentId) {
                                    codeDetailsRepository.save(updateAssessment);
                                    resultMap.put("codingDetails", assCodingDetail);
                                    resultMap.put("status", "success");
                                }
                            }
                        } else {
                            logger.info("Assessment coding marks are not present for this id " + marksId);
                        }
                    }
                }
                
            } else {
                resultMap.clear();
                resultMap.put("status", "Question id, Student id can't null");
                throw new NullPointerException("Question id, Student id can't null");
            }
        } else {
            resultMap.clear();
            logger.info("Key is not valid . Please eneter valid key");
            resultMap.put("status", "Key is not valid");
            throw new InvalidKey("Key is not valid . Please eneter valid key.");
        }
        return resultMap;
    }
    
    @Override
    public Map saveAndGetCodingScore(Map<String, Object> map) {
        Map resultSet = new HashMap();
        try {
            logger.info("AssessmentCreationServiceImpl -> saveAndGetCodingScore() ::  Method execution start with request data  ::  " + map);
            resultSet = this.saveAssessmentCodingQuestion(map);
            if (!resultSet.isEmpty()) {
                if (resultSet.containsKey("status") && resultSet.get("status") != null && resultSet.get("status").toString().equalsIgnoreCase("success")) {
                    int totalCodingScore = 0;
                    Long studentId = Long.parseLong(map.get("student_id").toString());
                    Long codingMarksId = Long.parseLong(map.get("marks_id").toString());
                    Long questionId = Long.parseLong(map.get("question_id").toString());
                    String language = questionMasterRepository.getLanguageNameById(questionId);
                    Optional<AssessmentCodingMarks> acm = codeMarksRepository.findById(codingMarksId);
                    if (acm.isPresent()) {
                        
                        long uid = studentId;
                        List<AssessmentCodingDetails> listOfcodingDetails = codeDetailsRepository.findByUserQuestionId(uid, questionId);
                        if (listOfcodingDetails != null && !listOfcodingDetails.isEmpty()) {
                            AssessmentCodingDetails codingDetails = listOfcodingDetails.get(0);
                            byte[] sourceCode = codingDetails.getCode_source();
                            int score = 0, critical = 0, major = 0, minor = 0;
                            if (codingDetails.getRun_score() > 0) {
                                codingDetails = this.validatingTestCases(questionId, codingDetails, language, acm.get());
                                score = codingDetails.getTestCase1Score() + codingDetails.getTestCase2Score() + codingDetails.getTestCase3Score();
                                if (score > 0) {
                                    String filename;
                                    if (language.equalsIgnoreCase("java")) {
                                        filename = "JavaCode.java";
                                    } else if (language.equalsIgnoreCase("python")) {
                                        filename = "PythonCode.py";
                                    } else if (language.equalsIgnoreCase("php")) {
                                        filename = "PhpCode.php";
                                    } else if (language.equalsIgnoreCase("javascript")) {
                                        filename = "JSCode.js";
                                    } else if (language.equalsIgnoreCase("c#")) {
                                        filename = "CsharpCode.cs";
                                    } else if (language.equalsIgnoreCase("kotlin")) {
                                        filename = "KotlinCode.kotlin";
                                    } else {
                                        filename = "ScalaCode.scala";
                                    }
                                    String pathDir = "sourcecode/" + studentId + questionId;
                                    String pathFile = "sourcecode/" + studentId + questionId + "/" + filename;
                                    File dir = new File(pathDir);
                                    dir.mkdirs();
                                    File file = new File(pathFile);
                                    file.createNewFile();
                                    logger.info("file created at  -> " + file.getAbsolutePath());
                                    try (FileWriter fw = new FileWriter(pathFile)) {
                                        fw.write(new String(sourceCode, StandardCharsets.UTF_8));
                                    }
                                    if (file.exists()) {
                                        Process p;
                                        if (language.equalsIgnoreCase("java")) {
                                            p = processBuilder(pathDir, "java", studentId.toString() + questionId);
                                        } else if (language.equalsIgnoreCase("python")) {
                                            p = processBuilder(pathDir, "py", studentId.toString() + questionId);
                                        } else if (language.equalsIgnoreCase("php")) {
                                            p = processBuilder(pathDir, "php", studentId.toString() + questionId);
                                        } else if (language.equalsIgnoreCase("javascript")) {
                                            p = processBuilder(pathDir, "js", studentId.toString() + questionId);
                                        } else if (language.equalsIgnoreCase("c#")) {
                                            p = processBuilder(pathDir, "cs", studentId.toString() + questionId);
                                        } else if (language.equalsIgnoreCase("kotlin")) {
                                            p = processBuilder(pathDir, "kotlin", studentId.toString() + questionId);
                                        } else {
                                            p = processBuilder(pathDir, "scala", studentId.toString() + questionId);
                                        }
                                        
                                        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                        while (true) {
                                            String log = r.readLine();
                                            logger.info("Scanner_Log: " + log);
                                            if (log == null) {
                                                SonarClient client = SonarClient.builder().url(sonarUrl).login(sonarLogin).password(sonarPassword).build();
                                                Map<String, Object> params = new HashMap<>();
                                                params.put("projects", "code_" + studentId + questionId);
                                                String res = client.get("/api/projects/search", params);
                                                JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                                                Integer exist = obj.get("paging").getAsJsonObject().get("total").getAsInt();
                                                int times = 1;
                                                while (exist == 0) {
                                                    while (times < 20) {
                                                        Thread.sleep(3000);
                                                        times++;
                                                        res = client.get("/api/projects/search", params);
                                                        obj = new JsonParser().parse(res).getAsJsonObject();
                                                        exist = obj.get("paging").getAsJsonObject().get("total").getAsInt();
                                                        if (times == 19) {
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (exist > 0) {
                                                    params.clear();
                                                    Thread.sleep(30000);
                                                    params.put("componentKeys", "code_" + studentId + questionId);
                                                    res = client.get("/api/issues/search", params);
                                                    logger.info("res /api/issues/search code_" + studentId + questionId + " -> " + res);
                                                    Set<AssessmentCodingIssues> codeIssues = new HashSet<>();
                                                    JsonElement root = new JsonParser().parse(res);
                                                    JsonArray object = root.getAsJsonObject().get("issues").getAsJsonArray();
                                                    Gson gson = new Gson();
                                                    List<Map<String, Object>> issueList = gson.fromJson(object, List.class);
                                                    logger.info("issue list " + issueList.size());
                                                    for (int i = 0; i < issueList.size(); i++) {
                                                        AssessmentCodingIssues aci = new AssessmentCodingIssues();
                                                        aci.setIssue_desc(issueList.get(i).get("message") + "");
                                                        aci.setIssue_type(issueList.get(i).get("type") + "");
                                                        if (issueList.get(i).get("line") != null) {
                                                            aci.setIssue_line_no((int) Math.round(Double.parseDouble(issueList.get(i).get("line").toString())));
                                                        }
                                                        codeIssues.add(aci);
                                                        if (issueList.get(i).get("severity").toString().equalsIgnoreCase("CRITICAL")) {
                                                            critical++;
                                                        } else if (issueList.get(i).get("severity").toString().equalsIgnoreCase("MAJOR")) {
                                                            major++;
                                                        } else if (issueList.get(i).get("severity").toString().equalsIgnoreCase("MINOR")) {
                                                            minor++;
                                                        }
                                                    }
                                                    if (minor <= acm.get().getMinorIssues()) {
                                                        score += acm.get().getMinorIssuesMarks();
                                                    }
                                                    if (critical <= acm.get().getCriticalIssues()) {
                                                        score += acm.get().getCriticalIssuesMarks();
                                                    }
                                                    if (major <= acm.get().getMajorIssues()) {
                                                        score += acm.get().getMajorIssuesMarks();
                                                    }
                                                    score += codingDetails.getRun_score() + codingDetails.getCompile_score();
                                                    totalCodingScore += score;
                                                    codingDetails.setCritical_issues(critical);
                                                    codingDetails.setMajor_issues(major);
                                                    codingDetails.setMinor_issues(minor);
                                                    codingDetails.setCoding_score(score);
                                                    codingDetails.setIssuesList(codeIssues);
                                                    codingDetails.setScan(true);
                                                    codeDetailsRepository.save(codingDetails);
                                                    listOfcodingDetails.stream().forEach(list -> {
                                                        list.setScan(true);
                                                    });
                                                    codeDetailsRepository.saveAll(listOfcodingDetails);
                                                    params.clear();
                                                    params.put("project", "code_" + studentId + questionId);
                                                    client.post("/api/projects/delete", params);
                                                    FileUtils.deleteDirectory(dir);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    totalCodingScore += score;
                                    codingDetails.setCritical_issues(critical);
                                    codingDetails.setMajor_issues(major);
                                    codingDetails.setMinor_issues(minor);
                                    codingDetails.setCoding_score(score);
                                    codingDetails.setScan(true);
                                    codeDetailsRepository.save(codingDetails);
                                    listOfcodingDetails.stream().forEach(list -> {
                                        list.setScan(true);
                                    });
                                    codeDetailsRepository.saveAll(listOfcodingDetails);
                                }
                            } else {
                                totalCodingScore += score;
                                codingDetails.setCritical_issues(critical);
                                codingDetails.setMajor_issues(major);
                                codingDetails.setMinor_issues(minor);
                                codingDetails.setCoding_score(score);
                                codingDetails.setScan(true);
                                codeDetailsRepository.save(codingDetails);
                                listOfcodingDetails.stream().forEach(list -> {
                                    list.setScan(true);
                                });
                                codeDetailsRepository.saveAll(listOfcodingDetails);
                                
                            }
                        }
                        
                        resultSet.put("totalCodingScore", totalCodingScore);
                        resultSet.put("status", "succes");
                        
                    } else {
                        logger.info("Assessment coding marks does not exist with this assessment: ");
                    }
                } else {
                    resultSet.put("status", "error");
                    return resultSet;
                }
            }
        } catch (Exception ex) {
            
        }
        return resultSet;
    }
    
    private Process processBuilder(String pathDir, String language, String id) throws IOException {
        return new ProcessBuilder("sh", "-c", "sonar-scanner -Dsonar.host.url=" + sonarUrl + " -Dsonar.login=" + sonarLogin + " -Dsonar.password=" + sonarPassword
                + "    -Dsonar.sources=" + pathDir + " "
                + "    -Dsonar.language=" + language + " "
                + "    -Dsonar.java.binaries=/classes "
                + "    -Dsonar.projectKey=code_" + id).start();
    }
    
    @Override
    public Map codingQuestionByLanguageId(Map<String, Object> map) {
        Map resultMap = new HashMap();
        try {
            if (map.containsKey("language_id") && map.get("language_id") != null && map.containsKey("organizationId") && map.get("organizationId") != null) {
                List<LinkedCaseInsensitiveMap> question = questionMasterRepository.findQuestionBylangId(Long.parseLong(map.get("language_id").toString()), Long.parseLong(map.get("organizationId").toString()));
                Long marksId = codeMarksRepository.findByOrganizationId(Long.parseLong(map.get("organizationId").toString()));
                resultMap.put("status", "success");
                resultMap.put("question", question);
                resultMap.put("marks_id", marksId);
            } else {
                throw new NullPointerException("Please Provide a Valid Key or Value !!");
            }
        } catch (Exception ex) {
            
        }
        return resultMap;
    }
    
    @Override
    public LinkedCaseInsensitiveMap getTopicWiseScoresForStudent(LinkedCaseInsensitiveMap stdntAssessment) {
        LinkedCaseInsensitiveMap stdntTopicScores = new LinkedCaseInsensitiveMap();
        List<LinkedCaseInsensitiveMap> stdntTopicWiseScores = new ArrayList<>();
        if (stdntAssessment != null) {
            stdntTopicScores.put("haveCoding", 0);
            Map<String, LinkedCaseInsensitiveMap> topicWiseScores = new HashMap<>();
            if (stdntAssessment.containsKey("student_assessment_id") && stdntAssessment.get("student_assessment_id") != null
                    && stdntAssessment.containsKey("assessment_id") && stdntAssessment.get("assessment_id") != null) {
                List<LinkedCaseInsensitiveMap> associatesQuestionStatus = userMasterRepository.stdntQuestionStatus(Long.parseLong(stdntAssessment.get("student_assessment_id").toString()));
                Long checkCodingCount = associatesQuestionStatus.stream().filter(data -> Long.parseLong(data.get("question_type_id").toString()) == 2).count();
                if (checkCodingCount > 0) {
                    stdntTopicScores.put("haveCoding", 1);
                }
                if (!associatesQuestionStatus.isEmpty() && associatesQuestionStatus.size() > 0) {
                    associatesQuestionStatus.stream().forEach(data -> {
                        if (!topicWiseScores.containsKey(data.get("topic_name").toString())) {
                            LinkedCaseInsensitiveMap topicCount = new LinkedCaseInsensitiveMap();
                            if (data.get("answer") != null && data.get("answer").toString().equalsIgnoreCase("Y")) {
                                topicCount.put("correct_questions", 1);
                            } else {
                                topicCount.put("correct_questions", 0);
                            }
                            topicCount.put("total_questions", 1);
                            topicWiseScores.put(data.get("topic_name").toString(), topicCount);
                        } else {
                            LinkedCaseInsensitiveMap topicWiseCount = topicWiseScores.get(data.get("topic_name").toString());
                            if (topicWiseCount != null) {
                                if (data.get("answer") != null && data.get("answer").toString().equalsIgnoreCase("Y")) {
                                    topicWiseCount.put("correct_questions", Integer.parseInt(topicWiseCount.get("correct_questions").toString()) + 1);
                                }
                                topicWiseCount.put("total_questions", Integer.parseInt(topicWiseCount.get("total_questions").toString()) + 1);
                            }
                            topicWiseScores.put(data.get("topic_name").toString(), topicWiseCount);
                        }
                    });
                }
            }
            if (!topicWiseScores.isEmpty()) {
                topicWiseScores.forEach((key, value) -> {
                    LinkedCaseInsensitiveMap questionsCount = new LinkedCaseInsensitiveMap();
                    LinkedCaseInsensitiveMap questions = (LinkedCaseInsensitiveMap) value;
                    questionsCount.put("topicName", key);
                    questionsCount.put("correct_questions", questions.get("correct_questions").toString());
                    questionsCount.put("total_questions", questions.get("total_questions").toString());
                    questionsCount.put("average", Math.round(((Float.parseFloat(questions.get("correct_questions").toString()) / Float.parseFloat(questions.get("total_questions").toString())) * 100)));
                    
                    stdntTopicWiseScores.add(questionsCount);
                });
            }
            stdntTopicScores.put("assessmentId", stdntAssessment.get("assessment_id").toString());
            stdntTopicScores.put("topicWiseScore", stdntTopicWiseScores);
        }
        return stdntTopicScores;
    }
    
    @Override
    public Map getCandidateByAssId(Map map) {
        Map resultSet = new HashMap();
        if (map.containsKey("assessmentId") && map.containsKey("organizationId") && map.get("assessmentId") != null && map.get("organizationId") != null) {
            List<LinkedCaseInsensitiveMap> candidates = studentMasterRepository.getCandidateByAssId(Long.parseLong(map.get("assessmentId").toString()), Long.parseLong(map.get("organizationId").toString()));
            resultSet.put("status", "success");
            resultSet.put("candidates", candidates);
        } else {
            throw new NullPointerException("Please Provide a Valid Key or Value !!");
        }
        return resultSet;
    }
    
}
