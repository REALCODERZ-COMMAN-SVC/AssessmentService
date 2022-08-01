/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.realcoderz.assessmentservice.payload.SwaggerController;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author rohini
 */
@Setter
@Getter
@NoArgsConstructor
public class StudentAssessmentControllerPayload {

    @ApiModelProperty(value = "stuasmt/quiz", example = "{'id': 0,'language_id':0}")
    private String quiz;
    @ApiModelProperty(value = "stuasmt/save", example = "{'assessment': 'string','user_id':0,'counter':'string','jobPortalId':0}")
    private String save;
    @ApiModelProperty(value = "stuasmt/getresult", example = "{'user_id': 0}")
    private String getresult;
    @ApiModelProperty(value = "stuasmt/savetext", example = "{'assessment': 'string','mcq':'string','user_id':0}")
    private String savetext;
    @ApiModelProperty(value = "stuasmt/gettext", example = "{'assessment_id': 0,'student_id':0}")
    private String gettext;
    @ApiModelProperty(value = "stuasmt/setTimer", example = "{'aid': 0,'sid':0}")
    private String setTimer;
    @ApiModelProperty(value = "coding/get", example = "{'student_email': 'abc@gmail.com','organizationId':0}")
    private String getCodingQuestion;
    @ApiModelProperty(value = "coding/getCodingDetails", example = "{'student_email': 'abc@gmail.com','organizationId':0}")
    private String getCodingDetails;

}
