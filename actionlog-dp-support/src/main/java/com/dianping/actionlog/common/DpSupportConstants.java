package com.dianping.actionlog.common;

/**
 * Created by jourrey on 16/08/09.
 */
public interface DpSupportConstants {

    // pigeon 日志开关
    String PIGEON_LOG_DEFAULT_VALUE = "false";

    // pigeon 上下文参数名
    String PIGEON_CLIENT_IP = "CLIENT_IP";
    String PIGEON_CLIENT_APP = "CLIENT_APP";
    String PIGEON_SOURCE_IP = "SOURCE_IP";
    String PIGEON_SOURCE_APP = "SOURCE_APP";

    // Cat 配置
    String CAT_TRANSACTION_DEFAULT_VALUE = "false";
    String CAT_EVENT_DEFAULT_VALUE = "false";
    String CAT_METRIC_COUNT_DEFAULT_VALUE = "false";
    String CAT_METRIC_DURATION_DEFAULT_VALUE = "false";
    String CAT_METRIC_DURATION_SLOW_THRESHOLD_DEFAULT_VALUE = "10000";

}
