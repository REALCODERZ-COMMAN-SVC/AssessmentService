/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.realcoderz.assessmentservice.auditable.Auditable;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Prateek,Aman Bansal
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "question_id")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class QuestionMaster extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "question-sequence-generator")
    @GenericGenerator(
            name = "question-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "question_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long question_id;
    @NotNull(message = "Language Id can't be empty")
    private Long language_id;

    @NotNull(message = "Difficulty Id can't be empty")
    private Long difficulty_id;

    private Long question_type_id;
    @Column(columnDefinition = "text")
    private String question_desc;
    @Column(columnDefinition = "text")
    private String codingTemplate;
//    @NotNull(message = "No. Of Answer can't be empty")

    private Integer no_of_answer;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    @OrderBy(value = "option_id ASC")
    @EqualsAndHashCode.Exclude
    private List<QuestionOptionMapping> options_list;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    @OrderBy(value = "testCaseId ASC")
    @EqualsAndHashCode.Exclude
    private List<CodingQuestionTestCases> testCases;

    @ManyToMany(mappedBy = "question_list", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @EqualsAndHashCode.Exclude
    private Set<AssessmentCreation> assessmentCreation;

//    @ManyToMany(mappedBy = "question_list", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    private Set<RCAssessmentCreation> rcAssessmentCreation;
    @NotNull(message = "Active/Deactive can't be empty")
    private Character active;

    private String parameterForTestCases;

    @Column(columnDefinition = "text")
    private String expectedOutput;
    private String questionTime;
    private Long organizationId;
    @NotNull(message = "Topic Id can't be empty")
    private Long topic_id;

    public QuestionMaster(Long question_id, Long language_id, Long question_type_id, String question_desc, Integer no_of_answer, List<QuestionOptionMapping> options_list, Set<AssessmentCreation> assessmentCreation, Character active, String parameterForTestCases, String expectedOutput, List<CodingQuestionTestCases> testCases, String questionTime) {
        this.question_id = question_id;
        this.language_id = language_id;
        this.question_type_id = question_type_id;
        this.question_desc = question_desc;
        this.no_of_answer = no_of_answer;
        this.options_list = options_list;
        this.assessmentCreation = assessmentCreation;
        this.active = active;
        this.expectedOutput = expectedOutput;
        this.testCases = testCases;
        this.questionTime = questionTime;
    }

}
