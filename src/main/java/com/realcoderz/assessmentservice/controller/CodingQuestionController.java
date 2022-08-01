/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.realcoderz.assessmentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realcoderz.assessmentservice.payload.SwaggerController.StudentAssessmentControllerPayload;
import com.realcoderz.assessmentservice.service.AssessmentCreationService;
import com.realcoderz.assessmentservice.util.EncryptDecryptUtils;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Arvind verma
 */
@CrossOrigin("*")
@RestController
@RequestMapping(path = "/coding")
public class CodingQuestionController {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AssessmentCreationService service;
    static final org.slf4j.Logger logger = LoggerFactory.getLogger(CodingQuestionController.class);

    @PostMapping(path = "/get")
    @ApiOperation(value = "Get coding question details", response = StudentAssessmentControllerPayload.class)
    public Map getCodingQuestion(@RequestBody String data) {
        logger.info("CodingQuestionController ->  getCodingQuestion() :: ");
        return service.getCodingQuestion(data);
    }

    @PostMapping(path = "/save")
    @ApiOperation(value = "Save assessment coding details", response = StudentAssessmentControllerPayload.class)
    public Map saveAssessmentCodingDetails(@RequestBody String data) {
        logger.info("CodingQuestionController ->  saveAssessmentCodingDetails()::  ");
        return service.saveAssessmentCodingDetails(data);
    }

    @PostMapping(path = "/getCodingDetails")
    @ApiOperation(value = "Get student coding details ", response = StudentAssessmentControllerPayload.class)
    public Map getCodingDetailsBasedOnAssId(@RequestBody String data) {
        logger.info("CodingQuestionController ->  getCodingDetailsBasedOnAssId() :: ");
        return service.getCodingDetailsBasedOnAssId(data);

    }

    @PostMapping(path = "/saveAndGetScore")
    public Map saveAndGetCodingScore(@RequestBody Map map) {
        Map resultSet = new HashMap();
        try {
            logger.info("CodingQuestionController ->  saveAndGetCodingScore() :: ");
//            Map<String, Object> map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            resultSet = service.saveAndGetCodingScore(map);
        } catch (Exception ex) {
            resultSet.put("status", "exception");
            logger.error(" Exception in CodingQuestionController ->  saveAndGetCodingScore() :: " + ex);
        }
        return resultSet;
    }

    @PostMapping(path = "/getByLangId")
    public Map codingQuestionByLanguageId(@RequestBody String data) {
        Map resultSet = new HashMap();
        try {
            logger.info("CodingQuestionController ->  codingQuestionByLanguageId() :: ");
            Map<String, Object> map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            resultSet = service.codingQuestionByLanguageId(map);
        } catch (Exception ex) {
            resultSet.put("status", "exception");
            logger.error(" Exception in CodingQuestionController ->  saveAndGetCodingScore() :: " + ex);
        }
        return resultSet;
    }
}
