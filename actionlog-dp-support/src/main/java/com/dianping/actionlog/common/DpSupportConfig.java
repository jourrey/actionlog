package com.dianping.actionlog.common;

import com.dianping.actionlog.context.LogContext;
import com.dianping.lion.client.Lion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jourrey on 17/1/11.
 */
public class DpSupportConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ActionLogConfig.class);

    public static String lionKey(String key) {
        try {
            return LogContext.getInstance().getAppName() + key;
        } catch (Throwable th) {
            LOG.error("lionKey Exception", th);
            return "";
        }
    }

    public static String lionValue(String key, String defaultValue) {
        try {
            return Lion.get(lionKey(key), defaultValue);
        } catch (Throwable th) {
            LOG.error("lionKey Exception", th);
            return defaultValue;
        }
    }

    /**
     * 是否打印ActionLog的Pigeon日志
     *
     * @return
     */
    public static boolean needPigeonLog() {
        try {
            return Boolean.valueOf(lionValue(LionKeys.PIGEON_LOG_SWITCH_KEY
                    , DpSupportConstants.PIGEON_LOG_DEFAULT_VALUE));
        } catch (Throwable th) {
            LOG.error("needPigeonLog Exception", th);
            return Boolean.valueOf(DpSupportConstants.PIGEON_LOG_DEFAULT_VALUE);
        }
    }

}
