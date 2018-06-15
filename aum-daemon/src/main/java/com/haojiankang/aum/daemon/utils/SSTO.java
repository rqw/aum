package com.haojiankang.aum.daemon.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@Data
@ApiModel(description = "系统标准传输对象")
public class SSTO<T> {
    @ApiModelProperty("状态码,通用状态码：成功-200,失败-500")
    private String code;
    @ApiModelProperty("提示信息")
    private String message;
    @ApiModelProperty("状态:成功-true，失败-false")
    private Boolean state;
    @ApiModelProperty("附加信息")
    private T data;
    public static <T> SSTO<T> build(String code) {
        return build(code,null,null,null);
    }
    public static <T> SSTO<T> build(Boolean state) {
        return build(null,state,null,null);
    }
    public static <T> SSTO<T> build(String code, String message) {
        return build(code,null,message,null);
    }
    public static <T> SSTO<T> build(Boolean state, String message) {
        return build(null,state,message,null);
    }
    public static <T> SSTO<T> build(String code, String message, T data) {
        return build(code,null,message,data);
    }
    public static <T> SSTO<T> build(Boolean state, String message, T data) {
        return build(null,state,message,data);
    }
    public static <T> SSTO<T> build(String code, Boolean state, String message, T data) {
        SSTO<T> ssto=new SSTO<>();
        ssto.code = code;
        ssto.message = message;
        ssto.state = state;
        ssto.data = data;
        return ssto;
    }
    public static <T> SSTO<T> ok(String message) {
        return ok(message,null);
    }
    public static <T> SSTO<T> ok(T data) {
        return ok(null,data);
    }
    public static <T> SSTO<T> ok(String message, T data) {
        return build("200",true,message,data);
    }
    public static <T> SSTO<T> not(String message) {
        return not(message,null);
    }
    public static <T> SSTO<T> not(String message, T data) {
        return not("500",message,data);
    }
    public static <T> SSTO<T> not(String code,String message, T data) {
        return build(code,false,message,data);
    }
}
