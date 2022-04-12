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
public class RCAssessmentCreationControllerPayload {
    @ApiModelProperty(value = "rcassessments/all", example = "{'organizationId': 0}")
    private String all;
    @ApiModelProperty(value = "rcassessments/delete", example = "{'id': 0}")
    private String delete;
    @ApiModelProperty(value = "rcassessments/deleteSkillBuilderAssessment", example = "{'id': 0}")
    private String deleteSkillBuilderAssessment;
    @ApiModelProperty(value = "rcassessments/getTopicsForRanAssess", example = "{'language_id': 0,'difficulty_id':0}")
    private String getTopicsForRanAssess;
    @ApiModelProperty(value = "rcassessments/saveRanAssess", example = "{'organizationId': 0,'rcassessment_desc':'string'}")
    private String saveRanAssess;
    @ApiModelProperty(value = "rcassessments/updateRanAssess", example = "{'rcassessment_id': 0,'language_id':0,'difficulty_id':0,'codingmarks_id':0,'rcassessment_desc':'string','time':0,'creation_type':'string','jobportal_id':0,'active':'string','randomTopics':''}")
    private String updateRanAssess;
    @ApiModelProperty(value = "rcassessments/get", example = "{'creation_type': 'string','id':0}")
    private String get;
    @ApiModelProperty(value = "rcassessments/getSkillAssForUpdate", example = "{'id':0}")
    private String getSkillAssForUpdate;
    @ApiModelProperty(value = "rcassessments/skillBuilderCourse", example = "{'assessmentdata':{'questions':0,'section':'string','subSectionList':'string','courseName':'string','skillAssessmentDesc':'string','instructions':'string','active':'string','learningJourney':'string'}}")
    private String skillBuilderCourse;
    @ApiModelProperty(value = "rcassessments/questions", example = "{'course':'string','section':'string','subsection':'string','active':'string'}")
    private String questions;
    
}
