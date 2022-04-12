/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.service;

import com.realcoderz.assessmentservice.domain.AssociateTopicScores;
import java.util.List;

/**
 *
 * @author anwar
 */
public interface AssociateTopicScoresService {

    public void saveAll(List<AssociateTopicScores> scores);
    
}
