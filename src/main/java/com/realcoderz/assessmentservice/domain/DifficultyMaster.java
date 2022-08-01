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
public class DifficultyMaster extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "difficulty-sequence-generator")
    @GenericGenerator(
            name = "difficulty-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "difficulty_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long difficulty_id;
    @NotEmpty(message = "Difficulty Name can't be empty")
    private String difficulty_name;

    @NotEmpty(message = "Difficulty Desc can't be empty")
    private String difficulty_desc;

    private Long organizationId;
    
    @NotNull(message = "Active/Deactive can't be empty")
    private Character active;

    public DifficultyMaster(String difficulty_name, String difficulty_desc, Character active,Long organizationId) {
        this.difficulty_name = difficulty_name;
        this.difficulty_desc = difficulty_desc;
        this.active = active;
        this.organizationId=organizationId;
    }

    @Override
    public String toString() {
        return "DifficultyMaster{" + "difficulty_id=" + difficulty_id + ", difficulty_name=" + difficulty_name + ", difficulty_desc=" + difficulty_desc + ", active=" + active + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.difficulty_id);
        hash = 29 * hash + Objects.hashCode(this.difficulty_name);
        hash = 29 * hash + Objects.hashCode(this.difficulty_desc);
        hash = 29 * hash + Objects.hashCode(this.active);
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
        final DifficultyMaster other = (DifficultyMaster) obj;
        if (!Objects.equals(this.difficulty_name, other.difficulty_name)) {
            return false;
        }
        if (!Objects.equals(this.difficulty_desc, other.difficulty_desc)) {
            return false;
        }
        if (!Objects.equals(this.difficulty_id, other.difficulty_id)) {
            return false;
        }
        if (!Objects.equals(this.active, other.active)) {
            return false;
        }
        if (!Objects.equals(this.organizationId, other.organizationId)) {
            return false;
        }
        return true;
    }

}
