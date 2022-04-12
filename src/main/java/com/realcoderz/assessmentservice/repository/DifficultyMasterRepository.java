/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.DifficultyMaster;
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
public interface DifficultyMasterRepository extends JpaRepository<DifficultyMaster, Long> {

    @Query("SELECT count(*) from DifficultyMaster where lower(difficulty_name)=lower(:difficulty_name) AND organizationId=:organizationId")
    public Integer difficultyHavingSameName(@Param("difficulty_name") String difficultyName, @Param("organizationId") Long organizationId);

    @Query("SELECT count(*) from DifficultyMaster where lower(difficulty_name)=lower(:difficulty_name) AND organizationId=:organizationId AND difficulty_id NOT IN (:difficulty_id)")
    public Integer checkLevelHavingSameNameWithLevelId(@Param("difficulty_name") String difficulty_name, @Param("difficulty_id") Long difficulty_id, @Param("organizationId") Long organizationId);

    @Query("SELECT dm.difficulty_name as name ,dm.difficulty_id as value from DifficultyMaster dm where dm.organizationId=:organizationId AND dm.active = 'Y'")
    public List<LinkedCaseInsensitiveMap> getDifficultyForDropDown(@Param("organizationId") Long organizationId);

    public List<DifficultyMaster> findByOrganizationId(@Param("organizationId") Long organizationId);
}
