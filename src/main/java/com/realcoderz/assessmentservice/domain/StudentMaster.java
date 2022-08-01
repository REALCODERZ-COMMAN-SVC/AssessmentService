/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import com.realcoderz.assessmentservice.auditable.Auditable;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Aman Bansal
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "student_id", scope = StudentMaster.class)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StudentMaster extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "student_generator")
    @GenericGenerator(
            name = "student_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "student_sequence"),
                @Parameter(name = "initial_value", value = "2"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long student_id;
    @NotBlank(message = "First Name can't be empty")
    private String first_name;

    @NotBlank(message = "Last Name can't be empty")
    private String last_name;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(javax.persistence.TemporalType.DATE)
   // @NotEmpty(message = "Date of Birth can't be empty")
//    @Past
    private Date dob;

//    @NotEmpty(message = "Gender can't be empty")
    private String gender;

//    @NotEmpty(message = "Mobile number can't be empty & length should be 10")
//    @Size(min = 10, max = 10)
//    @Digits(fraction = 0, integer = 10)
    private String mobile_no;

    @Email
    @NotBlank(message = "Email id can't be empty")
    private String email_id;

    private String city;
    private String pincode;
    private String district;
    private String state;
    private String currentAddress;

//    @NotEmpty(message = "Stream can't be empty")
    private String stream;

//    @NotEmpty(message = "Tenth percentage can't be empty")
//    @DecimalMin("0.00") 
//    @DecimalMax("100.00")
    private String tenth_percentage;

//    @NotEmpty(message = "Twelveth percentage can't be empty")
//    @DecimalMin("0.00") 
//    @DecimalMax("100.00")
    private String twelveth_percentage;

//    @NotEmpty
    private String graduation_name;

//    @NotEmpty(message = "Graduation Year can't be empty & length should be 4")
//    @Size(min = 4, max = 4)
//    @Digits(fraction = 0, integer = 4)
    private String graduation_year;

//    @NotEmpty(message = "Graduation percentage can't be empty")
//    @DecimalMin("0.00") 
//    @DecimalMax("100.00")
    private String graduation_percentage;

//    @NotBlank(message = "College name can't be empty")
    private String university_name;

//    @NotBlank(message = "Internship nature can't be empty")
    private String internship_nature;

    private Long scholar_rej;

//    @NotBlank(message = "AI score can't be empty")
    private Double ai_score;
    
    @Column(columnDefinition = "long default 0")
    private Long follow_up;
    
    private String recruiter_email;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recruiter_followup_date;


    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date candStageMsg1Date;

    private String email_notification;
    private Long jobPortalId;
    private String profilePath;
    private Long organizationId;
    private String countryCode;
    private String mobileCode;
    
    public StudentMaster(Long student_id, String first_name, String last_name, Date dob, String gender, String mobile_no, String email_id, String city, String pincode, String district, String state, String currentAddress, String stream, String tenth_percentage, String twelveth_percentage, String graduation_name, String graduation_year, String graduation_percentage, String university_name, String internship_nature, Long scholar_rej, Double ai_score,  Date candStageMsg1Date) {
        this.student_id = student_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.gender = gender;
        this.mobile_no = mobile_no;
        this.email_id = email_id;
        this.city = city;
        this.pincode = pincode;
        this.district = district;
        this.state = state;
        this.currentAddress = currentAddress;
        this.stream = stream;
        this.tenth_percentage = tenth_percentage;
        this.twelveth_percentage = twelveth_percentage;
        this.graduation_name = graduation_name;
        this.graduation_year = graduation_year;
        this.graduation_percentage = graduation_percentage;
        this.university_name = university_name;
        this.internship_nature = internship_nature;
        this.scholar_rej = scholar_rej;
        this.ai_score = ai_score;
        this.candStageMsg1Date = candStageMsg1Date;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.student_id);
        hash = 97 * hash + Objects.hashCode(this.first_name);
        hash = 97 * hash + Objects.hashCode(this.last_name);
        hash = 97 * hash + Objects.hashCode(this.dob);
        hash = 97 * hash + Objects.hashCode(this.gender);
        hash = 97 * hash + Objects.hashCode(this.mobile_no);
        hash = 97 * hash + Objects.hashCode(this.email_id);
        hash = 97 * hash + Objects.hashCode(this.city);
        hash = 97 * hash + Objects.hashCode(this.pincode);
        hash = 97 * hash + Objects.hashCode(this.district);
        hash = 97 * hash + Objects.hashCode(this.state);
        hash = 97 * hash + Objects.hashCode(this.currentAddress);
        hash = 97 * hash + Objects.hashCode(this.stream);
        hash = 97 * hash + Objects.hashCode(this.tenth_percentage);
        hash = 97 * hash + Objects.hashCode(this.twelveth_percentage);
        hash = 97 * hash + Objects.hashCode(this.graduation_name);
        hash = 97 * hash + Objects.hashCode(this.graduation_year);
        hash = 97 * hash + Objects.hashCode(this.graduation_percentage);
        hash = 97 * hash + Objects.hashCode(this.university_name);
        hash = 97 * hash + Objects.hashCode(this.internship_nature);
        hash = 97 * hash + Objects.hashCode(this.scholar_rej);
        hash = 97 * hash + Objects.hashCode(this.ai_score);
        hash = 97 * hash + Objects.hashCode(this.candStageMsg1Date);
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
        final StudentMaster other = (StudentMaster) obj;
        if (!Objects.equals(this.first_name, other.first_name)) {
            return false;
        }
        if (!Objects.equals(this.last_name, other.last_name)) {
            return false;
        }
        if (!Objects.equals(this.gender, other.gender)) {
            return false;
        }
        if (!Objects.equals(this.mobile_no, other.mobile_no)) {
            return false;
        }
        if (!Objects.equals(this.email_id, other.email_id)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.pincode, other.pincode)) {
            return false;
        }
        if (!Objects.equals(this.district, other.district)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.currentAddress, other.currentAddress)) {
            return false;
        }
        if (!Objects.equals(this.stream, other.stream)) {
            return false;
        }
        if (!Objects.equals(this.tenth_percentage, other.tenth_percentage)) {
            return false;
        }
        if (!Objects.equals(this.twelveth_percentage, other.twelveth_percentage)) {
            return false;
        }
        if (!Objects.equals(this.graduation_name, other.graduation_name)) {
            return false;
        }
        if (!Objects.equals(this.graduation_year, other.graduation_year)) {
            return false;
        }
        if (!Objects.equals(this.graduation_percentage, other.graduation_percentage)) {
            return false;
        }
        if (!Objects.equals(this.university_name, other.university_name)) {
            return false;
        }
        if (!Objects.equals(this.internship_nature, other.internship_nature)) {
            return false;
        }
        if (!Objects.equals(this.student_id, other.student_id)) {
            return false;
        }
        if (!Objects.equals(this.dob, other.dob)) {
            return false;
        }
        if (!Objects.equals(this.scholar_rej, other.scholar_rej)) {
            return false;
        }
        if (!Objects.equals(this.ai_score, other.ai_score)) {
            return false;
        }
        if (!Objects.equals(this.candStageMsg1Date, other.candStageMsg1Date)) {
            return false;
        }
        return true;
    }
}
