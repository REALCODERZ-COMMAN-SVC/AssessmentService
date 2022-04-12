/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.service;

import com.realcoderz.assessmentservice.domain.QuestionMaster;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Prateek
 */
 public interface QuestionMasterService {
     
     int a =10;
    
    public void saveQuestion(QuestionMaster questionMaster);
    
    public List<LinkedCaseInsensitiveMap> questions(Map map);
        
    public Optional<QuestionMaster> findById(Long id);
    
    public String delete(Long id);
    
    public void update(Long id, QuestionMaster updatedQuestion);
    
    public Boolean isAlreadyExist(QuestionMaster question);
        
    public Map uploadQuestions(MultipartFile file,Long organizationId,Long questionTypeId);
    
    public Integer excelSave(Map mp);
    
    public LinkedCaseInsensitiveMap getCodingQues(long id);

    public LinkedCaseInsensitiveMap getMcqQues(long id);

}
