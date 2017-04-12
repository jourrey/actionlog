package com.dianping.actionlog.context;

import com.dianping.actionlog.advice.LogInfo;
import com.dianping.actionlog.common.SysConstants;
import com.dianping.actionlog.context.log4j2.Log4J2ActionLogger;
import com.dianping.actionlog.util.AppInfoUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ActionLogger容器
 *
 * @author jourrey
 */
public class LogContext {
    private static final Logger LOG = LogManager.getLogger(LogContext.class);

    private static volatile LogContext instance = null;
    private Map<String, ActionLogger> actionLoggerResolverCache = new ConcurrentHashMap<String, ActionLogger>();
    private final ActionLogger defaultActionLogger = new DefaultActionLogger();
    private String appName;
    private ThreadLocal<Map<String, String>> localContext = new ThreadLocal<Map<String, String>>();
    private ThreadLocal<LogInfo> LOG_INFO = new InheritableThreadLocal<LogInfo>() {
        @Override
        protected LogInfo initialValue() {
            return new LogInfo();
        }
    };

    public LogInfo getLogInfo() {
        LOG_INFO.get().getParameters().clear();
        return LOG_INFO.get();
    }

    public void putLocalContext(String key, String value) {
        Map<String, String> context = localContext.get();
        if (context == null) {
            context = new HashMap<String, String>();
            localContext.set(context);
        }
        context.put(key, value);
    }

    public String getLocalContext(String key) {
        Map<String, String> context = localContext.get();
        if (context == null) {
            return null;
        }
        return context.get(key);
    }

    public void clearLocalContext() {
        Map<String, String> context = localContext.get();
        if (context != null) {
            context.clear();
        }
        localContext.remove();
    }

    /**
     * 获取项目名
     *
     * @return
     */
    public String getAppName() {
        if (appName == null) {
            appName = AppInfoUtils.getAppName();
        }
        if (appName == null) {
            appName = SysConstants.DEFAULT_APP_NAME;
        }
        return appName;
    }

    /**
     * 获取Logger
     * 这里loggerName直接使用"+"拼接字符串,是因为flow和action相对稳定,运行一次后会加入常量池,提升性能
     * 实际测试"+"QPS达到40W,MessageFormat.format在11W,String.format只有3-4W
     *
     * @param flow
     * @param action
     * @return
     */
    public ActionLogger getActionLogger(String flow, String action) {
        if (StringUtils.isBlank(action)) {
            return defaultActionLogger;
        }
        String loggerName = flow + "_" + action;
        ActionLogger actionLogger = actionLoggerResolverCache.get(loggerName);
        if (actionLogger == null) {
            synchronized (actionLoggerResolverCache) {
                actionLogger = actionLoggerResolverCache.get(loggerName);
                if (actionLogger == null) {
                    actionLogger = createActionLogger(loggerName, flow, action);
                    actionLoggerResolverCache.put(loggerName, actionLogger);
                }
            }
        }
        return actionLogger;
    }

    private ActionLogger createActionLogger(String loggerName, String flow, String action) {
        ActionLogger actionLogger = null;
        try {
            actionLogger = new Log4J2ActionLogger(loggerName, flow, action);
        } catch (Exception e) {
            LOG.error("createActionLogger", e);
        }
        return actionLogger == null ? defaultActionLogger : actionLogger;
    }

    private LogContext() {
    }

    public static LogContext getInstance() {
        if (instance == null) {
            synchronized (LogContext.class) {
                if (instance == null) {
                    instance = new LogContext();
                }
            }
        }
        return instance;
    }

    /**
     * 如果该对象被用于序列化，可以保证对象在序列化前后保持一致
     *
     * @returnType: Object
     */
    public Object readResolve() {
        return getInstance();
    }
}
