/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realcoderz.assessmentservice.payload.SwaggerController.CompilerControllerPayload;
import com.realcoderz.assessmentservice.service.CompilerService;
import com.realcoderz.assessmentservice.util.EncryptDecryptUtils;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Bipul Kr Singh
 *
 */
@CrossOrigin("*")
@RestController
@RequestMapping(path = "/cmp")
public class CompilerController {

    static final Logger logger = LoggerFactory.getLogger(CompilerController.class);

    @Autowired
    CompilerService compilerService;

    ObjectMapper mapper = new ObjectMapper();

    @ApiOperation(value = "save source code", response = CompilerControllerPayload.class)
    @PostMapping(path = "/crt", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public Map<String, Object> saveSourceCode(@RequestBody String data) {
        logger.info("CompilerController--> saveSourceCode() :: ");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> mp = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);
            logger.info("CompilerController--> saveSourceCode() :: request data : " + mp);
            resultMap = compilerService.saveSourceCode(mp);
        } catch (IOException ex) {
            resultMap.clear();
            resultMap.put("status", "exception");
            logger.error("CompilerController--> saveSourceCode() :: ", ex);

        }
        logger.info("CompilerController--> saveSourceCode() :: response data is " + resultMap);
        logger.info("CompilerController -> saveSourceCode() ::  Method execution completed");
        return resultMap;
    }

}
