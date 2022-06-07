/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.realcoderz.assessmentservice.auditable;

import com.realcoderz.assessmentservice.util.BearerTokenUtil;
import com.realcoderz.assessmentservice.util.TokenProvider;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

/**
 *
 * @author anwar
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    private static final Logger logger = LoggerFactory.getLogger(AuditorAwareImpl.class);

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            String token = BearerTokenUtil.getBearerTokenHeader();
            long userId = tokenProvider.getUserIdFromToken(token.substring(7, token.length()));
            return Optional.of(String.valueOf(userId));
        } catch (Exception ex) {
            logger.error("Problem in AuditorAwareImpl :: getCurrentAuditor() => " + ex);
            return Optional.of("0");
        }
    }

}
