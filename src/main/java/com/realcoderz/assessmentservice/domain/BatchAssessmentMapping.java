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
public class BatchAssessmentMapping extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "batch_assessment_mapping_generator")
    @GenericGenerator(
            name = "batch_assessment_mapping_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "batch_assessment_mapping_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    private Long batch_id;
    private Long assessment_id;
}
