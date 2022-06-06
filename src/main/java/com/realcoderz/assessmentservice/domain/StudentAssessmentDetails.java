/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.realcoderz.assessmentservice.auditable.Auditable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StudentAssessmentDetails extends Auditable<String> implements Serializable {
    
    @Id
    @GeneratedValue(generator = "student_assessment_details_generator")
    @GenericGenerator(
            name = "student_assessment_details_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "student_assessment_details_sequence")
                ,
                @Parameter(name = "initial_value", value = "1")
                ,
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long student_assessment_details_id;
    @NotNull(message = "Question id can't be null")
    private Long question_id;
    private String answer;
    @ManyToOne
    private StudentAssessment studentAssessment;

    public StudentAssessmentDetails(Long student_assessment_details_id, Long question_id, String answer, StudentAssessment studentAssessment) {
        this.student_assessment_details_id = student_assessment_details_id;
        this.question_id = question_id;
        this.answer = answer;
        this.studentAssessment = studentAssessment;
    }

}