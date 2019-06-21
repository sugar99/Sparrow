package com.sparrow.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        String body = "{objectName=user-dir-prefix/default_thumbnail.jpg, size=4992, mimeType=image/jpeg, height=1280, width=1280}";
//
//        body.replace("{","");
//        body.replace("}","");
//        body.replace("/","%2");
//        body.replace(",","&");
//
//        System.out.println(body);

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("objectName","user-dir-prefix/default_thumbnail.jpg");
        params.put("size","4992");
        params.put("mimeType","image/jpeg");
        params.put("height","1280");
        params.put("width","1280");

        List<String> keys = new ArrayList<String>(params.keySet());
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key).toString();
            value = URLEncoder.encode(value, "UTF-8");
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        System.out.println(prestr);
    }

}
