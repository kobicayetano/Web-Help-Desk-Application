package com.ojt.app.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    public static String toJson(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
