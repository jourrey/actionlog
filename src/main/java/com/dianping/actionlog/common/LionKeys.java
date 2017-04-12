package com.dianping.actionlog.common;

/**
 * Created by jourrey on 16/08/09.
 */
public interface LionKeys {

    // CAT监控配置
    String CAT_TRANSACTION_SWITCH_KEY = ".ActionLog.Cat.Transaction.Switch";
    String CAT_EVENT_SWITCH_KEY = ".ActionLog.Cat.Event.Switch";
    String CAT_METRIC_COUNT_SWITCH_KEY = ".ActionLog.Cat.MetricForCount.Switch";
    String CAT_METRIC_DURATION_SWITCH_KEY = ".ActionLog.Cat.MetricForDuration.Switch";
    String CAT_METRIC_DURATION_SLOW_THRESHOLD_KEY = ".ActionLog.Cat.MetricForDuration.SlowThreshold";

    // filter & pigeon 日志开关
    String FILTER_LOG_SWITCH_KEY = ".ActionLog.Filter.Log.Switch";
    String PIGEON_LOG_SWITCH_KEY = ".ActionLog.Pigeon.Log.Switch";

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

    // LogManual配置
    String LOG_MANUAL_INCLUDE_LOCATION = ".ActionLog.LogManual.IncludeLocation";

}
