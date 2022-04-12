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
public class QuestionMasterControllerPayload {
    @ApiModelProperty(value = "questions/upload", example = "{'file':'','organizationId' : 1,'questionTypeId':0}" )
    private String  upload;
    @ApiModelProperty(value = "questions/uploadSkillBuilder", example = "{'file':'','organizationId' : 1,'section':'string','subSection':'string','topic':'string'}" )
    private String  uploadSkillBuilder;
    @ApiModelProperty(value = "questions/esave", example = "{'data':'string','organizationId' : 1}" )
    private String  esave;
    @ApiModelProperty(value = "questions/esaveforskill", example = "{'data':'string','organizationId' : 1}" )
    private String  esaveforskill;
    @ApiModelProperty(value = "questions", example = "{'language_id':0,'topic_id' : 0,'difficulty_id':0,'organizationId':0}" )
    private String  questions;
    @ApiModelProperty(value = "questions/getSkillBuilderQuest", example = "{'course':'string','sub_course' : 'string','topic':'string','organizationId':0}" )
    private String  getSkillBuilderQuest;
    @ApiModelProperty(value = "questions/add", example = "{'questionmasterdata':{'options_list':'string','language_id':0,'topic_id':0,'difficulty_id':0,'question_type_id':0,'question_desc':'string','active':'string','organizationId':0,'no_of_answer':0,'testCases':'string','codingTemplate':'string','expectedOutput':'string'}}" )
    private String  add;
    @ApiModelProperty(value = "questions/addSkillBuilder", example = "{'questionmasterdata':{'options_list':'string','course':'string','sub_course':'string','topic':'string','question_type_id':0,'question_desc':'string','active':'string','organizationId':0,'no_of_answer':0,'paramValue':'string','expectedOutput':'string'}}" )
    private String  addSkillBuilder;
    @ApiModelProperty(value = "questions/get", example = "{'questionTypeId':0,'id':0}" )
    private String  get;
    @ApiModelProperty(value = "questions/delete", example = "{'id':0}" )
    private String  delete;
    @ApiModelProperty(value = "questions/update", example = "{'questionmasterdata':'string','id':0}" )
    private String  update;
    
    
}
