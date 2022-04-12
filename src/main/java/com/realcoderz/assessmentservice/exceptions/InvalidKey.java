/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.exceptions;

/**
 *
 * @author anwar
 */
public class InvalidKey extends RuntimeException{
     public InvalidKey(String message) {
        super(message);
    }
}
