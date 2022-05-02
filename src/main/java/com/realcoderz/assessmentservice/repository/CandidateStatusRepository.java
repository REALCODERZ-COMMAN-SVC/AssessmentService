/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.CandidateStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Aman Bansal
 */
@Repository
public interface CandidateStatusRepository extends JpaRepository<CandidateStatus, Long> {

    @Query("SELECT count(*) FROM CandidateStatus Where lower(email)=lower(:email) and (organizationId=:organizationId) and (jobPortalId=:jobPortalId) ")
    public int isCandidateExistWithJobPortal(@Param("email") String email, @Param("organizationId") Long organizationId, @Param("jobPortalId") Long jobPortalId);

    @Query("SELECT count(*) FROM CandidateStatus Where lower(email)=lower(:email) and organizationId=:organizationId")
    public int isStudentExist(@Param("email") String email, @Param("organizationId") Long organizationId);

    @Query(nativeQuery = true, value = "Select cm.id as id, cm.name as name,cm.job_portal_id as job_portal_id,cm.email as email,cm.organization_name as organization_name,cm.organization_id as organization_id from candidate_status cm Where cm.organization_id =:organizationId and cm.email=:email order by cm.id desc")
    public List<LinkedCaseInsensitiveMap> findByEmailAndOrganizationId(@Param("email") String email, @Param("organizationId") Long organizationId);

    @Query("From CandidateStatus Where lower(email)=lower(:email) and (organizationId=:organizationId) and (jobPortalId=:jobPortalId)")
    public CandidateStatus findByEmailAndOrganizationIdAndJobId(@Param("email") String email, @Param("organizationId") Long organizationId, @Param("jobPortalId") Long jobPortalId);

}
