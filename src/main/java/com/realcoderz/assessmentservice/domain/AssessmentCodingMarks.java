/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.realcoderz.assessmentservice.auditable.Auditable;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author Bipul kr Singh
 */
@Entity
@Getter
@Setter
public class AssessmentCodingMarks extends Auditable<String> implements Serializable{
    
    @Id
    @GeneratedValue(generator = "coding_marks-sequence-generator")
    @GenericGenerator(
            name = "coding_marks-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "coding_marks_sequence")
                ,
                @Parameter(name = "initial_value", value = "1")
                ,
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long marksId;
    @NotNull(message = "Marks Level can't be empty")
    private String marksLevel;
    
    @NotNull(message = "Compile Marks can't be empty")
    private int compileMarks;
    
    @NotNull(message = "Run Marks can't be empty")
    private int runMarks;
    @NotNull(message = "Test Case1 Marks can't be empty")
    private int testCase1Marks;
    
    @NotNull(message = "Test Case2 Marks can't be empty")
    private int testCase2Marks;
    
    @NotNull(message = "Test Case3 Marks can't be empty")
    private int testCase3Marks;
    
    @NotNull(message = "Critical Issue Marks can't be empty")
    private int criticalIssuesMarks;
    
    @NotNull(message = "Major Issue Marks can't be empty")
    private int majorIssuesMarks;
    
    @NotNull(message = "Minor Issue Marks can't be empty")
    private int minorIssuesMarks;
    
    @NotNull(message = "Critical Issues be empty")
    private int criticalIssues;
    
    @NotNull(message = "Major Issues be empty")
    private int majorIssues;
    
    @NotNull(message = "Minor Issues can't be empty")
    private int minorIssues;
    
    @NotNull(message = "Total Marks can't be empty")
    private int totalMarks;
}
