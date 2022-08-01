/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realcoderz.assessmentservice.domain.CodingQuestionTestCases;
import com.realcoderz.assessmentservice.domain.QuestionMaster;
import com.realcoderz.assessmentservice.domain.QuestionOptionMapping;
import com.realcoderz.assessmentservice.exceptions.EntiryNotFoundException;
import com.realcoderz.assessmentservice.payload.SwaggerController.QuestionMasterControllerPayload;
import com.realcoderz.assessmentservice.repository.CodingQuestionTestCaseRepository;
import com.realcoderz.assessmentservice.service.QuestionMasterService;
import com.realcoderz.assessmentservice.util.EncryptDecryptUtils;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Prateek
 */
@CrossOrigin("*")
@RestController
@RequestMapping(path = "/questions")
public class QuestionMasterController {

    static final Logger logger = LoggerFactory.getLogger(QuestionMasterController.class);

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private QuestionMasterService questionMasterService;

    @Autowired
    private CodingQuestionTestCaseRepository testCaseRepo;

    /**
     * Method: uploadExcelFileData
     *
     * @param file
     * @param organizationId
     * @param questionTypeId
     * @return: Return the data that is upload from the given file
     * @param: file
     */
    @ApiOperation(value = "upload excel file data", response = QuestionMasterControllerPayload.class)
    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map uploadExcelFileData(@RequestParam("file") MultipartFile file, @RequestParam("organizationId") Long organizationId, @RequestParam("questionTypeId") Long questionTypeId) {
        logger.info("QuestionMasterController -> uploadExcelFileData() :: Method execution start successfully.");
        Map resultMap = new HashMap();
        try {
            logger.info("QuestionMasterController -> uploadExcelFileData() :: Method execution start successfully with  request data : " + file + " and organization id  " + organizationId + " , question type id is " + questionTypeId);
            resultMap.put("data", questionMasterService.uploadQuestions(file, organizationId, questionTypeId));
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in QuestionMasterController -> uploadExcelFileData() :: ", ex);
        }
        logger.info("QuestionMasterController -> uploadExcelFileData() :: Method execution completed successfully with response data :" + resultMap);
        return resultMap;
    }

