/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.realcoderz.assessmentservice.domain.AssessmentCreation;
import com.realcoderz.assessmentservice.domain.AssociateTopicScores;
import com.realcoderz.assessmentservice.domain.AssociateValidate;
import com.realcoderz.assessmentservice.domain.QuestionMaster;
import com.realcoderz.assessmentservice.domain.UserAssessment;
import com.realcoderz.assessmentservice.domain.UserAssessmentDetails;
import com.realcoderz.assessmentservice.domain.UserMaster;
import com.realcoderz.assessmentservice.exceptions.EntiryNotFoundException;
import com.realcoderz.assessmentservice.payload.SwaggerController.AssessmentCreationControllerPayload;
import com.realcoderz.assessmentservice.payload.SwaggerController.RCAssessmentCreationControllerPayload;
import com.realcoderz.assessmentservice.payload.SwaggerController.StudentAssessmentControllerPayload;
import com.realcoderz.assessmentservice.repository.AssessmentCreationRepository;
import com.realcoderz.assessmentservice.repository.AssociateAnswerTrackRepository;
import com.realcoderz.assessmentservice.repository.AssociateValidateRepository;
import com.realcoderz.assessmentservice.repository.QuestionMasterRepository;
import com.realcoderz.assessmentservice.repository.StudentMasterRepository;
import com.realcoderz.assessmentservice.repository.UserAssessmentRepository;
import com.realcoderz.assessmentservice.repository.UserMasterRepository;
import com.realcoderz.assessmentservice.service.AssessmentCreationService;
import com.realcoderz.assessmentservice.service.AssociateTopicScoresService;
import com.realcoderz.assessmentservice.service.BatchMasterService;
import com.realcoderz.assessmentservice.service.UserAssessmentService;
import com.realcoderz.assessmentservice.serviceimpl.AIPredictionServiceImpl;
import com.realcoderz.assessmentservice.serviceimpl.AssessmentCreationServiceImpl;
import com.realcoderz.assessmentservice.util.EncryptDecryptUtils;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Prateek
 */
@RestController
@RequestMapping(path = "/assessments")
public class AssessmentCreationController {

    static final Logger logger = LoggerFactory.getLogger(AssessmentCreationController.class);

    ObjectMapper mapper = new ObjectMapper();

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private AssessmentCreationService assessmentCreationService;

    @Autowired
    QuestionMasterRepository questionMasterRepository;

    @Autowired
    private AssociateAnswerTrackRepository associateAnswerRepo;

    @Autowired
    private AssociateValidateRepository associateValidtaeRepo;

    @Autowired
    private UserAssessmentService userAssessmentService;

    @Autowired
    private AssessmentCreationServiceImpl assessmentCreationServiceImpl;

    @Autowired
    private UserAssessmentRepository userAssessmentRepository;

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private AssociateTopicScoresService topicScoresService;

    @Autowired
    private StudentMasterRepository studentMasterRepository;

    @Autowired
    private AIPredictionServiceImpl aIPredictionServiceImpl;

    @Autowired
    private BatchMasterService batchMasterService;

    @Autowired
    private AssessmentCreationRepository assessmentCreationRepository;

