package com.micerlab.sparrow.domain.params;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateFileMetaParams
{
    private String title;
    private String desc;
    private List<Long> categories;
    private List<Long> tags;
}
