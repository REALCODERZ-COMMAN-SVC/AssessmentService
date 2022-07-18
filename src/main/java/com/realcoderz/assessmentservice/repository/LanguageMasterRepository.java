/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.LanguageMaster;
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
public interface LanguageMasterRepository extends JpaRepository<LanguageMaster, Long> {

    @Query("SELECT count(*) from LanguageMaster where lower(language_name)=lower(:language_name) AND organizationId=:organizationId")
    public Integer languageHavingSameName(@Param("language_name") String languageName, @Param("organizationId") Long organizationId);

    @Query("SELECT count(*) from LanguageMaster where lower(language_name)=lower(:language_name) AND organizationId=:organizationId AND language_id NOT IN (:language_id)")
    public Integer checkLanguageHavingSameNameWithLanguageId(@Param("language_name") String language_name, @Param("language_id") Long language_id, @Param("organizationId") Long organizationId);

    @Query("SELECT lm.language_name as name ,lm.language_id as value ,lm.technical as technical from LanguageMaster lm where lm.organizationId=:organizationId AND lm.active='Y' order by name")
    public List<LinkedCaseInsensitiveMap> getLanguagesForDropDown(@Param("organizationId") Long organizationId);

    @Query("SELECT lm.language_id as language_id,lm.language_name as language_name,lm.language_desc as language_desc,lm.active as active FROM LanguageMaster lm where lm.organizationId=:organizationId")
    public List<LinkedCaseInsensitiveMap> findAllLanguages(@Param("organizationId") Long organizationId);

    @Query(value = "SELECT lm.language_name from LanguageMaster lm where lm.language_id=:language_id")
    public String getLangName(@Param("language_id") Long language_id);

    @Query(nativeQuery = true, value = "select lm.language_id from language_master lm where lower(lm.language_name)=lower(:language_name) and (lm.organization_id=:organizationId)")
    public Long findByName(@Param("language_name") String language_name, @Param("organizationId") Long organizationId);

}
