package com.sparrow.demo.service.document;

import com.sparrow.demo.utils.ResultModel;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author allen
 */
public interface DocumentService {
    ResultModel addDoc();

    ResultModel updateDocMeta(Map<String, Object> params);

    ResultModel getDocList(Map<String, Object> params);

    ResultModel getDocDetail(Map<String, Object> params);

    ResultModel deleteDoc(Map<String, Object> params);

    void downloadDoc(Map<String, Object> params, HttpServletResponse response);
}

