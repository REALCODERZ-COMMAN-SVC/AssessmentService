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
import com.realcoderz.assessmentservice.auditable.Auditable;
import lombok.EqualsAndHashCode;

/**
 *
 * @author anwar
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StudentTopicScores extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "student_id", length = 5)
    private long student_id;
    @Column(name = "assessment_id", length = 5)
    private long assessment_id;
    @Column(name = "topic_name", length = 100)
    private String topic_name;
    @Column(name = "score", length = 5)
    private float score;

    public StudentTopicScores(long student_id, long assessment_id, String topic_name, float score) {
        this.student_id = student_id;
        this.assessment_id = assessment_id;
        this.topic_name = topic_name;
        this.score = score;
    }
    
    
}