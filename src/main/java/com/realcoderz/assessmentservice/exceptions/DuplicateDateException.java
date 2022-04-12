/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.exceptions;

/**
 *
 * @author ROHAN
 */
public class DuplicateDateException extends RuntimeException{
    public DuplicateDateException(String message) {
        super(message);
    }
}
