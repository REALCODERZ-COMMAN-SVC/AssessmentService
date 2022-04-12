/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author Aman Bansal, Bipul Kr Singh, Shubham Mishra
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "student_feedback_id", scope = StudentInterviewFeedBack.class)
@Getter
@Setter
@NoArgsConstructor
public class StudentInterviewFeedBack extends Auditable<String> implements Serializable {

  
    @Id
    @GeneratedValue(generator = "studentinterviewfeedback_generator")
    @GenericGenerator(
            name = "studentinterviewfeedback_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "studentinterviewfeedback_sequence"),
                @Parameter(name = "initial_value", value = "2"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long student_feedback_id;
    private String profile;
    private String interviewer_name;
    private String interviewer_name2;
    private String virtual_interview;
    private String virtual_interview2;
    private String presentation_skills;
    private String presentation_skills2;
    private String coding_skills;
    private String coding_skills2;
    private String java_score;
    private String java_score2;
    private String oops_score;
    private String oops_score2;
    private String feedback1;
    private String feedback2;
    private String status;
    private String ftf_interview;
    private String ftf_interview2;
    private String scholarship;
    private Long student_id;
    private String stage1;
    private String stage2;
    private String feedback;
    @Column(columnDefinition = "text")
    private String comment;
    private Long progress_percentage;
    private Long organizationId;
    private Long job_portal_id;

    public StudentInterviewFeedBack(Long student_feedback_id, String profile, String interviewer_name, String interviewer_name2, String virtual_interview, String virtual_interview2, String presentation_skills, String presentation_skills2, String coding_skills, String coding_skills2, String java_score, String java_score2, String oops_score, String oops_score2, String feedback1, String feedback2, String status, String ftf_interview, String ftf_interview2, String scholarship, Long student_id, String stage1, String stage2, String feedback, String comment, Long progress_percentage, Long organizationId, Long job_portal_id) {
        this.student_feedback_id = student_feedback_id;
        this.profile = profile;
        this.interviewer_name = interviewer_name;
        this.interviewer_name2 = interviewer_name2;
        this.virtual_interview = virtual_interview;
        this.virtual_interview2 = virtual_interview2;
        this.presentation_skills = presentation_skills;
        this.presentation_skills2 = presentation_skills2;
        this.coding_skills = coding_skills;
        this.coding_skills2 = coding_skills2;
        this.java_score = java_score;
        this.java_score2 = java_score2;
        this.oops_score = oops_score;
        this.oops_score2 = oops_score2;
        this.feedback1 = feedback1;
        this.feedback2 = feedback2;
        this.status = status;
        this.ftf_interview = ftf_interview;
        this.ftf_interview2 = ftf_interview2;
        this.scholarship = scholarship;
        this.student_id = student_id;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.feedback = feedback;
        this.comment = comment;
        this.progress_percentage = progress_percentage;
        this.organizationId = organizationId;
        this.job_portal_id = job_portal_id;
    }


}
