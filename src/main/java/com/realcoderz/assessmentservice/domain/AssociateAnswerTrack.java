/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.realcoderz.assessmentservice.auditable.Auditable;

/**
 *
 * @author bipulsingh
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AssociateAnswerTrack extends Auditable<String> implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long answer;
    private Long questionId;
    private Long assessmentId;
    private Long associateId;
    
    
}
