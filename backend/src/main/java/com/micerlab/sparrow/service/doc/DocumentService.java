package com.micerlab.sparrow.service.doc;

import com.micerlab.sparrow.domain.Result;

import java.util.Map;

public interface DocumentService {

    Result createDoc(String user_id, String dir_id);

    Result getDoc(String doc_id);

    String getCreatorId(String doc_id);

    String getMasterDirId(String doc_id);

    Result updateDoc(String doc_id, Map<String, Object> paramMap);

    Result deleteDoc(String doc_id);
}
