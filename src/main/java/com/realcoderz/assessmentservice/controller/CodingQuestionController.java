/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.realcoderz.assessmentservice.controller;

import com.realcoderz.assessmentservice.payload.SwaggerController.StudentAssessmentControllerPayload;
import com.realcoderz.assessmentservice.service.AssessmentCreationService;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

}
