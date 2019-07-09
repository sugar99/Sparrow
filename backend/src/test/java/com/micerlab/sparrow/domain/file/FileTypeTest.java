package com.micerlab.sparrow.domain.file;

import com.micerlab.sparrow.SparrowApplicationTests;
import org.junit.Test;

import java.util.Map;

public class FileTypeTest extends SparrowApplicationTests
{
    @Test
    public void testLoadExts()
    {
        for(FileType fileType : FileType.values())
        {
            System.out.printf("%s_exts:%s\n", fileType.getType(),
                    fileType.getExts().toString()
            );
        }
        
        System.out.println("\next mapping");
        for(Map.Entry<String, FileType> entry: FileType.getExt2Type().entrySet())
            System.out.println(entry.getKey() + "\t=>\t" + entry.getValue());
    }
}