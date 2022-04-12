package com.realcoderz.assessmentservice.util;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/*
 * @author Shubham Mishra
 */
@Component
public class GcpBucketUtil {
    static final Logger logger = LoggerFactory.getLogger(GcpBucketUtil.class);
    static String bucketName = "rcpublicimages";

    @Value("${project_id}")
    static String projectId;

    // @Value("${bucket_id}")
    // static String bucketName;

    static Storage storage = StorageOptions.getDefaultInstance().getService();

    public static String uploadObject(MultipartFile fileStream,String type) throws IOException {
        
        Boolean hasError = false;
        String path = null;
        try {

            String object = "" + fileStream.getOriginalFilename();

            String fileFormat = object.substring(object.lastIndexOf(".") + 1);

            byte[] file = fileStream.getBytes();

            BlobId blobId = BlobId.of(bucketName, object);

            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                    .setContentType(type.equals("image") ? "image/"+fileFormat:"application/" + fileFormat).setCacheControl("cache-control: public, max-age=0, no-cache").build();

            storage.create(blobInfo, file);

            path = "https://storage.googleapis.com/" + bucketName + "/" + object;

        } catch (IOException | NumberFormatException e) {
            logger.error("Exception @ GcpBucketUtil.java : ", e);
            hasError = true;
        }
        if (path == null || hasError == true) {
            return "error";
        } else {
            return path;
        }

    }

    public static void deleteObject(String objectName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        storage.delete(bucketName, objectName);
        logger.info("Object " + objectName + " was deleted from " + bucketName);
    }
}
