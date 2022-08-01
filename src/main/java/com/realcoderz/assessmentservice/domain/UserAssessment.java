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
import com.realcoderz.assessmentservice.auditable.Auditable;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Vineet
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserAssessment extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "user_assessment_generator")
    @GenericGenerator(
            name = "user_assessment_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "user_assessment_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long user_assessment_id;
    @NotNull(message = "User id can't be empty")
    private Long user_id;

    @NotNull(message = "Assessment id can't be empty")
    private Long assessment_id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_assessment_id")
    @OrderBy(value = "user_assessment_details_id ASC")
    @EqualsAndHashCode.Exclude
    private Set<UserAssessmentDetails> detail_list;

//    @NotNull(message = "Correct Questions can't be empty")
    private Integer correct_questions;

//    @NotNull(message = "Total Questions can't be empty")
    private Integer total_no_of_questions;

    private String remarks;

    private Integer totalCodingQuestion;

    private Integer totalCodingScore;

    private double codingPercentage;

    private double mcqPercentage;

    @NotNull(message = "Total Percentage can't be null")
    private double totalPercentage;

    private boolean scan;

    private String assessmentName;

}
