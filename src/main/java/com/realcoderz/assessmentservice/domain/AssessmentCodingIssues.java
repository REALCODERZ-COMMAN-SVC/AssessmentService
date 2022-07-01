/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.realcoderz.assessmentservice.auditable.Auditable;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
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
public class AssessmentCodingIssues extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "coding_issue-sequence-generator")
    @GenericGenerator(
            name = "coding_issue-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "coding_issue_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private long assessmentCodingIssue_id;
    @NotNull(message = "Issue Line can't be empty")
    private int issue_line_no;

    @NotEmpty(message = "Issue Line can't be empty")
    private String issue_type;

    @NotEmpty(message = "Issue Line can't be empty")
    @Column(columnDefinition = "text")
    private String issue_desc;

    @ManyToOne
    private AssessmentCodingDetails coding_details;
}
