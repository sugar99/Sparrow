package com.sparrow.demo.utils;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.sparrow.demo.utils.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author allen
 */
@Component
public class OssUtils {

    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;

    @Autowired
    private FileUtils fileUtils;

    public void deleteFileInOSS(String objectName){
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        ossClient.deleteObject(bucketName,objectName);
        ossClient.shutdown();
    }

    public String getObjectUrl(String objectName){
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        // 一百年：3600L * 1000 * 24 * 365 * 100
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000);
        String url = ossClient.generatePresignedUrl(bucketName,objectName,expiration).toString();
        return url;
    }

    public String uploadFile(File inputFile) {
        String path = null;
        try {
            path = fileUtils.getFileLogicPath(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String objectName = path + inputFile.getName().trim();

        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ossClient.putObject(bucketName,objectName,inputStream);
        ossClient.shutdown();
        return objectName;
    }

    public Map<String,Object> uploadFileToOss(File inputFile) throws FileNotFoundException {
        Map<String, Object> fileInfo = new HashMap<>(2);
        String path = fileUtils.getFileLogicPath(inputFile);
        String objectName = path + inputFile.getName().trim();

        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        InputStream inputStream = new FileInputStream(inputFile);
        ossClient.putObject(bucketName,objectName,inputStream);

        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100);
        String url = ossClient.generatePresignedUrl(bucketName,objectName,expiration).toString();

        ossClient.shutdown();

        fileInfo.put("file_path", objectName);
        fileInfo.put("file_url", url);

        return fileInfo;
    }

    public void DownloadFileFromOSS(String fileName, String fileObjectName, HttpServletResponse response){
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        OSSObject ossObject = ossClient.getObject(bucketName,fileObjectName);

        BufferedInputStream bufferedInputStream = null;
        OutputStream responseOutput = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            String contentDisposition = "attachment; filename=" + fileName;
            response.setHeader("Content-Disposition", contentDisposition);

            responseOutput = response.getOutputStream();
            byte[] buff = new byte[1024];
            bufferedInputStream = new BufferedInputStream(ossObject.getObjectContent());

            int length;
            while ((length = bufferedInputStream.read(buff)) != -1){
                responseOutput.write(buff,0,length);
                responseOutput.flush();
            }

        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null){
                ossClient.shutdown();
            }

            if (bufferedInputStream != null){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (responseOutput != null){
                try {
                    responseOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public File getFileFromOSS(String objectName, String path){
        File file = new File(path);
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        ossClient.getObject(new GetObjectRequest(bucketName, objectName),file);
        return file;
    }

    public BufferedInputStream getFileStreamFromOSS(String fileObjectName){
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        OSSObject ossObject = ossClient.getObject(bucketName,fileObjectName);
        return new BufferedInputStream(ossObject.getObjectContent());
    }

    public Map<String, Object> uploadThumbnailToOss(File file) throws FileNotFoundException {
        Map<String, Object> thumbnailInfo = new HashMap<>(2);

        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);

        String path = fileUtils.getFileLogicPath(file);
        String finalPath = "";
        String[] strings = path.split("/");
        for (int i = 0 ; i < strings.length - 1; i++){
            finalPath += strings[i] + "/";
        }
        finalPath += "thumbnail/";

//        String objectName = "admin/allen/thumbnail/" + file.getName().trim();
        String objectName = finalPath + file.getName().trim();

        InputStream inputStream = new FileInputStream(file);
        ossClient.putObject(bucketName,objectName,inputStream);

        String url = "";
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100);
        url = ossClient.generatePresignedUrl(bucketName,objectName,expiration).toString();

        ossClient.shutdown();

        thumbnailInfo.put("thumbnail_path",objectName);
        thumbnailInfo.put("thumbnail_url",url);

        return thumbnailInfo;
    }
}
