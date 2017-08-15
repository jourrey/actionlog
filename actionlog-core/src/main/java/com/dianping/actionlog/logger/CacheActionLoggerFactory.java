package com.dianping.actionlog.logger;

import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.common.ActionLogConfig;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 子类请不要使用SLF4J API,否则ActionLog使用支持SLF4J API时,会引起死循环
 * Created by jourrey on 16/11/21.
 */
public abstract class CacheActionLoggerFactory implements ActionLoggerFactory {
    private static final String LOGGER_NAME_LINK_SYMBOL = "$ALK#";
    private final ConcurrentHashMap<String, ActionLogger> actionLoggerResolverCache = new ConcurrentHashMap<String, ActionLogger>();

    @Override
    public ActionLogger getLogger(ActionLogKey actionLogKey) {
        String loggerName = getLoggerName(actionLogKey);
        if (actionLoggerResolverCache.containsKey(loggerName)) {
            return actionLoggerResolverCache.get(loggerName);
        }

        ActionLogger actionLogger = getLogger(loggerName, actionLogKey);
        ActionLogger cacheActionLogger = actionLoggerResolverCache.putIfAbsent(loggerName, actionLogger);
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
    private String getLoggerName(ActionLogKey actionLogKey) {
        if (actionLogKey == null) {
            return null;
        }
        return ActionLogConfig.getAppName() + LOGGER_NAME_LINK_SYMBOL
                + actionLogKey.flow() + LOGGER_NAME_LINK_SYMBOL + actionLogKey.action();
    }

    /**
     * 根据loggerName和actionLogKey获取ActionLogger
     *
     * @param loggerName   The logger name. If null the name of the calling class will be used.
     * @param actionLogKey the actionLogKey of the Logger to return
     * @return
     */
    public abstract ActionLogger getLogger(String loggerName, ActionLogKey actionLogKey);

}
