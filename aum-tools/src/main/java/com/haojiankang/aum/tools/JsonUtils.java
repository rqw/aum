package com.haojiankang.aum.tools;

import org.xidea.el.json.JSONDecoder;
import org.xidea.el.json.JSONEncoder;

import java.io.IOException;

public class JsonUtils {
    public static String stringify(Object obj) throws IOException {
       return JSONEncoder.encode(obj);
    }


    public static <T extends Object> T parse( String jsonStr,Class<T> c) throws IOException {
        return JSONDecoder.decode(jsonStr,c);
    }
}
