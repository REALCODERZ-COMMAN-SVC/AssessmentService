/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.realcoderz.assessmentservice.auditable.Auditable;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author vineet
 * @Editor Bipul Kumar Singh
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "user_id", scope = UserMaster.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserMaster extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(generator = "user-generator")
    @GenericGenerator(name = "user-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "user_sequence"),
        @Parameter(name = "initial_value", value = "2"),
        @Parameter(name = "increment_size", value = "1")})
    private Long user_id;
    private String uid;
    //@NotBlank(message = "First Name can't be empty")
    private String first_name;
    
    //@NotBlank(message = "Last Name can't be empty")
    private String last_name;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(javax.persistence.TemporalType.DATE)
//    @Past(message = "Date of birth should be at least 18 year")
//     @NotNull(message = "Date of birth can't be null")
    private Date dob;
    
//    @NotNull(message = "Gender can't be empty")
    private String gender;
    
//     @NotEmpty(message = "Tenth percentage can't be empty")
//    @DecimalMin("0.00") 
//    @DecimalMax("100.00")
    private String tenth_percentage;
    
//     @NotEmpty(message = "Twelth percentage can't be empty")
//    @DecimalMin("0.00") 
//    @DecimalMax("100.00")
    private String twelve_percentage;
    
//     @NotEmpty(message = "Graduation percentage can't be empty")
//    @DecimalMin("0.00") 
//    @DecimalMax("100.00")
    private String graduation_percentage;
    
//     @NotEmpty(message = "Graduation Year can't be empty & length should be 4")
//    @Size(min = 4, max = 4)
//    @Digits(fraction = 0, integer = 4)
    private String graduation_year;
    
//     @NotBlank(message = "College name can't be empty")
    private String college_name;
   
    //@NotEmpty(message = "Mobile number can't be empty & length should be 10")
    //@Size(min = 10, max = 10)
    //@Digits(fraction = 0, integer = 10)
    //@Pattern(regexp = "^[1-9]+[0-9]*$")
    private String mobile_no;
    
//     @Email
//     @NotBlank(message = "Email id can't be empty")
    private String email_id;
    
//    @NotBlank(message = "Current Address can't be empty")
    private String current_address;
    
//     @NotNull(message = "Active/Deactive can't be empty")
    private Character active;
    
    private String graduation_name;
    
    private String internship_nature;
    
    private String stream;
    
    private String pinCode;
    private String city;
    private String district;
    private String state;
    private String status;
    private String profilePath;
    private Character entry_from;
    private Double ai_final_score;
    private Long default_role;
    
   
    private String emergency_no;
    private String reference_no_first;
    private String reference_no_second;
    private String tenth_year;
    private String middle_name;
    private String alternate_no;
    private String twelve_year;
    
    //@NotNull
    //@Size(max=4, message="Blood group can't be more than 4 character")
    //@Pattern(regexp = "/^[^*|\\\":<>[\\]{}`\\\\()';@&$]+$/")
    private String blood_group;
    private String university_name;
    
    private Long organizationId;
    private String countryCode;
    private String mobileCode;
    private Long departmentId;
    private Long empId;
    private Long empSupervisorId;
 

    
    private String batchName;
//    @ManyToOne
//    @JoinColumn(name = "instructor_batch_id", insertable = false, updatable = false)
//    private BatchMaster batch_for_instructor;

    public UserMaster(Long user_id, String first_name, String middle_name, String last_name, Date dob, String gender,
            String tenth_year, String tenth_percentage, String twelve_year, String twelve_percentage,
            String graduation_year, String graduation_percentage, String college_name, String university_name,
            String blood_group, String mobile_no, String emergency_no, String reference_no_first,
            String reference_no_second, String email_id, String alternate_no, String current_address, Character active,
            Character entry_from,Long dafultRole,String batchName) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.dob = dob;
        this.gender = gender;
        this.tenth_year = tenth_year;
        this.tenth_percentage = tenth_percentage;
        this.twelve_year = twelve_year;
        this.twelve_percentage = twelve_percentage;
        this.graduation_year = graduation_year;
        this.graduation_percentage = graduation_percentage;
        this.college_name = college_name;
        this.university_name = university_name;
        this.blood_group = blood_group;
        this.mobile_no = mobile_no;
        this.emergency_no = emergency_no;
        this.reference_no_first = reference_no_first;
        this.reference_no_second = reference_no_second;
        this.email_id = email_id;
        this.alternate_no = alternate_no;
        this.current_address = current_address;
        this.active = active;
        this.entry_from = entry_from;
//        this.batch_for_instructor = batch_for_instructor;
        this.default_role = dafultRole;
        this.batchName = batchName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.user_id);
        hash = 53 * hash + Objects.hashCode(this.first_name);
        hash = 53 * hash + Objects.hashCode(this.middle_name);
        hash = 53 * hash + Objects.hashCode(this.last_name);
        hash = 53 * hash + Objects.hashCode(this.dob);
        hash = 53 * hash + Objects.hashCode(this.gender);
        hash = 53 * hash + Objects.hashCode(this.blood_group);
        hash = 53 * hash + Objects.hashCode(this.mobile_no);
        hash = 53 * hash + Objects.hashCode(this.email_id);
        hash = 53 * hash + Objects.hashCode(this.active);
        hash = 53 * hash + Objects.hashCode(this.entry_from);
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
        final UserMaster other = (UserMaster) obj;
        if (!Objects.equals(this.first_name, other.first_name)) {
            return false;
        }
        if (!Objects.equals(this.middle_name, other.middle_name)) {
            return false;
        }
        if (!Objects.equals(this.last_name, other.last_name)) {
            return false;
        }
        if (!Objects.equals(this.blood_group, other.blood_group)) {
            return false;
        }
        if (!Objects.equals(this.mobile_no, other.mobile_no)) {
            return false;
        }
        if (!Objects.equals(this.email_id, other.email_id)) {
            return false;
        }
        if (!Objects.equals(this.user_id, other.user_id)) {
            return false;
        }
        if (!Objects.equals(this.dob, other.dob)) {
            return false;
        }
        if (!Objects.equals(this.gender, other.gender)) {
            return false;
        }
        if (!Objects.equals(this.active, other.active)) {
            return false;
        }
        return Objects.equals(this.entry_from, other.entry_from);
    }
}
