package com.dianping.actionlog.common;

import com.dianping.actionlog.advice.LogManual;
import com.dianping.actionlog.context.LogContext;
import com.dianping.actionlog.util.LionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jourrey on 17/1/11.
 */
public class SysConfig {
    private static final Logger LOG = LogManager.getLogger(SysConfig.class);

    public static String lionKey(String key) {
        try {
            return LogContext.getInstance().getAppName() + key;
        } catch (Throwable th) {
            LOG.error("lionKey exception", th);
            return "";
        }
    }

    public static String lionValue(String key, String defaultValue) {
        try {
            return LionUtils.getInstance().get(lionKey(key), defaultValue);
        } catch (Throwable th) {
            LOG.error("lionKey exception", th);
            return defaultValue;
        }
    }

    /**
     * 日志格式
     *
     * @return
     */
    public static String logConversionPattern() {
        try {
            return lionValue(LionKeys.LOG_CONVERSION_PATTERN, SysConstants.LOG_CONVERSION_PATTERN);
        } catch (Throwable th) {
            LOG.error("logConversionPattern exception", th);
            return SysConstants.LOG_CONVERSION_PATTERN;
        }
    }

    /**
     * 日志跟路径
     *
     * @return
     */
    public static String logHomePrefix() {
        try {
            return lionValue(LionKeys.LOG_HOME_PATH, SysConstants.LOG_HOME_PATH);
        } catch (Throwable th) {
            LOG.error("logHomePrefix exception", th);
            return SysConstants.LOG_HOME_PATH;
        }
    }

    /**
     * 日志文件最大大小
     *
     * @return
     */
    public static String logFileMaxSize() {
        try {
            return lionValue(LionKeys.LOG_FILE_MAX_SIZE, SysConstants.LOG_FILE_MAX_SIZE);
        } catch (Throwable th) {
            LOG.error("logFileMaxSize exception", th);
            return SysConstants.LOG_FILE_MAX_SIZE;
        }
    }

    /**
     * 日志文件最大数量
     *
     * @return
     */
    public static String logFileMaxNum() {
        try {
            return lionValue(LionKeys.LOG_FILE_MAX_NUM, SysConstants.LOG_FILE_MAX_NUM);
        } catch (Throwable th) {
            LOG.error("logFileMaxNum exception", th);
            return SysConstants.LOG_FILE_MAX_NUM;
        }
    }

    /**
     * 日志文件时间间隔
     *
     * @return
     */
    public static String logFileTimeInterval() {
        try {
            return lionValue(LionKeys.LOG_FILE_TIME_INTERVAL, SysConstants.LOG_FILE_TIME_INTERVAL);
        } catch (Throwable th) {
            LOG.error("logFileTimeInterval exception", th);
            return SysConstants.LOG_FILE_TIME_INTERVAL;
        }
    }

    /**
     * 日志文件时间间隔模式,true:是0点开始,false:当前时间开始
     *
     * @return
     */
    public static String logFileTimeIntervalModulate() {
        try {
            return lionValue(LionKeys.LOG_FILE_TIME_INTERVAL_MODULATE, SysConstants.LOG_FILE_TIME_INTERVAL_MODULATE);
        } catch (Throwable th) {
            LOG.error("logFileTimeIntervalModulate exception", th);
            return SysConstants.LOG_FILE_TIME_INTERVAL_MODULATE;
        }
    }

    /**
     * 日志Appender打印模式,true:即刻写到文件,false:缓存写到文件
     *
     * @return
     */
    public static String logAppenderImmediateFlush() {
        try {
            return lionValue(LionKeys.LOG_APPENDER_IMMEDIATE_FLUSH, SysConstants.LOG_APPENDER_IMMEDIATE_FLUSH);
        } catch (Throwable th) {
            LOG.error("logAppenderImmediateFlush exception", th);
            return SysConstants.LOG_APPENDER_IMMEDIATE_FLUSH;
        }
    }

    /**
     * 日志是否传递到上级logger
     *
     * @return
     */
    public static String loggerConfigAdditive() {
        try {
            return lionValue(LionKeys.LOGGER_ADDITIVE, SysConstants.LOGGER_ADDITIVE);
        } catch (Throwable th) {
            LOG.error("loggerConfigAdditive exception", th);
            return SysConstants.LOGGER_ADDITIVE;
        }
    }

    /**
     * 日志是否开启定位信息
     *
     * @return
     */
    public static String loggerIncludeLocation() {
        try {
            return lionValue(LionKeys.LOGGER_INCLUDE_LOCATION, SysConstants.LOGGER_INCLUDE_LOCATION);
        } catch (Throwable th) {
            LOG.error("loggerIncludeLocation exception", th);
            return SysConstants.LOGGER_INCLUDE_LOCATION;
        }
    }

    /**
     * 日志打印等级
     *
     * @return
     */
    public static LogLevel loggerConfigLogLevel() {
        try {
            return LogLevel.getLogLevel(lionValue(LionKeys.LOGGER_LOG_LEVEL, SysConstants.LOGGER_LOG_LEVEL));
        } catch (Throwable th) {
            LOG.error("loggerConfigLogLevel exception", th);
            return LogLevel.getLogLevel(SysConstants.LOGGER_LOG_LEVEL);
        }
    }

    /**
     * {@link LogManual}是否开启定位信息
     *
     * @return
     */
    public static String logManualIncludeLocation() {
        try {
            return lionValue(LionKeys.LOG_MANUAL_INCLUDE_LOCATION, SysConstants.LOG_MANUAL_INCLUDE_LOCATION);
        } catch (Throwable th) {
            LOG.error("logManualIncludeLocation exception", th);
            return SysConstants.LOG_MANUAL_INCLUDE_LOCATION;
        }
    }

    /**
     * 是否打印ActionLog的Filter日志
     *
     * @return
     */
    public static boolean needFilterLog() {
        try {
            return Boolean.valueOf(SysConfig.lionValue(LionKeys.FILTER_LOG_SWITCH_KEY
                    , SysConstants.FILTER_LOG_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needFilterLog exception", th);
            return Boolean.valueOf(SysConstants.FILTER_LOG_DEFAULT_VALUE);
        }
    }

    /**
     * 是否打印ActionLog的Pigeon日志
     *
     * @return
     */
    public static boolean needPigeonLog() {
        try {
            return Boolean.valueOf(SysConfig.lionValue(LionKeys.PIGEON_LOG_SWITCH_KEY
                    , SysConstants.PIGEON_LOG_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needPigeonLog exception", th);
            return Boolean.valueOf(SysConstants.PIGEON_LOG_DEFAULT_VALUE);
        }
    }

}
