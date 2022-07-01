/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.realcoderz.assessmentservice.auditable.Auditable;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author anwar
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class StudentCertification extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "student_certification_generator")
    @GenericGenerator(name = "student_certification_generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "student_certification_sequence"),
        @Parameter(name = "initial_value", value = "1"),
        @Parameter(name = "increment_size", value = "1")})
    private Long certificationId;
    private Long badgeId;
    @NotNull(message = "Student id can't be null")
    private Long studentId;
    @NotBlank(message = "Certificate Number can't be empty")
    private String certificateNumber;
    @NotBlank(message = "Certificate level can't be null")
    private String certificateLevel;
    private String certificateUrl;
    @NotNull(message = "Certificate assign date can't be null")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedOn;

    public StudentCertification(Long certificationId, Long badgeId, Long studentId, String certificateNumber, String certificateLevel, Date assigned_on) {
        this.certificationId = certificationId;
        this.badgeId = badgeId;
        this.studentId = studentId;
        this.certificateNumber = certificateNumber;
        this.certificateLevel = certificateLevel;
        this.assignedOn = assigned_on;
    }

}
