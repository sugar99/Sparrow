package com.micerlab.sparrow.service.file;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.SpaFilter;
import com.micerlab.sparrow.domain.SpaFilterType;

import java.util.Map;

public interface FileService {

    Result getPolicy(Map<String, Object> params);

    Result getPresignedUrl(Map<String, Object> params);

    Result deleteFile(Map<String, Object> params);

    Result downloadFile(String file_id);

    Result getFileVersions(String file_id);
    
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
