package com.haojiankang.aum.api;

import com.haojiankang.aum.tools.HttpUtils;
import com.haojiankang.aum.tools.JsonUtils;
import com.haojiankang.aum.tools.OsUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        String post = HttpUtils.post(daemonUrl, HttpUtils.formBody("appCode", appCode, "pointCode", pointCode, "version", version, "properties", JsonUtils.stringify(properties)), null);
        Map data = JsonUtils.parse(post, Map.class);
        if(data.get("data")!=null){
            Map d= (Map)data.get("data");
            if(d.get("status")!=null){
                if(StringUtils.equals(d.get("status").toString(),"0")){
                    //当前应用处于非正常状态
                    System.exit(0);
                    System.out.println("The current application is in an abnormal state, please check and resolve the update service before starting");
                }
            }
        }
        return true;
    }


}
