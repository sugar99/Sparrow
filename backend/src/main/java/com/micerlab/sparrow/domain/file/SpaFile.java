package com.micerlab.sparrow.domain.file;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Sparrow File
 */
@Setter
@Getter
public class SpaFile
{
    private String id;
    
    private String title;
    private String desc = "";
    private String creator;
    private String doc_id;
    private String type;
    private String ext;
    private long size;
    private List<Long> tags = Collections.emptyList();
    private List<Long> categories = Collections.emptyList();
    
    private String store_key;
    private String thumbnail;
    private List<String> derived_files = Collections.emptyList();
    
    private Date created_time;
    private Date modified_time;
    
    private byte version = 0;
    private String original_id;
    private String parent_id = null;
    
    private List<String> keywords = Collections.emptyList();
    private String content;
    
    public SpaFile()
    {
    }
    
    public SpaFile(String id, SpaFileCreateParams params)
    {
        this.id = id;
        
        this.title = params.getTitle();
        this.creator = params.getCreator();
        this.doc_id = params.getDoc_id();
        this.ext = params.getExt();
        this.type = FileType.fromExt(ext).getType();
        this.size = params.getSize();
        
        this.store_key = params.getStore_key();
        this.parent_id = params.getParent_id();
        
    }
}
