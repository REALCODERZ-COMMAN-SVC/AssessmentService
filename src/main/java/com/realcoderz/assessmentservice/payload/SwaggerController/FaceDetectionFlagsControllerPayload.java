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
public class FaceDetectionFlagsControllerPayload {
    @ApiModelProperty(value = "/facedetection/savefacedetectioncount", example = "{'cellphone_detection_count' : 0,'cellphone_detection_time':0,'morethanoneuser_count':0,'morethanoneuser_time':0,'usermovement_count':0,'usermovement_time':0,'email':'string','organizationId':0,'studentId':0}" )
    private String  savefacedetectioncount;
    @ApiModelProperty(value = "/facedetection/getfacedetectioncount", example = "{'studentId':0}")
    private String  getfacedetectioncount;
}
