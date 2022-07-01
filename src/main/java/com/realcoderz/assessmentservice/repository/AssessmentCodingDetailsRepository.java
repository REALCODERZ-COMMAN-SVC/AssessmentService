/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.AssessmentCodingDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Priti
 */
@Repository
public interface AssessmentCodingDetailsRepository extends JpaRepository<AssessmentCodingDetails, Long> {

    @Query("FROM AssessmentCodingDetails where user_id=:user_id and question_id=:question_id and scan=false order by createdTime desc")
    public List<AssessmentCodingDetails> findByUserQuestionId(@Param("user_id") Long user_id, @Param("question_id") Long question_id);

}
