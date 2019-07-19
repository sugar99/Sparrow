package com.micerlab.sparrow.service.doc;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.dao.es.SpaDocDao;
import com.micerlab.sparrow.dao.postgre.DocumentDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SpaDocUpdateParams;
import com.micerlab.sparrow.domain.pojo.Directory;
import com.micerlab.sparrow.domain.pojo.Document;
import com.micerlab.sparrow.eventBus.event.doc.DeleteDocEvent;
import com.micerlab.sparrow.eventBus.event.doc.InsertDocEvent;
import com.micerlab.sparrow.eventBus.event.doc.UpdateDocEvent;
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

    @Override
    public Result createDoc(String user_id, String cur_id) {
        String doc_id = UUID.randomUUID().toString();
        Timestamp timestamp = TimeUtil.currentTime();

        Document document = new Document(doc_id, user_id, timestamp);
        //创建文档事件
        EventBus.getDefault().post(new InsertDocEvent(doc_id, "doc", user_id, timestamp, timestamp));
        documentDao.setMasterDir(cur_id, doc_id);
        //用户对该文档有可读可写权限
        aclService.updateGroupPermission(userDao.getUserMetaById(user_id).getPersonal_group(), doc_id, "110");
        return Result.OK().data(document).build();
    }

    @Override
    public Result getDoc(String doc_id) {
        JSONObject docJson = spaDocDao.getJsonDoc(doc_id);
        if(docJson == null)
            throw new BusinessException(ErrorCode.NOT_FOUND_DOC_ID, doc_id);
        return Result.OK().data(docJson).build();
    }

    @Override
    public String getCreatorId(String doc_id) {
        return documentDao.getDoc(doc_id).getCreator_id();
    }

    @Override
    public String getMasterDirId(String doc_id) {
        return documentDao.getMasterDir(doc_id).getId();
    }

    @Override
    public Result updateDoc(String doc_id, SpaDocUpdateParams params) {
        String title = params.getTitle();
        String desc = params.getDesc();
        Timestamp modified_time = TimeUtil.currentTime();
        EventBus.getDefault().post(new UpdateDocEvent(doc_id, title, desc, modified_time));
        return Result.OK().build();
    }

    @Override
    public Result deleteDoc(String doc_id) {
        EventBus.getDefault().post(new DeleteDocEvent(doc_id));
        return Result.OK().build();
    }

    @Override
    public Result getSlaveFiles(String doc_id) {
        List<Map<String, Object>> files = spaDocDao.getFiles(doc_id);
        return Result.OK().data(files).build();
    }


}
