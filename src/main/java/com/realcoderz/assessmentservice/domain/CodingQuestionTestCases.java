/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author bipulsingh
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CodingQuestionTestCases implements Serializable{
       @Id
    @GeneratedValue(generator = "coding_question_test_case-sequence-generator")
    @GenericGenerator(
            name = "coding_question_test_case-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "coding_question_test_case_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long testCaseId;
    private String testCaseName;
    private String input;
    private String expectedOutput;
    private Integer score;
    @ManyToOne
    private QuestionMaster questionMaster;
    
}
