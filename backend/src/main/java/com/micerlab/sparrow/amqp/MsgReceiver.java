package com.micerlab.sparrow.amqp;

import com.micerlab.sparrow.config.RabbitConfig;
import com.micerlab.sparrow.dao.es.SpaFileDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.file.SpaFile;
import com.micerlab.sparrow.eventBus.event.file.UpdateFileThumbnailEvent;
import com.micerlab.sparrow.service.file.FileExtractService;
import com.micerlab.sparrow.service.fileStore.FileStoreService;
import com.micerlab.sparrow.utils.BusinessException;
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
import java.io.IOException;
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
    private static final Integer K_word = 8;

    /**
     * 生成缩略图 / 记录文本信息，并更新到es
     * @param content
     */
    @RabbitHandler
    public void process(String content) {
        logger.info("接收处理队列当中的消息： " + content);
        SpaFile fileMeta = spaFileDao.get(content);
        File file = fileStoreService.getFile(fileMeta);
        try {
            // 生成缩略图
            Map<String, Object> thumbnailInfo = fileUtil.getThumbnailInfo(file);
            // 提取全文信息
            String text = FileExtractUtil.extractString(file.getAbsolutePath());
            if (text != null) {
                logger.debug("extract text: " + text);
                List<String> keyword = fileExtractService.findKeyword(text, K_word);
                logger.debug("findKeyword: " + keyword);

            }

            // 使用 EventBus 更新 es
            EventBus.getDefault().post(new UpdateFileThumbnailEvent(content, (String)thumbnailInfo.get("thumbnail_url")));
        } catch (Exception e) {
            logger.error("消息消费失败（生成缩略图 / 提取文本信息）: " + e.getMessage());
        } finally {
            file.delete();
        }
    }
}
