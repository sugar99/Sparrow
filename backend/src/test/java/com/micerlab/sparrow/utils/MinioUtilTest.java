package com.micerlab.sparrow.utils;

import com.micerlab.sparrow.SparrowApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class MinioUtilTest extends SparrowApplicationTests {

    @Autowired
    private MinioUtil minioUtil;

    @Test
    public void updateGetThumbnaliUrl() {
        System.out.println(minioUtil.updateGetThumbnaliUrl("img/1000517.jpeg"));
    }
}