package com.micerlab.sparrow.service.dir;

import com.micerlab.sparrow.domain.Result;

import java.util.Map;

public interface DirectoryService {
    Result createDir(String user_id, String cur_id);

    String createPersonalDir(String user_id, String username);

    Result getDir(String dir_id);

    String getCreatorId(String dir_id);

    String getMasterDirId(String dir_id);

    Result updateDir(String dir_id, Map<String, Object> paramMap);

    Result deleteDir(String dir_id);

    Result getSlaveResources(String user_id, String dir_id);
}
