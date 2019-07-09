package com.micerlab.sparrow.amqp;

import com.micerlab.sparrow.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.QUEUE)
public class MsgReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitHandler
    public void process(String content) {
        System.out.println(content);
        logger.info("接收处理队列当中的消息： " + content);
    }
}
