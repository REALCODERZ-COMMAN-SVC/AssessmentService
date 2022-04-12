/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.BatchMaster;
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
public interface BatchMasterRepository extends JpaRepository<BatchMaster, Long> {

    @Query("select bm.batch_name as batch_name ,bm.batch_id as batch_id from BatchMaster bm JOIN bm.associates um where um.user_id=:id order by batch_start_date desc")
    public List<LinkedCaseInsensitiveMap> getBatchForAssociates(@Param("id") Long id);

}
