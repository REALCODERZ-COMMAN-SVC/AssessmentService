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
 * @author Arvind Verma 
 */
@Setter
@Getter
@NoArgsConstructor
public class AssessmentCreationControllerPayload {
    @ApiModelProperty(value = "/assessments/all", example = "{'organizationId' : 1}" )
    private String  all;
    @ApiModelProperty(value = "/assessments/verifyCode", example = "{'user_code' : 'A1B2C3D4'}" )
    private String  verifyCode;
    @ApiModelProperty(value = "/assessments/quizForStudent", example = "{'id' : 1}" )
    private String  quizForStudent;
    @ApiModelProperty(value = "/assessments/add", example = "{'assessmentdata':{'questions':'','language_id':0 , 'topic_id': 0 , 'difficulty_id': 0 , 'codingmarks_id' :0 , 'assessment_desc': 'Test' , 'event_id' : 0 ,'batch_id':0  ,'instructions':'data', 'time':'','creation_type':'','active':a ,'organizationId':0 }}" )
    private String  add;
    @ApiModelProperty(value = "/assessments/update", example = "{'assessmentdata':{'questions':'','language_id':0 , 'topic_id': 0 , 'difficulty_id': 0 , 'codingmarks_id' :0 , 'assessment_desc': 'Test' , 'event_id' : 0 ,'batch_id':0  ,'instructions':'data', 'time':'','active':a ,'organizationId':0 ,'id':0}}" )
    private String  update;
    @ApiModelProperty(value = "/assessments/questions", example = "{'topic_id':0,'language_id': 0 , 'difficulty_id' : 0 ,'organizationId': 0}" )
    private String  questions;
    @ApiModelProperty(value = "/assessments/getTopicsForRanAssess", example = "{'language_id': 0 , 'difficulty_id' : 0 ,'organizationId': 0}" )
    private String  getTopicsForRanAssess;
    @ApiModelProperty(value = "/assessments/saveRanAssess", example = "{'language_id':0 , 'difficulty_id': 0 , 'codingmarks_id' :0 , 'assessment_desc': 'Test' , 'event_id' : 0 ,'batch_id':0  ,'instructions':'data', 'time':'','creation_type':'','active':a ,'organizationId':0 , 'randomTopics':'Topic'}" )
    private String  saveRanAssess;
    @ApiModelProperty(value = "/assessments/updateRanAssess", example = "{'language_id':0 , 'difficulty_id': 0 , 'codingmarks_id' :0 , 'assessment_desc': 'Test' , 'event_id' : 0 ,'batch_id':0  ,'instructions':'data', 'time':'','creation_type':'','active':a ,'organizationId':0 , 'randomTopics':'Topic'}" )
    private String  updateRanAssess;
    @ApiModelProperty(value = "/assessments/get", example = "{'id':0 , 'creation_type': '' }" )
    private String  get;
    @ApiModelProperty(value = "/assessments/delete", example = "{'id':0 }" )
    private String  delete;
}
