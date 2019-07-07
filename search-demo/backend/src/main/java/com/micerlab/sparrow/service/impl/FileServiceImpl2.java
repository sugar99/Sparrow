package com.micerlab.sparrow.service.impl;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.SpaFilter;
import com.micerlab.sparrow.domain.SpaFilterType;
import com.micerlab.sparrow.service.FileService2;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileServiceImpl2 implements FileService2
{
    public Result createFileMeta(String file_id, Map<String, Object> params)
    {
        return null;
    }
    
    public Result retrieveFileMeta(String file_id)
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
