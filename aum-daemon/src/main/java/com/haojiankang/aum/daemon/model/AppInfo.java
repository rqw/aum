package com.haojiankang.aum.daemon.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "应用信息")
public class AppInfo {
    @ApiModelProperty(value="唯一标识",example="uuid")
    private String id;
    @ApiModelProperty(value="应用代码,来自于VCC系统",example="appcode",required = true)
    private String appCode;
    @ApiModelProperty(value="节点代码,不同节点节点代码不能相同",example="pointCode",required = true)
    private String pointCode;
    @ApiModelProperty(value="应用属性",example="properties",required = true)
    private String properties;
    @ApiModelProperty(value="当前版本",example="version",required = true)
    private String version;
}
