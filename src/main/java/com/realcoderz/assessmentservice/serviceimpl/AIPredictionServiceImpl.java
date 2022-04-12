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
import com.realcoderz.assessmentservice.domain.StudentMaster;
import com.realcoderz.assessmentservice.repository.StudentMasterRepository;
import com.realcoderz.assessmentservice.service.AIPredictionService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/*
 * @author Prateek, Shubham Mishra
 */
@Service
public class AIPredictionServiceImpl implements AIPredictionService {

    static final Logger logger = LoggerFactory.getLogger(AIPredictionServiceImpl.class);

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private StudentMasterRepository studentMasterRepository;


    @Override
    public String predictAIScore(Long studentId) {
        StudentMaster studentMaster = studentMasterRepository.findByStudentId(studentId);
        if (studentMaster != null) {
            if (profile.split(",")[0].equalsIgnoreCase("prod")) {
                String data;
                try {
                    do {
                        data = studentMasterRepository.getDataToPredictAIScore(studentId);
                        logger.info("student_data: " + data);
                    } while (data == null);
                    Storage storage = StorageOptions.getDefaultInstance().getService();
                    List<com.google.protobuf.Value> values = new ArrayList<>();

                    String[] predictData = data.split(",");
                    //int predictDataLength = predictData.length;
                    for (int i = 0; i < predictData.length; i++) {
                        if (i == 0 && i == 1 && i == 2 && i == 3 && i == 5) {
                            int roundVal = (int) Math.round(Double.valueOf(predictData[i]));
                            values.add(com.google.protobuf.Value.newBuilder()
                                    .setNumberValue(roundVal).build());
                        } else {
                            values.add(com.google.protobuf.Value.newBuilder().setStringValue(predictData[i]).build());
                        }
                    }
                    logger.info("PredictAIScore : setAi_score");

                    Double x = predict("assessment-service", "TBL1286663899986264064", values);
                    studentMaster.setAi_score(x);
                } catch (Exception ex) {
                    logger.error("Getting Exception: " + ex);
                    return "exception";
                }
            } else {
                logger.info("Setting score zero");
                studentMaster.setAi_score(0.00);
            }
            studentMasterRepository.save(studentMaster);
            return "success";
        } else {
            return "error";
        }
    }

    public Double predict(String projectId, String modelId, List<com.google.protobuf.Value> values) throws IOException {
        Double aiResponseValue = 0.0;
        try ( PredictionServiceClient client = PredictionServiceClient.create()) {
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
        return aiResponseValue;
    }
}
