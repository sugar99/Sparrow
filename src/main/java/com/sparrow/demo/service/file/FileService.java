package com.sparrow.demo.service.file;

import com.sparrow.demo.utils.ResultModel;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author allen
 */
public interface FileService {
    ResultModel uploadFile(Map<String, Object> params);

    ResultModel deleteFile(Map<String, Object> params);

    void uploadFileData(Map<String, Object> params);

    void downloadFile(Map<String, Object> params, HttpServletResponse response);

    ResultModel getFileUrl(Map<String, Object> params);

    ResultModel getFileVersion(Map<String, Object> params);
}
