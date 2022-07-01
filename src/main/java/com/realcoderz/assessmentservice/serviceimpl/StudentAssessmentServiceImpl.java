/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.google.cloud.automl.v1beta1.AnnotationPayload;
import com.google.cloud.automl.v1beta1.ExamplePayload;
import com.google.cloud.automl.v1beta1.ModelName;
import com.google.cloud.automl.v1beta1.PredictRequest;
import com.google.cloud.automl.v1beta1.PredictResponse;
import com.google.cloud.automl.v1beta1.PredictionServiceClient;
import com.google.cloud.automl.v1beta1.Row;
import com.google.cloud.automl.v1beta1.TablesAnnotation;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.realcoderz.assessmentservice.domain.AssessmentCodingDetails;
import com.realcoderz.assessmentservice.domain.AssessmentCodingIssues;
import com.realcoderz.assessmentservice.domain.AssessmentCodingMarks;
import com.realcoderz.assessmentservice.domain.CodingQuestionTestCases;
import com.realcoderz.assessmentservice.domain.StudentAssessment;
import com.realcoderz.assessmentservice.domain.StudentCertification;
import com.realcoderz.assessmentservice.domain.StudentMaster;
import com.realcoderz.assessmentservice.repository.AssessmentCodingDetailsRepository;
import com.realcoderz.assessmentservice.repository.AssessmentCodingMarksRepository;
import com.realcoderz.assessmentservice.repository.AssessmentCreationRepository;
import com.realcoderz.assessmentservice.repository.CodingQuestionTestCaseRepository;
import com.realcoderz.assessmentservice.repository.StudentAssessmentRepository;
import com.realcoderz.assessmentservice.repository.StudentCertificateRepository;
import com.realcoderz.assessmentservice.repository.StudentMasterRepository;
import com.realcoderz.assessmentservice.service.StudentAssessmentService;
import com.realcoderz.assessmentservice.util.CommonCompiler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.LoggerFactory;
import org.sonar.wsclient.SonarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 *
 * @author Aman Bansal, Bipul Kr Singh, Shubham Mishra
 */
@Service
public class StudentAssessmentServiceImpl implements StudentAssessmentService {

    static final org.slf4j.Logger logger = LoggerFactory.getLogger(StudentAssessmentServiceImpl.class);

    @Autowired
    private StudentMasterRepository studentMasterRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    StudentAssessmentRepository studentAssessmentRepo;

    @Autowired
    AssessmentCreationRepository assessmentCreationRepository;

    @Autowired
    AssessmentCodingMarksRepository codeMarksRepository;

    @Autowired
    AssessmentCodingDetailsRepository codeDetailsRepository;

    @Autowired
    private CodingQuestionTestCaseRepository testCaseRepo;

    @Value("${compiler_url}")
    private String compilerUrl;

    @Value("${sonar_url}")
    private String sonarUrl;

    @Value("${sonar_login}")
    private String sonarLogin;

    @Value("${sonar_password}")
    private String sonarPassword;

    @Value("${organization_id}")
    private String organizationId;

    @Autowired
    private StudentCertificateRepository certificateRepository;

