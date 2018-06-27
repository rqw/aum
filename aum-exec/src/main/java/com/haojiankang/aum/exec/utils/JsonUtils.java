package com.haojiankang.aum.exec.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonUtils {
    public static String stringify(Object obj) throws IOException {
        ObjectMapper ojbmapper = new ObjectMapper();
        ojbmapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        ojbmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return ojbmapper.writeValueAsString(obj);
    }


    public static <T extends Object> T parse( String jsonStr,Class<T> c) throws IOException {
        ObjectMapper ojbmapper = new ObjectMapper();
        ojbmapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        ojbmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        ojbmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return ojbmapper.readValue(jsonStr, c);

    }
}
