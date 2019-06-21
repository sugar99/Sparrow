package com.sparrow.demo.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author allen
 */
@Component
public class ElasticSearchUtils {
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private Integer port;
    @Value("${elasticsearch.schema}")
    private String schema;

    @Autowired
    private OssUtils ossUtils;

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchUtils.class);

    public RestHighLevelClient CreateClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host,port,schema)
                )
        );
        return client;
    }

//    public void updateExistList(String index,String type,String id,String field,String value,boolean isDelete) throws Exception{
//        client = oldclient.setupTransportClient();
//        Map<String,Object> params = new HashMap<>();
//        params.put(field,value);
//        String script_str;
//        if (isDelete){
//            script_str = "if (ctx._source."+field+".indexOf(params."+field+") != -1){" +
//                    "ctx._source."+field+".remove(ctx._source."+field+".indexOf(params."+field+"))}";
//        }else {
//            //表示如果该doc不包含该字段，在该doc新建字段并赋值value，
//            //如果存在该字段,会比较传入的值是否存在于list中，如果不存在就添加
//            script_str = "if(!ctx._source.containsKey('"+field+"')){ctx._source."+field+"=[params."+field+"]}else{" +
//                    "if (ctx._source."+field+".indexOf(params."+field+") == -1){ ctx._source."+field+".add(params."+field+")}}";
//        }
//        Script script = new Script(ScriptType.INLINE,Script.DEFAULT_SCRIPT_LANG,script_str,params);
//        logger.info("script:\n"+script);
//        UpdateResponse updateResponse = client.prepareUpdate(index,type,id)
//                .setScript(script)
//                .get();
//        System.out.println(updateResponse.status());
//    }

    public Script generateScript(String field, String value, boolean isDelete){
        Map<String,Object> params = new HashMap<>();
        params.put(field,value);
        String scriptStr;
        if (isDelete){
            scriptStr = "if (ctx._source."+field+".indexOf(params."+field+") != -1){" +
                    "ctx._source."+field+".remove(ctx._source."+field+".indexOf(params."+field+"))}";
        }else {
            //表示如果该doc不包含该字段，在该doc新建字段并赋值value，
            //如果存在该字段,会比较传入的值是否存在于list中，如果不存在就添加
            scriptStr = "if(!ctx._source.containsKey('"+field+"')){ctx._source."+field+"=[params."+field+"]}else{" +
                    "if (ctx._source."+field+".indexOf(params."+field+") == -1){ ctx._source."+field+".add(params."+field+")}}";
        }
        return new Script(ScriptType.INLINE,Script.DEFAULT_SCRIPT_LANG,scriptStr,params);
    }

    public void deleteFile(String index, String type, String id){
        RestHighLevelClient client = CreateClient();
        // 先找到文件在OSS的存储位置，并删除
        GetRequest getRequest = new GetRequest(index, type, id);
        GetResponse getResponse;
        try {
            getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            // 删除所有版本文件
            if ((Integer)getResponse.getSource().get("file_version") != 0){
                String parentFileId = getResponse.getSource().get("parent_file").toString();
                deleteFile(index,type,parentFileId);
            }
            // 删除所有衍生文件
            if (getResponse.getSource().get("derived_file") != null && !"".equals(getResponse.getSource().get("derived_file"))){
                List<String> derivedFileIds = (List<String>) getResponse.getSource().get("derived_file");
                if (derivedFileIds.size() > 0){
                    for (String derivedFileId : derivedFileIds) {
                        deleteFile(index,type,derivedFileId);
                    }
                }
            }

            if (getResponse.getSource().get("file_key") != null) {
                String fileObjectName = getResponse.getSource().get("file_key").toString();
                ossUtils.deleteFileInOSS(fileObjectName);
                LOG.info("OSS删除文件：[" + id + "]成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("OSS删除文件：[" + id + "]失败！");
        }

        //在ES删除文件的meta
        DeleteRequest deleteRequest = new DeleteRequest(index,type,id);
        try {
            client.delete(deleteRequest,RequestOptions.DEFAULT);
            LOG.info("ES删除文件：[" + id + "]成功！");
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("ES删除文件：[" + id + "]失败！");
        }
    }
}
