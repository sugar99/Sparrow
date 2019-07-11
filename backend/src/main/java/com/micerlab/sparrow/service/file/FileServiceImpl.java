package com.micerlab.sparrow.service.file;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.dao.es.SpaDocDao;
import com.micerlab.sparrow.dao.es.SpaFileDao;
import com.micerlab.sparrow.dao.es.SpaFilterDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.doc.SpaDoc;
import com.micerlab.sparrow.domain.file.SpaFile;
import com.micerlab.sparrow.domain.params.CreateSpaFileParams;
import com.micerlab.sparrow.domain.params.UpdateFileMetaParams;
import com.micerlab.sparrow.domain.search.SpaFilter;
import com.micerlab.sparrow.domain.search.SpaFilterType;
import com.micerlab.sparrow.utils.BusinessException;
import com.micerlab.sparrow.utils.MapUtils;
import com.micerlab.sparrow.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService
{
    
    @Autowired
    private SpaFileDao spaFileDao;
    
    @Autowired
    private SpaDocDao spaDocDao;
    
    @Autowired
    private SpaFilterDao spaFilterDao;
    
    public Result getFileVersions(String file_id)
    {
        
        return null;
    }
    
    public Result createFileMeta(String file_id, CreateSpaFileParams params)
    {
        // TODO: 文件历史版本关联
        
        SpaFile file = new SpaFile(file_id, params);
        String parent_id = file.getParent_id();
        String doc_id = file.getDoc_id();
        
        Map<String, Object> map = spaFileDao.getDocAndParentFile(doc_id, parent_id);
        SpaDoc doc = MapUtils.jsonMap2Obj((Map<String, Object>) map.get("doc"), SpaDoc.class);
        String modified_time = TimeUtil.currentTimeStr();
        file.setModified_time(modified_time);
        
        if (StringUtils.isEmpty(parent_id))
        {
            file.setOriginal_id(file_id);
            file.setCreated_time(modified_time);
            doc.getFiles().add(file_id);
        } else
        {
            if(!doc.getFiles().contains(parent_id))
                throw new BusinessException(ErrorCode.NOT_FOUND_FILE_IN_DOC,
                        "doc_id: " + doc_id + ";parent_id: " + parent_id);
            
            SpaFile parent = MapUtils.jsonMap2Obj((Map<String, Object>) map.get("parent"), SpaFile.class);
            
            // 继承父版本文件的属性
            file.setVersion((byte)(parent.getVersion() + 1));
            file.setDesc(parent.getDesc());
            file.setCategories(parent.getCategories());
            file.setTags(parent.getTags());
            file.setCreated_time(parent.getCreated_time());
            file.setOriginal_id(parent.getOriginal_id());
            
            // TODO: 是否限制父版本文件与当前文件的格式一致？
            // if(!file.getExt().equals(parent.getExt()))
            //
            
            doc.getFiles().remove(parent_id);
            doc.getFiles().add(file_id);
        }
        
        spaFileDao.createFileMeta(file_id, MapUtils.obj2JsonMap(file));
        spaDocDao.updateDocMeta(doc_id, MapUtils.obj2JsonMap(doc));
        return Result.OK().build();
    }
    
    public Result retrieveFileMeta(String file_id)
    {
        SpaFile file = spaFileDao.getFileMeta(file_id);
        return Result.OK().data(file).build();
    }
    
    @Override
    public Result deleteFileMeta(String file_id)
    {
        spaFileDao.deleteFileMeta(file_id);
        return Result.OK().build();
    }
    
    public Result updateFileMeta(String file_id, UpdateFileMetaParams params)
    {
        JSONObject jsonMap = (JSONObject) JSONObject.toJSON(params);
        // TODO: current time
        String modified_time = "2019-07-09 18:03:00";
        jsonMap.put("modified_time", modified_time);
        spaFileDao.updateFileMeta(file_id, jsonMap);
        return Result.OK().build();
    }
    
    public Result createSpaFilter(SpaFilterType spaFilterType, SpaFilter spaFilter)
    {
        
        return null;
    }
    
    public Result retrieveSpaFilter(SpaFilterType spaFilterType, String filter_id)
    {
        SpaFilter spaFilter = spaFilterDao.getSpaFilter(spaFilterType, filter_id);
        if (spaFilter == null)
            if (spaFilterType == SpaFilterType.TAG)
                throw new BusinessException(ErrorCode.NOT_FOUND_TAG_ID, filter_id);
            else throw new BusinessException(ErrorCode.NOT_FOUND_CATEGORY_ID, filter_id);
        return Result.OK().data(spaFilter).build();
    }
    
    public Result updateSpaFilter(SpaFilterType spaFilterType, String filter_id, SpaFilter spaFilter)
    {
        spaFilter.setId(Integer.parseInt(filter_id));
        spaFilterDao.updateSpaFilter(spaFilterType, filter_id, spaFilter);
        return Result.OK().build();
    }
    
    public Result deleteSpaFilter(SpaFilterType spaFilterType, String filter_id)
    {
        spaFilterDao.deleteSpaFilter(spaFilterType, filter_id);
        return Result.OK().build();
    }
    
    @Override
    public Result retrieveFileSpaFilters(String file_id, SpaFilterType spaFilterType)
    {
        List<SpaFilter> spaFilters = spaFileDao.retrieveFileSpaFilters(file_id, spaFilterType);
        return Result.OK().data(spaFilters).build();
    }
    
    @Override
    public Result updateFileSpaFilters(String file_id, SpaFilterType spaFilterType, List<Long> spaFilterIds)
    {
        spaFileDao.updateFileSpaFilters(file_id, spaFilterType, spaFilterIds);
        return Result.OK().build();
    }
}
