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

    // LogManual配置
    String LOG_MANUAL_INCLUDE_LOCATION = ".ActionLog.LogManual.IncludeLocation";

}