    @PostMapping
    public Map list(@RequestBody String data) {
        logger.info("AssessmentCreationController -> list() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("AssessmentCreationController -> list() :: Method started successfully with request data : " + map);
            resultMap.put("list", assessmentCreationService.assessments(map));
            resultMap.put("status", "success");
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "error");
            logger.error("Problem in AssessmentCreationController -> list() :: ", ex);
        }
        logger.info("AssessmentCreationController -> list() :: Method completed successfully. With response date : " + resultMap);
        return resultMap;
    }

    @PostMapping(path = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ApiOperation(value = "Save assessment details", notes = "THIS METHOD IS USE TO SAVE ASSESSMENT DETAILS", response = AssessmentCreationControllerPayload.class)
    public Map add(@RequestBody String data) {
        logger.info("AssessmentCreationController -> add() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("AssessmentCreationController -> add() :: Method started successfully with request data : " + map);
            resultMap = assessmentCreationService.add(map);
        } catch (Exception objException) {
            resultMap.clear();
            resultMap.put("status", objException);
            logger.error("Problem in AssessmentCreationController -> add() :: ", objException.getMessage());
        }
        logger.info("AssessmentCreationController -> add() :: Method completed successfully. With response date : " + resultMap);

        return resultMap;
    }

    @ApiOperation(value = "All assessment list", response = AssessmentCreationControllerPayload.class)
    @PostMapping(path = "/all", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map allAssessmentList(@RequestBody String data) {
        logger.info("AssessmentCreationController -> allAssessmentList() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map<String, Object> map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("AssessmentCreationController -> allAssessmentList() :: Method started successfully with request data :" + map);
            resultMap.put("list", assessmentCreationService.allAssessmentsList(map));
            resultMap.put("status", "success");
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "error");
            logger.error("Problem in AssessmentCreationController -> allAssessmentList() :: ", ex.getMessage());
        }
        logger.info("AssessmentCreationController -> allAssessmentList() :: Method completed successfully with response date : " + resultMap);

        return resultMap;
    }

    @ApiOperation(value = "Get by ID", response = RCAssessmentCreationControllerPayload.class)
    @PostMapping(path = "/get", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map getById(@RequestBody String data) {
        logger.info("AssessmentCreationController -> getById() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("AssessmentCreationController -> getById() :: Method started successfully with request data : " + map);
            resultMap.put("data", assessmentCreationService.findRanAssess(map));
            resultMap.put("status", "success");

        } catch (EntiryNotFoundException ex) {
            resultMap.clear();
            resultMap.put("status", "error");
            resultMap.put("msg", "Assessment not found !!");
            logger.error("Problem in AssessmentCreationController -> getById() :: ", ex.getMessage());
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> getById() :: ", ex.getMessage());
        }
        logger.info("AssessmentCreationController -> getById() :: Method completed successfully. With response date : " + resultMap);

        return resultMap;
    }

    @ApiOperation(value = "Get topic for random assessment", response = RCAssessmentCreationControllerPayload.class)
    @PostMapping(path = "/getTopicsForRanAssess", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map getTopicsForAssessment(@RequestBody String data) {
        logger.info("AssessmentCreationController -> getTopicsForAssessment() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("AssessmentCreationController -> getTopicsForAssessment() :: Method started successfully.");
            resultMap.put("list", assessmentCreationService.getTopicsForRanAssess(map));
            logger.info("AssessmentCreationController -> getTopicsForAssessment() :: Method started successfully with requested data " + map);
            resultMap.put("status", "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> getTopicsForRanAssess() :: " + ex.getMessage());
        }
        logger.info("AssessmentCreationController -> getTopicsForRanAssess() :: Method completed successfully. With response date : " + resultMap);

        return resultMap;
    }

    @ApiOperation(value = "Save Random Assessment", response = RCAssessmentCreationControllerPayload.class)
    @PostMapping(path = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map update(@RequestBody String data) {
        logger.info("AssessmentCreationController -> update() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("AssessmentCreationController -> update() :: Method started successfully with request data ::" + map);
            AssessmentCreation saveUpd = assessmentCreationService.update(map);
            if (saveUpd != null) {
                resultMap.put("status", "success");
            } else {
                resultMap.put("status", "error");
            }
        } catch (Exception ex) {
            resultMap.clear();
            ex.printStackTrace();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> update() :: " + ex.getMessage());
        }
        logger.info("AssessmentCreationController -> update() :: Method completed successfully with response date : " + resultMap);

        return resultMap;
    }

//    @ApiOperation(value = "Get assessment", response = TakingAssessmentControllerPayload.class)
    @PostMapping(path = "/assessment", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map getAssessment(@RequestBody String data) {
        logger.info("AssessmentCreationController -> list() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            AssessmentCreation assessment = assessmentCreationService.findById(Long.parseLong(map.get("id").toString()));
            List<AssociateValidate> count = associateValidtaeRepo.findByAssociateIdAndAssessmentId(Long.parseLong(map.get("uid").toString()), Long.parseLong(map.get("id").toString()));
            if (assessment != null && count.isEmpty()) {
                LinkedCaseInsensitiveMap result = new LinkedCaseInsensitiveMap();
                List<LinkedCaseInsensitiveMap> questions = new ArrayList<>();
                Set topicList = new HashSet<>();
                result.put("assessment_id", assessment.getAssessment_id());
                result.put("assessment_desc", assessment.getAssessment_desc());
                result.put("instructions", assessment.getInstructions());
                result.put("time", assessment.getTime());
                result.put("language", assessmentCreationService.getLangName(assessment.getLanguage_id()));
                result.put("assessmentTimeBound", assessment.getAssessmentTimeBound());
                assessment.getQuestion_list().stream().forEach(que -> {
                    LinkedCaseInsensitiveMap quest = new LinkedCaseInsensitiveMap();
                    LinkedCaseInsensitiveMap topics = new LinkedCaseInsensitiveMap();

                    quest.put("question_id", que.getQuestion_id());
                    quest.put("question_type_id", que.getQuestion_type_id());
                    quest.put("question_desc", que.getQuestion_desc());
                    quest.put("codingTemplate", que.getCodingTemplate());
                    quest.put("no_of_answer", que.getNo_of_answer());
                    quest.put("questionTime", que.getQuestionTime());
                    quest.put("topic_id", que.getTopic_id());
                    quest.put("topic_Name", assessmentCreationRepository.topicNameById(que.getTopic_id()));
                    topics.put("topic_id", que.getTopic_id());
                    topics.put("topic_Name", assessmentCreationRepository.topicNameById(que.getTopic_id()));
                    topics.put("count_no_of_question", assessmentCreationRepository.countNoOfQuestion(que.getTopic_id(), assessment.getOrganizationId(), assessment.getAssessment_id()));
                    topicList.add(topics);

                    List<LinkedCaseInsensitiveMap> options = new ArrayList<>();
                    if (que.getOptions_list() != null) {
                        que.getOptions_list().stream().forEach(opt -> {
                            LinkedCaseInsensitiveMap option = new LinkedCaseInsensitiveMap();
                            option.put("option_id", opt.getOption_id());
                            option.put("option_desc", opt.getOption_desc());
                            options.add(option);
                        });
                    }
                    Collections.shuffle(options);
                    quest.put("options_list", options);
                    questions.add(quest);
                });
                Collections.shuffle(questions);
                result.put("question_list", questions);
                result.put("topic_list", topicList);
                result.put("topic_wise_question", questions);
                resultMap.put("assessment", result);
                resultMap.put("status", "success");
            } else if (!count.isEmpty()) {
                if (count.get(0).getAssessmentSubmit()) {
                    resultMap.put("msg", "Assessment already attempted!");
                    resultMap.put("status", "error");
                    logger.info("AssessmentCreationController -> getAssessment() :: Assessment already attempted!: ");

                } else {
                    resultMap = resumeTest(assessment, Long.parseLong(map.get("uid").toString()));
                }
            } else {
                resultMap.put("msg", "Assessment not exist.");
                resultMap.put("status", "error");
            }
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> getAssessment() :: ", ex.getMessage());
        }
        logger.info("AssessmentCreationController -> getAssessment() :: Method completed successfully with response date : " + resultMap);

        return resultMap;
    }

    private LinkedCaseInsensitiveMap resumeTest(AssessmentCreation assessment, Long userId) {
        LinkedCaseInsensitiveMap resultMap = new LinkedCaseInsensitiveMap();
        LinkedCaseInsensitiveMap result = new LinkedCaseInsensitiveMap();
        List<LinkedCaseInsensitiveMap> questions = new ArrayList<>();
        result.put("assessment_id", assessment.getAssessment_id());
        result.put("assessment_desc", assessment.getAssessment_desc());
        result.put("instructions", assessment.getInstructions());
        result.put("time", assessment.getTime());
        result.put("resume", true);
        result.put("assessmentTimeBound", assessment.getAssessmentTimeBound());
        List<LinkedCaseInsensitiveMap> selectedQuestions = associateAnswerRepo.findByAssociateIdAndAssessmentId(userId, assessment.getAssessment_id());
        assessment.getQuestion_list().stream().forEach(que -> {
            Optional<LinkedCaseInsensitiveMap> present = selectedQuestions.stream().filter(sq -> sq.get("questionId").toString().equalsIgnoreCase(que.getQuestion_id().toString())).findFirst();
            LinkedCaseInsensitiveMap quest = new LinkedCaseInsensitiveMap();
            quest.put("question_id", que.getQuestion_id());
            quest.put("question_type_id", que.getQuestion_type_id());
            quest.put("question_desc", que.getQuestion_desc());
            quest.put("codingTemplate", que.getCodingTemplate());
            quest.put("no_of_answer", que.getNo_of_answer());
            quest.put("attempted", present.isPresent());
            quest.put("questionTime", que.getQuestionTime());
            List<LinkedCaseInsensitiveMap> options = new ArrayList<>();
            if (que.getOptions_list() != null) {
                que.getOptions_list().stream().forEach(opt -> {
                    LinkedCaseInsensitiveMap option = new LinkedCaseInsensitiveMap();
                    option.put("option_id", opt.getOption_id());
                    option.put("option_desc", opt.getOption_desc());
                    if (present.isPresent()) {
                        if (opt.getOption_id() == Long.parseLong(present.get().get("answer").toString())) {
                            option.put("isAnswer", "Y");
                            option.put("selected", true);
                        }
                    }
                    options.add(option);
                });
            }
            Collections.shuffle(options);
            quest.put("options_list", options);
            questions.add(quest);
        });
        if (assessment.getQuestion_list().size() > selectedQuestions.size()) {
            Collections.sort(questions, Comparator.comparing(map -> !Boolean.parseBoolean(map.get("attempted").toString())));
            result.put("question_list", questions);
            resultMap.put("assessment", result);
            resultMap.put("status", "success");
        } else {
            result.put("question_list", questions);
            resultMap.put("assessment", result);
            resultMap.put("user_id", userId);
            resultMap.put("counter", "0");
            this.saveAssessment(resultMap);
            resultMap.clear();
            resultMap.put("msg", "Assessment already attempted!");
            resultMap.put("status", "error");
        }
        return resultMap;
    }

    private void saveAssessment(Map map) {
        Map assessmentData = (Map) map.get("assessment");
        int windowSwitch = Integer.parseInt(map.get("counter").toString());
        AssessmentCreation assessment = assessmentCreationService.findById(Long.parseLong(assessmentData.get("assessment_id").toString()));
        if (assessment != null) {
            UserAssessment userAssessment = new UserAssessment();
            Set<UserAssessmentDetails> detailList = new HashSet<>();
            List<LinkedCaseInsensitiveMap> questionList = (List<LinkedCaseInsensitiveMap>) assessmentData.get("question_list");
            userAssessment.setUser_id(Long.parseLong(map.get("user_id").toString()));
            userAssessment.setAssessment_id(assessment.getAssessment_id());
            userAssessment.setAssessmentName(assessment.getAssessment_desc());
            userAssessment.setStartTime(null);
            userAssessment.setRemarks(windowSwitch == 0 ? "" : "This user switch their window " + windowSwitch + " times during assessment.");
            userAssessment.setEndTime(new Date());
            questionList.stream().forEach(question -> {
                UserAssessmentDetails details = new UserAssessmentDetails();
                List<LinkedCaseInsensitiveMap> options = (List<LinkedCaseInsensitiveMap>) question.get("options_list");
                List<LinkedCaseInsensitiveMap> selectedOptions = options.parallelStream().filter(op -> op.containsKey("isAnswer") && "Y".equalsIgnoreCase(op.get("isAnswer").toString())).collect(Collectors.toList());
                String answer = selectedOptions.stream().map(s -> s.get("option_id").toString()).collect(Collectors.joining(","));
                details.setUserAssessment(userAssessment);
                details.setQuestion_id(Long.parseLong(question.get("question_id").toString()));
                details.setAnswer(answer);
                detailList.add(details);
            });
            userAssessment.setDetail_list(detailList);
            userAssessmentService.saveUserAssessment(userAssessment);
            Map result = userAssessmentService.calculateResult(userAssessment.getUser_id(), userAssessment.getAssessment_id());
            int totalMcqMarks = Integer.parseInt(result.get("totalNoOfQuestion").toString());
            int totalMcqScore = Integer.parseInt(result.get("correctQuestion").toString());
            userAssessment.setTotal_no_of_questions(totalMcqMarks);
            userAssessment.setCorrect_questions(totalMcqScore);
            if (totalMcqScore != 0) {
                DecimalFormat df = new DecimalFormat("#.00");
                float mcqPercentage = Float.valueOf(df.format((totalMcqScore * 100) / (float) totalMcqMarks));
                userAssessment.setMcqPercentage(mcqPercentage);
                userAssessment.setTotalPercentage(mcqPercentage);
            }
            UserAssessment userAss = userAssessmentService.saveUserAssessment(userAssessment);
            //To Calculate topic wise scores
            try {
//                List<LinkedCaseInsensitiveMap> assessments = new ArrayList<>();
                LinkedCaseInsensitiveMap userAssessments = new LinkedCaseInsensitiveMap();
                userAssessments.put("assessment_id", userAss.getAssessment_id());
                userAssessments.put("user_assessment_id", userAss.getUser_assessment_id());
                userAssessments.put("total_questions", userAss.getTotal_no_of_questions());
                LinkedCaseInsensitiveMap topicWiseScores = assessmentCreationService.getTopicWiseScoresForAssociates(userAssessments);
                List<AssociateTopicScores> scores = new ArrayList<>();
                if (topicWiseScores != null) {
                    if (topicWiseScores.containsKey("topicWiseScore") && topicWiseScores.get("topicWiseScore") != null) {
                        List<LinkedCaseInsensitiveMap> topicScores = (List<LinkedCaseInsensitiveMap>) topicWiseScores.get("topicWiseScore");
                        for (LinkedCaseInsensitiveMap topic : topicScores) {
                            scores.add(new AssociateTopicScores(Long.parseLong(map.get("user_id").toString()), userAss.getAssessment_id(), topic.get("topicName").toString(), Float.parseFloat(topic.get("average").toString())));
                        }
                        topicScoresService.saveAll(scores);
                    }
                }
//                new Thread(() -> {
//                    long userId = userAss.getUser_id();
//                    List<LinkedCaseInsensitiveMap> topicWiseScore = assessmentCreationServiceImpl.getTopicWiseScoresForAssociates(userId, assessments);
//                    if (topicWiseScore.size() > 0 && !topicWiseScore.isEmpty()) {
//                        List<AssociateTopicScores> scores = new ArrayList<>();
//                        for (LinkedCaseInsensitiveMap assess : topicWiseScore) {
//                            List<LinkedCaseInsensitiveMap> topicScore = (List<LinkedCaseInsensitiveMap>) assess.get("topicWiseScore");
//                            for (LinkedCaseInsensitiveMap topic : topicScore) {
//                                scores.add(new AssociateTopicScores(userId, Long.parseLong(assess.get("assessmentId").toString()), topic.get("topicName").toString(), Float.parseFloat(topic.get("average").toString())));
//                            }
//                        }
//                        topicScoresService.saveAll(scores);
//                    }
//                }).start();
            } catch (Exception ex) {
                logger.error("Problem in saveAssessment() :: While saving topic wise scores => " + ex);
            }
            associateAnswerRepo.deleteByAssociateId(Long.parseLong(map.get("user_id").toString()));
            associateValidtaeRepo.updateAssessmentSubmit(Long.parseLong(map.get("user_id").toString()), Long.parseLong(assessmentData.get("assessment_id").toString()));
            long count = userAssessmentRepository.countByUserId(Long.parseLong(map.get("user_id").toString()));
            if (count == 12) {
                try {
                    UserMaster userMaster = userMasterRepository.findByUserId(Long.parseLong(map.get("user_id").toString()));
                    if (userMaster != null) {
                        if (profile.split(",")[0].equalsIgnoreCase("prod")) {
                            List<com.google.protobuf.Value> values = new ArrayList<>();
                            String studentAssessmentData = studentMasterRepository.getDataToPredictAIScore(userMasterRepository.getStudentIdFromUserId(Long.parseLong(map.get("user_id").toString())));
                            String[] predictData = studentAssessmentData.split(",");
                            for (String value : predictData) {
                                values.add(com.google.protobuf.Value.newBuilder().setNumberValue(Integer.parseInt(value)).build());
                            }
                            List<Long> scores = userAssessmentRepository.getAssociateScoreForPrediction(Long.parseLong(map.get("user_id").toString()));
                            Storage storage = StorageOptions.getDefaultInstance().getService();
                            for (long value : scores) {
                                values.add(com.google.protobuf.Value.newBuilder().setNumberValue((double) value).build());
                            }
                            int predictDataLength = values.size();
                            while (predictDataLength < 19) {
                                values.add(com.google.protobuf.Value.newBuilder().setNumberValue(0).build());
                                predictDataLength++;
                            }
                            userMaster.setAi_final_score(aIPredictionServiceImpl.predict("realcoderz-production", "TBL2656536640939360256", values));
                        } else {
                            userMaster.setAi_final_score(0.00);
                        }
                        userMasterRepository.save(userMaster);
                    }
                } catch (Exception obException) {
                    logger.error("Problem in AssessmentCreationController -> saveAssessment() on AI Prediction part :: ", obException);
                }
            }
        }
    }

    @PostMapping(path = "/allassessment", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
//    @ApiOperation(value = "Resume upload", notes = "This method is use to upload resume in GCS",
//            response = DashboardControllerPayload.class)
    public Map getAssessmentForAssociates(@RequestBody String data
    ) {
        logger.info("AssessmentCreationController -> getAssessmentForAssociates() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("AssessmentCreationController -> getAssessmentForAssociates() :: Method started successfully with ");
            List<LinkedCaseInsensitiveMap> batchList = batchMasterService.getBatchForAssociates(map);
            if (batchList != null && batchList.size() > 0) {
                batchList.stream().forEach(batch -> {
                    Map resultSet = assessmentCreationService.getAssessmentByBatchAssociateId(Long.parseLong(batch.get("batch_id").toString()), Long.parseLong(map.get("id").toString()));
                    batch.put("assessments", resultSet.get("assessments"));
                    batch.put("learningRecommendations", resultSet.get("learning_recommendation"));
                });
                resultMap.put("list", batchList);
                resultMap.put("status", "success");
            } else {
                resultMap.put("msg", "Please talk to your Instructor because there is no batch assign to you yet.");
                resultMap.put("status", "error");
            }
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> getAssessmentForAssociates() :: ", ex);
        }
        logger.info("AssessmentCreationController -> getAssessmentForAssociates() :: Method completed successfully with response date : " + resultMap);

        return resultMap;
    }
//    @ApiOperation(value = "Set timer", response = TakingAssessmentControllerPayload.class)
// this is when associate starts the test

    @PostMapping(path = "/settimer", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map setTimer(@RequestBody String data
    ) {
        logger.info("AssessmentCreationController -> setTimer() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            Long assessment_id = (map.containsKey("aid") ? Long.parseLong(map.get("aid") + "") : null);
            Long user_id = (map.containsKey("uid") ? Long.parseLong(map.get("uid") + "") : null);
            Long orgId = (map.containsKey("orgId") ? Long.parseLong(map.get("uid").toString()) : null);
            if ((assessment_id != null) && (user_id != null) && (orgId != null)) {
                AssessmentCreation assessment = assessmentCreationService.findById(assessment_id);
                if (assessment != null) {
                    Boolean withinLimit = userAssessmentService.sendEmailWhenLimitExceed(orgId);
                    if (withinLimit) {
                        boolean assessmentSubmit = assessment.getAssessmentTimeBound().equalsIgnoreCase("assessmentTime");
                        AssociateValidate av = new AssociateValidate(user_id, assessment_id, user_id, assessmentSubmit);
                        associateValidtaeRepo.save(av);
                        if (assessmentSubmit) {
                            long timerInMiliSec = ((assessment.getTime() + 1) * 60 * 1000);
                            Timer timer = new Timer("Assessment_Timer_For_" + user_id + "+" + assessment_id);
                            final Long userId = user_id;
                            final Long assessmentId = assessment_id;
                            final Date startTime = new Date();
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    List<LinkedCaseInsensitiveMap> list = assessmentCreationService.getUserAssessmentByUserAssessmentId(userId, assessmentId);
                                    if ((list == null) || (list.isEmpty())) {
                                        UserAssessment userAssessment = new UserAssessment();
                                        Set<UserAssessmentDetails> detailList = new HashSet<>();
                                        Set<QuestionMaster> questionList = assessment.getQuestion_list();
                                        userAssessment.setUser_id(Long.parseLong(map.get("user_id").toString()));
                                        userAssessment.setAssessment_id(assessment.getAssessment_id());
                                        userAssessment.setStartTime(startTime);
                                        userAssessment.setEndTime(new Date());
                                        userAssessment.setCreatedBy(userId.toString());
                                        userAssessment.setLastModifiedBy(userId.toString());
                                        userAssessment.setRemarks("By Backend(Associates) -> Timer run out start at." + startTime + " and end at" + new Date());
                                        List<LinkedCaseInsensitiveMap> selectedQuestions = associateAnswerRepo.findByAssociateIdAndAssessmentId(userId, assessmentId);
                                        questionList.stream().forEach(question -> {
                                            Optional<LinkedCaseInsensitiveMap> present = selectedQuestions.stream().filter(sq -> sq.get("questionId").toString().equalsIgnoreCase(question.getQuestion_id().toString())).findFirst();
                                            UserAssessmentDetails details = new UserAssessmentDetails();
                                            details.setUserAssessment(userAssessment);
                                            details.setQuestion_id(question.getQuestion_id());
                                            if (present.isPresent()) {
                                                String answer = present.get().get("answer").toString();
                                                details.setAnswer(answer);
                                            } else {
                                                details.setAnswer("");
                                            }
                                            detailList.add(details);
                                        });
                                        userAssessment.setDetail_list(detailList);
                                        userAssessmentService.saveUserAssessment(userAssessment);
                                        Map result = userAssessmentService.calculateResult(userAssessment.getUser_id(), userAssessment.getAssessment_id());
                                        int totalMcqMarks = Integer.parseInt(result.get("totalNoOfQuestion").toString());
                                        int totalMcqScore = Integer.parseInt(result.get("correctQuestion").toString());
                                        userAssessment.setTotal_no_of_questions(totalMcqMarks);
                                        userAssessment.setCorrect_questions(totalMcqScore);
                                        if (totalMcqScore != 0) {
                                            DecimalFormat df = new DecimalFormat("#.00");
                                            float mcqPercentage = Float.valueOf(df.format((totalMcqScore * 100) / (float) totalMcqMarks));
                                            userAssessment.setMcqPercentage(mcqPercentage);
                                            userAssessment.setTotalPercentage(mcqPercentage);
                                        }
                                        UserAssessment userAss = userAssessmentService.saveUserAssessment(userAssessment);
                                        associateAnswerRepo.deleteByAssociateId(Long.parseLong(map.get("user_id").toString()));
                                        //To Calculate topic wise scores
                                        try {
                                            List<LinkedCaseInsensitiveMap> assessments = new ArrayList<>();
                                            LinkedCaseInsensitiveMap userIds = new LinkedCaseInsensitiveMap();
                                            userIds.put("assessment_id", userAss.getAssessment_id());
                                            userIds.put("user_assessment_id", userAss.getUser_assessment_id());
                                            userIds.put("total_questions", userAss.getTotal_no_of_questions());
                                            assessments.add(userIds);
                                            LinkedCaseInsensitiveMap topicWiseScores = assessmentCreationService.getTopicWiseScoresForAssociates(userIds);
                                            List<AssociateTopicScores> scores = new ArrayList<>();
                                            if (topicWiseScores != null) {
                                                if (topicWiseScores.containsKey("topicWiseScore") && topicWiseScores.get("topicWiseScore") != null) {
                                                    List<LinkedCaseInsensitiveMap> topicScores = (List<LinkedCaseInsensitiveMap>) topicWiseScores.get("topicWiseScore");
                                                    for (LinkedCaseInsensitiveMap topic : topicScores) {
                                                        scores.add(new AssociateTopicScores(Long.parseLong(map.get("user_id").toString()), userAss.getAssessment_id(), topic.get("topicName").toString(), Float.parseFloat(topic.get("average").toString())));
                                                    }
                                                    topicScoresService.saveAll(scores);
                                                }
                                            }
//                                            new Thread(() -> {
//                                                long userId = userAss.getUser_id();
//                                                List<LinkedCaseInsensitiveMap> topicWiseScore = assessmentCreationServiceImpl.getTopicWiseScoresForAssociates(userId, assessments);
//                                                if (topicWiseScore.size() > 0 && !topicWiseScore.isEmpty()) {
//                                                    List<AssociateTopicScores> scores = new ArrayList<>();
//                                                    for (LinkedCaseInsensitiveMap assess : topicWiseScore) {
//                                                        List<LinkedCaseInsensitiveMap> topicScore = (List<LinkedCaseInsensitiveMap>) assess.get("topicWiseScore");
//                                                        for (LinkedCaseInsensitiveMap topic : topicScore) {
//                                                            scores.add(new AssociateTopicScores(userId, Long.parseLong(assess.get("assessmentId").toString()), topic.get("topicName").toString(), Float.parseFloat(topic.get("average").toString())));
//                                                        }
//                                                    }
//                                                    topicScoresService.saveAll(scores);
//                                                }
//                                            }).start();
                                        } catch (Exception ex) {
                                            logger.error("Problem in saveAssessment() :: While saving topic wise scores => " + ex.getMessage());
                                        }
                                    }
                                    timer.cancel();
                                }
                            };
                            timer.schedule(task, timerInMiliSec);
                            resultMap.put("status", "success");
                        }
                    } else {
                        resultMap.put("status", "countExceed");
                        resultMap.put("msg", "Oops!!! Something went wrong.. Please contact to administrator.");

                    }
                }
            }

        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in setTimer() :: While saving topic wise scores => " + ex);
        }
        logger.info("AssessmentCreationController -> list() :: Method completed successfully. With response date : " + resultMap);

        return resultMap;
    }

    @PostMapping(path = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map saveUserAssessment(@RequestBody String data
    ) {
        logger.info("AssessmentCreationController -> saveUserAssessment() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            Map assessmentData = (Map) map.get("assessment");
            int windowSwitch = Integer.parseInt(map.get("counter").toString());
            AssessmentCreation assessment = assessmentCreationService.findById(Long.parseLong(assessmentData.get("assessment_id").toString()));
            if (assessment != null) {
                List<LinkedCaseInsensitiveMap> list = assessmentCreationService.getUserAssessmentByUserAssessmentId(Long.parseLong(map.get("user_id").toString()), assessment.getAssessment_id());
                if ((list == null) || (list.isEmpty())) {
                    UserAssessment userAssessment = new UserAssessment();
                    Set<UserAssessmentDetails> detailList = new HashSet<>();
                    Set<QuestionMaster> questionList = assessment.getQuestion_list();
                    List<LinkedHashMap> selectedQuestions = (List<LinkedHashMap>) assessmentData.get("question_list");
                    userAssessment.setUser_id(Long.parseLong(map.get("user_id").toString()));
                    userAssessment.setAssessment_id(assessment.getAssessment_id());
                    userAssessment.setAssessmentName(assessment.getAssessment_desc());
                    userAssessment.setStartTime(null);
                    userAssessment.setRemarks(windowSwitch == 0 ? "" : "This user switch their window " + windowSwitch + " times during assessment.");
                    userAssessment.setEndTime(new Date());
                    questionList.stream().forEach(question -> {
                        Optional<LinkedHashMap> present = selectedQuestions.stream().filter(sq -> sq.get("question_id").toString().equalsIgnoreCase(question.getQuestion_id().toString())).findFirst();
                        if (present.isPresent()) {
                            UserAssessmentDetails details = new UserAssessmentDetails();
                            List<LinkedHashMap> options = (List<LinkedHashMap>) ((LinkedHashMap) present.get()).get("options_list");
                            List<LinkedHashMap> selectedOptions = options.parallelStream().filter(op -> op.containsKey("isAnswer") && "Y".equalsIgnoreCase(op.get("isAnswer").toString())).collect(Collectors.toList());
                            String answer = selectedOptions.stream().map(s -> s.get("option_id").toString()).collect(Collectors.joining(","));
                            details.setUserAssessment(userAssessment);
                            details.setQuestion_id(question.getQuestion_id());
                            details.setAnswer(answer);
                            detailList.add(details);
                        }
                    });
                    userAssessment.setDetail_list(detailList);
                    userAssessmentService.saveUserAssessment(userAssessment);
                    Map result = userAssessmentService.calculateResult(userAssessment.getUser_id(), userAssessment.getAssessment_id());
                    int totalMcqMarks = Integer.parseInt(result.get("totalNoOfQuestion").toString());
                    int totalMcqScore = Integer.parseInt(result.get("correctQuestion").toString());
                    userAssessment.setTotal_no_of_questions(totalMcqMarks);
                    userAssessment.setCorrect_questions(totalMcqScore);
                    if (totalMcqScore != 0) {
                        DecimalFormat df = new DecimalFormat("#.00");
                        float mcqPercentage = Float.valueOf(df.format((totalMcqScore * 100) / (float) totalMcqMarks));
                        userAssessment.setMcqPercentage(mcqPercentage);
                        userAssessment.setTotalPercentage(mcqPercentage);
                    }
                    UserAssessment userAss = userAssessmentService.saveUserAssessment(userAssessment);
                    //To Calculate topic wise scores
                    try {
                        List<LinkedCaseInsensitiveMap> assessments = new ArrayList<>();
                        LinkedCaseInsensitiveMap userAssessments = new LinkedCaseInsensitiveMap();
                        userAssessments.put("assessment_id", userAss.getAssessment_id());
                        userAssessments.put("user_assessment_id", userAss.getUser_assessment_id());
                        userAssessments.put("total_questions", userAss.getTotal_no_of_questions());
                        assessments.add(userAssessments);
                        LinkedCaseInsensitiveMap topicWiseScores = assessmentCreationService.getTopicWiseScoresForAssociates(userAssessments);
                        List<AssociateTopicScores> scores = new ArrayList<>();
                        if (topicWiseScores != null) {
                            if (topicWiseScores.containsKey("topicWiseScore") && topicWiseScores.get("topicWiseScore") != null) {
                                List<LinkedCaseInsensitiveMap> topicScores = (List<LinkedCaseInsensitiveMap>) topicWiseScores.get("topicWiseScore");
                                for (LinkedCaseInsensitiveMap topic : topicScores) {
                                    scores.add(new AssociateTopicScores(Long.parseLong(map.get("user_id").toString()), userAss.getAssessment_id(), topic.get("topicName").toString(), Float.parseFloat(topic.get("average").toString())));
                                }
                                topicScoresService.saveAll(scores);
                            }
                        }
//                        new Thread(() -> {
//                            long userId = userAss.getUser_id();
//                            LinkedCaseInsensitiveMap topicWiseScore = assessmentCreationService.getTopicWiseScoresForAssociates(userAssessments);
//                            if (topicWiseScore 0 && !topicWiseScore.isEmpty()) {
//                                List<AssociateTopicScores> scores = new ArrayList<>();
//                                for (LinkedCaseInsensitiveMap assess : topicWiseScore) {
//                                    List<LinkedCaseInsensitiveMap> topicScore = (List<LinkedCaseInsensitiveMap>) assess.get("topicWiseScore");
//                                    for (LinkedCaseInsensitiveMap topic : topicScore) {
//                                        scores.add(new AssociateTopicScores(userId, Long.parseLong(assess.get("assessmentId").toString()), topic.get("topicName").toString(), Float.parseFloat(topic.get("average").toString())));
//                                    }
//                                }
//                                topicScoresService.saveAll(scores);
//                            }
//                        }).start();
                    } catch (Exception ex) {
                        logger.error("Problem in saveAssessment() :: While saving topic wise scores => " + ex);
                    }
                    associateAnswerRepo.deleteByAssociateId(Long.parseLong(map.get("user_id").toString()));
                    associateValidtaeRepo.updateAssessmentSubmit(Long.parseLong(map.get("user_id").toString()), Long.parseLong(assessmentData.get("assessment_id").toString()));
                    long count = userAssessmentRepository.countByUserId(Long.parseLong(map.get("user_id").toString()));
                    if (count == 12) {
                        try {
                            UserMaster userMaster = userMasterRepository.findByUserId(Long.parseLong(map.get("user_id").toString()));
                            if (userMaster != null) {
                                if (profile.split(",")[0].equalsIgnoreCase("prod")) {
                                    List<com.google.protobuf.Value> values = new ArrayList<>();
                                    String studentAssessmentData = studentMasterRepository.getDataToPredictAIScore(userMasterRepository.getStudentIdFromUserId(Long.parseLong(map.get("user_id").toString())));
                                    String[] predictData = studentAssessmentData.split(",");
                                    for (String value : predictData) {
                                        values.add(com.google.protobuf.Value.newBuilder().setNumberValue(Integer.parseInt(value)).build());
                                    }
                                    List<Long> scores = userAssessmentRepository.getAssociateScoreForPrediction(Long.parseLong(map.get("user_id").toString()));
                                    Storage storage = StorageOptions.getDefaultInstance().getService();
                                    for (long value : scores) {
                                        values.add(com.google.protobuf.Value.newBuilder().setNumberValue((double) value).build());
                                    }
                                    int predictDataLength = values.size();
                                    while (predictDataLength < 19) {
                                        values.add(com.google.protobuf.Value.newBuilder().setNumberValue(0).build());
                                        predictDataLength++;
                                    }
                                    userMaster.setAi_final_score(aIPredictionServiceImpl.predict("realcoderz-production", "TBL2656536640939360256", values));
                                } else {
                                    userMaster.setAi_final_score(0.00);
                                }
                                userMasterRepository.save(userMaster);
                            }
                        } catch (Exception obException) {
                            resultMap.put("status", "success");
                            logger.error("Problem in AssessmentCreationController -> saveAssessment() on AI Prediction part :: ", obException);
                        }
                    }
                }
                resultMap.put("status", "success");
            } else {
                resultMap.put("msg", "Assessment not exist.");
                resultMap.put("status", "error");
            }
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> saveAssessment() :: ", ex);
        }
        logger.info("AssessmentCreationController -> list() :: Method completed successfully. With response date : " + resultMap);

        return resultMap;
    }

    @PostMapping(path = "/stdntquiz")
    public Map getQuiz(@RequestBody String data) {
        logger.info("AssessmentCreationController -> getQuiz() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("AssessmentCreationController --> getQuiz() :: map data" + map);
//            resultMap = assessmentCreationService.getQuiz(Long.parseLong(map.get("id").toString()), Long.parseLong(map.get("jobportal_id").toString()), Long.parseLong(map.get("organizationId").toString()));
        resultMap = assessmentCreationService.getQuiz(Long.parseLong(map.get("id").toString()), map.get("jobportal_id") != null ? Long.parseLong(map.get("jobportal_id").toString()) : null, Long.parseLong(map.get("organizationId").toString()), map.get("assessmentId") != null ? Long.parseLong(map.get("assessmentId").toString()) : null);
        } catch (IOException ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> getQuiz() :: ", ex);
        }
        logger.info("AssessmentCreationController -> getQuiz () :: Method completed successfully. With response date : " + resultMap);
        return resultMap;
    }

    @PostMapping(path = "/stdntresult", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map saveAssessment(@RequestBody String data, HttpServletRequest request
    ) {
        logger.info("AssessmentCreationController -> saveAssessment() :: Method started successfully.");
        Map resultMap = new HashMap<>();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            map.put("accessToken", request.getHeader("Authorization"));
            Long assessmentId = ((map.containsKey("assessmentId") && map.get("assessmentId") != null) ? Long.parseLong(map.get("assessmentId").toString()) : 0);
            if (assessmentId > 0) {
                resultMap.put("status", "success");
                assessmentCreationService.saveStudentFeedBack(map);
                assessmentCreationService.saveAssessment(map);
            } else {
                resultMap.put("msg", "Nothing to save.");
                resultMap.put("status", "error");
            }
        } catch (IOException ex) {
            logger.error("Problem in AssessmentCreationController -> saveAssessment() :: ", ex);
        }
        logger.info("AssessmentCreationController -> saveAssessment() :: Method completed successfully. With response date : " + resultMap);

        return resultMap;
    }

    @PostMapping(path = "/delete", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map delete(@RequestBody String data
    ) {
        logger.info("AssessmentCreationController -> delete() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            AssessmentCreation assessmentCreation = assessmentCreationService.findById(Long.parseLong(map.get("id").toString()));
            assessmentCreationService.delete(assessmentCreation);
            resultMap.put("status", "success");
        } catch (EntiryNotFoundException ex) {
            resultMap.clear();
            resultMap.put("status", "error");
            resultMap.put("msg", "Assessment not found !!");
            logger.error("Problem in AssessmentCreationController -> delete() :: ", ex);
        } catch (Exception ex) {
            resultMap.clear();
            ex.printStackTrace();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> delete() :: ", ex);
        }
        logger.info("AssessmentCreationController -> delete() :: Method completed successfully. With response date : " + resultMap);

        return resultMap;
    }

    @PostMapping(path = "/save_ans_details")
    public void saveAnswerDetails(@RequestBody String data
    ) {
        logger.info("AssessmentCreationController -> saveAnswerDetails() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            assessmentCreationService.saveAnswerDetails(map);
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> saveAnswerDetails() :: ", ex);
        }
        logger.info("AssessmentCreationController -> saveAnswerDetails() :: Method completed successfully. With response date : " + resultMap);

    }

    // this api is meant to get completed assessment counts of candidate and associate both
    @PostMapping(path = "/completedcount", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map getCompletedAssessmentCount(@RequestBody String data
    ) {
        logger.info("AssessmentCreationController -> getCompletedAssessmentCount() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            String orgId = mapper.readValue(EncryptDecryptUtils.decrypt(data), String.class);
            if (orgId == null || orgId == "") {
                resultMap.put("error", "Please provide organization id!!!");
            } else {
                resultMap = associateValidtaeRepo.getCompletedAssessments(Long.parseLong(orgId));
            }
        } catch (Exception ex) {
            resultMap.clear();
            ex.printStackTrace();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> delete() :: ", ex);
        }
        logger.info("AssessmentCreationController -> getCompletedAssessmentCount() :: Method completed successfully. With response date : " + resultMap);

        return resultMap;
    }

    @PostMapping(path = "/associatetopicscore")
    public Map getAssociateTopicScores(@RequestBody String data) {
        logger.info("AssessmentCreationController -> getAssociateTopicScores() :: Method started successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            resultMap = assessmentCreationService.getAssociateTopicScores(map);
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in AssessmentCreationController -> getAssociateTopicScores() :: ", ex);
        }
        logger.info("AssessmentCreationController -> getAssociateTopicScores() :: Method completed successfully. With response date : " + resultMap);

        return resultMap;
    }

    @ApiOperation(value = "save text assessment", response = StudentAssessmentControllerPayload.class)
    @PostMapping(path = "/savetext", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map saveTextAssessment(@RequestBody String data) {
        Map resultMap = new HashMap<>();
        try {
            logger.info("StudentAssessmentController -> saveTextAssessment() ::  Method execution start");
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("StudentAssessmentController -> saveTextAssessment() ::  Method execution start with request data : " + map);
            Map assessmentData = ((map.containsKey("assessment") && map.get("assessment") != null) ? (Map) map.get("assessment") : null);
            if (assessmentData != null) {
                resultMap = assessmentCreationService.saveTextAssessment(map);
            } else {
                resultMap.put("msg", "Nothing to save.");
                resultMap.put("status", "error");
            }
        } catch (IOException ex) {
            logger.error("Problem in StudentAssessmentController -> saveTextAssessment() :: ", ex);
        }
        logger.info("StudentAssessmentController -> saveTextAssessment() ::  Method execution complete with response data :: " + resultMap);

        return resultMap;
    }

    @ApiOperation(value = "get text answer", response = StudentAssessmentControllerPayload.class)
    @PostMapping(path = "/gettext", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map getTextAnswer(@RequestBody String data) {
        logger.info("StudentAssessmentController -> getTextAnswer() ::  Method execution start");
        Map resultMap = new HashMap<>();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("StudentAssessmentController -> getTextAnswer() :: request data :: " + map);
            if (map.containsKey("assessment_id") && map.get("assessment_id") != null) {
                resultMap = assessmentCreationService.getTextAnswer(map);
            } else {
                resultMap.put("msg", "Nothing to save.");
                resultMap.put("status", "error");
                logger.info("StudentAssessmentController -> getTextAnswer() ::  Nothing to save");

            }
        } catch (IOException ex) {
            logger.error("Problem in StudentAssessmentController -> getTextAnswer() :: ", ex);
        }
        logger.info("StudentAssessmentController -> getTextAnswer() ::  Response data :: " + resultMap);
        logger.info("StudentAssessmentController -> getTextAnswer() ::  Method execution completed");

        return resultMap;
    }

    @ApiOperation(value = "get result", response = StudentAssessmentControllerPayload.class)
    @PostMapping(path = "/getresult", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map findResult(@RequestBody String data) {
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("AssessmentCreationController -> findResult() ::  Method execution start with request data : " + map);
            resultMap = assessmentCreationService.getResultByUserId(map);
        } catch (IOException ex) {
            logger.error("Problem in AssessmentCreationController -> findResult() :: ", ex);
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("AssessmentCreationController -> findResult() :: Exception Occured !! ", ex);
        }
        logger.info("AssessmentCreationController -> findResult() ::  Method execution complete with response data : " + resultMap);
        return resultMap;
    }
}
