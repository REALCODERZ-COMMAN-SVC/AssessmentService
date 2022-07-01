/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.google.api.client.util.Charsets.UTF_8;
import com.realcoderz.assessmentservice.domain.AssessmentCodingDetails;
import com.realcoderz.assessmentservice.domain.AssessmentCodingMarks;
import com.realcoderz.assessmentservice.repository.AssessmentCodingDetailsRepository;
import com.realcoderz.assessmentservice.repository.AssessmentCodingMarksRepository;
import com.realcoderz.assessmentservice.repository.AssessmentCreationRepository;
import com.realcoderz.assessmentservice.repository.QuestionMasterRepository;
import com.realcoderz.assessmentservice.service.CompilerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bipul Kr Singh
 *
 */
@Service
public class CompilerServiceImpl implements CompilerService {

    static final Logger logger = LoggerFactory.getLogger(CompilerServiceImpl.class);

    @Autowired
    private AssessmentCodingMarksRepository codingMarkRepo;

    @Autowired
    private QuestionMasterRepository queMasterRepo;

    @Autowired
    private AssessmentCreationRepository assessmentRepo;

    @Autowired
    private AssessmentCodingDetailsRepository codeDetailsRepository;

    ObjectMapper mapper = new ObjectMapper();

    /**
     * @author Bipul Singh
     * @param mp
     * @return python script output
     */
    @Override
    public Map<String, Object> saveSourceCode(Map<String, Object> mp) {
        logger.info("CompilerServiceImpl -> saveSourceCode() :: Method execution start with request data ::  " + mp);
        Map<String, Object> resultMap = new HashMap<>();
        if ((mp != null && !mp.isEmpty())
                && (mp.containsKey("code") && !"".equalsIgnoreCase(mp.get("code").toString().trim()))
                && (mp.containsKey("id") && !"".equalsIgnoreCase(mp.get("id").toString().trim()))
                && (mp.containsKey("qid") && !"".equalsIgnoreCase(mp.get("qid").toString().trim()))
                && (mp.containsKey("aid") && !"".equalsIgnoreCase(mp.get("aid").toString().trim()))
                && (mp.containsKey("input"))) {
            byte[] bytes = mp.get("code").toString().trim().replace("\u00a0", " ").getBytes(UTF_8);
            String expectedOutput = queMasterRepo.findExpectedOutputById(Long.parseLong(mp.get("qid").toString()));
            Long codingMarksId = assessmentRepo.findCodingMarksIdByAssessmentId(Long.parseLong(mp.get("aid").toString()));
            AssessmentCodingDetails codeEvaluator = new AssessmentCodingDetails();
            codeEvaluator.setUser_id(Long.parseLong(mp.get("id").toString()));
            codeEvaluator.setQuestion_id(Long.parseLong(mp.get("qid").toString()));
            codeEvaluator.setAssessment_id(Long.parseLong(mp.get("aid").toString()));
            codeEvaluator.setCode_source(bytes);
            if (expectedOutput != null && codingMarksId != null && mp.get("stdout").toString() != null) {
                List<Boolean> finalResult = new ArrayList<>();
                String[] expectedOutputs = expectedOutput.trim().split("\n");
                String[] stdOuts = mp.get("stdout").toString().trim().split("\n");
                if (expectedOutputs.length == stdOuts.length) {
                    for (int i = 0; i <= (expectedOutputs.length) - 1; i++) {
                        finalResult.add(stdOuts[i].toLowerCase().replace(",", " ").contains(expectedOutputs[i].toLowerCase().replace(",", " ")));
                    }
                }
                if (!finalResult.isEmpty() && !finalResult.contains(false)) {
                    AssessmentCodingMarks codingMarks = codingMarkRepo.findById(codingMarksId).get();
                    codeEvaluator.setCompile_score(codingMarks.getCompileMarks());
                    codeEvaluator.setRun_score(codingMarks.getRunMarks());
                }
            }
            codeDetailsRepository.save(codeEvaluator);
            resultMap.put("status", "success");
        }
        logger.info("CompilerServiceImpl -> saveSourceCode() :: Method execution complete with response data ::  " + resultMap);
        return resultMap;
    }

}
