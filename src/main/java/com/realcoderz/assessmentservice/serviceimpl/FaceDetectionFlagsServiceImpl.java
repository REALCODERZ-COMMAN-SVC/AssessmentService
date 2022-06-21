/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realcoderz.assessmentservice.domain.FaceDetectionFlags;
import com.realcoderz.assessmentservice.exceptions.EntiryNotFoundException;
import com.realcoderz.assessmentservice.repository.FaceDetectionFlagsRepository;
import com.realcoderz.assessmentservice.service.FaceDetectionFlagsService;
import com.realcoderz.assessmentservice.util.EncryptDecryptUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author rohini
 */
@Service
public class FaceDetectionFlagsServiceImpl implements FaceDetectionFlagsService {

    private static final Logger logger = LoggerFactory.getLogger(FaceDetectionFlagsServiceImpl.class);

    @Autowired
    private FaceDetectionFlagsRepository facedetectionflagsrepository;


    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, Object> addFaceDetectionFlags(Map map) 
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
        FaceDetectionFlags flags=new FaceDetectionFlags();
        flags.setCellphone_detection_count((Integer)map.get("cellphone_detection_count"));
        flags.setCellphone_detection_time((String)map.get("cellphone_detection_time"));
        flags.setMorethanoneuser_count((Integer)map.get("morethanoneuser_count"));
        flags.setMorethanoneuser_time((String)map.get("morethanoneuser_time"));
        flags.setUsermovement_count((Integer)map.get("usermovement_count"));
        flags.setUsermovement_time((String)map.get("usermovement_time"));
        flags.setEmail((String)map.get("email"));
        flags.setOrgId((Long.parseLong(map.get("organizationId").toString())));
        flags.setStudentId((Long.parseLong(map.get("studentId").toString())));
        FaceDetectionFlags findByStudentId=facedetectionflagsrepository.findByStudentId(Long.parseLong(map.get("studentId").toString()));
        if(findByStudentId != null && findByStudentId.getStudentId() == (Long.parseLong(map.get("studentId").toString())))
        {
        flags.setFacedetection_id(findByStudentId.getFacedetection_id());
        facedetectionflagsrepository.save(flags);
        }else{
        facedetectionflagsrepository.save(flags);
        }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            logger.error("Exception occurred !");
        }
          return  resultMap;

    }

    public int sumOfFaceDeductionTime(String s){
        int[] data = Stream.of(s.replace(",", " ").split("\\s+")).mapToInt(Integer::parseInt).toArray();
        List<Integer> list = Arrays.stream(data).boxed().collect(Collectors.toList());
        return list.stream().mapToInt(Integer::intValue).sum();
    }
    @Override
    public Map getFaceDetectionFlagsCountById(String data) 
    {
      Map resultMap = new HashMap<>();
      try{
      Map map = mapper.readValue(EncryptDecryptUtils.decrypt(data), LinkedCaseInsensitiveMap.class);      
      logger.info("FaceDetectionFlagsController :: getFaceDetectionFlagsCountById() => Request data "+map);
      Boolean jobProfileWebCamOnDetails= facedetectionflagsrepository.findWebcamOnByJobPortalId(Long.parseLong(map.get("jobPortalId").toString()));
      if(jobProfileWebCamOnDetails == false){
          resultMap.put("webcam", "N");
      }
      else
      {
      FaceDetectionFlags faceDetectionFlagDetails= facedetectionflagsrepository.findByStudentId(Long.parseLong(map.get("studentId").toString()));
      resultMap.put("webcam", "Y");
      if(faceDetectionFlagDetails!=null)
      {
          if(faceDetectionFlagDetails.getCellphone_detection_count()>0 && faceDetectionFlagDetails.getCellphone_detection_time().length()>0)
          {
              resultMap.put("Cellphone_detection_count_msg", "Yes");
              resultMap.put("Cellphone_detection_timecount_msg" ,this.sumOfFaceDeductionTime(faceDetectionFlagDetails.getCellphone_detection_time())+ " sec");
          }
          else{
          resultMap.put("Cellphone_detection_count_msg", "No");
          resultMap.put("Cellphone_detection_timecount_msg", "N/A");
          }
           if(faceDetectionFlagDetails.getMorethanoneuser_count()>0 && faceDetectionFlagDetails.getMorethanoneuser_time().length()>0)
          {
              resultMap.put("More_than_one_user_count_msg", "Yes");
              resultMap.put("More_than_one_user_timecount_msg",this.sumOfFaceDeductionTime(faceDetectionFlagDetails.getMorethanoneuser_time())+  " sec");
          }
           else
           {
              resultMap.put("More_than_one_user_count_msg", "No");
              resultMap.put("More_than_one_user_timecount_msg", "N/A");
           }
           if(faceDetectionFlagDetails.getUsermovement_count()>0 && faceDetectionFlagDetails.getUsermovement_time().length()>0) 
           {  
              resultMap.put("person_not_available_count_msg", "Yes");
              resultMap.put("person_not_available_timecount_msg",this.sumOfFaceDeductionTime(faceDetectionFlagDetails.getUsermovement_time())+  " sec");
           }
           else
           {
              resultMap.put("person_not_available_count_msg", "No");
              resultMap.put("person_not_available_timecount_msg", "N/A");
           }
          resultMap.put("status", "success");
      }
      else{
          throw new NullPointerException();
      }
      }
      }catch(IOException io){
          resultMap.put("status", "Input not valid");
      }
      catch(NullPointerException ex)
      {
          resultMap.clear();
          resultMap.put("status", "student Id can't be null");
      }catch(Exception e){
          resultMap.put("status", "exception");
          e.printStackTrace();
          resultMap.put("msg", "something went wrong");
      }
      logger.info("FaceDetectionFlagsController :: getFaceDetectionFlagsCountById() => Response data " +resultMap);
      return resultMap;
    }

}