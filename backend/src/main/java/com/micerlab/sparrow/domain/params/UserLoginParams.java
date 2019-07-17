package com.micerlab.sparrow.domain.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class UserLoginParams
{
    @ApiModelProperty(example = "0001")
    private String work_no;
    
    @ApiModelProperty(example = "sparrow")
    private String password;
}
