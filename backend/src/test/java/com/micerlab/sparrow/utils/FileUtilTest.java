package com.micerlab.sparrow.utils;

import com.micerlab.sparrow.SparrowApplicationTests;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilTest extends SparrowApplicationTests
{
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
}