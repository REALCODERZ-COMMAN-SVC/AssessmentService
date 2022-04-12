/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.CodingQuestionTestCases;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bipulsingh
 */
@Repository
public interface CodingQuestionTestCaseRepository extends JpaRepository<CodingQuestionTestCases, Long>{

    @Query("SELECT testCases from CodingQuestionTestCases as testCases where testCases.questionMaster.question_id = :question_id")
    public List<CodingQuestionTestCases> findByQuestionId(@Param("question_id")Long question_id);
}
