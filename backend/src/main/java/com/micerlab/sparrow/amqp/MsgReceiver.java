package com.micerlab.sparrow.amqp;

import com.micerlab.sparrow.config.RabbitConfig;
import com.micerlab.sparrow.dao.es.SpaFileDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.file.SpaFile;
import com.micerlab.sparrow.eventBus.event.file.UpdateFileThumbnailEvent;
import com.micerlab.sparrow.service.fileStore.FileStoreService;
import com.micerlab.sparrow.utils.BusinessException;
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
import java.io.IOException;
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

    // 生成缩略图，并更新到es
    @RabbitHandler
    public void process(String content) {
        logger.info("接收处理队列当中的消息： " + content);
        SpaFile fileMeta = spaFileDao.getFileMeta(content);
        File file = fileStoreService.getFile(fileMeta);
        try {
            Map<String, Object> thumbnailInfo = fileUtil.getThumbnailInfo(file);
            EventBus.getDefault().post(new UpdateFileThumbnailEvent(content, (String)thumbnailInfo.get("thumbnail_url")));
        } catch (Exception e){
            logger.error("生成缩略图失败: " + e.getMessage());
        }finally {
            file.delete();
        }
    }
}
