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
public class UserAssessmentDetails extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "user_assessment_details_generator")
    @GenericGenerator(
            name = "user_assessment_details_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "user_assessment_details_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long user_assessment_details_id;
//    @NotNull(message = "Question id can't be blank")
    private Long question_id;
    private String answer;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    private UserAssessment userAssessment;

}
