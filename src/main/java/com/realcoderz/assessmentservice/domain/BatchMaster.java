/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Prateek
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "batch_id", scope = BatchMaster.class)
@Getter
@Setter
@NoArgsConstructor
public class BatchMaster extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "batch-sequence-generator")
    @GenericGenerator(
            name = "batch-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "batch_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long batch_id;

    @NotEmpty(message = "Batch Name Should Not Be Empty")
    private String batch_name;

//    @NotEmpty(message = "Learning Journey Should Not Be Empty")
    private String learning_journey;

//    @NotEmpty(message = "Batch Desc Should Not Be Empty")
    private String batch_desc;

    @NotNull(message = "Batch Start Date Should Not Be Empty")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date batch_start_date;

    @NotNull(message = "Selected No. Pf Weeks Should Not Be Empty")
    private Integer selected_no_of_weeks;

    @NotNull(message = "Batch End Date Should Not Be Empty")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date batch_end_date;

    @NotNull(message = "Language id can't be null")
    private Long language_id;

    @NotNull(message = "Active/Deactive Should Not Be Empty")
    private Character active;

    private Long organizationId;

    private Long learningJourneyId;

    private Boolean webcam;

    @OneToMany
    @JoinColumn(name = "batch_id")
    private Set<UserMaster> associates;

    public BatchMaster(Long batch_id, String learning_journey, String batch_desc, Date batch_start_date, Integer selected_no_of_weeks, Date batch_end_date, Long language_id, Character active, Long learningJourneyId) {
        this.batch_id = batch_id;
        this.learning_journey = learning_journey;
        this.batch_desc = batch_desc;
        this.batch_start_date = batch_start_date;
        this.selected_no_of_weeks = selected_no_of_weeks;
        this.batch_end_date = batch_end_date;
        this.language_id = language_id;
        this.active = active;
        this.learningJourneyId = learningJourneyId;
//        this.instructors = instructors;
    }

}
