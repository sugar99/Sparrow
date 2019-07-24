package com.micerlab.sparrow.service.dir;

import com.micerlab.sparrow.dao.postgre.ACLDao;
import com.micerlab.sparrow.dao.postgre.DirectoryDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.ResourceType;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.pojo.Directory;
import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.utils.BusinessException;
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

    /**
     * 创建目录
     * @param user_id User's Id
     * @param cur_id Current Directory's Id
     * @return Directory
     */
    @Override
    public Result createDir(String user_id, String cur_id) {
        String dir_id = UUID.randomUUID().toString();
        Directory directory = new Directory(dir_id, user_id, TimeUtil.currentTime());
        directoryDao.createDir(directory);
        directoryDao.setMasterDir(cur_id, dir_id);
        //用户对该目录有可读可写权限
        aclService.updateGroupPermission(userDao.getUserMetaById(user_id).getPersonal_group(), dir_id, ResourceType.DIR, "110");
        return Result.OK().data(directory).build();
    }

    /**
     * 创建用户个人目录
     * @param user_id User's Id
     * @param username Username
     * @return Directory's Id
     */
    @Override
    public String createPersonalDir(String user_id, String username) {
        String dir_id = UUID.randomUUID().toString();
        Directory directory = new Directory(dir_id, userDao.getAdminId(), TimeUtil.currentTime(), username);
        directory.setModifiable(0);
        directoryDao.createDir(directory);
        directoryDao.setMasterDir(directoryDao.getHomeDir().getId(), dir_id);
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
    public Result updateDir(String user_id, String dir_id, Map<String, Object> paramMap) {
        if (directoryDao.isModifiable(dir_id) == 0 && !user_id.equals(userDao.getAdminId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        String title = paramMap.get("title").toString();
        directoryDao.updateDir(dir_id, title);
        return Result.OK().build();
    }

    @Override
    public Result deleteDir(String user_id, String dir_id) {
        if (directoryDao.isModifiable(dir_id) == 0 && !user_id.equals(userDao.getAdminId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
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
