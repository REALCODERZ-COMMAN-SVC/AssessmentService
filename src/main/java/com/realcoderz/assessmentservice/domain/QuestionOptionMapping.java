/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author Prateek,Aman Bansal
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "option_id")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionOptionMapping extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "question-option-sequence-generator")
    @GenericGenerator(
            name = "question-option-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "question_option_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long option_id;
    private String option_desc;
    @NotNull(message = "Active status can't be empty")
    private Character isActive;
    @ManyToOne
    private QuestionMaster questionMaster;

    public QuestionOptionMapping(Long option_id, String option_desc, Character isActive, QuestionMaster questionMaster) {
        this.option_id = option_id;
        this.option_desc = option_desc;
        this.isActive = isActive;
        this.questionMaster = questionMaster;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.option_id);
        hash = 89 * hash + Objects.hashCode(this.option_desc);
        hash = 89 * hash + Objects.hashCode(this.isActive);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuestionOptionMapping other = (QuestionOptionMapping) obj;
        if (!Objects.equals(this.option_desc, other.option_desc)) {
            return false;
        }
        if (!Objects.equals(this.option_id, other.option_id)) {
            return false;
        }
        return Objects.equals(this.isActive, other.isActive);
    }

}
