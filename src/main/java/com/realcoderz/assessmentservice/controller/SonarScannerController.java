/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realcoderz.assessmentservice.service.StudentAssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Bipul kr Singh
 */
@RestController
@RequestMapping(path = "/sonar")
public class SonarScannerController {
    private static final Logger logger = LoggerFactory.getLogger(SonarScannerController.class);

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private StudentAssessmentService studentAssessmentService;
    
     @PostMapping(path = "/scan")
     public void sonarScannerInterval() {
         try{
             studentAssessmentService.sonarScannerInterval();
         }catch(Exception ex){
             logger.error("Problem in SonarScannerController -> sonarScannerInterval() :: ", ex);
         }
     }
     
    
}
