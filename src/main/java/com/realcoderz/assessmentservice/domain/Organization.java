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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author ROHAN
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Organization extends Auditable<String> implements Serializable {
    
    @Id
    @GeneratedValue(generator = "organization-sequence-generator")
    @GenericGenerator(
            name = "organization-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "organization_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long organizationId;
    
    @NotEmpty(message = "Organization Name can't be empty")
    private String organizationName;
    
    private String organizationImage;
    
    @NotNull(message = "Active status can't be null")
    private Character active;
    
    @NotEmpty(message = "Organization Image can't be empty")
    private String url;
    
    private String testCode;
  
     public Organization(String organizationName,String url, Character active) {
        this.organizationName = organizationName;
        this.url = url;
        this.active = active;
    }
    
}
