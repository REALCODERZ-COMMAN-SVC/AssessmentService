/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.service;

import java.util.Map;
import org.springframework.util.LinkedCaseInsensitiveMap;


/**
 *
 * @author Aman Bansal
 */
public interface StudentAssessmentService {

    public Map getTopicScores(LinkedCaseInsensitiveMap assess);


}
