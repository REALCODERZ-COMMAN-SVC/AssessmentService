/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author anwar
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class AssociateTopicScores extends Auditable<String> implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_id", length = 5)
    private long user_id;
    @Column(name = "assessment_id", length = 5)
    private long assessment_id;
    @Column(name = "topic_name", length = 100)
    private String topic_name;
    @Column(name = "score", length = 5)
    private float score;

    public AssociateTopicScores(long user_id,long assessment_id, String topic_name, float score) {
        this.user_id = user_id;
        this.assessment_id = assessment_id;
        this.topic_name = topic_name;
        this.score = score;
    }
    
    
    
}
