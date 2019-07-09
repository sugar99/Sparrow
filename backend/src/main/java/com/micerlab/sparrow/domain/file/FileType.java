package com.micerlab.sparrow.domain.file;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.utils.BusinessException;
import com.micerlab.sparrow.utils.FileUtil;

import java.util.*;

public enum FileType
{
    ALL(),
    IMAGE(),
    DOC(),
    VIDEO(),
    AUDIO(),
    OTHERS()
    ;

    private String type;
    private final List<String> exts;

    private final static Map<String, FileType> ext2Type;

    static {
        Map<String, FileType> ext2TypeTemp = new HashMap<>();
        for(FileType fileType : values())
            if (!fileType.equals(FileType.ALL)
                    && !fileType.equals(FileType.OTHERS))
                for (String ext : fileType.exts)
                    ext2TypeTemp.put(ext, fileType);
        ext2Type = Collections.unmodifiableMap(ext2TypeTemp);
    }

    FileType()
    {
        this.type = name().toLowerCase();
        if(type.equals("all") || type.equals("others"))
            this.exts = Collections.emptyList();
        else this.exts = FileUtil.loadTypeExtsConfig(type);
    }

    public String getType()
    {
        return type;
    }

    public List<String> getExts()
    {
        return exts;
    }

    @Override
    public String toString()
    {
        return type;
    }

    public static FileType fromExt(String ext)
    {
        if(ext2Type.containsKey(ext))
            return ext2Type.get(ext);
        return FileType.OTHERS;
    }

    public static Map<String, FileType> getExt2Type()
    {
        return ext2Type;
    }

    public static FileType fromType(String type)
    {
        try
        {
            return FileType.valueOf(type.toUpperCase());
        }catch (Exception ex)
        {
            throw new BusinessException(ErrorCode.PARAM_ERR_SEARCH_TYPE, "error search type <" + type + ">");
        }
    }

    public static void validateFileType(String type)
    {
        fromType(type);
    }
}

