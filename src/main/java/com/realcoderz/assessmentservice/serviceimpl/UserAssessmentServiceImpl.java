/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realcoderz.assessmentservice.domain.AssociateAnswerTrack;
import com.realcoderz.assessmentservice.domain.UserAssessment;
import com.realcoderz.assessmentservice.exceptions.EntiryNotFoundException;
import com.realcoderz.assessmentservice.repository.AssociateAnswerTrackRepository;
import com.realcoderz.assessmentservice.repository.AssociateValidateRepository;
import com.realcoderz.assessmentservice.repository.UserAssessmentRepository;
import com.realcoderz.assessmentservice.service.UserAssessmentService;
import com.realcoderz.assessmentservice.util.BearerTokenUtil;
import com.realcoderz.assessmentservice.util.EncryptDecryptUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Vineet
 */
@Service
public class UserAssessmentServiceImpl implements UserAssessmentService {

    @Autowired
    private UserAssessmentRepository userAssessmentRepository;

    @Autowired
    private AssociateAnswerTrackRepository associateTrackRepository;

    @Autowired
    private AssociateValidateRepository associateValidateRepo;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${emailFrom}")
    private String from;

    @Value("${api_base_url}")
    private String recruitURL;


    ObjectMapper mapper = new ObjectMapper();

    @Override
    public UserAssessment saveUserAssessment(UserAssessment userAssessment) {
        return userAssessmentRepository.save(userAssessment);
    }

    @Override
    public List<UserAssessment> getResultByUserId(Long userId) {
        return userAssessmentRepository.findByUserId(userId);
    }

    @Override
    public Map calculateResult(Long userId, Long assessemntId) {
        Map resultMap = new HashMap();
        List<LinkedCaseInsensitiveMap> rows = userAssessmentRepository.getAssessmentResultByUserAssessmentId(userId, assessemntId);
        if (rows != null) {
            String question = "";
            String assess = "";
            Map<String, String> dataMap = new LinkedHashMap<>();
            List<Map<String, String>> returnList = new ArrayList<>();
            for (LinkedCaseInsensitiveMap mp : rows) {
                if ("".equalsIgnoreCase(assess)) {
                    assess = mp.get("assessment_desc").toString();
                }
                if ("".equalsIgnoreCase(question)) {
                    question = mp.get("question_id").toString();
                    dataMap = new LinkedHashMap<>();
                    dataMap.put("questionId", question);
                    dataMap.put("questionDesc", mp.get("question_desc").toString());
                    dataMap.put("sanswer", mp.get("sanswer").toString());
                    if ("Y".equalsIgnoreCase(mp.get("danswer").toString())) {
                        dataMap.put("danswer", mp.get("option_id").toString());
                    }
                } else {
                    if (!question.equalsIgnoreCase(mp.get("question_id").toString())) {
                        returnList.add(dataMap);
                        dataMap = new LinkedHashMap<>();
                        question = mp.get("question_id").toString();
                        dataMap.put("questionId", question);
                        dataMap.put("questionDesc", mp.get("question_desc").toString());
                        dataMap.put("sanswer", mp.get("sanswer").toString());
                        if ("Y".equalsIgnoreCase(mp.get("danswer").toString())) {
                            dataMap.put("danswer", mp.get("option_id").toString());
                        }
                    } else {
                        if ("Y".equalsIgnoreCase(mp.get("danswer").toString())) {
                            dataMap.put("danswer", (dataMap.get("danswer") != null ? (dataMap.get("danswer") + "," + mp.get("option_id").toString()) : mp.get("option_id").toString()));
                        }
                    }
                }
            }
            returnList.add(dataMap);
            returnList.stream().forEach(a -> {
                if (a.get("sanswer") != null && a.get("danswer") != null) {
                    if (a.get("sanswer").equalsIgnoreCase(a.get("danswer"))) {
                        a.put("status", "Correct");
                    } else {
                        a.put("status", "Incorrect");
                    }
                } else {
                    a.put("status", "Incorrect");
                }
            });
            resultMap.put("questions", returnList);
            Long countCorrectQuestions = Long.parseLong("0");
            if (returnList.size() > 1) {
                countCorrectQuestions = returnList.parallelStream().filter(a -> a.get("status").equalsIgnoreCase("Correct")).count();

            }
            resultMap.put("correctQuestion", countCorrectQuestions);
            resultMap.put("totalNoOfQuestion", returnList.size());
            resultMap.put("assessment", assess);
            System.out.println(resultMap);
            return resultMap;
        } else {
            return Collections.EMPTY_MAP;
        }
    }

