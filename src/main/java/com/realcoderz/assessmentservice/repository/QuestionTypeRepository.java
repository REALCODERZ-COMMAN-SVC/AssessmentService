/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.QuestionType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Vineet
 */
@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType, Long> {

    @Query("SELECT question_type_name as name ,question_type_id as value FROM QuestionType ORDER BY name ")
    public List<LinkedCaseInsensitiveMap> getAllQuestionTypes();
}
