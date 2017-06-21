package com.dianping.actionlog.logger.impl;

import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.context.DefaultActionLogKey;
import com.dianping.actionlog.logger.ActionLogger;
import com.dianping.actionlog.logger.ActionLoggerFactory;
import com.dianping.actionlog.logger.log4j.Log4j2LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jourrey on 16/11/21.
 */
public class Log4j2ActionLoggerFactory implements ActionLoggerFactory {
    private static final String KEY_LINK_SYMBOL = "$ALK#";
    private final ConcurrentHashMap<String, ActionLogger> actionLoggerResolverCache = new ConcurrentHashMap<String, ActionLogger>();

    @Override
    public ActionLogger getLogger(ActionLogKey actionLogKey) {
        String cacheActionLogKey = getActionLogKey(actionLogKey);
        if (actionLoggerResolverCache.containsKey(cacheActionLogKey)) {
            return actionLoggerResolverCache.get(cacheActionLogKey);
        }

        ActionLogger actionLogger = new Log4J2ActionLogger(Log4j2LoggerFactory.create(actionLogKey).createLogger());
        ActionLogger cacheActionLogger = actionLoggerResolverCache.putIfAbsent(cacheActionLogKey, actionLogger);
        if (cacheActionLogger != null) {
            actionLogger = cacheActionLogger;
        }
        return actionLogger;
    }

    /**
     * 这里loggerName直接使用"+"拼接字符串,是因为flow和action相对稳定,运行一次后会加入常量池,提升性能
     * 实际测试"+"QPS达到40W,MessageFormat.format在11W,String.format只有3-4W
     *
     * @param actionLogKey
     * @return
     */
    private String getActionLogKey(ActionLogKey actionLogKey) {
        if (actionLogKey == null) {
            return null;
        }
        return actionLogKey.flow() + KEY_LINK_SYMBOL + actionLogKey.action();
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
