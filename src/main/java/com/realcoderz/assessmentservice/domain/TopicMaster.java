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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@ToString
@EqualsAndHashCode(callSuper = false)
public class TopicMaster extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "topic-sequence-generator")
    @GenericGenerator(
            name = "topic-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "topic_sequence"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long topic_id;

    @NotBlank(message = "Topic name can't be empty")
    private String topic_name;

    @NotBlank(message = "Topic Description can't be empty")
    private String topic_desc;

    private Long organizationId;

    @NotNull(message = "Active status can't be null")
    private Character active;

    public TopicMaster(String topic_name, String topic_desc, Character active, Long organizationId) {
        this.topic_name = topic_name;
        this.topic_desc = topic_desc;
        this.active = active;
        this.organizationId = organizationId;
    }

}
