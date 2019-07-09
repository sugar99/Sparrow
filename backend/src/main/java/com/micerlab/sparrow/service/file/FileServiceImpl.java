package com.micerlab.sparrow.service.file;

import com.micerlab.sparrow.dao.es.SpaFileDao;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.file.SpaFile;
import com.micerlab.sparrow.domain.search.SpaFilter;
import com.micerlab.sparrow.domain.search.SpaFilterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private SpaFileDao spaFileDao;
    
    public Result getFileVersions(String file_id){
        return null;
    }
    
    public Result createFileMeta(String file_id, Map<String, Object> params)
    {
        return null;
    }
    
    public Result retrieveFileMeta(String file_id)
    {
        SpaFile file = spaFileDao.getFileMeta(file_id);
        return Result.OK().data(file).build();
    }
    
    @Override
    public Result deleteFileMeta(String file_id)
    {
        return null;
    }
    
    public Result updateFileMeta(String file_id, Map<String, Object> params)
    {
        return null;
    }
    
    public Result createSpaFilter(SpaFilterType spaFilterType, SpaFilter spaFilter)
    {
        return null;
    }
    
    public Result retrieveSpaFilter(SpaFilterType spaFilterType, String filter_id)
    {
        return null;
    }
    
    public Result updateSpaFilter(SpaFilterType spaFilterType, String filter_id, SpaFilter spaFilter)
    {
        return null;
    }
    
    public Result deleteSpaFilter(SpaFilterType spaFilterType, String filter_id)
    {
        return null;
    }
    
    @Override
    public Result retrieveFileSpaFilters(String file_id, SpaFilterType spaFilterType)
    {
        return null;
    }
    
    @Override
    public Result updateFileSpaFilters(String file_id, SpaFilterType spaFilterType)
    {
        return null;
    }
}
