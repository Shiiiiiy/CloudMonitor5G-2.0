package com.datang.common.util;

import java.util.Map;

public class MapUtil {
    public static String mapSafeGet(Map<String, Object> map, String key){
        if(map==null){
            return "";
        }
        Object o = map.get(key);
        if(o==null){
            return "";
        }
        return o.toString();
    }
}
