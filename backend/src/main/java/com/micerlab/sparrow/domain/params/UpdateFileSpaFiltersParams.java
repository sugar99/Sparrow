package com.micerlab.sparrow.domain.params;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UpdateFileSpaFiltersParams
{
    private List<Long> tags;
    private List<Long> categories;
}
