package com.micerlab.sparrow.domain.params;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateSpaFileParams
{
    private String title;
    private String creator;
    private String doc_id;
    private String ext;
    private long size;
    
    private String store_key;
    
    private String parent_id;
}
