package com.micerlab.sparrow.service.fileStore;

import com.micerlab.sparrow.domain.Result;

import java.util.Map;

public interface FileStoreService {

    Result getPolicy(Map<String, Object> params);

    Result getPresignedUrl(Map<String, Object> params);

    Result deleteFile(Map<String, Object> params);

    Result downloadFile(String file_id);
}
