/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realcoderz.assessmentservice.repository.BatchMasterRepository;
import com.realcoderz.assessmentservice.service.BatchMasterService;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Prateek
 * @Editor Aashi Singhal
 */
@Service
public class BatchMasterServiceImpl implements BatchMasterService {

    static final Logger logger = LoggerFactory.getLogger(BatchMasterServiceImpl.class);

    @Autowired
    private BatchMasterRepository batchMasterRepository;
    
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<LinkedCaseInsensitiveMap> getBatchForAssociates(Map map) {
        return batchMasterRepository.getBatchForAssociates(Long.parseLong(map.get("id").toString()));
    }

    
}
