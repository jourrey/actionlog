package com.dianping.actionlog.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * Created by jourrey on 17/1/11.
 */
public class CatConfig {
    private static final Logger LOG = LoggerFactory.getLogger(CatConfig.class);
    private static final String CAT_EVENT_KEY_TEMPLATE = "{0}_{1}";
    private static final String CAT_METRIC_COUNT_KEY_TEMPLATE = "{0}_{1}_Count";
    private static final String CAT_METRIC_DURATION_KEY_TEMPLATE = "{0}_{1}_Duration";

    public static boolean needTransaction() {
        try {
            return Boolean.valueOf(DpSupportConfig.lionValue(LionKeys.CAT_TRANSACTION_SWITCH_KEY
                    , DpSupportConstants.CAT_TRANSACTION_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needTransaction Exception", th);
            return Boolean.valueOf(DpSupportConstants.CAT_TRANSACTION_DEFAULT_VALUE);
        }
    }

    public static boolean needEvent() {
        try {
            return Boolean.valueOf(DpSupportConfig.lionValue(LionKeys.CAT_EVENT_SWITCH_KEY
                    , DpSupportConstants.CAT_EVENT_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needEvent Exception", th);
            return Boolean.valueOf(DpSupportConstants.CAT_EVENT_DEFAULT_VALUE);
        }
    }

    public static boolean needMetricForCount() {
        try {
            return Boolean.valueOf(DpSupportConfig.lionValue(LionKeys.CAT_METRIC_COUNT_SWITCH_KEY
                    , DpSupportConstants.CAT_METRIC_COUNT_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needMetricForCount Exception", th);
            return Boolean.valueOf(DpSupportConstants.CAT_METRIC_COUNT_DEFAULT_VALUE);
        }
    }

    public static boolean needMetricForDuration() {
        try {
            return Boolean.valueOf(DpSupportConfig.lionValue(LionKeys.CAT_METRIC_DURATION_SWITCH_KEY
                    , DpSupportConstants.CAT_METRIC_DURATION_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needMetricForDuration Exception", th);
            return Boolean.valueOf(DpSupportConstants.CAT_METRIC_DURATION_DEFAULT_VALUE);
        }
    }

    public static String getEventKey(String flow, String action) {
        try {
            return MessageFormat.format(CAT_EVENT_KEY_TEMPLATE, flow, action);
        } catch (Throwable th) {
            LOG.error("getEventKey Exception", th);
            return "ActionLog_Event";
        }
    }

    public static String getMetricCountKey(String flow, String action) {
        try {
            return MessageFormat.format(CAT_METRIC_COUNT_KEY_TEMPLATE, flow, action);
        } catch (Throwable th) {
            LOG.error("getMetricCountKey Exception", th);
            return "ActionLog_MetricCount";
        }
    }

    public static String getMetricDurationKey(String flow, String action) {
        try {
            return MessageFormat.format(CAT_METRIC_DURATION_KEY_TEMPLATE, flow, action);
        } catch (Throwable th) {
            LOG.error("getMetricDurationKey Exception", th);
            return "ActionLog_MetricDuration";
        }
    }

    public static int getMetricForDurationSlowThreshold() {
        try {
            return Integer.valueOf(DpSupportConfig.lionValue(LionKeys.CAT_METRIC_DURATION_SLOW_THRESHOLD_KEY
                    , DpSupportConstants.CAT_METRIC_DURATION_SLOW_THRESHOLD_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("getMetricForDurationSlowThreshold Exception", th);
            return Integer.valueOf(DpSupportConstants.CAT_METRIC_DURATION_SLOW_THRESHOLD_DEFAULT_VALUE);
        }
    }

}
