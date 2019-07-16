package com.micerlab.sparrow.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * 用fastjson库转换对象
 * @author 陈绿佳
 * @see com.alibaba.fastjson.JSONObject
 */
public class MapUtils
{
    /**
     * 将json转换为java对象
     * @param jsonMap 表示json的Map
     * @param objClass 特定java对象的Class
     * @param <E> java对象的类型
     * @return java对象 或 null
     */
    public static <E> E jsonMap2Obj(Map<String, Object> jsonMap, Class<E> objClass)
    {
        if(jsonMap == null) return null;
        return (new JSONObject(jsonMap)).toJavaObject(objClass);
    }
    
    /**
     * 将Java对象转换为json
     * @param obj java对象
     * @return json对象
     */
    @SuppressWarnings("unchecked")
    public static JSONObject obj2JsonMap(Object obj)
    {
        return (JSONObject) JSONObject.toJSON(obj);
    }
}