    @Override
    public Map getTopicScores(LinkedCaseInsensitiveMap givenAssessments) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("haveCoding", 0);
        if (givenAssessments.containsKey("student_assessment_id")) {
            LinkedCaseInsensitiveMap details = new LinkedCaseInsensitiveMap();
            List<LinkedCaseInsensitiveMap> userAssessments = new LinkedList<>();
            List<LinkedCaseInsensitiveMap> correctQuestions = studentMasterRepository.correctQuestions(Long.parseLong(givenAssessments.get("student_assessment_id").toString()));
            details.put("user_assessment_id", givenAssessments.get("student_assessment_id").toString());
            details.put("correctQuestionId", correctQuestions);
            details.put("totalQuestion", givenAssessments.get("total_questions").toString());
            details.put("assessment_id", givenAssessments.get("assessment_id").toString());
            userAssessments.add(details);
            Map<String, Integer> topicTotalCount = new HashMap();
            Map<String, Integer> topicCorrectTotalCount = new HashMap();
            userAssessments.stream().forEach(userAss -> {
                List<LinkedCaseInsensitiveMap> correctQuestionIds = (List<LinkedCaseInsensitiveMap>) userAss.get("correctQuestionId");
                Map<String, List<String>> topicWithIds = new HashMap<>();
                correctQuestionIds.stream().forEach(qId -> {
                    List<LinkedCaseInsensitiveMap> list = studentMasterRepository.findTnameAndQCount(Long.parseLong(qId.get("question_id").toString()));
                    if (!topicWithIds.containsKey(list.get(0).get("topicName").toString())) {
                        List<String> questionCount = new ArrayList<>();
                        questionCount.add(list.get(0).get("topicId").toString());
                        topicWithIds.put(list.get(0).get("topicName").toString(), questionCount);
                    } else {
                        topicWithIds.get(list.get(0).get("topicName").toString()).add(list.get(0).get("topicId").toString());
                    }
                });
                topicWithIds.entrySet().stream().forEach(topic -> {
                    List<LinkedCaseInsensitiveMap> totalCount = studentMasterRepository.totalQuestionOfTopic(Long.parseLong(topic.getValue().get(0)), Long.parseLong(userAss.get("assessment_id").toString()));

                    if (!topicCorrectTotalCount.containsKey(topic.getKey())) {
                        topicCorrectTotalCount.put(topic.getKey(), topic.getValue().size());
                    } else {
                        topicCorrectTotalCount.put(topic.getKey(), (topicCorrectTotalCount.get(topic.getKey()) + topic.getValue().size()));
                    }

                    if (!topicTotalCount.containsKey(topic.getKey())) {
                        topicTotalCount.put(topic.getKey(), Integer.parseInt(totalCount.get(0).get("topicCount").toString()));
                    } else {
                        topicTotalCount.put(topic.getKey(), (topicTotalCount.get(topic.getKey()) + Integer.parseInt(totalCount.get(0).get("topicCount").toString())));
                    }
                });

            });
            List<LinkedCaseInsensitiveMap> average = new LinkedList<>();
            topicCorrectTotalCount.entrySet().stream().forEach(correct -> {
                topicTotalCount.entrySet().stream().forEach(total -> {
                    LinkedCaseInsensitiveMap map = new LinkedCaseInsensitiveMap();
                    if (correct.getKey().equalsIgnoreCase(total.getKey())) {
                        map.put(total.getKey(), total.getKey());
                        if (Float.parseFloat(total.getValue().toString()) != 0) {
                            map.put("average", Math.round(((Float.parseFloat(correct.getValue().toString()) / Float.parseFloat(total.getValue().toString())) * 100)));
                            map.put("total_questions", total.getValue().toString());
                            map.put("correct_questions", correct.getValue().toString());
                            average.add(map);
                        }

                    }
                });
            });
            List<LinkedCaseInsensitiveMap> totalQuestionsIds = studentMasterRepository.totalQuestionsIds(Long.parseLong(givenAssessments.get("student_assessment_id").toString()));
            Set<String> wrongTopics = new HashSet<>();
            totalQuestionsIds.stream().forEach(wt -> {
                LinkedCaseInsensitiveMap value = studentMasterRepository.findTopicName(Long.parseLong(wt.get("question_id").toString()));
                if (value.containsKey("questionId") && value.containsKey("topicName") && value.containsKey("questionTypeId")) {
                    if (value.get("questionTypeId") != null) {
                        if (value.get("questionTypeId").toString().equals("1")) {
                            if (value.get("topicName") != null && value.get("topicName") != "") {
                                wrongTopics.add(value.get("topicName").toString());
                            }
                        } else if (value.get("questionTypeId").toString().equals("2")) {
                            resultMap.put("haveCoding", 1);
                        }
                    }
                }
            });
            List<LinkedCaseInsensitiveMap> returnList = new LinkedList<>();
            average.stream().forEach(avg -> {
                topicTotalCount.entrySet().stream().forEach(t -> {
                    LinkedCaseInsensitiveMap mp = new LinkedCaseInsensitiveMap();
                    mp.put("topicName", avg.get(t.getKey()));
                    mp.put("average", avg.get("average") != null ? avg.get("average").toString() : "0");
                    mp.put("total_questions", avg.get("total_questions") != null ? avg.get("total_questions").toString() : "0");
                    mp.put("correct_questions", avg.get("correct_questions") != null ? avg.get("correct_questions").toString() : "0");
                    if (mp.get("topicName") != null) {
                        returnList.add(mp);
                        if (wrongTopics.contains(mp.get("topicName").toString())) {
                            wrongTopics.remove(mp.get("topicName").toString());
                        }

                    }
                });
            });
            wrongTopics.stream().forEach(wt -> {
                LinkedCaseInsensitiveMap mp = new LinkedCaseInsensitiveMap();
                Integer total_questions = studentMasterRepository.findTotalQuestion(Long.parseLong(givenAssessments.get("assessment_id").toString()), wt);
                mp.put("topicName", wt);
                mp.put("average", "0");
                mp.put("correct_questions", "0");
                mp.put("total_questions", total_questions);
                returnList.add(mp);
            });
            resultMap.put("status", "success");
            resultMap.put("msg", "Fetched topic wise performance for a particular candidate successfully.!");
            resultMap.put("report", returnList);
        }
        return resultMap;
    }

    private String getLanguageIdByName(String language) {
        String id = null;
        if (language.equalsIgnoreCase("java")) {
            return id = "62";
        } else if (language.equalsIgnoreCase("python")) {
            id = "71";
        } else if (language.equalsIgnoreCase("php")) {
            id = "68";
        } else if (language.equalsIgnoreCase("javascript")) {
            id = "63";
        } else if (language.equalsIgnoreCase("c#")) {
            id = "51";
        } else if (language.equalsIgnoreCase("kotlin")) {
            id = "78";
        } else if (language.equalsIgnoreCase("objective c")) {
            id = "79";
        } else if (language.equalsIgnoreCase("r")) {
            id = "80";
        } else if (language.equalsIgnoreCase("rust")) {
            id = "73";
        } else if (language.equalsIgnoreCase("scala")) {
            id = "81";
        }
        return id;

    }

    private AssessmentCodingDetails validatingTestCases(Long qid, AssessmentCodingDetails acd, String lang, AssessmentCodingMarks acm) {
        List<CodingQuestionTestCases> list = testCaseRepo.findByQuestionMaster(qid);
        int testCaseSquence = 0;
        LinkedHashMap map = new LinkedHashMap();
        String sourceCode = new String(acd.getCode_source()).replaceAll("\u00a0", " ");
        map.put("source_code", sourceCode);
        map.put("language_id", this.getLanguageIdByName(lang));
        CommonCompiler compiler = new CommonCompiler();
        if (!list.isEmpty()) {
            for (CodingQuestionTestCases a : list) {
                map.put("stdin", a.getInput());
                map.put("expected_output", a.getExpectedOutput());
                LinkedHashMap response = compiler.compilerAndRun(map, compilerUrl);
                if (!response.isEmpty()) {
                    if (response.get("description").toString().equalsIgnoreCase("accepted")) {
                        if (testCaseSquence < 1) {
                            testCaseSquence++;
                            acd.setTestCase1Score(acm.getTestCase1Marks());
                        } else if (testCaseSquence < 2 && testCaseSquence > 0) {
                            testCaseSquence++;
                            acd.setTestCase2Score(acm.getTestCase2Marks());
                        } else if (testCaseSquence < 3 && testCaseSquence > 1) {
                            acd.setTestCase3Score(acm.getTestCase3Marks());
                        }
                    } else {
                        testCaseSquence++;
                    }
                }
            }
        } else {
            acd.setTestCase1Score(acm.getTestCase1Marks());
            acd.setTestCase2Score(acm.getTestCase2Marks());
            acd.setTestCase3Score(acm.getTestCase3Marks());
        }
        return acd;
    }

    @Override
    public void sonarScannerInterval() {
        if (profile.equalsIgnoreCase("prod")) {
            StudentAssessment studentAssessment = studentAssessmentRepo.getListForSonarScanner();
            try {
                if (studentAssessment != null) {
                    LinkedCaseInsensitiveMap assessment = assessmentCreationRepository.findByAssessmentId(studentAssessment.getAssessment_id());
                    if (assessment != null && !assessment.isEmpty()) {
                        LinkedCaseInsensitiveMap lang = assessmentCreationRepository.getLangName(Long.parseLong(assessment.get("language_id").toString()));
                        String language = String.valueOf(lang.get("language")).trim();
                        List<Long> ids = assessmentCreationRepository.findQuestionIdByAssessmentId(studentAssessment.getAssessment_id());
                        int totalCodingQuestion = ids.size();
                        int totalCodingScore = 0;
                        Optional<AssessmentCodingMarks> acm = codeMarksRepository.findById(Long.parseLong(assessment.get("codingmarks_id").toString()));
                        if (acm.isPresent()) {
                            for (Long qid : ids) {
                                long uid = Long.parseLong(studentAssessment.getStudent_id().toString());
                                List<AssessmentCodingDetails> listOfcodingDetails = codeDetailsRepository.findByUserQuestionId(uid, qid);
                                if (listOfcodingDetails != null && !listOfcodingDetails.isEmpty()) {
                                    AssessmentCodingDetails codingDetails = listOfcodingDetails.get(0);
                                    byte[] sourceCode = codingDetails.getCode_source();
                                    int score = 0, critical = 0, major = 0, minor = 0;
                                    if (codingDetails.getRun_score() > 0) {
                                        codingDetails = this.validatingTestCases(qid, codingDetails, language, acm.get());
                                        score = codingDetails.getTestCase1Score() + codingDetails.getTestCase2Score() + codingDetails.getTestCase3Score();
                                        if (score > 0) {
                                            String filename;
                                            if (language.equalsIgnoreCase("java")) {
                                                filename = "JavaCode.java";
                                            } else if (language.equalsIgnoreCase("python")) {
                                                filename = "PythonCode.py";
                                            } else if (language.equalsIgnoreCase("php")) {
                                                filename = "PhpCode.php";
                                            } else if (language.equalsIgnoreCase("javascript")) {
                                                filename = "JSCode.js";
                                            } else if (language.equalsIgnoreCase("c#")) {
                                                filename = "CsharpCode.cs";
                                            } else if (language.equalsIgnoreCase("kotlin")) {
                                                filename = "KotlinCode.kotlin";
                                            } else {
                                                filename = "ScalaCode.scala";
                                            }
                                            String pathDir = "sourcecode/" + studentAssessment.getStudent_id().toString() + qid;
                                            String pathFile = "sourcecode/" + studentAssessment.getStudent_id().toString() + qid + "/" + filename;
                                            File dir = new File(pathDir);
                                            dir.mkdirs();
                                            File file = new File(pathFile);
                                            file.createNewFile();
                                            logger.info("file created at  -> " + file.getAbsolutePath());
                                            try (FileWriter fw = new FileWriter(pathFile)) {
                                                fw.write(new String(sourceCode, StandardCharsets.UTF_8));
                                            }
                                            if (file.exists()) {
                                                Process p;
                                                if (language.equalsIgnoreCase("java")) {
                                                    p = processBuilder(pathDir, "java", studentAssessment.getStudent_id().toString() + qid);
                                                } else if (language.equalsIgnoreCase("python")) {
                                                    p = processBuilder(pathDir, "py", studentAssessment.getStudent_id().toString() + qid);
                                                } else if (language.equalsIgnoreCase("php")) {
                                                    p = processBuilder(pathDir, "php", studentAssessment.getStudent_id().toString() + qid);
                                                } else if (language.equalsIgnoreCase("javascript")) {
                                                    p = processBuilder(pathDir, "js", studentAssessment.getStudent_id().toString() + qid);
                                                } else if (language.equalsIgnoreCase("c#")) {
                                                    p = processBuilder(pathDir, "cs", studentAssessment.getStudent_id().toString() + qid);
                                                } else if (language.equalsIgnoreCase("kotlin")) {
                                                    p = processBuilder(pathDir, "kotlin", studentAssessment.getStudent_id().toString() + qid);
                                                } else {
                                                    p = processBuilder(pathDir, "scala", studentAssessment.getStudent_id().toString() + qid);
                                                }

                                                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                                while (true) {
                                                    String log = r.readLine();
                                                    logger.info("Scanner_Log: " + log);
                                                    if (log == null) {
                                                        SonarClient client = SonarClient.builder().url(sonarUrl).login(sonarLogin).password(sonarPassword).build();
                                                        Map<String, Object> params = new HashMap<>();
                                                        params.put("projects", "code_" + studentAssessment.getStudent_id().toString() + qid);
                                                        String res = client.get("/api/projects/search", params);
                                                        JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                                                        Integer exist = obj.get("paging").getAsJsonObject().get("total").getAsInt();
                                                        int times = 1;
                                                        while (exist == 0) {
                                                            while (times < 20) {
                                                                Thread.sleep(3000);
                                                                times++;
                                                                res = client.get("/api/projects/search", params);
                                                                obj = new JsonParser().parse(res).getAsJsonObject();
                                                                exist = obj.get("paging").getAsJsonObject().get("total").getAsInt();
                                                                if (times == 19) {
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if (exist > 0) {
                                                            params.clear();
                                                            Thread.sleep(30000);
                                                            params.put("componentKeys", "code_" + studentAssessment.getStudent_id().toString() + qid);
                                                            res = client.get("/api/issues/search", params);
                                                            logger.info("res /api/issues/search code_" + studentAssessment.getStudent_id().toString() + qid + " -> " + res);
                                                            Set<AssessmentCodingIssues> codeIssues = new HashSet<>();
                                                            JsonElement root = new JsonParser().parse(res);
                                                            JsonArray object = root.getAsJsonObject().get("issues").getAsJsonArray();
                                                            Gson gson = new Gson();
                                                            List<Map<String, Object>> issueList = gson.fromJson(object, List.class);
                                                            logger.info("issue list " + issueList.size());
                                                            for (int i = 0; i < issueList.size(); i++) {
                                                                AssessmentCodingIssues aci = new AssessmentCodingIssues();
                                                                aci.setIssue_desc(issueList.get(i).get("message") + "");
                                                                aci.setIssue_type(issueList.get(i).get("type") + "");
                                                                if (issueList.get(i).get("line") != null) {
                                                                    aci.setIssue_line_no((int) Math.round(Double.parseDouble(issueList.get(i).get("line").toString())));
                                                                }
                                                                codeIssues.add(aci);
                                                                if (issueList.get(i).get("severity").toString().equalsIgnoreCase("CRITICAL")) {
                                                                    critical++;
                                                                } else if (issueList.get(i).get("severity").toString().equalsIgnoreCase("MAJOR")) {
                                                                    major++;
                                                                } else if (issueList.get(i).get("severity").toString().equalsIgnoreCase("MINOR")) {
                                                                    minor++;
                                                                }
                                                            }
                                                            if (minor <= acm.get().getMinorIssues()) {
                                                                score += acm.get().getMinorIssuesMarks();
                                                            }
                                                            if (critical <= acm.get().getCriticalIssues()) {
                                                                score += acm.get().getCriticalIssuesMarks();
                                                            }
                                                            if (major <= acm.get().getMajorIssues()) {
                                                                score += acm.get().getMajorIssuesMarks();
                                                            }
                                                            score += codingDetails.getRun_score() + codingDetails.getCompile_score();
                                                            totalCodingScore += score;
                                                            codingDetails.setCritical_issues(critical);
                                                            codingDetails.setMajor_issues(major);
                                                            codingDetails.setMinor_issues(minor);
                                                            codingDetails.setCoding_score(score);
                                                            codingDetails.setIssuesList(codeIssues);
                                                            codingDetails.setScan(true);
                                                            codeDetailsRepository.save(codingDetails);
                                                            listOfcodingDetails.stream().forEach(list -> {
                                                                list.setScan(true);
                                                            });
                                                            codeDetailsRepository.saveAll(listOfcodingDetails);
                                                            params.clear();
                                                            params.put("project", "code_" + studentAssessment.getStudent_id().toString() + qid);
                                                            client.post("/api/projects/delete", params);
                                                            FileUtils.deleteDirectory(dir);
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        } else {
                                            totalCodingScore += score;
                                            codingDetails.setCritical_issues(critical);
                                            codingDetails.setMajor_issues(major);
                                            codingDetails.setMinor_issues(minor);
                                            codingDetails.setCoding_score(score);
                                            codingDetails.setScan(true);
                                            codeDetailsRepository.save(codingDetails);
                                            listOfcodingDetails.stream().forEach(list -> {
                                                list.setScan(true);
                                            });
                                            codeDetailsRepository.saveAll(listOfcodingDetails);
                                        }
                                    } else {
                                        totalCodingScore += score;
                                        codingDetails.setCritical_issues(critical);
                                        codingDetails.setMajor_issues(major);
                                        codingDetails.setMinor_issues(minor);
                                        codingDetails.setCoding_score(score);
                                        codingDetails.setScan(true);
                                        codeDetailsRepository.save(codingDetails);
                                        listOfcodingDetails.stream().forEach(list -> {
                                            list.setScan(true);
                                        });
                                        codeDetailsRepository.saveAll(listOfcodingDetails);

                                    }
                                }
                            }
                            int totalCodingMarks = 0;
                            double codingPercentage = 0;
                            int totalMarks = studentAssessment.getTotal_no_of_questions() + (totalCodingQuestion * 5);
                            studentAssessment.setTotalCodingQuestion(totalCodingQuestion);
                            if (totalCodingQuestion != 0 && totalCodingScore != 0) {
                                totalCodingMarks = (totalCodingQuestion * acm.get().getTotalMarks());
                                codingPercentage = (totalCodingScore * 100) / (totalCodingMarks);
                                studentAssessment.setCodingPercentage(codingPercentage);
                                studentAssessment.setTotalCodingScore(totalCodingScore);
                            }
                            if (totalCodingScore != 0) {
                                double totalScore = studentAssessment.getCorrect_questions() + ((5 * totalCodingQuestion * codingPercentage) / 100);
                                double totalPercentage = (totalScore * 100) / totalMarks;
                                studentAssessment.setTotalPercentage(totalPercentage);
                            }
                            if (totalCodingQuestion != 0 && totalCodingScore == 0 && studentAssessment.getCorrect_questions() != 0) {
                                double totalScore = studentAssessment.getCorrect_questions();
                                double totalPercentage = (totalScore * 100) / totalMarks;
                                studentAssessment.setTotalPercentage(totalPercentage);
                            }
                            if (totalCodingScore == 0 && studentAssessment.getCorrect_questions() == 0) {
                                studentAssessment.setTotalPercentage(0);
                            }
                            studentAssessment.setScan(true);
                            this.save(studentAssessment);

                            getDataToPredictAIScore(studentAssessment.getStudent_id());
                        } else {
                            studentAssessment.setScan(true);
                            save(studentAssessment);
                            getDataToPredictAIScore(studentAssessment.getStudent_id());
                            logger.info("Assessment coding marks does not exist with this assessment: " + studentAssessment.getAssessment_id());
                        }
                    } else {
                        studentAssessment.setScan(true);
                        save(studentAssessment);
                        getDataToPredictAIScore(studentAssessment.getStudent_id());
                        logger.info("Assessment does not exist with this id: " + studentAssessment.getAssessment_id());
                    }
                } else {
                    logger.info("Cheked for sonar scanner but there is no file to scan!");
                }
            } catch (JsonSyntaxException | IOException | InterruptedException | NumberFormatException ex) {
                Logger.getLogger(StudentAssessmentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
        }
    }

    private Process processBuilder(String pathDir, String language, String id) throws IOException {
        return new ProcessBuilder("sh", "-c", "sonar-scanner -Dsonar.host.url=" + sonarUrl + " -Dsonar.login=" + sonarLogin + " -Dsonar.password=" + sonarPassword
                + "    -Dsonar.sources=" + pathDir + " "
                + "    -Dsonar.language=" + language + " "
                + "    -Dsonar.java.binaries=/classes "
                + "    -Dsonar.projectKey=code_" + id).start();
    }

    private StudentAssessment save(StudentAssessment studentAssessment) {
        return studentAssessmentRepo.save(studentAssessment);
    }

    public Double predict(String projectId, String modelId, List<com.google.protobuf.Value> values) throws IOException {
        logger.info("StudentAssessmentServiceImpl -> predict() ::  Method execution started.");
        Double aiResponseValue = 0.0;
        try (PredictionServiceClient client = PredictionServiceClient.create()) {
            ModelName name = ModelName.of(projectId, "us-central1", modelId);
            Row row = Row.newBuilder().addAllValues(values).build();
            ExamplePayload payload = ExamplePayload.newBuilder().setRow(row).build();
            PredictRequest request = PredictRequest.newBuilder().setName(name.toString()).setPayload(payload)
                    .putParams("feature_importance", "true").build();
            PredictResponse response = client.predict(request);
            for (AnnotationPayload annotationPayload : response.getPayloadList()) {
                TablesAnnotation tablesAnnotation = annotationPayload.getTables();
                aiResponseValue = Math.round(tablesAnnotation.getValue().getNumberValue() * 100.0) / 100.0;
            }
        }
        logger.info("StudentAssessmentServiceImpl -> predict() ::  Method execution completed with response data :: " + aiResponseValue);
        return aiResponseValue;
    }

    private void getDataToPredictAIScore(Long studentId) throws IOException {
        logger.info("StudentAssessmentServiceImpl -> getDataToPredictAIScore() ::  Method execution start with request data  ::  " + studentId);
        StudentMaster studentMaster = studentMasterRepository.findByStudentId(studentId);
        if (studentMaster != null) {
            if (profile.equalsIgnoreCase("prod")) {
                String data;
                try {
                    do {
                        data = studentMasterRepository.getDataToPredictAIScore(studentId);
                        logger.info("student_data: " + data);
                    } while (data == null);
                    Storage storage = StorageOptions.getDefaultInstance().getService();
                    List<com.google.protobuf.Value> values = new ArrayList<>();
                    String[] predictData = data.split(",");
                    for (int i = 0; i < predictData.length; i++) {
                        if (i == 0 && i == 1 && i == 2 && i == 3 && i == 5) {
                            int roundVal = (int) Math.round(Double.valueOf(predictData[i]));
                            values.add(com.google.protobuf.Value.newBuilder()
                                    .setNumberValue(roundVal).build());
                        } else {
                            values.add(com.google.protobuf.Value.newBuilder().setStringValue(predictData[i]).build());
                        }
                    }
                    Double x = predict("realcoderz-production", "TBL3075959645005676544", values);
                    studentMaster.setAi_score(x);
                    if (x > 60 && Double.parseDouble(predictData[5]) > 60 && studentMaster.getOrganizationId() == Long.parseLong(organizationId)) {
                        StudentCertification sc = certificateRepository.findByStudentId(studentId);
                        if (sc == null) {
                            sc = new StudentCertification();
                            sc.setAssignedOn(new Date());
                            sc.setCertificateLevel("level 1");
                            sc.setCertificateUrl("https://storage.googleapis.com/rcpublicimages/CA.png");
                            sc.setCertificateNumber("RCIT" + studentId + "00" + Math.round(Math.random() * 1000));
                            sc.setStudentId(studentId);
                            certificateRepository.save(sc);
                        }
                    }
                } catch (IOException | NumberFormatException ex) {
                    logger.error("Getting Exception: " + ex);
                    logger.error("StudentAssessmentServiceImpl -> getDataToPredictAIScore() ::  NumberFormateException " + ex);

                }
            } else {
                logger.info("Setting score zero");
                studentMaster.setAi_score(0.00);
            }
            studentMasterRepository.save(studentMaster);
        }
        logger.info("StudentAssessmentServiceImpl -> getDataToPredictAIScore() ::  Method execution completed ");

    }
}