    @ApiOperation(value = "excel save", response = QuestionMasterControllerPayload.class)
    @PostMapping(path = "/esave", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map excelSave(@RequestBody String data) {
        logger.info("QuestionMasterController -> uploadExcelFileData() :: Method execution start successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            resultMap.put("count", questionMasterService.excelSave(map));
            resultMap.put("status", "success");
        } catch (IOException ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in QuestionMasterController -> excelSave() :: ", ex);
        }
        logger.info("QuestionMasterController -> excelSave() :: Method execution completed successfully with response data :" + resultMap);
        return resultMap;
    }

    @ApiOperation(value = "Get question", response = QuestionMasterControllerPayload.class)
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map list(@RequestBody String data) {
        logger.info("QuestionMasterController -> uploadExcelFileData() :: Method execution start successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            resultMap.put("list", questionMasterService.questions(map));
            resultMap.put("status", "success");
        } catch (IOException ex) {
            resultMap.clear();
            resultMap.put("status", "error");
            logger.error("Problem in QuestionMasterController -> list() :: ", ex);
        }
        logger.info("QuestionMasterController -> list() :: Method execution completed successfully with response data :" + resultMap);
        return resultMap;
    }

    @ApiOperation(value = "Add", response = QuestionMasterControllerPayload.class)
    @PostMapping(path = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map add(@RequestBody String data) {
        logger.info("QuestionMasterController -> add() :: Method execution start successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("QuestionMasterController -> add() :: Method execution start successfully with request data : " + map);
            Map mpp = (Map) map.get("questionmasterdata");
            List<LinkedHashMap> list = (List<LinkedHashMap>) mpp.get("options_list");
            List<QuestionOptionMapping> options = new ArrayList<>();
            List<CodingQuestionTestCases> testCases = new ArrayList<>();
            QuestionMaster questionMaster = new QuestionMaster();
            questionMaster.setLanguage_id(Long.parseLong(mpp.get("language_id").toString()));
            questionMaster.setDifficulty_id(Long.parseLong(mpp.get("difficulty_id").toString()));
            questionMaster.setQuestion_type_id(Long.parseLong(mpp.get("question_type_id").toString()));
            questionMaster.setQuestion_desc((String) mpp.get("question_desc"));
            questionMaster.setActive(mpp.get("active").toString().charAt(0));
            questionMaster.setQuestionTime(mpp.get("questionTime").toString());
            questionMaster.setOrganizationId(Long.parseLong(mpp.get("organizationId").toString()));
            if (questionMaster.getQuestion_type_id() == 1) {
                questionMaster.setNo_of_answer(Integer.parseInt(mpp.get("no_of_answer").toString()));
                list.stream().map(qom -> {
                    QuestionOptionMapping questionOptionMapping = new QuestionOptionMapping();
                    questionOptionMapping.setOption_desc((String) qom.get("option_desc"));
                    questionOptionMapping.setIsActive(qom.get("isActive").toString().charAt(0));
                    return questionOptionMapping;
                }).map(questionOptionMapping -> {
                    questionOptionMapping.setQuestionMaster(questionMaster);
                    return questionOptionMapping;
                }).forEachOrdered(questionOptionMapping -> {
                    options.add(questionOptionMapping);
                });
                questionMaster.setOptions_list(options);
            } else if (questionMaster.getQuestion_type_id() == 2) {
                List<LinkedHashMap> testCaseList = (List<LinkedHashMap>) mpp.get("testCases");
                testCaseList.stream().map(qtm -> {
                    CodingQuestionTestCases queTestCase = new CodingQuestionTestCases();
                    queTestCase.setTestCaseName(qtm.get("testCaseName").toString());
                    queTestCase.setInput(qtm.get("input").toString());
                    queTestCase.setExpectedOutput(qtm.get("expectedOutput").toString());
                    queTestCase.setScore(Integer.parseInt(qtm.get("score").toString()));
                    return queTestCase;
                }).map(queTestCase -> {
                    queTestCase.setQuestionMaster(questionMaster);
                    return queTestCase;
                }).forEachOrdered(queTestCase -> {
                    testCases.add(queTestCase);
                });
                questionMaster.setCodingTemplate(mpp.get("codingTemplate").toString());
                questionMaster.setTestCases(testCases);
                questionMaster.setExpectedOutput((String) mpp.get("expectedOutput"));
            }
            if (!questionMasterService.isAlreadyExist(questionMaster)) {
                questionMasterService.saveQuestion(questionMaster);
                resultMap.put("status", "success");
            } else {
                resultMap.clear();
                resultMap.put("status", "error");
                resultMap.put("msg", "There is already a question with the same question desc in selected mandatory field.!");
                logger.info("QuestionMasterController -> add() :: Method execution completed successfully .There is already a question with the same question desc in selected mandatory field");
            }

        } catch (IOException | NumberFormatException ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in QuestionMasterController -> add() :: ", ex);
        }
        logger.info("QuestionMasterController -> add() :: Method execution completed successfully with response data :" + resultMap);

        return resultMap;

    }

    @ApiOperation(value = "Get by ID", response = QuestionMasterControllerPayload.class)
    @PostMapping(path = "/get", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map getById(@RequestBody String data) {
        logger.info("QuestionMasterController -> uploadExcelFileData() :: Method execution start successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            Long questionType = (map.containsKey("questionTypeId") && map.get("questionTypeId") != null) ? Long.parseLong(map.get("questionTypeId").toString()) : 0L;
            if (questionType == 2) {
                LinkedCaseInsensitiveMap codingQuestion = questionMasterService.getCodingQues(Long.parseLong(map.get("id").toString()));
                resultMap.put("data", codingQuestion);
            } else {
                LinkedCaseInsensitiveMap mcqQuestion = questionMasterService.getMcqQues(Long.parseLong(map.get("id").toString()));
                resultMap.put("data", mcqQuestion);
            }
            resultMap.put("status", "success");
        } catch (EntiryNotFoundException ex) {
            resultMap.clear();
            resultMap.put("status", "error");
            resultMap.put("msg", "Question not found !!");
            logger.error("Problem in QuestionMasterController -> getById() :: ", ex);
        } catch (IOException | NumberFormatException ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in QuestionMasterController -> getById() :: ", ex);
        }
        logger.info("QuestionMasterController -> getById() :: Method execution completed successfully with response data :" + resultMap);

        return resultMap;
    }

    @ApiOperation(value = "Delete question", response = QuestionMasterControllerPayload.class)
    @PostMapping(path = "/delete", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map delete(@RequestBody String data) {
        logger.info("QuestionMasterController -> delete() :: Method execution start successfully.");
        Map resultMap = new HashMap();
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            String msg = questionMasterService.delete(Long.parseLong(map.get("id").toString()));
            if (msg.equalsIgnoreCase("success")) {
                resultMap.put("status", "success");
            } else {
                resultMap.clear();
                resultMap.put("status", "error");
                resultMap.put("msg", msg);
            }
        } catch (EntiryNotFoundException ex) {
            resultMap.clear();
            resultMap.put("status", "error");
            resultMap.put("msg", "Question not found !!");
            logger.error("Problem in QuestionMasterController -> delete() :: ", ex);
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in QuestionMasterController -> delete() :: ", ex);
        }
        logger.info("QuestionMasterController -> delete() :: Method execution completed successfully with response data :" + resultMap);

        return resultMap;
    }

    @ApiOperation(value = "update question", response = QuestionMasterControllerPayload.class)
    @PostMapping(path = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map update(@RequestBody String data) {
        logger.info("QuestionMasterController -> update() :: Method execution start successfully.");
        Map resultMap = new HashMap();
        try {
            Map mapp = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            Map mpp = (Map) mapp.get("questionmasterdata");
            logger.info("QuestionMasterController -> update() :: Method execution start successfully with request data :" + mapp);
            QuestionMaster qm = mapper.convertValue(mpp, QuestionMaster.class);
            qm.setQuestion_id(Long.parseLong(mapp.get("id").toString()));
            if (qm.getQuestion_type_id() == 2) {
                qm.setNo_of_answer(null);
                List<CodingQuestionTestCases> testCases = qm.getTestCases();
                testCases.stream().forEach(test -> {
                    test.setQuestionMaster(qm);
                });
                testCaseRepo.saveAll(testCases);
            }
            if (!questionMasterService.isAlreadyExist(qm)) {
                questionMasterService.update(Long.parseLong(mapp.get("id").toString()), qm);
                resultMap.put("status", "success");
            } else {
                resultMap.clear();
                resultMap.put("status", "error");
                resultMap.put("msg", "There is already a question with the same question Desc.!");
                logger.info("QuestionMasterController -> update() :: There is already a question with the same question Desc.!");

            }
        } catch (EntiryNotFoundException ex) {
            resultMap.clear();
            resultMap.put("status", "error");
            resultMap.put("msg", "Role not found !!");
            logger.info("QuestionMasterController -> update() :: There is already a question with the same question Desc.!");
            logger.error("Problem in QuestionMasterController -> update() :: There is already a question with the same question Desc.! ", ex);
        } catch (Exception ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("Problem in QuestionMasterController -> update() :: ", ex);
        }
        logger.info("QuestionMasterController -> update() :: Method execution completed successfully with response data :" + resultMap);

        return resultMap;
    }

}
