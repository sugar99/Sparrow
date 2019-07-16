package com.micerlab.sparrow.dao.es;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.config.ElasticsearchConfig;
import com.micerlab.sparrow.utils.MapUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RestHighLevelClient;


public abstract class ESCRUDRepository<E>
{
    protected abstract String index();
    
    protected final ElasticsearchConfig.Indices indices;
    
    protected final ESBaseDao ESBaseDao;
    
    protected final RestHighLevelClient restHighLevelClient;
    
    protected final Class<E> clazz;
    
    public ESCRUDRepository(Class<E> clazz, ESBaseDao ESBaseDao, ElasticsearchConfig.Indices indices)
    {
        this.clazz = clazz;
        this.ESBaseDao = ESBaseDao;
        this.restHighLevelClient = ESBaseDao.getRestHighLevelClient();
        this.indices = indices;
    }
    
    public JSONObject getJsonDoc(String id)
    {
        return ESBaseDao.getESDoc(index(), id);
    }
    
    public E get(String id)
    {
        return MapUtils.jsonMap2Obj(getJsonDoc(id), clazz);
    }
    
    public String createJsonDoc(JSONObject jsonMap)
    {
        if(jsonMap == null) throw new IllegalArgumentException("Null doc isn't allowed");
        return ESBaseDao.createESDoc(index(),jsonMap);
    }
    
    public String create(E element)
    {
        return createJsonDoc(MapUtils.obj2JsonMap(element));
    }
    
    public DocWriteResponse.Result indexJsonDoc(String id, JSONObject jsonMap, boolean overwriteIfExist)
    {
        if(jsonMap == null) throw new IllegalArgumentException("Null doc isn't allowed");
        return ESBaseDao.indexESDoc(index(), id, jsonMap, overwriteIfExist);
    }
    
    public DocWriteResponse.Result index(String id, E element, boolean overwriteIfExist)
    {
        return indexJsonDoc(id, MapUtils.obj2JsonMap(element), overwriteIfExist);
    }
    
    /**
     * 部分更新
     * @param jsonMap 包含更新字段的JsonMap
     * @return 更新后的ES文档
     */
    public JSONObject updateJsonDoc(String id, JSONObject jsonMap)
    {
        if(jsonMap == null) throw new IllegalArgumentException("Null doc isn't allowed");
        return ESBaseDao.updateESDoc(index(), id, jsonMap);
    }
    
    /**
     * 全量更新
     * @return 更新后的ES文档
     */
    public E update(String id, E element)
    {
        JSONObject jsonMap = MapUtils.obj2JsonMap(element);
        return MapUtils.jsonMap2Obj(updateJsonDoc(id, jsonMap), clazz);
    }
    
    public DeleteResponse delete(String id)
    {
        return ESBaseDao.deleteESDoc(index(),id);
    }
}
