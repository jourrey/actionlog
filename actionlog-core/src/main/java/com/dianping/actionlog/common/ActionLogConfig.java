package com.dianping.actionlog.common;

import com.dianping.actionlog.advice.LogManual;
import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.context.DefaultActionLogKey;
import com.google.common.base.MoreObjects;

/**
 * Created by jourrey on 17/1/11.
 */
public class ActionLogConfig {

    /**
     * 应用名称
     */
    private static String appName;
    /**
     * 日志打印等级
     */
    private static LogLevel logLevel;
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
        logLevel = LogLevel.INFO;
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

    public static LogLevel getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(LogLevel logLevel) {
        ActionLogConfig.logLevel = logLevel;
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

    /**
     * 如果actionLogKey为null,则提供默认{@link DefaultActionLogKey#DEFAULT}
     *
     * @param actionLogKey 日志标记
     * @return
     */
    public static ActionLogKey ifNullOfferDefault(ActionLogKey actionLogKey) {
        return MoreObjects.firstNonNull(actionLogKey, DefaultActionLogKey.DEFAULT);
    }

    /**
     * 如果handleType为null,则提供默认{@link HandleType#PARAM}
     *
     * @param handleType 日志类型
     * @return
     */
    public static HandleType ifNullOfferDefault(HandleType handleType) {
        return MoreObjects.firstNonNull(handleType, HandleType.PARAM);
    }

}
