/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author Aman Bansal
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class StudentAssessment extends Auditable<String> implements Serializable {

    
    @Id
    @GeneratedValue(generator = "student_assessment_generator")
    @GenericGenerator(
            name = "student_assessment_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "student_assessment_sequence")
                ,
                @Parameter(name = "initial_value", value = "1")
                ,
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long student_assessment_id;
    @NotNull(message = "Student id can't be null")
    private Long student_id;
    @NotNull(message = "Assessment id can't be null")
    private Long assessment_id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endTime;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_assessment_id")
    @OrderBy(value = "student_assessment_details_id ASC")
    private Set<StudentAssessmentDetails> detail_list;
    private Integer correct_questions;
//    @NotNull(message = "Total no of questions can't be null")
    private Integer total_no_of_questions;
    private Double ai_score;
    private String remarks;
    private Integer totalCodingQuestion;
    private Integer totalCodingScore;
    private double codingPercentage;
    private double mcqPercentage;
//    @NotNull(message = "Total percentage can't be null")
    private double totalPercentage;
    private Long jobPortalId;
    private boolean scan;
    
    public StudentAssessment(Long student_assessment_id, Long student_id, Long assessment_id, Date startTime, Date endTime, Set<StudentAssessmentDetails> detail_list, Integer correct_questions, Integer total_no_of_questions, String remarks, Long jobPortalId) {
        this.student_assessment_id = student_assessment_id;
        this.student_id = student_id;
        this.assessment_id = assessment_id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.detail_list = detail_list;
        this.correct_questions = correct_questions;
        this.total_no_of_questions = total_no_of_questions;
        this.remarks = remarks;
        this.jobPortalId = jobPortalId;
    }    

}
