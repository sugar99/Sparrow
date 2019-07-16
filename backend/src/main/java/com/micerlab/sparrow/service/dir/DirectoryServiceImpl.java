package com.micerlab.sparrow.service.dir;

import com.micerlab.sparrow.dao.postgre.ACLDao;
import com.micerlab.sparrow.dao.postgre.DirectoryDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.pojo.Directory;
import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class DirectoryServiceImpl implements DirectoryService{

    @Autowired
    private DirectoryDao directoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ACLService aclService;

    @Autowired
    private ACLDao aclDao;

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
        //创建目录
        String dir_id = UUID.randomUUID().toString();
        Directory directory = new Directory();
        directory.setId(dir_id);
        directory.setTitle(username);
        directory.setCreated_at(TimeUtil.currentTime());
        directory.setCreator_id(userDao.getAdminId());
        directoryDao.createDir(directory);
        //该目录在home目录下
        String master_id = directoryDao.getHomeDir().getId();
        directoryDao.setMasterDir(master_id, dir_id);
        return dir_id;
    }

    @Override
    public Result getDir(String dir_id) {
        return Result.OK().data(directoryDao.getDir(dir_id)).build();
    }

    @Override
    public String getCreatorId(String dir_id) {
        return directoryDao.getDir(dir_id).getCreator_id();
    }

    @Override
    public String getMasterDirId(String dir_id) {
        if (dir_id.equals(directoryDao.getRootDir().getId())) {
            return dir_id;
        }
        return directoryDao.getMasterDir(dir_id).getId();
    }

    @Override
    public Result updateDir(String dir_id, Map<String, Object> paramMap) {
        String title = paramMap.get("title").toString();
        directoryDao.updateDir(dir_id, title);
        return Result.OK().build();
    }

    @Override
    public Result deleteDir(String dir_id) {
        List<String> slavesIdList = directoryDao.getTotalSlavesId(dir_id);
        if (slavesIdList != null) {
            for (String slave_id: slavesIdList) {
                directoryDao.dropSlaveDir(slave_id);
                aclService.deleteResourcePermission(slave_id);
                directoryDao.deleteDir(dir_id);
            }
        }
        directoryDao.dropSlaveDir(dir_id);
        aclService.deleteResourcePermission(dir_id);
        directoryDao.deleteDir(dir_id);
        return Result.OK().build();
    }

    @Override
    public Result getSlaveResources(String dir_id) {
        return Result.OK().data(directoryDao.getOneLevelSlaves(dir_id)).build();

    }
}
