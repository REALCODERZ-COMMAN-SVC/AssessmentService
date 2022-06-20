/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
public class AssociateValidate implements Serializable {

    @Id
    @GeneratedValue(generator = "assovalidate-sequence-generator")
    @GenericGenerator(
            name = "assovalidate-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "assovalidate_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    @NotNull(message = "Associate Id can't be empty")
    private Long associateId;
    
    @NotNull(message = "Assessment Id can't be empty")
    private Long assessmentId;
    private Long createdBy;
    @CreationTimestamp
    private LocalDateTime createdTime;
    private Boolean assessmentSubmit;

    public AssociateValidate(Long associateId, Long assessmentId, Long createdBy, Boolean assessmentSubmit) {
        this.associateId = associateId;
        this.assessmentId = assessmentId;
        this.createdBy = createdBy;
        this.assessmentSubmit = assessmentSubmit;
    }
    
    public AssociateValidate(Long associateId, Long assessmentId, Long createdBy) {
        this.associateId = associateId;
        this.assessmentId = assessmentId;
        this.createdBy = createdBy;
    }

}
