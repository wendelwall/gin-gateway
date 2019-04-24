package com.riskeys.gateway.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ：sunrise
 * @description ：
 * @copyright ：	Copyright 2019 yowits Corporation. All rights reserved.
 * @create ：2019/4/20 23:09
 */
public class NsMapCache {

    private static final ConcurrentMap<String, String> routeMap = new ConcurrentHashMap<>();

    public static String getCache(String key){
        return routeMap.getOrDefault(key, null);
    }

    public static void initCache(String key, String value){
        routeMap.put(key, value);
    }
}
