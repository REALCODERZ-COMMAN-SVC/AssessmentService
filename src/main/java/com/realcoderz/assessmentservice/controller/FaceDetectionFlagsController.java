/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realcoderz.assessmentservice.payload.SwaggerController.FaceDetectionFlagsControllerPayload;
import com.realcoderz.assessmentservice.service.FaceDetectionFlagsService;
import com.realcoderz.assessmentservice.util.EncryptDecryptUtils;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rohini
 */
@RestController
@RequestMapping(path = "/facedetection")
@CrossOrigin("*")
public class FaceDetectionFlagsController {

    private static final Logger logger = LoggerFactory.getLogger(FaceDetectionFlagsController.class);

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private FaceDetectionFlagsService facedetectionflagsservice;

    @ApiOperation(value = "save face detection count", response = FaceDetectionFlagsControllerPayload.class)
    @PostMapping(value="/savefacedetectioncount", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public Map addfacedetectionflags(@RequestBody String data) {
        logger.info("FaceDetectionFlagsController :: addfacedetectionflags() => Method excution start successfully ");
        Map<String, Object> resultmap = new HashMap<>();
        try {
           Map mpp = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);   
           logger.info("FaceDetectionFlagsController :: addfacedetectionflags() => Request data " +mpp);
           facedetectionflagsservice.addFaceDetectionFlags(mpp);
           resultmap.put("status", "success");
           
        } catch (Exception ex) 
        {
            resultmap.put("status", "Exception");
            logger.info("problem in FaceDetectionFlagsController :: addfacedetectionflags() =>" + ex);
        }
        logger.info("FaceDetectionFlagsController :: addfacedetectionflags() => Response data " +resultmap);
        return resultmap;
    }
    
     @ApiOperation(value = "get face detection count", response = FaceDetectionFlagsControllerPayload.class)
     @PostMapping(value="/getfacedetectioncount", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
     public Map getfacedetectionflagsCount(@RequestBody String data){
         logger.info("FaceDetectionFlagsController :: getFaceDetectionFlagsCountById() => Method excution start successfully  ");
         return facedetectionflagsservice.getFaceDetectionFlagsCountById(data);  
         
    }

}