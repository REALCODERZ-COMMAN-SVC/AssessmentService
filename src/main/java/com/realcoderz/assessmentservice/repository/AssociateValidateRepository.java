/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.AssociateValidate;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Bipul kr Singh
 */
@Repository
public interface AssociateValidateRepository extends JpaRepository<AssociateValidate, Long> {

    @Query("from AssociateValidate where associateId=:associateId and assessmentId=:assessmentId")
    public List<AssociateValidate> findByAssociateIdAndAssessmentId(@Param("associateId") Long associateId, @Param("assessmentId") Long assessmentId);

    @Transactional
    @Modifying
    @Query("UPDATE AssociateValidate set assessmentSubmit=true where associateId=:associateId and assessmentId=:assessmentId ")
    public void updateAssessmentSubmit(@Param("associateId") Long associateId, @Param("assessmentId") Long assessmentId);

    @Query("select count(*) as associate_assessments,"
            + "(select count(student_assessment_id) from StudentAssessment where assessment_id in (select assessment_id from AssessmentCreation where organizationId =:orgId)) as applicant_assessment"
            + " from UserAssessment where organization_id=:orgId")
    public Map getCompletedAssessments(@Param("orgId") Long orgId);

}
