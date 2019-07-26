package com.micerlab.sparrow.domain.params;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSpaGroupParams {

    @ApiModelProperty(example = "测试团队")
    private String group_name;

    @ApiModelProperty(example = "测试组")
    private String group_desc;
}
