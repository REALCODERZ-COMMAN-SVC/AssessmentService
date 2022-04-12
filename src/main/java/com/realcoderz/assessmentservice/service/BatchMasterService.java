/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.service;

import com.realcoderz.assessmentservice.domain.BatchMaster;
import java.util.List;
import java.util.Map;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Prateek
 * @Editor Aashi Singhal
 * @Editor Arvind Verma
 */
public interface BatchMasterService {

    public List<LinkedCaseInsensitiveMap> getBatchForAssociates(Map map);
   
   
}
