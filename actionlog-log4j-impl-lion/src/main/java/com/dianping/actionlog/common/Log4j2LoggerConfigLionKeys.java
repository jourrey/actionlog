package com.dianping.actionlog.common;

/**
 * Created by jourrey on 16/08/09.
 */
public interface Log4j2LoggerConfigLionKeys {

    // 日志Appender配置
    String LOG_CONVERSION_PATTERN = ".ActionLog.Log.ConversionPattern";
    String LOG_HOME_PATH = ".ActionLog.Log.HomePath";
    String LOG_FILE_MAX_SIZE = ".ActionLog.Log.File.MaxSize";
    String LOG_FILE_MAX_NUM = ".ActionLog.Log.File.MaxNum";
    String LOG_FILE_TIME_INTERVAL = ".ActionLog.Log.File.TimeInterval";
    String LOG_FILE_TIME_INTERVAL_MODULATE = ".ActionLog.Log.File.TimeIntervalModulate";
    String LOG_APPENDER_IMMEDIATE_FLUSH = ".ActionLog.Log.Appender.ImmediateFlush";

    // 日志LOGGER配置
    String LOGGER_ADDITIVE = ".ActionLog.Logger.Additive";
    String LOGGER_INCLUDE_LOCATION = ".ActionLog.Logger.IncludeLocation";
    String LOGGER_LOG_LEVEL = ".ActionLog.Logger.LogLevel";

}
