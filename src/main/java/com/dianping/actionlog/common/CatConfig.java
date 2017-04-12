package com.dianping.actionlog.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

/**
 * Created by jourrey on 17/1/11.
 */
public class CatConfig {
    private static final Logger LOG = LogManager.getLogger(CatConfig.class);
    private static final String CAT_EVENT_KEY_TEMPLATE = "{0}_{1}";
    private static final String CAT_METRIC_COUNT_KEY_TEMPLATE = "{0}_{1}_Count";
    private static final String CAT_METRIC_DURATION_KEY_TEMPLATE = "{0}_{1}_Duration";

    public static boolean needTransaction() {
        try {
            return Boolean.valueOf(SysConfig.lionValue(LionKeys.CAT_TRANSACTION_SWITCH_KEY
                    , SysConstants.CAT_TRANSACTION_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needTransaction exception", th);
            return Boolean.valueOf(SysConstants.CAT_TRANSACTION_DEFAULT_VALUE);
        }
    }

    public static boolean needEvent() {
        try {
            return Boolean.valueOf(SysConfig.lionValue(LionKeys.CAT_EVENT_SWITCH_KEY
                    , SysConstants.CAT_EVENT_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needEvent exception", th);
            return Boolean.valueOf(SysConstants.CAT_EVENT_DEFAULT_VALUE);
        }
    }

    public static boolean needMetricForCount() {
        try {
            return Boolean.valueOf(SysConfig.lionValue(LionKeys.CAT_METRIC_COUNT_SWITCH_KEY
                    , SysConstants.CAT_METRIC_COUNT_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needMetricForCount exception", th);
            return Boolean.valueOf(SysConstants.CAT_METRIC_COUNT_DEFAULT_VALUE);
        }
    }

    public static boolean needMetricForDuration() {
        try {
            return Boolean.valueOf(SysConfig.lionValue(LionKeys.CAT_METRIC_DURATION_SWITCH_KEY
                    , SysConstants.CAT_METRIC_DURATION_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needMetricForDuration exception", th);
            return Boolean.valueOf(SysConstants.CAT_METRIC_DURATION_DEFAULT_VALUE);
        }
    }

    public static String getEventKey(String flow, String action) {
        try {
            return MessageFormat.format(CAT_EVENT_KEY_TEMPLATE, flow, action);
        } catch (Throwable th) {
            LOG.error("getEventKey exception", th);
            return "ActionLog_Event";
        }
    }

    public static String getMetricCountKey(String flow, String action) {
        try {
            return MessageFormat.format(CAT_METRIC_COUNT_KEY_TEMPLATE, flow, action);
        } catch (Throwable th) {
            LOG.error("getMetricCountKey exception", th);
            return "ActionLog_MetricCount";
        }
    }

    public static String getMetricDurationKey(String flow, String action) {
        try {
            return MessageFormat.format(CAT_METRIC_DURATION_KEY_TEMPLATE, flow, action);
        } catch (Throwable th) {
            LOG.error("getMetricDurationKey exception", th);
            return "ActionLog_MetricDuration";
        }
    }

    public static int getMetricForDurationSlowThreshold() {
        try {
            return Integer.valueOf(SysConfig.lionValue(LionKeys.CAT_METRIC_DURATION_SLOW_THRESHOLD_KEY
                    , SysConstants.CAT_METRIC_DURATION_SLOW_THRESHOLD_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("getMetricForDurationSlowThreshold exception", th);
            return Integer.valueOf(SysConstants.CAT_METRIC_DURATION_SLOW_THRESHOLD_DEFAULT_VALUE);
        }
    }

}
