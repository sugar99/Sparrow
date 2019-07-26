package com.micerlab.sparrow.dao.es;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.config.ESConfig;
import com.micerlab.sparrow.utils.MapUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * ES CRUD抽象类
 * @author chenlvjia
 * @param <E> 表示索引文档对象的Pojo类型
 */
public abstract class ESCRUDRepository<E>
{
    protected abstract String index();
    
    protected final ESConfig.Indices sparrowIndices;
    
    protected final ESBaseDao ESBaseDao;
    
    protected final RestHighLevelClient restHighLevelClient;
    
    // 表示索引文档对象的Pojo的Class对象
    protected final Class<E> clazz;
    
    public ESCRUDRepository(Class<E> clazz, ESBaseDao ESBaseDao, ESConfig.Indices sparrowIndices)
    {
        this.clazz = clazz;
        this.ESBaseDao = ESBaseDao;
        this.restHighLevelClient = ESBaseDao.getRestHighLevelClient();
        this.sparrowIndices = sparrowIndices;
    }
    
    /**
     * 获取ES文档对象
     * @param id ES文档id
     * @return  1.the json map of the doc (Map<String, Object>)
     *          2.null if the specific doc doesn't exist
     */
    public JSONObject getJsonDoc(String id)
    {
        return ESBaseDao.getESDoc(index(), id);
    }
    
    /**
     * 获取ES文档对象
     * @param id ES文档id
     * @return  1.the pojo representing the ES doc
     *          2.null if the specific doc doesn't exist
     */
    public E get(String id)
    {
        return MapUtils.jsonMap2Obj(getJsonDoc(id), clazz);
    }
    
    /**
     * 创建ES文档
     * @param jsonMap 用json表示的ES文档
     * @return 创建ES文档的id（uuid）
     */
    public String createJsonDoc(JSONObject jsonMap)
    {
        if(jsonMap == null) throw new IllegalArgumentException("Null doc isn't allowed");
        return ESBaseDao.createESDoc(index(),jsonMap);
    }
    
    /**
     * 创建ES文档
     * @param element 用pojo表示的ES文档
     * @return 创建ES文档的id（uuid）
     */
    public String create(E element)
    {
        return createJsonDoc(MapUtils.obj2JsonMap(element));
    }
    
    /**
     * 索引存储ES文档对象
     * @param id ES文档id
     * @param jsonMap 用json表示的ES文档
     * @param overwriteIfExist 如果包含该id的ES文档已存在，是否覆盖原有ES文档
     *                1.overwriteIfExist = true：  覆盖原有ES文档
     *                2.overwriteIfExist = false： 不覆盖
     * @return 1.DocWriteResponse.Result.CREATED 创建了新的ES文档
     *         2.DocWriteResponse.Result.UPDATED 更新了原有ES文档
     */
    public DocWriteResponse.Result indexJsonDoc(String id, JSONObject jsonMap, boolean overwriteIfExist)
    {
        if(jsonMap == null) throw new IllegalArgumentException("Null doc isn't allowed");
        return ESBaseDao.indexESDoc(index(), id, jsonMap, overwriteIfExist);
    }
    
    /**
     * 索引存储ES文档对象
     * @param id ES文档id
     * @param element 用pojo表示的ES文档
     * @param overwriteIfExist 如果包含该id的ES文档已存在，是否覆盖原有ES文档
     *                1.overwriteIfExist = true：  覆盖原有ES文档
     *                2.overwriteIfExist = false： 不覆盖
     * @return 1.DocWriteResponse.Result.CREATED 创建了新的ES文档
     *         2.DocWriteResponse.Result.UPDATED 更新了原有ES文档
     */
    public DocWriteResponse.Result index(String id, E element, boolean overwriteIfExist)
    {
        return indexJsonDoc(id, MapUtils.obj2JsonMap(element), overwriteIfExist);
    }
    
    /**
     * 部分更新
     * @param id ES文档id
     * @param jsonMap 包含更新字段的JsonMap
     * @return 更新请求的处理结果
     */
    public UpdateResponse updateJsonDoc(String id, JSONObject jsonMap)
    {
        if(jsonMap == null) throw new IllegalArgumentException("Null doc isn't allowed");
        return ESBaseDao.updateESDoc(index(), id, jsonMap);
    }
    
    /**
     * 更新（几乎是全量更新，因为pojo对象的字段包含默认值！）
     * @param id ES文档id
     * @param element 用pojo表示的ES文档
     * @return 更新请求的处理结果
     */
    public UpdateResponse update(String id, E element)
    {
        JSONObject jsonMap = MapUtils.obj2JsonMap(element);
        return updateJsonDoc(id, jsonMap);
    }
    
    /**
     * 删除ES文档
     * @param id ES文档id
     * @return 删除结果的响应
     */
    public DeleteResponse delete(String id)
    {
        return ESBaseDao.deleteESDoc(index(),id);
    }
}
