/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.Organization;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Aman Bansal
 */
@Repository
@Transactional
public interface OrganizationRepository extends JpaRepository<Organization,Long> {

    @Query("select organizationId from Organization where lower(organizationName)=lower(:orgName)")
    public Long findIdByOrgName(@Param("orgName") String orgName);
    
    @Query("SELECT om.testCode FROM Organization om where om.organizationId=:id")
    public String findTestCodeByOrgId(@Param("id") Long id);
}
