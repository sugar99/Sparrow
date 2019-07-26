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

    /**
     * 获取创建者id
     * @param dir_id Directory's Id
     * @return Creator's Id
     */
    @Override
    public String getCreatorId(String dir_id) {
        return directoryDao.getDir(dir_id).getCreator_id();
    }

    /**
     * 获取父目录id
     * @param dir_id Directory's Id
     * @return Master Directory's Id
     */
    @Override
    public String getMasterDirId(String dir_id) {
        if (dir_id.equals(directoryDao.getRootDir().getId())) {
            return dir_id;
        }
        return directoryDao.getMasterDir(dir_id).getId();
    }

    /**
     * 更新目录元数据
     * @param user_id User's Id
     * @param dir_id Directory's Id
     * @param paramMap 参数
     * @return
     */
    @Override
    public Result updateDir(String user_id, String dir_id, Map<String, Object> paramMap) {
        //检查该目录是否可修改，若不可修改，则除非用户是管理员，否则操作失败
        if (directoryDao.isModifiable(dir_id) == 0 && !user_id.equals(userDao.getAdminId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        String title = paramMap.get("title").toString();
        directoryDao.updateDir(dir_id, title);
        return Result.OK().build();
    }

    /**
     * 删除目录
     * @param user_id User's Id
     * @param dir_id Directory's Id
     * @return result
     */
    @Override
    public Result deleteDir(String user_id, String dir_id) {
        if (directoryDao.isModifiable(dir_id) == 0 && !user_id.equals(userDao.getAdminId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        List<String> slavesIdList = directoryDao.getTotalSlavesId(dir_id);
        //删除子资源，子孙资源.....
        if (slavesIdList != null) {
            for (String slave_id: slavesIdList) {
                //解绑父子资源关系
                directoryDao.dropSlaveDir(slave_id);
                //删除群组资源授权关系
                aclService.deleteResourcePermission(slave_id);
                //删除资源
                directoryDao.deleteDir(dir_id);
            }
        }
        directoryDao.dropSlaveDir(dir_id);
        aclService.deleteResourcePermission(dir_id);
        directoryDao.deleteDir(dir_id);
        return Result.OK().build();
    }

    /**
     * 获取下级目录资源
     * @param dir_id Directory's Id
     * @return List of Directories or Documents
     */
    @Override
    public Result getSlaveResources(String dir_id) {
        return Result.OK().data(directoryDao.getOneLevelSlaves(dir_id)).build();

    }
}
