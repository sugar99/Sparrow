package com.micerlab.sparrow.utils;

import com.micerlab.sparrow.SparrowApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilTest extends SparrowApplicationTests
{
    @Autowired
    private FileUtil fileUtil;


    @Test
    public void testLoadTypeExtsConfig()
    {
        String[] types = {"image", "doc", "video", "audio", "arch", "app"};
        for(String type: types)
        {
            List<String> exts  = FileUtil.loadTypeExtsConfig(type);
            System.out.println(type + "_exts: " + exts);
        }
    }

    @Test
    public void testGetThumbnailInfo(){
        File file = new File("D:\\test\\a.pdf");
        try {
            System.out.println(fileUtil.getThumbnailInfo(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}