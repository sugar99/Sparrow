package com.micerlab.sparrow.domain.file;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SpaFileCreateParams
{
    private String title;
    private String creator;
    private String doc_id;
    private String ext;
    private long size;
    
    private String store_key;
    
    private String parent_id;
}
