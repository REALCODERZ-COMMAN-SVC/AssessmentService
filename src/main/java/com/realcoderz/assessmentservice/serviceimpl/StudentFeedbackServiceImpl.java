/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.realcoderz.assessmentservice.domain.StudentInterviewFeedBack;
import com.realcoderz.assessmentservice.repository.StudentInterviewFeedbackRepository;
import com.realcoderz.assessmentservice.service.StudentFeedbackService;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Aman Bansal
 */
@Service
public class StudentFeedbackServiceImpl implements StudentFeedbackService {

    @Autowired
    private StudentInterviewFeedbackRepository stdntFdbckrepo;

    @Override
    public void saveStdntFeedback(Map map) {

        if (map.containsKey("user_id") && map.get("user_id") != null) {
            StudentInterviewFeedBack stdntFdbck = new StudentInterviewFeedBack();
            stdntFdbck.setStatus("Assessment Completed");
            stdntFdbck.setScholarship("In Process");
            Long no_of_round = stdntFdbckrepo.getInterviewRounds(Long.parseLong(map.get("user_id").toString()));
            if (no_of_round == Long.parseLong("1")) {
                stdntFdbck.setProgress_percentage(Long.parseLong("50"));
            } else if (no_of_round == Long.parseLong("3")) {
                stdntFdbck.setProgress_percentage(Long.parseLong("20"));
            } else {
                stdntFdbck.setProgress_percentage(Long.parseLong("25"));

            }
            stdntFdbck.setStudent_id(Long.parseLong(map.get("user_id").toString()));
            stdntFdbck.setScholarship("In Process");
            stdntFdbck.setCreatedDate(new Date());
            stdntFdbck.setCreatedBy(map.get("user_id").toString());
            stdntFdbck.setLastModifiedBy(map.get("user_id").toString());
            stdntFdbck.setLastModifiedDate(new Date());
            stdntFdbck.setJob_portal_id(Long.parseLong(map.get("jobPortalId").toString()));
            if (map.containsKey("organization_name") && map.get("organization_name") != null) {
                Long organizationId = stdntFdbckrepo.findOrganizationIdByName(map.get("organization_name").toString());
                if (organizationId != null) {
                    stdntFdbck.setOrganizationId(organizationId);
                }
            }
            stdntFdbckrepo.save(stdntFdbck);
        }
    }

    @Override
    public void saveFeedback(Map map) {
        StudentInterviewFeedBack feedback = stdntFdbckrepo.findByStudentId(Long.parseLong(map.get("user_id").toString()));
        if (feedback != null) {
            feedback.setFeedback(map.get("feedbck").toString());
            feedback.setComment(map.get("comment").toString());
            stdntFdbckrepo.save(feedback);
        }
    }
    //checking status that feedback is already submitted in db or not

    @Override
    public LinkedCaseInsensitiveMap getFeedbackByStudentId(Map map) {
        LinkedCaseInsensitiveMap resMap = new LinkedCaseInsensitiveMap();
        if (map.containsKey("user_id") && map.get("user_id") != null) {
            StudentInterviewFeedBack feedback = stdntFdbckrepo.findByStudentId(Long.parseLong(map.get("user_id").toString()));
            if (feedback.getComment().length() != 0 && feedback.getFeedback().length() != 0) {
                resMap.put("status", "success");
            } else {
                resMap.put("status", "error");
            }
        }

        return resMap;
    }
}
