/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.realcoderz.assessmentservice.auditable.Auditable;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author Bipul kr Singh
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AssessmentTextDetails extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "student_assessment_text-sequence-generator")
    @GenericGenerator(
            name = "student_assessment_text-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "student_assessment_text_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long textDetailsId;
    
    @NotNull(message = "Student Id can't be empty")
    private Long studentId;
    
    @NotNull(message = "Question id can't be empty")
    private Long questionId;
    
    @NotNull(message = "Assessment id can't be empty")
    private Long assessmentId;
    

//    @NotEmpty(message = "Source Code can't be empty")

    @Column(columnDefinition = "text")
    private String textAnswer;
   
    @Column
    private Long textScore;
    
    @Column
    private boolean scan;
    

    @Column
    private boolean assessmentCompleted;
    

    @CreationTimestamp
    private LocalDateTime createdTime;


}
