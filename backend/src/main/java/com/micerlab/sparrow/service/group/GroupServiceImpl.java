package com.micerlab.sparrow.service.group;

import com.micerlab.sparrow.dao.postgre.ACLDao;
import com.micerlab.sparrow.dao.postgre.GroupDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.domain.pojo.Resource;
import com.micerlab.sparrow.domain.pojo.User;
import com.micerlab.sparrow.domain.principal.UserPrincipal;
import com.micerlab.sparrow.eventBus.event.group.DeleteGroupEvent;
import com.micerlab.sparrow.eventBus.event.group.InsertGroupEvent;
import com.micerlab.sparrow.eventBus.event.group.UpdateGroupEvent;
import com.micerlab.sparrow.utils.TimeUtil;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;


@Service("groupService")
public class GroupServiceImpl implements GroupService{

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ACLDao aclDao;

    /**
     * 新建群组
     * @param user_id 用户id
     * @param paramMap 参数
     * @return Result (data: group)
     */
    @Override
    public Result createGroup(String user_id, Map<String, Object> paramMap) {
        //群组信息
        Group group = new Group();
        group.setGroup_id(UUID.randomUUID().toString());
        group.setGroup_name(paramMap.get("group_name").toString());
        group.setGroup_desc(paramMap.get("group_desc").toString());
        group.setCreator_id(user_id);
        group.setCreated_at(TimeUtil.currentTime());
        //产生InsertGroupEvent, 在ES和PostgreSql中同步更新
        EventBus.getDefault().post(new InsertGroupEvent(group));
        return Result.OK().data(group).build();
    }

    /**
     * 新建个人群组（用以授权）
     * @param user_id 用户id
     * @param username 用户名
     * @return group_id
     */
    @Override
    public String createPersonalGroup(String user_id, String username) {
        String admin_id = userDao.getAdminId();
        Group group = new Group();
        String group_id = UUID.randomUUID().toString();
        group.setGroup_id(group_id);
        //群组名称为用户名
        group.setGroup_name(username);
        group.setGroup_desc("个人群组用以授权");
        //创建者为管理员，用户不可对该群组进行任何修改
        group.setCreator_id(admin_id);
        group.setCreated_at(TimeUtil.currentTime());
        group.setPersonal(1);
        groupDao.createGroup(group);
        //将用户添加到群组中
        groupDao.addMember(group_id, user_id, TimeUtil.currentTime());
        //更新用户的群组列表 (redis)
        updateUserGroupsList(user_id, group_id, "add");
        return group_id;
    }

    /**
     * 获取群组元数据
     * @param group_id 群组id
     * @return Result (data: group)
     */
    @Override
    public Result getGroupMeta(String group_id) {
        return Result.OK().data(groupDao.getGroupMeta(group_id)).build();
    }

    /**
     * 获取群主id
     * @param group_id 群组id
     * @return creator_id
     */
    @Override
    public String getGroupOwnerId(String group_id) {
        return groupDao.getGroupOwnerId(group_id);
    }

    /**
     * 更新群组元数据
     * @param group_id 群组id
     * @param paramMap 参数
     * @return Result (data: null)
     */
    @Override
    public Result updateGroupMeta(String group_id, Map<String, Object> paramMap) {
        String group_name = paramMap.get("group_name").toString();
        String group_desc= paramMap.get("group_desc").toString();
        //产生updateGroupEvent，在ES和PostgreSql中同步更新群组信息
        EventBus.getDefault().post(new UpdateGroupEvent(group_id, group_name, group_desc));
        return Result.OK().build();
    }

    /**
     * 删除群组
     * @param group_id 群组id
     * @return Result (data: null)
     */
    @Override
    public Result deleteGroup(String group_id) {
        //产生deleteGroupEvent，在ES和PostgreSql中同步更新
        EventBus.getDefault().post(new DeleteGroupEvent(group_id));
        return Result.OK().build();
    }

    /**
     * 添加群组成员
     * @param group_id 群组id
     * @param paramMap 参数
     * @return Result (data: null)
     */
    @Override
    public Result addGroupMember(String group_id, Map<String, Object> paramMap) {
        List<String> usersIdList = (List<String>) paramMap.get("users");
        Timestamp timestamp = TimeUtil.currentTime();
        for (String member_id: usersIdList) {
            //更新数据库
            groupDao.addMember(group_id, member_id, timestamp);
            //更新用户的群组列表
            updateUserGroupsList(member_id, group_id, "add");
        }
        return Result.OK().build();
    }

    /**
     * 获取群组成员
     * @param user_id 用户id
     * @param group_id 群组id
     * @return Result (data: userList)
     */
    @Override
    public Result getGroupMember(String user_id, String group_id) {
        List<User> usersList = groupDao.getGroupMembers(group_id);
        Map<String, Object> resultMap = new HashMap<>();
        //接口调用者是否为群组的群主，前端根据该标志进行渲染
        resultMap.put("isOwner", user_id.equals(groupDao.getGroupMeta(group_id).getCreator_id())? 1 : 0);
        List<Map<String, Object>> memberList = new ArrayList<>();
        for (User user: usersList) {
            //成员信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("user_id", user.getUser_id());
            userInfo.put("username", user.getUsername());
            userInfo.put("work_no", user.getWork_no());
            memberList.add(userInfo);
        }
        resultMap.put("memberList", memberList);
        return Result.OK().data(resultMap).build();
    }

    /**
     * 移除成员
     * @param group_id 群组id
     * @param member_id 成员id
     * @return Result (data: null)
     */
    @Override
    public Result deleteGroupMember(String group_id, String member_id) {
        //更新数据库
        groupDao.removeMember(group_id, member_id);
        //更新用户的群组列表
        updateUserGroupsList(member_id, group_id, "drop");
        return Result.OK().build();
    }

    /**
     * 获取群组具有操作权限的所有资源信息
     * @param group_id 群组id
     * @return Result (data: resourceList)
     */
    @Override
    public Result getAuthResource(String group_id) {
        List<Map<String, String>> data = groupDao.getGroupResources(group_id);
        return Result.OK().data(data).build();
    }

    /**
     * 将用户从群组移除后，更新用户的群组列表
     * @param user_id 用户id
     * @param group_id 群组id
     */
    private void updateUserGroupsList(String user_id, String group_id, String type) {
        UserPrincipal userPrincipal = (UserPrincipal) redisTemplate.opsForValue().get(user_id);
        if (userPrincipal != null) {
            List<String> groupsIdListTemp = userPrincipal.getGroupsIdList();
            if (type.equals("add")){
                groupsIdListTemp.add(group_id);
            } else {
                groupsIdListTemp.remove(group_id);
            }
            userPrincipal.setGroupsIdList(groupsIdListTemp);
            redisTemplate.opsForValue().set(user_id, userPrincipal);
        }
    }
}
