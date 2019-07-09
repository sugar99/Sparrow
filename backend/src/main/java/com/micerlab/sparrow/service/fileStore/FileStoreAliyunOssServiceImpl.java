package com.micerlab.sparrow.service.fileStore;

import com.aliyun.oss.common.utils.BinaryUtil;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.utils.FileUtil;
import com.micerlab.sparrow.utils.AliyunOssUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("aliyunOssService")
public class FileStoreAliyunOssServiceImpl implements FileStoreService {

    @Value("${oss.callbackHost}")
    private String callbackHost;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private AliyunOssUtil aliyunOssUtil;

    @Override
    public Result getPolicy(Map<String, Object> params, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
//        String callbackUrl = "http://" + appServerHost + ":" + appServerPort + "/callback";
        String file_id = fileUtil.generateFileId();
        String callbackUrl = "http://" + callbackHost + "/v1/files/" + file_id;

        Map<String, String> respMap = aliyunOssUtil.getPolicy(params);
        respMap.put("file_uuid", file_id + "." + params.get("ext"));
        // TODO: respMap.put("creator",BaseService.getUser_Id(httpServletRequest));
        respMap.put("creator", "");
        try {
            JSONObject jasonCallback = new JSONObject();
            jasonCallback.put("callbackUrl", callbackUrl);
            jasonCallback.put("callbackBody",
                    "title=${title}&store_key=${store_key}&doc_id=${doc_id}&parent_id=${parent_id}&ext=${ext}&creator=${creator}&size=${size}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);

//            JSONObject ja1 = JSONObject.fromObject(respMap);
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST");
//            response(httpServletRequest, httpServletResponse,ja1.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.OK().data(respMap).build();
    }

    @Override
    public Result getPresignedUrl(Map<String, Object> params, HttpServletRequest httpServletRequest){
        // 文件扩展名
        String suffix = (String) params.get("ext");
        // OSS存储路径
        String path = fileUtil.getFileStorePath(suffix);
        // 文件ID
        String file_id = fileUtil.generateFileId();

        String objectName = path + file_id + "." + suffix;
        String url = aliyunOssUtil.getPutUrl(objectName);
        Map<String, String> respMap = new HashMap<>();
        respMap.put("url", url);
        // TODO: respMap.put("creator", BaseService.getUser_Id(httpServletRequest));
        respMap.put("creator", "");
        return Result.OK().data(respMap).build();
    }

    @Override
    public Result deleteFile(List<String> objectNames){
        for (String objectName: objectNames) {
            aliyunOssUtil.deleteFile(objectName);
        }
        return Result.OK().build();
    }

    @Override
    public void downloadFile(String title, String key, HttpServletResponse httpServletResponse){
        aliyunOssUtil.downloadFile(title, key, httpServletResponse);
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

    @Override
    public Map<String, Object> uploadThumbnail(File file){
        return aliyunOssUtil.uploadThumbnail(file);
    }
}
