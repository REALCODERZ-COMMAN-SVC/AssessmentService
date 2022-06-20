/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.realcoderz.assessmentservice.auditable.Auditable;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author Prateek
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QuestionType extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "question-type-sequence-generator")
    @GenericGenerator(
            name = "question-type-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "question_type_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long question_type_id;
    @NotEmpty(message = "Question Type Name can't be empty")
    private String question_type_name;
    @NotEmpty(message = "Question Type Desc can't be empty")
    private String question_type_desc;
    @NotNull(message = "Active/Deactive can't be empty")
    private Character active;

    public QuestionType(String question_type_name, String question_type_desc, Character active) {
        this.question_type_name = question_type_name;
        this.question_type_desc = question_type_desc;
        this.active = active;
    }

    @Override
    public String toString() {
        return "QuestionType{" + "question_type_id=" + question_type_id + ", question_type_name=" + question_type_name + ", question_type_desc=" + question_type_desc + ", active=" + active + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.question_type_id);
        hash = 59 * hash + Objects.hashCode(this.question_type_name);
        hash = 59 * hash + Objects.hashCode(this.question_type_desc);
        hash = 59 * hash + Objects.hashCode(this.active);
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
        final QuestionType other = (QuestionType) obj;
        if (!Objects.equals(this.question_type_name, other.question_type_name)) {
            return false;
        }
        if (!Objects.equals(this.question_type_desc, other.question_type_desc)) {
            return false;
        }
        if (!Objects.equals(this.question_type_id, other.question_type_id)) {
            return false;
        }
        if (!Objects.equals(this.active, other.active)) {
            return false;
        }
        return true;
    }
}
