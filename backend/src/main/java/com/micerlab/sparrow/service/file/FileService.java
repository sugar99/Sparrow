package com.micerlab.sparrow.service.file;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.CreateSpaFileParams;
import com.micerlab.sparrow.domain.params.UpdateFileMetaParams;
import com.micerlab.sparrow.domain.search.SpaFilter;
import com.micerlab.sparrow.domain.search.SpaFilterType;

import java.util.List;

public interface FileService {

    Result getFileVersions(String file_id);
    
    Result createFileMeta(String file_id, CreateSpaFileParams params);
    
    Result getFileMeta(String file_id);
    
    Result updateFileMeta(String file_id, UpdateFileMetaParams params);
    
    Result deleteFileMeta(String file_id);
    
    Result createSpaFilter(SpaFilterType spaFilterType, SpaFilter spaFilter);
    
    Result retrieveSpaFilter(SpaFilterType spaFilterType, String filter_id);
    
    Result updateSpaFilter(SpaFilterType spaFilterType, String filter_id, SpaFilter spaFilter);
    
    Result deleteSpaFilter(SpaFilterType spaFilterType, String filter_id);
    
    Result retrieveFileSpaFilters(String file_id, SpaFilterType spaFilterType);
    
    Result updateFileSpaFilters(String file_id, SpaFilterType spaFilterType, List<Long> spaFilterIds);
}
