package com.micerlab.sparrow.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MapUtils
{
    public static <E> E jsonMap2Obj(Map<String, Object> jsonMap, Class<E> objClass)
    {
        return (new JSONObject(jsonMap)).toJavaObject(objClass);
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> obj2JsonMap(Object obj)
    {
        return (Map<String, Object>) JSONObject.toJSON(obj);
    }
}
