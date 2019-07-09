package com.micerlab.sparrow.service.file;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.dao.es.SpaFileDao;
import com.micerlab.sparrow.dao.es.SpaFilterDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.file.SpaFile;
import com.micerlab.sparrow.domain.params.CreateSpaFileParams;
import com.micerlab.sparrow.domain.params.UpdateFileMetaParams;
import com.micerlab.sparrow.domain.search.SpaFilter;
import com.micerlab.sparrow.domain.search.SpaFilterType;
import com.micerlab.sparrow.utils.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService
{
    
    @Autowired
    private SpaFileDao spaFileDao;
    
    @Autowired
    private SpaFilterDao spaFilterDao;
    
    public Result getFileVersions(String file_id)
    {
        
        return null;
    }
    
    public Result createFileMeta(String file_id, CreateSpaFileParams params)
    {
        // TODO: 文件历史版本关联
        
        SpaFile spaFile = new SpaFile(file_id, params);
        
        spaFile.setOriginal_id(file_id);
        // TODO: current time
        String created_time = "2019-07-09 18:03:00";
        String modified_time = "2019-07-09 18:03:00";
        spaFile.setCreated_time(created_time);
        spaFile.setModified_time(modified_time);
        JSONObject jsonMap = (JSONObject) JSONObject.toJSON(spaFile);
        spaFileDao.createFileMeta(file_id, jsonMap);
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
