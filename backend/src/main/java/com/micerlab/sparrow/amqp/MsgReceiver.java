package com.micerlab.sparrow.amqp;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.config.RabbitConfig;
import com.micerlab.sparrow.dao.es.SpaFileDao;
import com.micerlab.sparrow.domain.file.FileType;
import com.micerlab.sparrow.domain.file.SpaFile;
import com.micerlab.sparrow.eventBus.event.file.UpdateFileEvent;
import com.micerlab.sparrow.service.file.FileExtractService;
import com.micerlab.sparrow.service.fileStore.FileStoreService;
import com.micerlab.sparrow.utils.FileExtractUtil;
import com.micerlab.sparrow.utils.FileUtil;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitConfig.QUEUE)
public class MsgReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    @Qualifier("minioService")
    private FileStoreService fileStoreService;

    @Autowired
    private SpaFileDao spaFileDao;

    @Autowired
    private FileExtractService fileExtractService;

    /**
     * 提取关键词的个数
     */
    private static final int K_words = 8;

    /**
     * 生成缩略图 / 记录文本信息，并更新到es
     * @param message
     */
    @RabbitHandler
    public void process(String message) {
        logger.info("接收处理队列当中的消息： " + message);
        SpaFile fileMeta = spaFileDao.get(message);
        File file = fileStoreService.getFile(fileMeta);
        try {
            JSONObject jsonMap = new JSONObject();
            
            // 生成缩略图
            Map<String, Object> thumbnailInfo = fileUtil.getThumbnailInfo(file);
            jsonMap.put("thumbnail_url", thumbnailInfo.get("thumbnail_url"));
            
            // 提取全文信息
            // content 和 keyword 需要记录插入到 es
            if(FileType.DOC.getType().equals(fileMeta.getType()))
            {
                String content = FileExtractUtil.extractString(file.getAbsolutePath());
                if (content != null)
                {
                    logger.debug("extract text: " + content);
                    List<String> keywords = fileExtractService.findKeyword(content, K_words);
                    logger.debug("findKeyword: " + keywords);
        
                    jsonMap.put("content", content);
                    jsonMap.put("keywords", keywords);
                }
            }

            // 使用 EventBus 更新 es
            EventBus.getDefault().post(new UpdateFileEvent(message, jsonMap));
        } catch (Exception e) {
            logger.error("消息消费失败（生成缩略图 / 提取文本信息）: " + e.getMessage());
        } finally {
            file.delete();
        }
    }
}
