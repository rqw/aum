package com.haojiankang.aum.api;

import com.haojiankang.aum.api.utils.HttpUtils;
import com.haojiankang.aum.api.utils.JsonUtils;
import com.haojiankang.aum.api.utils.OsUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
@Getter
@Setter
public class AumApi {
    private static AumApi api=new AumApi();
    private String daemonUrl="http://localhost:4437/us/appinfo";
    private String appCode;
    private String pointCode;
    private Map<String,Object> properties;
    private String version;

    private AumApi(){
        properties=new HashMap<>();
        properties.put("pid",OsUtils.getPid());
    }
    public static AumApi api(){
        return api;
    }
    public boolean register() throws IOException {
        Map<String,String> bodys=new HashMap<>();
        HttpUtils.post(daemonUrl, HttpUtils.formBody("appCode",appCode,"pointCode",pointCode,"version",version,"properties",JsonUtils.stringify(properties)), null);
        return true;
    }


}
