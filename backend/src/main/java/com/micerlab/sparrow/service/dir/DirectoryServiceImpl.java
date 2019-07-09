package com.micerlab.sparrow.service.dir;

import com.micerlab.sparrow.dao.postgre.DirectoryDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.pojo.Directory;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class DirectoryServiceImpl implements DirectoryService{

    @Autowired
    private DirectoryDao directoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ACLService aclService;

    @Override
    public Result createDir(String user_id, String cur_id) {
        String dir_id = UUID.randomUUID().toString();
        Directory directory = new Directory();
        directory.setId(dir_id);
        directory.setCreator_id(user_id);
        directory.setCreated_at(TimeUtil.currentTime());
        directoryDao.createDir(directory);
        directoryDao.setMasterDir(cur_id, dir_id);
        String personal_group_id = userDao.getUserMetaById(user_id).getPersonal_group();
        aclService.updateGroupPermission(personal_group_id, dir_id, "100");
        return Result.OK().data(directory).build();
    }

    @Override
    public String createPersonalDir(String user_id, String username) {
        return null;
    }

    @Override
    public Result getDir(String dir_id) {
        return null;
    }

    @Override
    public String getCreatorId(String dir_id) {
        return null;
    }

    @Override
    public String getMasterDirId(String dir_id) {
        return null;
    }

    @Override
    public Result updateDir(String dir_id, Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public Result deleteDir(String dir_id) {
        return null;
    }

    @Override
    public Result getSlaveResources(String user_id, String dir_id) {
        return null;
    }
}
