package com.micerlab.sparrow.service.doc;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SpaDocUpdateParams;

public interface DocumentService {

    Result createDoc(String user_id, String cur_id);

    Result getDoc(String doc_id);

    String getCreatorId(String doc_id);

    String getMasterDirId(String doc_id);

    Result updateDoc(String doc_id, SpaDocUpdateParams params);

    Result deleteDoc(String doc_id);

    @Deprecated
    Result getSlaveFiles1(String doc_id, int page, int per_page);
    
    Result getSlaveFiles2(String doc_id, int page, int per_page);
}
