package com.realcoderz.assessmentservice.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.LinkedHashMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author bipulsingh
 */
public class CommonCompiler {

    RestTemplate restTemplate;
    {
        restTemplate = new RestTemplate();
    }
    
    public LinkedHashMap compilerAndRun(LinkedHashMap object,String compilerUrl){
        LinkedHashMap resultMap = new LinkedHashMap();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<LinkedHashMap> request = new HttpEntity<>(object, headers);
        LinkedHashMap response = restTemplate.exchange(compilerUrl + "/submissions", HttpMethod.POST, request, LinkedHashMap.class).getBody();
        if (response != null && !response.isEmpty()) {
            String token = response.get("token").toString();
            if (token != null) {
                LinkedHashMap output = restTemplate.getForObject(compilerUrl + "/submissions/{token}", LinkedHashMap.class, token);
                LinkedHashMap status = (LinkedHashMap) output.get("status");
                int id = Integer.parseInt(status.get("id").toString());
                while (id < 3) {
                    output = restTemplate.getForObject(compilerUrl + "/submissions/{token}", LinkedHashMap.class, token);
                    status = (LinkedHashMap) output.get("status");
                    id = Integer.parseInt(status.get("id").toString());
                }
                if (id > 2) {
                    return status;
                }
            }
        }
        return resultMap;
    }

}
