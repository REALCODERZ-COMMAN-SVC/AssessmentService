/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.realcoderz.assessmentservice.auditable.Auditable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author rohini
 */
@Entity
@Table(name = "facedetectionflags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FaceDetectionFlags{
    
    @Id
    @GeneratedValue(generator = "facedetection-sequence-generator")
    @GenericGenerator(
            name = "facedetection-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "facedetection"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long facedetection_id;
    
    private int cellphone_detection_count;
    private String cellphone_detection_time;
    private int morethanoneuser_count;
    private  String morethanoneuser_time;
    private int usermovement_count;
    private String usermovement_time;
    private Long studentId;
    private String email;
    private Long orgId;
    
}