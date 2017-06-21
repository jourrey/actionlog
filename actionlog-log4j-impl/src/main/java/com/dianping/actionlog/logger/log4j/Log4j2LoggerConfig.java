package com.dianping.actionlog.logger.log4j;

import com.dianping.actionlog.common.LogLevel;

import java.io.File;

/**
 * Created by jourrey on 17/1/11.
 */
public class Log4j2LoggerConfig {

    /**
     * 日志格式
     */
    private static String logConversionPattern;
    /**
     * 日志跟路径
     */
    private static String logHomePath;
    /**
     * 日志文件最大大小
     */
    private static String logFileMaxSize;
    /**
     * 日志文件最大数量
     */
    private static String logFileMaxNum;
    /**
     * 日志文件时间间隔
     */
    private static String logFileTimeInterval;
    /**
     * 日志Appender打印模式,true:即刻写到文件,false:缓存写到文件
     */
    private static String logFileTimeIntervalModulate;
    /**
     * 日志文件时间间隔模式,true:是0点开始,false:当前时间开始
     */
    private static String logAppenderImmediateFlush;
    /**
     * 日志是否传递到上级logger
     */
    private static String loggerAdditive;
    /**
     * 日志是否开启定位信息
     */
    private static String loggerIncludeLocation;
    /**
     * 日志打印等级
     */
    private static LogLevel loggerLogLevel;

    static {
        logConversionPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS z} %-5level [%t] %msg%xEx%n";
        logHomePath = File.separator + "data" + File.separator + "applogs";
        logFileMaxSize = "50M";
        logFileMaxNum = "100";
        logFileTimeInterval = "24";
        logFileTimeIntervalModulate = "true";
        logAppenderImmediateFlush = "false";
        loggerAdditive = "false";
        loggerIncludeLocation = "false";
        loggerLogLevel = LogLevel.INFO;
    }

    private Log4j2LoggerConfig() {
    }

    public static String getLogConversionPattern() {
        return logConversionPattern;
    }

    public static void setLogConversionPattern(String logConversionPattern) {
        Log4j2LoggerConfig.logConversionPattern = logConversionPattern;
    }

    public static String getLogHomePath() {
        return logHomePath;
    }

    public static void setLogHomePath(String logHomePath) {
        Log4j2LoggerConfig.logHomePath = logHomePath;
    }

    public static String getLogFileMaxSize() {
        return logFileMaxSize;
    }

    public static void setLogFileMaxSize(String logFileMaxSize) {
        Log4j2LoggerConfig.logFileMaxSize = logFileMaxSize;
    }

    public static String getLogFileMaxNum() {
        return logFileMaxNum;
    }

    public static void setLogFileMaxNum(String logFileMaxNum) {
        Log4j2LoggerConfig.logFileMaxNum = logFileMaxNum;
    }

    public static String getLogFileTimeInterval() {
        return logFileTimeInterval;
    }

    public static void setLogFileTimeInterval(String logFileTimeInterval) {
        Log4j2LoggerConfig.logFileTimeInterval = logFileTimeInterval;
    }

    public static String getLogFileTimeIntervalModulate() {
        return logFileTimeIntervalModulate;
    }

    public static void setLogFileTimeIntervalModulate(String logFileTimeIntervalModulate) {
        Log4j2LoggerConfig.logFileTimeIntervalModulate = logFileTimeIntervalModulate;
    }

    public static String getLogAppenderImmediateFlush() {
        return logAppenderImmediateFlush;
    }

    public static void setLogAppenderImmediateFlush(String logAppenderImmediateFlush) {
        Log4j2LoggerConfig.logAppenderImmediateFlush = logAppenderImmediateFlush;
    }

    public static String getLoggerAdditive() {
        return loggerAdditive;
    }

    public static void setLoggerAdditive(String loggerAdditive) {
        Log4j2LoggerConfig.loggerAdditive = loggerAdditive;
    }

    public static String getLoggerIncludeLocation() {
        return loggerIncludeLocation;
    }

    public static void setLoggerIncludeLocation(String loggerIncludeLocation) {
        Log4j2LoggerConfig.loggerIncludeLocation = loggerIncludeLocation;
    }

    public static LogLevel getLoggerLogLevel() {
        return loggerLogLevel;
    }

    public static void setLoggerLogLevel(LogLevel loggerLogLevel) {
        Log4j2LoggerConfig.loggerLogLevel = loggerLogLevel;
    }

}
