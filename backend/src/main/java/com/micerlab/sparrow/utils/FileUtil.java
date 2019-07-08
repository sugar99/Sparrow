package com.micerlab.sparrow.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final String exts_dir = "classpath:static/type_exts/";
    
    /**
     * 加载拓展名配置文件，获取特定类型下的拓展名
     * @param type 文档类型
     * @return 该类型下的拓展名
     */
    public static List<String> loadTypeExtsConfig(String type)
    {
        String filePath = null;
        try
        {
            filePath = exts_dir + type + "_exts.txt";
            File type_exts_file = ResourceUtils.getFile(filePath);
            Scanner scanner = new Scanner(type_exts_file);
            List<String> exts = new ArrayList<>();
            while (scanner.hasNext())
                exts.add(scanner.next());
            return exts;
        } catch (FileNotFoundException e)
        {
            String errorMsg = "无法加载拓展名配置文件:" + filePath;
            logger.error(errorMsg);
        }
        return Collections.emptyList();
    }
    
}
