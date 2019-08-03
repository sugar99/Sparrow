package com.micerlab.sparrow.service.doc;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.dao.es.SpaDocDao;
import com.micerlab.sparrow.dao.postgre.DocumentDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.ResourceType;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SpaDocUpdateParams;
import com.micerlab.sparrow.domain.pojo.Document;
import com.micerlab.sparrow.message.eventBus.event.doc.DeleteDocEvent;
import com.micerlab.sparrow.message.eventBus.event.doc.InsertDocEvent;
import com.micerlab.sparrow.message.eventBus.event.doc.UpdateDocEvent;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.utils.BusinessException;
import com.micerlab.sparrow.utils.TimeUtil;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService{

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SpaDocDao spaDocDao;

    @Autowired
    private ACLService aclService;

    /**
     * 创建文档
     * @param user_id User's Id
     * @param cur_id Current Directory's Id
     * @return Result
     */
    @Override
    public Result createDoc(String user_id, String cur_id) {
        String doc_id = UUID.randomUUID().toString();
        Timestamp timestamp = TimeUtil.currentTime();

        Document document = new Document(doc_id, user_id, timestamp);
        //创建文档事件
        EventBus.getDefault().post(new InsertDocEvent(doc_id, "doc", user_id, timestamp, timestamp));
        documentDao.setMasterDir(cur_id, doc_id);
        //用户对该文档有可读可写权限
        aclService.updateGroupPermission(userDao.getUserMetaById(user_id).getPersonal_group(), doc_id, ResourceType.DOC, "110");
        return Result.OK().data(document).build();
    }

    /**
     * 获取文档元数据
     * @param doc_id Document's Id
     * @return Document
     */
    @Override
    public Result getDoc(String doc_id) {
        JSONObject docJson = spaDocDao.getJsonDoc(doc_id);
        if(docJson == null)
            throw new BusinessException(ErrorCode.NOT_FOUND_DOC_ID, doc_id);
        return Result.OK().data(docJson).build();
    }

    /**
     * 获得创建者id
     * @param doc_id Document's Id
     * @return Creator's Id
     */
    @Override
    public String getCreatorId(String doc_id) {
        return documentDao.getDoc(doc_id).getCreator_id();
    }

    /**
     * 获取父目录id
     * @param doc_id Document's Id
     * @return Master Dirctory's Id
     */
    @Override
    public String getMasterDirId(String doc_id) {
        return documentDao.getMasterDir(doc_id).getId();
    }

    /**
     * 更新文档元数据
     * @param doc_id Document's Id
     * @param params 参数
     * @return Result
     */
    @Override
    public Result updateDoc(String doc_id, SpaDocUpdateParams params) {
        String title = params.getTitle();
        String desc = params.getDesc();
        Timestamp modified_time = TimeUtil.currentTime();
        EventBus.getDefault().post(new UpdateDocEvent(doc_id, title, desc, modified_time));
        return Result.OK().build();
    }

    /**
     * 删除文档
     * @param doc_id Directory's Id
     * @return Result
     */
    @Override
    public Result deleteDoc(String doc_id) {
        //产生删除文档事件
        EventBus.getDefault().post(new DeleteDocEvent(doc_id));
        return Result.OK().build();
    }

    /**
     * 获取下级文件
     * @param doc_id Document's Id
     * @param page
     * @param per_page
     * @return List of Files
     */
    @Deprecated
    @Override
    public Result getSlaveFiles1(String doc_id, int page, int per_page) {
        List<Map<String, Object>> files = spaDocDao.getFiles1(doc_id, page, per_page);
        return Result.OK().data(files).build();
    }
    
    /**
     * 获取下级文件
     * @param doc_id Document's Id
     * @param page 第几页
     * @param per_page 每页几条记录
     * @return 文件Meta
     */
    @Override
    public Result getSlaveFiles2(String doc_id, int page, int per_page) {
        return Result.OK().data(
                spaDocDao.getFiles2(doc_id, page, per_page)
        ).build();
    }
}
