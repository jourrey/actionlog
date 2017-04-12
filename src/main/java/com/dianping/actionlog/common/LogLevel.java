package com.dianping.actionlog.common;

/**
 * 支持的日志级别
 * Created by jourrey on 16/11/16.
 */
public enum LogLevel {

    DEBUG, INFO, WARN, ERROR, OFF;

    public static LogLevel getLogLevel(String level) {
        try {
            return LogLevel.valueOf(level);
        } catch (IllegalArgumentException e) {
            return LogLevel.INFO;
        }
    }

    public static void main(String[] args) {
        System.out.println(LogLevel.getLogLevel("DEBUG"));
        System.out.println(LogLevel.getLogLevel("OFF"));
        System.out.println(LogLevel.getLogLevel("OFFF"));
    }

}
