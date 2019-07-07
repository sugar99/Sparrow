package com.micerlab.sparrow.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class SerializeUtil implements RedisSerializer<Object> {

    private Converter<Object, byte[]> serializer = new SerializingConverter();
    private Converter<byte[], Object> deserializer = new DeserializingConverter();

    private static final byte[] EMPTY_ARRAY = new byte[0];

    private static final Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        byte[] byteArray = null;
        if (null == o) {
            logger.info("---------------------->:Redis序列化的对象为空.");
            byteArray = EMPTY_ARRAY;
        } else {
            try {
                byteArray = serializer.convert(o);
            } catch (Exception e) {
                logger.info("------------------->:Redis序列化对象失败，异常信息: " + e.getMessage());
                byteArray = EMPTY_ARRAY;
            }
        }
        return byteArray;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        Object o = null;
        if ((bytes == null) || (bytes.length == 0)) {
            logger.info("----------------------->:Redis待反序列化的对象为空.");
        } else {
            try {
                o = deserializer.convert(bytes);
            } catch (Exception e) {
                logger.info("---------------------->:Redis反序列化对象失败，异常信息: " + e.getMessage());
            }
        }
        return o;
    }
}
