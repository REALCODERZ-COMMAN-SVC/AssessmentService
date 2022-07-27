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
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author bipulsingh
 */
@Repository
public interface CodingQuestionTestCaseRepository extends JpaRepository<CodingQuestionTestCases, Long> {

    @Query("SELECT testCases from CodingQuestionTestCases as testCases where testCases.questionMaster.question_id = :question_id")
    public List<CodingQuestionTestCases> findByQuestionMaster(@Param("question_id") Long question_id);

    @Query(nativeQuery = true, value = "select qm.question_id,ac.codingmarks_id as marks_id, qm.question_desc from question_master qm, assessment_creation ac where qm.question_type_id=2  and ac.assessment_id=:student_assessment_id and ac.language_id=qm.language_id and qm.organization_id=:organization_id")
    public List<LinkedCaseInsensitiveMap> getCodingQuestion(@Param("student_assessment_id") Long student_assessment_id, @Param("organization_id") Long organization_id);

    @Query("SELECT testCases from CodingQuestionTestCases as testCases where testCases.questionMaster.question_id = :question_id")
    public List<CodingQuestionTestCases> findByQuestionId(@Param("question_id") Long question_id);
}
