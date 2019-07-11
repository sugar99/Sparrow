package com.micerlab.sparrow.service.doc;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SpaDocUpdateParams;

import java.util.Map;

public interface DocumentService {

    Result createDoc(String user_id, String cur_id);

    Result getDoc(String doc_id);

    String getCreatorId(String doc_id);

    String getMasterDirId(String doc_id);

    Result updateDoc(String doc_id, SpaDocUpdateParams params);

    Result deleteDoc(String doc_id);

    Result getSlaveFiles(String doc_id);
}
