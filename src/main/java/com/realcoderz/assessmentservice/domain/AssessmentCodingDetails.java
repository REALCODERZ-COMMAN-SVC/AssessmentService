package com.realcoderz.assessmentservice.domain;

import com.realcoderz.assessmentservice.auditable.Auditable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author Priti
 */
@Entity
@Getter
@Setter
public class AssessmentCodingDetails extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "assessment_coding-sequence-generator")
    @GenericGenerator(
            name = "assessment_coding-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "assessment_coding_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private long assessmentCodingDetails_id;
    private long user_id;
    private byte[] code_source;

    @NotNull(message = "Major Issues can't be null")
    private int major_issues;

    @NotNull(message = "Minor Issues  can't be null")
    private int minor_issues;

    @NotNull(message = "Critical Issues can't be null")
    private int critical_issues;

    private int testCase1Score;
    private int testCase2Score;
    private int testCase3Score;
    private int coding_score;
    private long question_id;
    private long assessment_id;
    private long compile_score;
    private long run_score;
    private boolean scan;
    @CreationTimestamp
    private LocalDateTime createdTime;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessmentCodingDetails_id")
    @OrderBy(value = "assessmentCodingIssue_id ASC")
    private Set<AssessmentCodingIssues> issuesList;

}
