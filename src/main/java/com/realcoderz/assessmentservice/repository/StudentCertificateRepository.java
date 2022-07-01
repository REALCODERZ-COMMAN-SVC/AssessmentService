/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.StudentCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author anwar
 */
@Repository
public interface StudentCertificateRepository extends JpaRepository<StudentCertification, Long> {

    @Query("FROM StudentCertification where studentId=:studentId and certificateLevel='level 1'")
    public StudentCertification findByStudentId(@Param("studentId") Long studentId);

}
