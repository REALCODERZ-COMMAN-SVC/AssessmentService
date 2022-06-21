/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.service;

import java.util.Map;

/**
 *
 * @author rohini
 */
public interface FaceDetectionFlagsService {
        
    public Map<String, Object> addFaceDetectionFlags(Map flags);
    public Map getFaceDetectionFlagsCountById(String data);
    
}