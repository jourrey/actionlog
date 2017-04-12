package com.dianping.actionlog.common;

import java.io.File;

/**
 * Created by jourrey on 16/08/09.
 */
public interface SysConstants {

    // 系统常用配置
    String DEFAULT_APP_NAME = "ActionLog";
    String DEFAULT_FLOW = "ActionLog";
    String DEFAULT_ACTION = "ActionLog";

    String LOG_INFO_PREFIX = "_[";
    String LOG_INFO_SUFFIX = "]_";

    String KEY_VALUE_SEPARATOR = "=";
    String EXTEND_INFO_SEPARATOR = "#_#";

    String LOG_REQUEST_ID = "LOG_REQUEST_ID";
    String LOG_REQUEST_IP = "LOG_REQUEST_IP";

    String PIGEON_CLIENT_IP = "CLIENT_IP";
    String PIGEON_CLIENT_APP = "CLIENT_APP";
    String PIGEON_SOURCE_IP = "SOURCE_IP";
    String PIGEON_SOURCE_APP = "SOURCE_APP";

    String FILE_SEPARATOR = File.separator;
    String NAME_SEPARATOR = "_";

    // 日志Appender配置
    String LOG_CONVERSION_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS z} %-5level [%t] %msg%xEx%n";
    String LOG_HOME_PATH = FILE_SEPARATOR + "data" + FILE_SEPARATOR + "applogs";
    String LOG_FILE_MAX_SIZE = "50M";
    String LOG_FILE_MAX_NUM = "100";
    String LOG_FILE_TIME_INTERVAL = "24";
    String LOG_FILE_TIME_INTERVAL_MODULATE = "true";
    String LOG_APPENDER_IMMEDIATE_FLUSH = "false";

    // 日志LOGGER配置
    String LOGGER_ADDITIVE = "false";
    String LOGGER_INCLUDE_LOCATION = "false";
    String LOGGER_LOG_LEVEL = "INFO";

    // LogManual配置
    String LOG_MANUAL_INCLUDE_LOCATION = "false";

    // Cat配置
    String CAT_TRANSACTION_DEFAULT_VALUE = "false";
    String CAT_EVENT_DEFAULT_VALUE = "false";
    String CAT_METRIC_COUNT_DEFAULT_VALUE = "false";
    String CAT_METRIC_DURATION_DEFAULT_VALUE = "false";
    String CAT_METRIC_DURATION_SLOW_THRESHOLD_DEFAULT_VALUE = "10000";

    // filter & pigeon 日志开关
    String FILTER_LOG_DEFAULT_VALUE = "false";
    String PIGEON_LOG_DEFAULT_VALUE = "false";

}
