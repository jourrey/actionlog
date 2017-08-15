package com.dianping.actionlog.logger.impl;

import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.context.DefaultActionLogKey;
import com.dianping.actionlog.logger.ActionLogger;
import com.dianping.actionlog.logger.CacheActionLoggerFactory;
import com.dianping.actionlog.logger.log4j.Log4j2LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jourrey on 16/11/21.
 */
public class Log4j2ActionLoggerFactory extends CacheActionLoggerFactory {
    private static final Logger LOG = LogManager.getLogger(Log4j2ActionLoggerFactory.class);

    @Override
    public ActionLogger getLogger(String loggerName, ActionLogKey actionLogKey) {
        try {
            return new Log4J2ActionLogger(new Log4j2LoggerFactory(loggerName, actionLogKey).createLogger());
        } catch (Exception e) {
            LOG.error("getLogger Exception", e);
        }
        return null;
    }

    public static void main(String[] args) {
        Log4j2ActionLoggerFactory actionLoggerFactory = new Log4j2ActionLoggerFactory();
        ActionLogger actionLogger;
        for (int i = 0; i < 5; i++) {
            actionLogger = actionLoggerFactory.getLogger(DefaultActionLogKey.DEFAULT);
            for (int j = 0; j < 500; j++) {
                actionLogger.info("Testing testing testing 111");
                actionLogger.debug("Testing testing testing 222");
                actionLogger.error("Testing testing testing 333");
            }
        }
    }

}
