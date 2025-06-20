package com.crieff.framework.basic.utils;

import java.util.Map;

/**
 * @description:
 * @author: aKuang
 * @time: 2020-05-24 21:44
 */
public class ContextMapHolder {

    private static final ThreadLocal<Map<String, String>> contextMapThreadLocal = new ThreadLocal<>();

    public static Map<String, String> getContextMap() {
        return contextMapThreadLocal.get();
    }

    public static void setContextMap(Map<String, String> currentThreadContextMap) {
        contextMapThreadLocal.set(currentThreadContextMap);
    }

    public static void removeContextMap() {
        contextMapThreadLocal.remove();
    }
}
