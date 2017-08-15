package com.dianping.actionlog.context;

import com.dianping.actionlog.advice.ActionLogBean;
import com.dianping.actionlog.advice.LogInfo;
import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.common.ActionLogConfig;
import com.dianping.actionlog.logger.ActionLogger;
import com.dianping.actionlog.logger.ActionLoggerBuilder;
import com.dianping.actionlog.logger.CacheActionLoggerFactory;
import com.dianping.actionlog.logger.impl.NOPActionLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * ActionLogger容器
 *
 * @author jourrey
 */
public class LogContext {
    private static volatile LogContext instance = null;
    private ThreadLocal<Map<String, Object>> localContext = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };
    private ThreadLocal<LogInfo> LOG_INFO = new InheritableThreadLocal<LogInfo>() {
        @Override
        protected LogInfo initialValue() {
            return new LogInfo();
        }
    };
    private ThreadLocal<ActionLogBean> ACTION_LOG_BEAN = new InheritableThreadLocal<ActionLogBean>() {
        @Override
        protected ActionLogBean initialValue() {
            return new ActionLogBean();
        }
    };

    /**
     * 为了减少new的对象,线程内缓存一个,每次使用前进行属性清空
     *
     * @return
     */
    public LogInfo getLogInfo() {
        return LOG_INFO.get().clear();
    }

    /**
     * 为了减少new的对象,线程内缓存一个,每次使用前进行属性清空
     *
     * @return
     */
    public ActionLogBean getActionLogBean() {
        return ACTION_LOG_BEAN.get().clear();
    }

    /**
     * 添加一个上下文本地缓存
     *
     * @param key
     * @param value
     */
    public void putLocalContext(String key, Object value) {
        localContext.get().put(key, value);
    }

    /**
     * 获取一个上下文本地缓存
     *
     * @param key
     * @return
     */
    public Object getLocalContext(String key) {
        return localContext.get().get(key);
    }

    /**
     * 清空本地上下文缓存
     */
    public void clearLocalContext() {
        localContext.get().clear();
        localContext.remove();
    }

    /**
     * 根据actionLogKey获取ActionLogger
     * 如果actionLogKey为null,则提供默认{@link DefaultActionLogKey#DEFAULT}
     * 这里没有缓存机制,由各自ActionLoggerFactory实现{@link CacheActionLoggerFactory},目的是方便自定义热加载策略
     * 不能使用SLF4J-API,否则获取Logger过程中,会引起循环
     *
     * @param actionLogKey the actionLogKey of the Logger to return
     */
    public ActionLogger getActionLogger(ActionLogKey actionLogKey) {
        ActionLogger actionLogger;
        try {
            actionLogKey = ActionLogConfig.ifNullOfferDefault(actionLogKey);
            actionLogger = ActionLoggerBuilder.getLogger(actionLogKey);
        } catch (Exception e) {
            actionLogger = NOPActionLogger.NOP_ACTION_LOGGER;
        }
        actionLogger = actionLogger == null ? NOPActionLogger.NOP_ACTION_LOGGER : actionLogger;
        return actionLogger;
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
