/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.service;

import java.util.List;
import java.util.Map;

import com.realcoderz.assessmentservice.domain.UserAssessment;

/**
 *
 * @author Vineet
 */
public interface UserAssessmentService {

    public UserAssessment saveUserAssessment(UserAssessment userAssessment);

    public Map calculateResult(Long userId, Long assessemntId);

    public List<UserAssessment> getResultByUserId(Long userId);

    public void saveAnswerDetails(Map map);
}
