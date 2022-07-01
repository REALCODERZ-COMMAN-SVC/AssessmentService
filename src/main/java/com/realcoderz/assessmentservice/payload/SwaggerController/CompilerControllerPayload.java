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
public class CompilerControllerPayload {
    @ApiModelProperty(value = "cmp/crt", example = "{'code': 0,'id':0,'qid':0,'aid':0,'input':'string','stdout':'string'}")
    private String upload;
    
}
