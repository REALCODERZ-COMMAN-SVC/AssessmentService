/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.realcoderz.assessmentservice.domain.AssociateTopicScores;
import com.realcoderz.assessmentservice.repository.AssociateTopicScoresRepo;
import com.realcoderz.assessmentservice.service.AssociateTopicScoresService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author anwar
 */
@Service
public class AssociateTopicScoresSerImpl implements AssociateTopicScoresService{

    @Autowired
    private AssociateTopicScoresRepo scoresRepo;
    
    @Override
    public void saveAll(List<AssociateTopicScores> scores) {
        scoresRepo.saveAll(scores);
    }
    
}
