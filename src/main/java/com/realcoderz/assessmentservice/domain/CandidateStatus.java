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
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
@EqualsAndHashCode
public class CandidateStatus extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "candidatestatus-sequence-generator")
    @GenericGenerator(
            name = "candidatestatus-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "candidatestatus_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    Long id;
    Long studentId;
    String status;
    String name;
    String email;
    String organizationName;
    Long organizationId;
    String jobPortalName;
    Long jobPortalId;
}
