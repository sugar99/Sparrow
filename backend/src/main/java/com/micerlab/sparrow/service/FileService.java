package com.micerlab.sparrow.service;

import com.micerlab.sparrow.domain.Result;
import java.util.Map;

public interface FileService {

    Result getPolicy(Map<String, Object> params);

    Result getPresignedUrl(Map<String, Object> params);

    Result deleteFile(Map<String, Object> params);

    Result downloadFile(String file_id);

    Result getFileVersions(String file_id);
}
