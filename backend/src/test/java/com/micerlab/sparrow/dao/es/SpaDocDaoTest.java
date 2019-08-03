package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.SparrowApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class SpaDocDaoTest extends SparrowApplicationTests
{
    @Autowired
    private SpaDocDao spaDocDao;
    
    @Test
    public void getFiles()
    {
        String doc_id = "algs";
        List<Map<String, Object>> files = spaDocDao.getFiles1(doc_id, 1, 10);
        System.out.println(files);
    }
}