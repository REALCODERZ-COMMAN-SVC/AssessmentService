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
public class LanguageMaster extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "language-sequence-generator")
    @GenericGenerator(
            name = "language-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "language_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long language_id;

    @NotEmpty(message = "Language Name can't be empty")
    private String language_name;

    @NotEmpty(message = "Language Desc can't be empty")
    private String language_desc;

    private Long organizationId;

    private Boolean technical;
    
    @NotNull(message = "Active status can't be empty")
    private Character active;

    public LanguageMaster(String language_name, String language_desc, Character active,Long organizationId) {
        this.language_name = language_name;
        this.language_desc = language_desc;
        this.active = active;
        this.organizationId=organizationId;
    }

    public LanguageMaster(Long language_id, String language_name, String language_desc, Long organizationId, Boolean technical, Character active) {
        this.language_id = language_id;
        this.language_name = language_name;
        this.language_desc = language_desc;
        this.organizationId = organizationId;
        this.technical = technical;
        this.active = active;
    }

    public LanguageMaster(String language_name, String language_desc, Long organizationId, Boolean technical, Character active) {
        this.language_name = language_name;
        this.language_desc = language_desc;
        this.organizationId = organizationId;
        this.technical = technical;
        this.active = active;
    }
    

    public LanguageMaster(Long language_id, String language_name, String language_desc, Character active,Long organizationId) {
        this.language_id = language_id;
        this.language_name = language_name;
        this.language_desc = language_desc;
        this.active = active;
        this.organizationId=organizationId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 55 * hash + Objects.hashCode(this.language_id);
        hash = 55 * hash + Objects.hashCode(this.language_name);
        hash = 55 * hash + Objects.hashCode(this.language_desc);
        hash = 55 * hash + Objects.hashCode(this.active);
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
        final LanguageMaster other = (LanguageMaster) obj;
        if (!Objects.equals(this.language_name, other.language_name)) {
            return false;
        }
        if (!Objects.equals(this.language_desc, other.language_desc)) {
            return false;
        }
        if (!Objects.equals(this.language_id, other.language_id)) {
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
