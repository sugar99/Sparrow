package com.sparrow.demo.service.document.impl;

import com.sparrow.demo.constant.ElasticSearchConstant;
import com.sparrow.demo.mapper.SpaDocumentMapper;
import com.sparrow.demo.service.document.DocumentService;
import com.sparrow.demo.utils.ElasticSearchUtils;
import com.sparrow.demo.utils.OssUtils;
import com.sparrow.demo.utils.ResultModel;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author allen
 */
@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    @Autowired
    private OssUtils ossUtils;

    @Autowired
    private SpaDocumentMapper spaDocumentMapper;

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Override
    public ResultModel addDoc() {
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        IndexRequest indexRequest = new IndexRequest(ElasticSearchConstant.INDEX_DOC, ElasticSearchConstant.TYPE_DOC);
        List<Map<String,Object>> fileList = new LinkedList<>();
        Map<String, Object> addMap = new HashMap();
        addMap.put("meta_state",0);
        addMap.put("file_state",0);
        addMap.put("file",fileList);
        addMap.put("create_time",System.currentTimeMillis());
        addMap.put("modified_time",System.currentTimeMillis());
        indexRequest.source(addMap);
        IndexResponse indexResponse;
        try {
            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("在ES中新建文档元数据失败！");
            return ResultModel.failure("在ES中新建文档元数据失败！");
        }
        String id = indexResponse.getId();
        Map<String,Object> resultMap = new HashMap(1);
        resultMap.put("doc_id",id);

        // TODO: paramsMap添加owner_uuid字段值
        Map<String,Object> paramsMap = new HashMap(2);
        paramsMap.put("uuid",id);
        paramsMap.put("ownerUuid","");
        try {
            spaDocumentMapper.docAdd(paramsMap);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("同步新建文档信息到mysql失败！");
            return ResultModel.failure("同步新建文档信息到mysql失败！");
        }
        LOG.info("新建文档：[" + id + "] 成功！");
        return ResultModel.success(resultMap);
    }

    @Override
    public ResultModel updateDocMeta(Map<String, Object> params) {
        String docId = params.get("doc_id").toString();
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        UpdateRequest updateRequest = new UpdateRequest(ElasticSearchConstant.INDEX_DOC, ElasticSearchConstant.TYPE_DOC,docId);
        Map<String, Object> updateMap = new HashMap();
        if (!("").equals(params.get("doc_name")) && params.get("doc_name") != null) {
            updateMap.put("doc_name", params.get("doc_name"));
        }
        if (!("").equals(params.get("doc_desc")) && params.get("doc_desc") != null) {
            updateMap.put("doc_desc", params.get("doc_desc"));
        }
        if (updateMap.size() > 0) {
            updateMap.put("modified_time", System.currentTimeMillis());
            updateMap.put("meta_state",1);
        }
        updateRequest.doc(updateMap);
        try {
            client.update(updateRequest,RequestOptions.DEFAULT);
            LOG.info("ES更新文档：["+ docId +"] '元数据成功！");
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("ES更新文档：["+ docId +"] '元数据失败！");
            return ResultModel.failure("ES更新文档：["+ docId +"] '元数据失败！");
        }

        Map<String, Object> update = new HashMap<>(2);
        update.put("uuid",docId);
        update.put("docName",params.get("doc_name"));
        try {
            spaDocumentMapper.updateDocMeta(update);
            LOG.info("mysql更新文档：["+ docId +"] '元数据成功！");
        } catch (Exception e){
            LOG.info("mysql更新文档：["+ docId +"] '元数据失败！");
            return ResultModel.failure("mysql更新文档：["+ docId +"] '元数据失败！");
        }
        return ResultModel.success(null);
    }

    @Override
    public ResultModel getDocList(Map<String, Object> params) {
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        SearchRequest searchRequest = new SearchRequest(ElasticSearchConstant.INDEX_DOC);
        // 这里可以根据需要修改，返回更多字段
        final String[] docIncludeFields = {"doc_id", "doc_name"};
        final String[] docExcludeFields = {"@timestamp", "@version"};

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(pageSize);
        sourceBuilder.from((pageNum-1) * pageSize);
        sourceBuilder.fetchSource(docIncludeFields,docExcludeFields);

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultModel.failure("在ES中获取文档列表失败！");
        }

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<Map> docList = new LinkedList<>();
        for(SearchHit searchHit: searchHits)
        {
            Map entity = searchHit.getSourceAsMap();
            entity.put("doc_id", searchHit.getId());
            docList.add(entity);
        }

        return ResultModel.success(searchResponse.getHits().totalHits,pageNum,pageSize,docList);
    }

    @Override
    public ResultModel getDocDetail(Map<String, Object> params){
        String docId = params.get("doc_id").toString();
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> fileList = new LinkedList<>();

        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        GetRequest getDocRequest = new GetRequest(ElasticSearchConstant.INDEX_DOC, ElasticSearchConstant.TYPE_DOC,docId);
        GetResponse getDocResponse;
        try {
            getDocResponse = client.get(getDocRequest,RequestOptions.DEFAULT);
            Map<String, Object> docSource = getDocResponse.getSource();
            // 构造返回参数resultMap
            if (docSource.get("doc_name") != null){
                resultMap.put("doc_name",docSource.get("doc_name"));
            }
            if (docSource.get("doc_desc") != null){
                resultMap.put("doc_desc",docSource.get("doc_desc"));
            }
            if (docSource.get("creator") != null){
                resultMap.put("creator",docSource.get("creator"));
            }
            if (docSource.get("create_time") != null){
                resultMap.put("create_time",docSource.get("create_time"));
            }
            if (docSource.get("modified_time") != null){
                resultMap.put("modified_time",docSource.get("modified_time"));
            }
            if (docSource.get("file") != null) {
                List<String> fileIds = (List<String>) docSource.get("file");
                if (fileIds.size() > 0) {
                    for (String fileId : fileIds) {
                        GetRequest getFileRequest = new GetRequest(ElasticSearchConstant.INDEX_FILE, ElasticSearchConstant.TYPE_FILE, fileId);
                        GetResponse getFileResponse = client.get(getFileRequest, RequestOptions.DEFAULT);
                        Map<String, Object> fileSource = getFileResponse.getSource();
                        Map<String, Object> fileMap = new HashMap<>();
                        fileMap.put("file_id", fileId);
                        if (fileSource.get("file_name") != null) {
                            fileMap.put("file_name", getFileResponse.getSource().get("file_name"));
                        }
                        if (fileSource.get("file_thumbnail") != null) {
                            fileMap.put("file_thumbnail", getFileResponse.getSource().get("file_thumbnail"));
                        }
                        fileList.add(fileMap);
                    }
                }
            }
            resultMap.put("file",fileList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultModel.failure("从ES获取元数据失败！");
        }
        return ResultModel.success(resultMap);
    }

    @Override
    public ResultModel deleteDoc(Map<String, Object> params) {
        String docId = params.get("doc_id").toString();
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        GetRequest getDocRequest = new GetRequest(ElasticSearchConstant.INDEX_DOC, ElasticSearchConstant.TYPE_DOC,docId);
        GetResponse getDocResponse;
        try {
            getDocResponse = client.get(getDocRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("ES获取文档元数据失败,请检查[doc_id]是否正确");
            return ResultModel.failure("ES获取文档元数据失败,请检查[doc_id]是否正确");
        }
        if (getDocResponse.isSourceEmpty()){
            LOG.info("ES中不存在文档[" + docId + "]" );
            return ResultModel.failure("ES中不存在文档[" + docId + "]" );
        }
        // 删除文档关联的所有文件信息
        Map<String, Object> getDocSource = getDocResponse.getSource();
        if (getDocSource.get("file") != null){
            List<String> fileIds = (List<String>) getDocSource.get("file");
            if (fileIds.size() > 0){
                for (String fileId : fileIds){
                    elasticSearchUtils.deleteFile(ElasticSearchConstant.INDEX_FILE, ElasticSearchConstant.TYPE_FILE, fileId);
                }
                LOG.info("删除文档所有关联文件操作完成！");
            }
        }

        // 删除文档meta
        DeleteRequest deleteRequest = new DeleteRequest(ElasticSearchConstant.INDEX_DOC,ElasticSearchConstant.TYPE_DOC,docId);
        try {
            client.delete(deleteRequest, RequestOptions.DEFAULT);
            LOG.info("删除文档["+ docId +"]meta成功！");
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("删除文档["+ docId +"]meta失败！");
            return ResultModel.failure("删除文档["+ docId +"]失败！");
        }
        return ResultModel.success(null);
    }

    @Override
    public void downloadDoc(Map<String, Object> params, HttpServletResponse response) {
        String docId = params.get("doc_id").toString();
        ZipOutputStream zipOutputStream = null;
        RestHighLevelClient client = elasticSearchUtils.CreateClient();
        GetRequest getDocRequest = new GetRequest(ElasticSearchConstant.INDEX_DOC,ElasticSearchConstant.TYPE_DOC,docId);
        try {
            GetResponse getDocResponse = client.get(getDocRequest,RequestOptions.DEFAULT);
            if (getDocResponse.isSourceEmpty()){
                LOG.info("未找到文档["+ docId +"]相关信息");
                return;
            }
            Map<String, Object> docMap = getDocResponse.getSource();
            if (docMap.get("file") != null){
                List<String> fileIds = (List<String>) docMap.get("file");
                if (fileIds.size() > 0){
                    String zipName = UUID.randomUUID() + ".zip";
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition","attachment; filename="+ zipName);
                    zipOutputStream = new ZipOutputStream(response.getOutputStream());

                    for (String fileId : fileIds){
                        GetRequest getFileRequest = new GetRequest(ElasticSearchConstant.INDEX_FILE,ElasticSearchConstant.TYPE_FILE,fileId);
                        GetResponse getFileResponse = client.get(getFileRequest,RequestOptions.DEFAULT);
                        String fileName = getFileResponse.getSource().get("file_name").toString();
                        String fileObjectName = getFileResponse.getSource().get("file_key").toString();

                        BufferedInputStream bufferedInputStream = ossUtils.getFileStreamFromOSS(fileObjectName);

                        zipOutputStream.putNextEntry(new ZipEntry(fileName));
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = bufferedInputStream.read(buffer)) !=-1){
                            zipOutputStream.write(buffer,0,length);
                            zipOutputStream.flush();
                        }
                        LOG.info("成功将文件["+ fileName +"]打包到zip");
                    }
                    zipOutputStream.close();
                    LOG.info("成功打包文档包含的所有文件");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