    @Override
    public void saveAnswerDetails(Map map) {
        if (map.containsKey("aid") && map.containsKey("uid") && map.containsKey("qid") && map.containsKey("answer")) {
            AssociateAnswerTrack assAnsTrack = associateTrackRepository.findByAssociateIdAndQuestionIdAndAssessmentId(Long.parseLong(map.get("uid").toString()), Long.parseLong(map.get("qid").toString()), Long.parseLong(map.get("aid").toString()));
            if (assAnsTrack == null) {
                AssociateAnswerTrack ant = new AssociateAnswerTrack();
                ant.setAssessmentId(Long.parseLong(map.get("aid").toString()));
                ant.setAssociateId(Long.parseLong(map.get("uid").toString()));
                ant.setQuestionId(Long.parseLong(map.get("qid").toString()));
                ant.setAnswer(Long.parseLong(map.get("answer").toString()));
                associateTrackRepository.save(ant);
            } else {
                assAnsTrack.setAnswer(Long.parseLong(map.get("answer").toString()));
                associateTrackRepository.save(assAnsTrack);
            }
        }
    }

    @Override
     public boolean sendEmailWhenLimitExceed(Long orgId) {
        Boolean withinLimit = true;
        JSONObject json = new JSONObject();
        try {
            json.put("orgId", orgId);
        } catch (JSONException ex) {
            Logger.getLogger(UserAssessmentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.TEXT_PLAIN);
        header.set("Authorization", BearerTokenUtil.getBearerTokenHeader());

        HttpEntity<String> entity2 = new HttpEntity(EncryptDecryptUtils.encrypt(orgId.toString()), header);

        Map orgMap = restTemplate.exchange(recruitURL + "/license/getAdminEmail", HttpMethod.POST, entity2, Map.class).getBody();
     
        try {
            Map map = mapper.readValue(EncryptDecryptUtils.decrypt(orgMap.get("data").toString()), Map.class);
            Map map2 = associateValidateRepo.getCompletedAssessments(orgId);
            if (map.isEmpty()) { // null entry will only come if no license found with the conditon in repo
                throw new EntiryNotFoundException("License is not active yet");
            }

            Long applicant_completed_assessments = Long.parseLong(map2.get("applicant_assessment").toString());
            Long allowed_assessments = Long.parseLong(map.get("allowed_assessment").toString());
            Long associate_completed_assessment = Long.parseLong(map2.get("associate_assessments").toString());
            Long total_completed_assessments = associate_completed_assessment + applicant_completed_assessments;

            if (allowed_assessments != 0 && map.containsKey("type") && !map.get("type").toString().equals("HCM")) {
                String licenseType = map.get("type").toString();
                List<Map> adminList = (List) (map.get("adminEmails"));
                String[] adminEmails = adminList.stream().map(e -> e.get("email").toString()).collect(Collectors.toList()).toArray(new String[adminList.size()]);

                Long tolerance = ((total_completed_assessments + 1) * 100) / allowed_assessments;
                header.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity(EncryptDecryptUtils.encrypt(json.toString()), header);

                if (total_completed_assessments > Math.round((allowed_assessments * 110) / 100)) {

                    String subject = "Limit exceed";
                    String text = "You've exceeded maximum assessment conduct limit. Please contact administrator to upgrade your package";
                    new Thread(() -> adminList.forEach(u -> sendEmailNotification(adminEmails, licenseType, subject, text))).start();
                    new Thread(() -> restTemplate.exchange(recruitURL + "/licesneNotification/add", HttpMethod.POST, entity, LinkedCaseInsensitiveMap.class).getBody()).start();
                    withinLimit = false;
                } else if (total_completed_assessments + 1 == allowed_assessments) {
                    String subject = "Limit exceed";
                    String text = "You've reached to maximum conduct assessment limit. Please contact administrator to upgrade your package";
                    new Thread(() -> adminList.forEach(u -> sendEmailNotification(adminEmails, licenseType, subject, text))).start();
                    new Thread(() -> restTemplate.exchange(recruitURL + "/licesneNotification/add", HttpMethod.POST, entity, LinkedCaseInsensitiveMap.class).getBody()).start();
                    withinLimit = true;
                } else {
                    if (total_completed_assessments == Math.round(allowed_assessments / 2) || total_completed_assessments == Math.round((allowed_assessments * 3) / 4) || tolerance >= 90) {
                        String subject = "Alert for usage";
                        String text = "You've reached to " + tolerance + "% of maximum conduct assessment limit. Please contact administrator incase you want to upgrade your package";
                        new Thread(() -> adminList.forEach(u -> sendEmailNotification(adminEmails, licenseType, subject, text))).start();
                        new Thread(() -> restTemplate.exchange(recruitURL + "/licesneNotification/add", HttpMethod.POST, entity, LinkedCaseInsensitiveMap.class).getBody()).start();
                        withinLimit = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return withinLimit;

    }

    
    public boolean sendEmailNotification(String[] email, String licenseType, String subject, String text) {

        String content = "<html>\n"
                + "<body>\n"
                + "    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n"
                + "        <tr>\n"
                + "            <td style=\"padding:40px 0px 40px 0px;background-color:#f4f4f4;\">\n"
                + "                <table width=\"702\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#58585a\" style=\"height:10px;line-height:10px;font-size:10px;\" align=\"center\">\n"
                + "                    <tr>\n"
                + "                        <td valign=\"top\" align=\"right\" style=\"height:10px;line-height:10px;font-size:10px;padding:0px 0px 0px 0px;\">&nbsp;</td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "                <table width=\"702\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" bgcolor=\"#FFFFFF\">\n"
                + "                    <tr>\n"
                + "                        <td width=\"700\" bgcolor=\"#f4f4f4\">\n"
                + "                            <table width=\"700\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" bgcolor=\"#FFF\">\n"
                + "                                <tr>\n"
                + "                                    <td width=\"100%\" align=\"center\" bgcolor=\"#111112\" style=\"padding:20px;\" colspan=\"3\">\n"
                + "                                        <a href=\"https://rcteambuilder.com/\" target=\"_blank\">\n"
                + "                                            <img src=\"https://rcteambuilder.com/img/logo.png\" alt=\"logo\" border=\"0\" width=\"150px\" />\n"
                + "                                        </a>\n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "                                <tr>\n"
                + "                                    <td width=\"1\" bgcolor=\"#dadada\"></td>\n"
                + "                                    <td width=\"697\" align=\"center\">\n"
                + "                                        <table width=\"696\" cellspacing=\"0\" cellpadding=\"1\" border=\"0\" bgcolor=\"#FFFFFF\">\n"
                + "                                            <tr>\n"
                + "                                                <td>\n"
                + "                                                    <div class=\"dataDiv\" style=\"color:#666666;font-family:Lucida Grande,Lucida Sans,Lucida Sans Unicode,Arial,Helvetica,Verdana,sans-serif;font-size:12.5px;line-height:1.75em;padding:0 60px;\">\n"
                + "                                                        <br>\n"
                + "                                                        Dear Admin,<br> <br>\n"
                + "                                         License Type: " + licenseType + "<br>\n"
                + text + "<br>\n"
                + "                                                        Thank you for using our services.\n"
                + "                                                        <br><br>\n"
                + "                                                        Sincerely, <br>\n"
                + "                                                        The realcoderz.com team <br><br>\n"
                + "                                                    </div>\n"
                + "                                                </td>\n"
                + "                                            </tr>\n"
                + "                                        </table>\n"
                + "                                    </td>\n"
                + "                                    <td width=\"1\" bgcolor=\"#dadada\"></td>\n"
                + "                                </tr>\n"
                + "                            </table>\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "                <table width=\"702\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#68686a\" style=\"height:10px;line-height:10px;font-size:10px;\" align=\"center\">\n"
                + "                    <tr>\n"
                + "                        <td align=\"center\" valign=\"top\" style=\"height:10px;line-height:10px;font-size:10px;padding:0px 0 0px 0;\">\n"
                + "                            <div id=\"footerDiv\" style=\"font-family:Lucida Grande,Arial,Helvetica,Geneva,Verdana,sans-serif;color:#FFFFFF;font-size:12px;line-height:1em;text-align:center;padding:8px 10px 12px 10px;\">\n"
                + "                                Copyright &copy; <a href=\"https://rcteambuilder.com/\" style=\"text-decoration:none; color:#fff;\">realcoderz All Right Reserved</a></div>\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "    </table>\n"
                + "</body>\n"
                + "</html>";

        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            emailSender.send(mimeMessage);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("problem in send" + ex.getMessage());
            return false;
        }

    }

}
