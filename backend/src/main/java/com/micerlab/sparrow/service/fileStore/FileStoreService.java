package com.micerlab.sparrow.service.fileStore;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.file.SpaFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface FileStoreService {

    Result getPolicy(Map<String, Object> params, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    Result getPresignedUrl(Map<String, Object> params, HttpServletRequest httpServletRequest);

    Result deleteFile(List<String> objectNames);

    void downloadFile(String title, String key, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    Map<String, Object> uploadThumbnail(File file);

    File getFile(SpaFile fileMeta);
}
