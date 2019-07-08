package com.micerlab.sparrow.utils;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MinioUtil {
//    @Value("${minio.endpoint}")
    private String endpoint;
//    @Value("${minio.accessKey}")
    private String accessKey;
//    @Value("${minio.secretKey}")
    private String secretKey;
//    @Value("${minio.bucketName}")
    private String bucketName;

    private MinioClient minioClient;

    public String getObjectUrl(String objectName) {
        String url = null;
        try {
            minioClient = new MinioClient("http://" + endpoint, accessKey, secretKey);
            url = minioClient.presignedGetObject(bucketName, objectName, 60*60*24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}
