package com.micerlab.sparrow.utils;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.micerlab.sparrow.SparrowApplicationTests;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AliyunOssUtilTest extends SparrowApplicationTests {

    @Test
    public void uploadToOss(){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAInhMpeCqDsM4c";
        String accessKeySecret = "RXxpqMZIlo7bwTIbX3MiAGgxdKX4v7";
        String bucketName = "douban-test";
        String objectName = "image/a.jpg";

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {
            // 生成签名URL。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000);
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.PUT);
            // 设置过期时间。
            request.setExpiration(expiration);
            // 设置Content-Type。
            request.setContentType("application/octet-stream");
            // 添加用户自定义元信息。
            request.addUserMetadata("author", "aliy");
            // 生成签名URL（HTTP PUT请求）。
            URL signedUrl = ossClient.generatePresignedUrl(request);
            System.out.println("signed url for putObject: " + signedUrl);

            // 使用签名URL发送请求。
            File f = new File("C:\\Users\\xiaobin\\Pictures\\Camera Roll\\timg.jpg");
            FileInputStream fin = new FileInputStream(f);
            // 添加PutObject请求头。
            Map<String, String> customHeaders = new HashMap<String, String>();
            customHeaders.put("Content-Type", "application/octet-stream");
            customHeaders.put("x-oss-meta-author", "aliy");

            PutObjectResult result = ossClient.putObject(signedUrl, fin, f.length(), customHeaders);
        }catch (Exception e){
            e.printStackTrace();
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}