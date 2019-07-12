package com.micerlab.sparrow.service.file;

import com.hankcs.hanlp.seg.common.Term;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;
import java.util.List;

/**
 * @Description TODO
 * @Author Honda
 * @Date 2019/7/12 16:13
 **/
public interface FileExtractService {

    /**
     * 提取文本关键词
     * @param path
     */
    List<Term> createFileIndex(String path);
}
