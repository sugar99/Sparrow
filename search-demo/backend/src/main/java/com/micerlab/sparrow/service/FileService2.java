package com.micerlab.sparrow.service;

import com.micerlab.sparrow.domain.SpaFilterType;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.SpaFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface FileService2
{
    Result createFileMeta(String file_id, Map<String, Object> params);
    
    Result retrieveFileMeta(String file_id);
    
    Result updateFileMeta(String file_id, Map<String, Object> params);
    
    Result createSpaFilter(SpaFilterType spaFilterType, SpaFilter spaFilter);
    
    Result retrieveSpaFilter(SpaFilterType spaFilterType, String filter_id);
    
    Result updateSpaFilter(SpaFilterType spaFilterType, String filter_id, SpaFilter spaFilter);
    
    Result deleteSpaFilter(SpaFilterType spaFilterType, String filter_id);
    
    Result retrieveFileSpaFilters(String file_id, SpaFilterType spaFilterType);
    
    Result updateFileSpaFilters(String file_id, SpaFilterType spaFilterType);
}
