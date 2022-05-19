/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.TopicMaster;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Prateek
 */
@Repository
public interface TopicMasterRepository extends JpaRepository<TopicMaster, Long>{
    
 
    @Query("select tm.topic_name as name ,tm.topic_id as value from TopicMaster tm where tm.organizationId=:organizationId AND tm.active = 'Y' Order by name")
    public List<LinkedCaseInsensitiveMap> getTopicForDropDown(@Param("organizationId")Long organizationId);
    
    public List<TopicMaster> findByOrganizationId(@Param("organizationId")Long organizationId);
    

}
