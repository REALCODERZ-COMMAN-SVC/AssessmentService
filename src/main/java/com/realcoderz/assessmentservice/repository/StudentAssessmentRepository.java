/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.repository;

import com.realcoderz.assessmentservice.domain.StudentAssessment;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author admin
 */
@Repository
public interface StudentAssessmentRepository extends JpaRepository<StudentAssessment, Long> {

    @Query("Select ua.assessment_id as assessment_id ,ua.correct_questions as correct_questions from StudentAssessment ua where ua.student_id=:user_id and ua.assessment_id=:assessment_id")
    public List<LinkedCaseInsensitiveMap> getAssessmentDetailsByUserAssessmentId(@Param("user_id") Long user_id, @Param("assessment_id") Long assessment_id);

    @Query(nativeQuery = true, value = "select sad.answer as sanswer , sad.question_id as question_id ,qom.option_id  as danswer ,qm.question_desc as question_desc from student_assessment sa inner join student_assessment_details sad on sad.student_assessment_id=sa.student_assessment_id and sad.created_by=sa.created_by inner join question_option_mapping qom on qom.question_id=sad.question_id and qom.is_active='Y' inner join question_master qm on qm.question_id=sad.question_id  where sa.student_assessment_id= (select student_assessment_id from student_assessment where assessment_id=:assessment_id and student_id=:student_id order by created_date desc limit 1)")
    public Set<LinkedCaseInsensitiveMap> getAssessmentResultByUserAssessmentId(@Param("student_id") Long studentId, @Param("assessment_id") Long assessemntId);

    @Query("FROM StudentAssessment where student_id=?1")
    public List<StudentAssessment> getAssessmentByUserId(Long userId);

    @Query(nativeQuery = true, value = "select * from student_assessment where scan=false order by created_date limit 0,1")
    public StudentAssessment getListForSonarScanner();

    @Query(nativeQuery = true, value = "SELECT  assessment_id FROM student_assessment sa , student_master sm  where sa.student_id=sm.student_id and sa.student_id =:student_id  and sm.organization_id=:organization_id order by sa.student_assessment_id desc limit 1 ")
    public Long findByStudentIdAndOrg(@Param("student_id") Long student_id, @Param("organization_id") Long organization_id);

}
