package com.micerlab.sparrow.domain.params;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateAuthGroupsParams {

    private String permission;

    private List<String> groups;
}
