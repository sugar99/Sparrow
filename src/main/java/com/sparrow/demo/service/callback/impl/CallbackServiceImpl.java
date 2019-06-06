package com.sparrow.demo.service.callback.impl;

import com.sparrow.demo.service.file.FileService;
import net.sf.json.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.sparrow.demo.service.callback.CallbackService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author allen
 */
@Service
public class CallbackServiceImpl implements CallbackService {
    @Value("${app.server.host}")
    private String appServerHost;
    @Value("${app.server.port}")
    private String appServerPort;
    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;

    @Autowired
    private FileService fileService;

    private static final Logger LOG = LoggerFactory.getLogger(CallbackServiceImpl.class);

    @Override
    public void doPolicyGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Start get policy");
        // 请填写您的AccessKeyId。
//        String accessId = "LTAItVC5Mg2ZFnVz";
        // 请填写您的AccessKeySecret。
//        String accessKey = "CrD1Bv78OiCgqJnAQw5kp25SNfMPcq";
        // 请填写您的 endpoint。
//        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
        // 请填写您的 bucketname 。
//        String bucket = "http-test-sparrow";
        // host的格式为 bucketname.endpoint
        String host = "http://" + bucketName + "." + endpoint;
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
//        String callbackUrl = "http://" + appServerHost + ":" + appServerPort + "/callback";
        String callbackUrl = "http://a9r5sv.natappfree.cc/callback";
        // 用户上传文件时指定的前缀。
        // TODO: 将获取policy的接口改为POST，传文件名（带后缀）
        String dir = "user-dir-prefix/";

        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            long expireTime = 30000;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

            JSONObject jasonCallback = new JSONObject();
            jasonCallback.put("callbackUrl", callbackUrl);
            jasonCallback.put("callbackBody",
                    "bucket=${bucket}&objectName=${object}&filename=${x:filename}&docId=${x:doc_id}&parentId=${x:parent_id}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);

            JSONObject ja1 = JSONObject.fromObject(respMap);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
            response(request, response,ja1.toString());

            LOG.info("Finish get policy");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void doCallbackPost(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.info("Start callback post");
        String ossCallbackBody = getPostBody(params);
        boolean ret = verifyOSSCallbackRequest(request, ossCallbackBody);
        fileService.uploadFileData(params);
        System.out.println("verify result : " + ret);
        System.out.println("OSS Callback Body:" + ossCallbackBody);
        if (ret) {
            response(request, response, "{\"Status\":\"OK\"}", HttpServletResponse.SC_OK);
        } else {
            response(request, response, "{\"Status\":\"verdify not ok\"}", HttpServletResponse.SC_BAD_REQUEST);
        }
        LOG.info("Finish callback post");
    }

    public String getPostBody(Map<String, Object> params) throws UnsupportedEncodingException {
        String callbackBody = "";
        List<String> keys = new ArrayList<String>(params.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key).toString();
            value = URLEncoder.encode(value, "UTF-8");
            //拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                callbackBody = callbackBody + key + "=" + value;
            } else {
                callbackBody = callbackBody + key + "=" + value + "&";
            }
        }
        return callbackBody;
    }

    public String executeGet(String url) {
        BufferedReader in = null;

        String content = null;
        try {
            // 定义HttpClient
            @SuppressWarnings("resource")
            DefaultHttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            content = sb.toString();
        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();// 最后要关闭BufferedReader
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return content;
        }
    }

    public boolean verifyOSSCallbackRequest(HttpServletRequest request, String ossCallbackBody)
            throws NumberFormatException, IOException {
        LOG.info("Begin Verify callback request");
        boolean ret = false;
        String autorizationInput = new String(request.getHeader("Authorization"));
        System.out.println("autorizationInput: "+ autorizationInput);

        String pubKeyInput = request.getHeader("x-oss-pub-key-url");
        System.out.println("pubKeyInput: "+ pubKeyInput);

        byte[] authorization = BinaryUtil.fromBase64String(autorizationInput);
        byte[] pubKey = BinaryUtil.fromBase64String(pubKeyInput);

        String pubKeyAddr = new String(pubKey);
        System.out.println("pubKeyAddr: "+ pubKeyAddr);

        if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/")
                && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")) {
            System.out.println("pub key addr must be oss addrss");
            return false;
        }

        String retString = executeGet(pubKeyAddr);
        System.out.println("retString: "+ retString);

        retString = retString.replace("-----BEGIN PUBLIC KEY-----", "");
        retString = retString.replace("-----END PUBLIC KEY-----", "");
        String queryString = request.getQueryString();
        String uri = request.getRequestURI();
        String decodeUri = java.net.URLDecoder.decode(uri, "UTF-8");
        String authStr = decodeUri;
        if (queryString != null && !queryString.equals("")) {
            authStr += "?" + queryString;
        }

        authStr += "\n" + ossCallbackBody;

        System.out.println("authStr: "+ authStr);

        ret = doCheck(authStr, authorization, retString);

        return ret;
    }

    public boolean doCheck(String content, byte[] sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            boolean bverify = signature.verify(sign);
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void response(HttpServletRequest request, HttpServletResponse response, String results) throws IOException {
        String callbackFunName = request.getParameter("callback");
        if (callbackFunName == null || callbackFunName.equalsIgnoreCase("")) {
            response.getWriter().println(results);
        } else {
            response.getWriter().println(callbackFunName + "( " + results + " )");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    private void response(HttpServletRequest request, HttpServletResponse response, String results, int status)
            throws IOException {
        String callbackFunName = request.getParameter("callback");
        response.addHeader("Content-Length", String.valueOf(results.length()));
        if (callbackFunName == null || callbackFunName.equalsIgnoreCase("")) {
            response.getWriter().println(results);
        } else {
            response.getWriter().println(callbackFunName + "( " + results + " )");
        }
        response.setStatus(status);
        response.flushBuffer();
    }
}
