package com.micerlab.sparrow.domain.doc;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Sparrow Doc
 */
@Setter
@Getter
public class SpaDoc
{
    private String id;
    private String title;
    private String desc;
    private String creator;
    private String[] files;
    private String created_time;
    private String modified_time;
    private byte meta_state;
    
    public SpaDoc()
    {
    }
    
    public SpaDoc(String id, String title, String desc, String creator, String[] files, String created_time, String modified_time, byte meta_state)
    {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.creator = creator;
        this.files = files;
        this.created_time = created_time;
        this.modified_time = modified_time;
        this.meta_state = meta_state;
    }
}
