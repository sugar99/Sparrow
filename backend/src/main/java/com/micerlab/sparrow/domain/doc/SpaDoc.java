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
    private Date created_time;
    private Date modified_time;
    private byte meta_state;
}
