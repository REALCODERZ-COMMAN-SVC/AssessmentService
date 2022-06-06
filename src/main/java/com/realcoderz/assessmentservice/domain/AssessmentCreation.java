/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.realcoderz.assessmentservice.auditable.Auditable;

/**
 *
 * @author Prateek
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "assessment_id")
public class AssessmentCreation extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "assessment-sequence-generator")
    @GenericGenerator(
            name = "assessment-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "assessment_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long assessment_id;

    @NotEmpty(message = "Assessment Desc can't be empty")
    private String assessment_desc;

//    @NotNull(message = "Event Id can't be empty")
    private Long event_id;

//    @Column(columnDefinition = "text")
//    @NotNull(message = "Instructions can't be empty")
    private String instructions;

    @NotNull(message = "Coding Marks Id can't be empty")
    private Long codingmarks_id;

//    @NotNull(message = "Language Id can't be empty")
    private Long language_id;

    @NotNull(message = "Difficulty Id can't be empty")
    private Long difficulty_id;

    @NotNull(message = "Time can't be empty")
    private Integer time;

//    @NotNull(message = "Creation Type can't be empty")
    private String creation_type;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "assessment_question", joinColumns = @JoinColumn(name = "assessment_id"), inverseJoinColumns = @JoinColumn(name = "question_id"))
    @OrderBy(value = "question_id ASC")
    private Set<QuestionMaster> question_list;

    private String assessmentTimeBound;

    @NotNull(message = "Active/Deactive can't be empty")
    private Character active;
    private Long organizationId;

    public AssessmentCreation(Long assessment_id, String assessment_desc, Long event_id, String instructions, Long language_id, Long difficulty_id, Integer time, Set<QuestionMaster> question_list, Character active, String assessmentTimeBound) {
        this.assessment_id = assessment_id;
        this.assessment_desc = assessment_desc;
        this.event_id = event_id;
        this.instructions = instructions;
        this.language_id = language_id;
        this.difficulty_id = difficulty_id;
        this.time = time;
        this.question_list = question_list;
        this.active = active;
        this.assessmentTimeBound = assessmentTimeBound;
    }

    public void removeQuestion(QuestionMaster qm) {
        this.question_list.remove(qm);
        qm.getAssessmentCreation().remove(this);
    }

    @Override
    public String toString() {
        return "AssessmentCreation{" + "assessment_id=" + assessment_id + ", assessment_desc=" + assessment_desc + ", event_id=" + event_id + ", instructions=" + instructions + ", language_id=" + language_id + ", difficulty_id=" + difficulty_id + ", time=" + time + ", question_list=" + question_list + ", active=" + active + '}';
    }
}
