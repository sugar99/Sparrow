package com.micerlab.sparrow.utils;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.SparrowApplicationTests;
import com.micerlab.sparrow.domain.search.SpaFilter;
import org.junit.Test;

public class FastJsonTest extends SparrowApplicationTests
{
   @Test
    public void testObject()
    {
        SpaFilter spaFilter = new SpaFilter(1, "ABC","26");
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(spaFilter);
        System.out.println(jsonObject);
    }
    
}
