package com.haojiankang.aum.daemon.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "安装包信息")
public class PackageInfo {
    @ApiModelProperty(value="唯一标识",example="uuid")
    private String id;
    @ApiModelProperty(value="安装包版本",example="version",required = true)
    private String version;
    @ApiModelProperty(value="依赖版本",example="dependent",required = true)
    private String dependent;
    @ApiModelProperty(value="版本描述",example="describe")
    private String describe;
    @ApiModelProperty(value="应用代码",example="appCode",required = true)
    private String appcode;
    @ApiModelProperty(value="节点代码,不同节点节点代码不能相同",example="pointCode",required = true)
    private String pointcode;
    @ApiModelProperty(value="上传时间",example="yyyy-MM-dd hh24:mm:ss")
    private Date uploadtime;
    @ApiModelProperty(value="升级时间",example="yyyy-MM-dd hh24:mm:ss")
    private Date upgradetime;
    @ApiModelProperty(value="升级结果",example="true")
    private Boolean state;


}
