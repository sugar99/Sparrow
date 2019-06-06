package com.sparrow.demo.service.file.impl;

import com.sparrow.demo.constant.ElasticSearchConstant;
import com.sparrow.demo.service.file.FileService;
import com.sparrow.demo.utils.ElasticSearchUtils;
import com.sparrow.demo.utils.OssUtils;
import com.sparrow.demo.utils.ResultModel;
import com.sparrow.demo.utils.file.FileSuffixType;
import com.sparrow.demo.utils.file.FileUtils;
import com.sparrow.demo.utils.file.VideoUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author allen
 */
@Service
public class FileServiceImpl implements FileService {
    @Value("${file.temp.path}")
    private String tempFilePath;

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private OssUtils ossUtils;

    @Autowired
    private VideoUtils videoUtils;

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public ResultModel uploadFile(Map<String, Object> params) {
        return null;
    }

    @Override
    public ResultModel deleteFile(Map<String, Object> params) {
        String docId = params.get("doc_id").toString();
        List<String> fileIds = (List<String>) params.get("file_id");

        RestHighLevelClient client = elasticSearchUtils.CreateClient();

        for (String fileId : fileIds) {
//            GetRequest getRequest = new GetRequest(ElasticSearchConstant.INDEX_FILE, ElasticSearchConstant.TYPE_FILE, fileId);
//            GetResponse getResponse;
//            try {
//                getResponse = client.get(getRequest, RequestOptions.DEFAULT);
//                LOG.info("获取文件：[" + fileId + "]元数据成功！");
//                // 在OSS中删除文件
//                if (getResponse.getSource().get("file_key") != null) {
//                    String fileObjectName = getResponse.getSource().get("file_key").toString();
//                    ossUtils.deleteFileInOSS(fileObjectName);
//                    LOG.info("OSS删除文件：[" + fileId + "]成功！");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                LOG.info("获取文件：[" + fileId + "]元数据失败！");
//                continue;
//            }
//            // ES的文件meta中删除文件
//            DeleteRequest deleteRequest = new DeleteRequest(ElasticSearchConstant.INDEX_FILE,ElasticSearchConstant.TYPE_FILE,fileId);
//            try {
//                client.delete(deleteRequest,RequestOptions.DEFAULT);
//                LOG.info("ES文件meta删除文件：[" + fileId + "]成功！");
//            } catch (IOException e) {
//                e.printStackTrace();
//                LOG.info("ES文件meta删除文件：[" + fileId + "]失败！");
//                continue;
//            }

            // ES && OSS删除文件
            elasticSearchUtils.deleteFile(ElasticSearchConstant.INDEX_FILE,ElasticSearchConstant.TYPE_FILE,fileId);

            // ES文档meta中删除文件
            UpdateRequest updateRequest = new UpdateRequest(ElasticSearchConstant.INDEX_DOC,ElasticSearchConstant.TYPE_DOC,docId);
            Script script = elasticSearchUtils.generateScript("file",fileId,true);
            updateRequest.script(script);
            try {
                client.update(updateRequest,RequestOptions.DEFAULT);
                LOG.info("ES文档meta删除文件：[" + fileId + "]成功！");
            } catch (IOException e) {
                e.printStackTrace();
                LOG.info("ES文档meta删除文件：[" + fileId + "]失败！");
                continue;
            }
        }
        // 判断删除文件后，文档是否还包含文件，如果不包含文件的话要将file_state 置为0  以及更新sparrow_doc中相关meta
        UpdateRequest updateDocRequest = new UpdateRequest(ElasticSearchConstant.INDEX_DOC,ElasticSearchConstant.TYPE_DOC,docId);
        Map<String,Object> updateMap = new HashMap<>(2);
        updateMap.put("modified_time",System.currentTimeMillis());

        GetRequest getDocRequest = new GetRequest(ElasticSearchConstant.INDEX_DOC,ElasticSearchConstant.TYPE_DOC,docId);
        try {
            GetResponse getDocResponse = client.get(getDocRequest,RequestOptions.DEFAULT);
            List<String> fileList = (List<String>) getDocResponse.getSource().get("file");
            if (fileList.size() == 0){
                updateMap.put("file_state",0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("获取文档：[" + docId + "]元数据失败！");
        }
        updateDocRequest.doc(updateMap);
        try {
            client.update(updateDocRequest,RequestOptions.DEFAULT);
            LOG.info("ES更新文档：["+ docId +"]元数据成功！");
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("ES更新文档：["+ docId +"]元数据失败！");
        }
        LOG.info("ES删除文件操作结束！");
        return ResultModel.success(null);
    }

    @Override
    public void downloadFile(Map<String, Object> params, HttpServletResponse response) {
        String fileId = params.get("file_id").toString();
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        GetRequest getRequest = new GetRequest(ElasticSearchConstant.INDEX_FILE,ElasticSearchConstant.TYPE_FILE,fileId);
        GetResponse getResponse;
        try {
            getResponse = client.get(getRequest,RequestOptions.DEFAULT);
            if (getResponse.isSourceEmpty()){
                LOG.info("未找到文件["+ fileId +"]相关信息");
                return;
            }
            String fileObjectName = getResponse.getSource().get("file_key").toString();
            String fileName = getResponse.getSource().get("file_name").toString();

            BufferedInputStream bufferedInputStream = ossUtils.getFileStreamFromOSS(fileObjectName);
            OutputStream responseOutputStream = null;
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/octet-stream");
                String contentDisposition = "attachment; filename=" + fileName;
                response.setHeader("Content-Disposition", contentDisposition);

                responseOutputStream = response.getOutputStream();
                byte[] buff = new byte[1024];

                int length;
                while ((length = bufferedInputStream.read(buff)) != -1) {
                    responseOutputStream.write(buff, 0, length);
                    responseOutputStream.flush();
                }

            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                if (bufferedInputStream != null){
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (responseOutputStream != null){
                    try {
                        responseOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultModel getFileUrl(Map<String, Object> params) {
        String fileId = params.get("file_id").toString();
        Map<String, Object> resultMap = new HashMap<>(1);
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        GetRequest getRequest = new GetRequest(ElasticSearchConstant.INDEX_FILE,ElasticSearchConstant.TYPE_FILE,fileId);
        try {
            GetResponse getResponse = client.get(getRequest,RequestOptions.DEFAULT);
            if (getResponse.isSourceEmpty()){
                return ResultModel.failure("未找到文件["+ fileId +"]相关信息");
            }
            String fileObjectName = getResponse.getSource().get("file_key").toString();
            String fileUrl = ossUtils.getObjectUrl(fileObjectName);
            resultMap.put("file_url",fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultModel.failure("ES中获取文件["+ fileId +"]数据失败！");
        }
        return ResultModel.success(resultMap);
    }

    @Override
    public ResultModel getFileVersion(Map<String, Object> params) {
        String fileId = params.get("file_id").toString();
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> fileList = new LinkedList<>();
        Map<String, Object> fileMap = getFileInfo(fileId);
        fileList.add(fileMap);

        while (true) {
            if ((int) fileMap.get("file_version") != 0) {
                String parentId = fileMap.get("parent_file").toString();
                fileMap = getFileInfo(parentId);
                fileList.add(fileMap);
            }else {
                break;
            }
        }
        resultMap.put("file",fileList);
        return ResultModel.success(resultMap);
    }

    @Async
    @Override
    public void uploadFileData(Map<String, Object> params) {
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        // 文件名
        String fileName = params.get("filename").toString();
        String objectName = params.get("objectName").toString();
        String docId = params.get("docId").toString();
        String parentId = params.get("parentId").toString();

        String fileId = "";

        // 文件类型
        String fileType = fileUtils.getFileType(fileName);
        // 文件版本
        int fileVersion = 0;

        // 上传新版本文件时获取当前文件版本号
        if(parentId != null && !"".equals(parentId)){
            GetRequest getParentRequest = new GetRequest(ElasticSearchConstant.INDEX_FILE,ElasticSearchConstant.TYPE_FILE,parentId);
            GetResponse getParentResponse;
            int parentVersion;
            try {
                getParentResponse = client.get(getParentRequest,RequestOptions.DEFAULT);
                parentVersion = (int) getParentResponse.getSource().get("file_version");
                fileVersion = parentVersion + 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 从OSS获取上传的文件
        String path = tempFilePath + fileName;
        File file = ossUtils.getFileFromOSS(objectName,path);
        // 文件缩略图
        String fileThumbnail = "";
        try {
            fileThumbnail = fileUtils.getThumbnailStr(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IndexRequest fileIndexRequest = new IndexRequest(ElasticSearchConstant.INDEX_FILE,ElasticSearchConstant.TYPE_FILE);
        Map<String, Object> fileIndexMap = new HashMap<>();
        List<String> derivedFileIds = new LinkedList<>();
        fileIndexMap.put("file_name",fileName);
        fileIndexMap.put("file_type",fileType);
        fileIndexMap.put("file_thumbnail",fileThumbnail);
        fileIndexMap.put("file_key",objectName);
        fileIndexMap.put("file_version",fileVersion);
        fileIndexMap.put("parent_file",parentId);
        fileIndexMap.put("derived_file",derivedFileIds);
        fileIndexMap.put("original_file","");
        fileIndexMap.put("create_time",System.currentTimeMillis());
        fileIndexMap.put("modified_time",System.currentTimeMillis());
        fileIndexRequest.source(fileIndexMap);
        IndexResponse fileIndexResponse;
        try {
            fileIndexResponse = client.index(fileIndexRequest,RequestOptions.DEFAULT);
            fileId = fileIndexResponse.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 当上传的文件是Office文档时，为了在线预览需要将其转换为PDF并保存
        if ("document".equals(fileType)){
            try {
                if (! FileSuffixType.PDF.getValue().equals(fileUtils.getFileExtension(fileName))) {
                    File pdfFile = fileUtils.transferDocToPdf(file);
                    List<String> fileIds = new LinkedList<>();
                    String pdfFileId = uploadDerivedFileMeta(pdfFile, fileId);
                    fileIds.add(pdfFileId);
                    UpdateRequest updateRequest = new UpdateRequest(ElasticSearchConstant.INDEX_FILE, ElasticSearchConstant.TYPE_FILE, fileId);
                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("derived_file", fileIds);
                    updateRequest.doc(updateMap);
                    client.update(updateRequest, RequestOptions.DEFAULT);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 当上传的文件是视频时，对视频按指定时间长度截取五帧
        if ("video".equals(fileType)){
            try {
                File[] frames = videoUtils.getVideoFramesByInterval(file);
                List<String> frameIds = new LinkedList<>();
                for (File frame : frames){
                    String frameId = uploadDerivedFileMeta(frame, fileId);
                    frameIds.add(frameId);
                }
                UpdateRequest updateRequest = new UpdateRequest(ElasticSearchConstant.INDEX_FILE, ElasticSearchConstant.TYPE_FILE, fileId);
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("derived_file",frameIds);
                updateRequest.doc(updateMap);
                client.update(updateRequest,RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // ES在文档元数据中的文件List中添加当前文件ID（如果是新版本文件，要将旧版本文件ID删除，再添加新版本文件ID）
        if ("".equals(parentId)){
            UpdateRequest updateRequest = new UpdateRequest(ElasticSearchConstant.INDEX_DOC, ElasticSearchConstant.TYPE_DOC,docId);
            Script script = elasticSearchUtils.generateScript("file",fileId,false);
            updateRequest.script(script);
            try {
                client.update(updateRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            // TODO: 测试以下同一个Request重复对script赋值能否覆盖
            UpdateRequest updateRequest1 = new UpdateRequest(ElasticSearchConstant.INDEX_DOC, ElasticSearchConstant.TYPE_DOC,docId);
            Script delScript = elasticSearchUtils.generateScript("file",parentId,true);
            updateRequest1.script(delScript);
            try {
                client.update(updateRequest1, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            UpdateRequest updateRequest2 = new UpdateRequest(ElasticSearchConstant.INDEX_DOC, ElasticSearchConstant.TYPE_DOC,docId);
            Script addScript = elasticSearchUtils.generateScript("file",fileId,false);
            updateRequest2.script(addScript);
            try {
                client.update(updateRequest2, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 更新文档元数据的file_state
        UpdateRequest updateRequest3 = new UpdateRequest(ElasticSearchConstant.INDEX_DOC, ElasticSearchConstant.TYPE_DOC,docId);
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("file_state",1);
        updateMap.put("modified_time",System.currentTimeMillis());
        updateRequest3.doc(updateMap);
        try {
            client.update(updateRequest3,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String uploadDerivedFileMeta(File file, String originalId){
        String fileId = "";
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        String objectName = ossUtils.uploadFile(file);
        String fileName = file.getName();

        IndexRequest indexRequest = new IndexRequest(ElasticSearchConstant.INDEX_FILE,ElasticSearchConstant.TYPE_FILE);
        Map<String, Object> indexMap = new HashMap<>();
        indexMap.put("file_name",fileName);
        indexMap.put("file_type",fileUtils.getFileType(fileName));
        indexMap.put("file_thumbnail","");
        indexMap.put("file_key",objectName);
        indexMap.put("file_version",0);
        indexMap.put("parent_file","");
        indexMap.put("derived_file","");
        indexMap.put("original_file",originalId);
        indexMap.put("create_time",System.currentTimeMillis());
        indexMap.put("modified_time",System.currentTimeMillis());
        indexRequest.source(indexMap);

        try {
            IndexResponse indexResponse = client.index(indexRequest,RequestOptions.DEFAULT);
            fileId = indexResponse.getId();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return fileId;
    }

    private Map<String, Object> getFileInfo(String fileId){
        Map<String, Object> resultMap = new HashMap<>();
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        GetRequest getRequest = new GetRequest(ElasticSearchConstant.INDEX_FILE,ElasticSearchConstant.TYPE_FILE,fileId);
        try {
            GetResponse getResponse = client.get(getRequest,RequestOptions.DEFAULT);
            if (getResponse.isSourceEmpty()){
                return null;
            }
            resultMap.put("file_id",fileId);
            resultMap.put("file_name",getResponse.getSource().get("file_name"));
            resultMap.put("file_thumbnail",getResponse.getSource().get("file_thumbnail"));
            resultMap.put("file_version",getResponse.getSource().get("file_version"));
            resultMap.put("parent_file",getResponse.getSource().get("parent_file"));
            resultMap.put("create_time",getResponse.getSource().get("create_time"));
            resultMap.put("modified_time",getResponse.getSource().get("modified_time"));
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return resultMap;
    }

}
