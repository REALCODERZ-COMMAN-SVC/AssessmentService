/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.StudentInterviewFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author admin
 */
@Repository
public interface StudentInterviewFeedbackRepository extends JpaRepository<StudentInterviewFeedBack, Long> {

 
}
