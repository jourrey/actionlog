package com.dianping.actionlog.lion;

import com.dianping.actionlog.common.DpSupportConfig;
import com.dianping.actionlog.common.Log4j2LoggerConfigLionKeys;
import com.dianping.actionlog.common.LogLevel;
import com.dianping.actionlog.logger.log4j.Log4j2LoggerConfig;
import com.dianping.lion.client.ConfigEvent;
import com.dianping.lion.client.ConfigListener;
import com.dianping.lion.client.Lion;

/**
 * @author jourrey.liu
 * @ClassName: Log4j2LoggerConfigListener
 * @Description: 刷新 {@link Log4j2LoggerConfig}
 * @date 2016-11-16 下午3:13:17
 */
public class Log4j2LoggerConfigListener implements ConfigListener {

    static {
        Lion.addConfigListener(new Log4j2LoggerConfigListener());

        flushLog4j2LoggerConfig();
    }

    @Override
    public void configChanged(ConfigEvent configEvent) {
        flushLog4j2LoggerConfig();
    }

    private static void flushLog4j2LoggerConfig() {
        // 日志Appender配置
        Log4j2LoggerConfig.setLogConversionPattern(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOG_CONVERSION_PATTERN)));
        Log4j2LoggerConfig.setLogHomePath(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOG_HOME_PATH)));
        Log4j2LoggerConfig.setLogFileMaxSize(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOG_FILE_MAX_SIZE)));
        Log4j2LoggerConfig.setLogFileMaxNum(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOG_FILE_MAX_NUM)));
        Log4j2LoggerConfig.setLogFileTimeInterval(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOG_FILE_TIME_INTERVAL)));
        Log4j2LoggerConfig.setLogFileTimeIntervalModulate(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOG_FILE_TIME_INTERVAL_MODULATE)));
        Log4j2LoggerConfig.setLogAppenderImmediateFlush(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOG_APPENDER_IMMEDIATE_FLUSH)));

        // 日志LOGGER配置
        Log4j2LoggerConfig.setLoggerAdditive(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOGGER_ADDITIVE)));
        Log4j2LoggerConfig.setLoggerIncludeLocation(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOGGER_INCLUDE_LOCATION)));
        Log4j2LoggerConfig.setLoggerLogLevel(LogLevel.getLogLevel(Lion.get(DpSupportConfig.lionKey(Log4j2LoggerConfigLionKeys.LOGGER_LOG_LEVEL))));
    }

}
