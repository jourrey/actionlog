package com.dianping.actionlog.common;

import com.dianping.actionlog.advice.LogManual;

/**
 * Created by jourrey on 17/1/11.
 */
public class ActionLogConfig {

    /**
     * 应用名称
     */
    private static String appName;
    /**
     * {@link LogManual}是否开启定位信息
     */
    private static boolean logManualIncludeLocation;
    /**
     * 是否打印ActionLog的Filter日志
     */
    private static boolean needFilterLog;

    static {
        appName = ActionLogConstants.DEFAULT_APP_NAME;
        logManualIncludeLocation = false;
        needFilterLog = false;
    }

    private ActionLogConfig() {
    }

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        ActionLogConfig.appName = appName;
    }

    public static boolean isLogManualIncludeLocation() {
        return logManualIncludeLocation;
    }

    public static void setLogManualIncludeLocation(boolean logManualIncludeLocation) {
        ActionLogConfig.logManualIncludeLocation = logManualIncludeLocation;
    }

    public static boolean isNeedFilterLog() {
        return needFilterLog;
    }

    public static void setNeedFilterLog(boolean needFilterLog) {
        ActionLogConfig.needFilterLog = needFilterLog;
    }

}
