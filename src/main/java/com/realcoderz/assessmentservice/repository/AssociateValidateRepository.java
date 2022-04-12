/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.AssociateValidate;
import java.util.List;
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
public interface AssociateValidateRepository extends JpaRepository<AssociateValidate, Long>{

    @Query("from AssociateValidate where associateId=:associateId and assessmentId=:assessmentId")
    public List<AssociateValidate> findByAssociateIdAndAssessmentId(@Param("associateId") Long associateId,@Param("assessmentId") Long assessmentId);

    @Transactional
    @Modifying
    @Query("UPDATE AssociateValidate set assessmentSubmit=true where associateId=:associateId and assessmentId=:assessmentId ")
    public void updateAssessmentSubmit(@Param("associateId") Long associateId,@Param("assessmentId") Long assessmentId);
    
}
