package com.dianping.actionlog.context;

import com.dianping.actionlog.advice.ActionLogBean;
import com.dianping.actionlog.advice.LogInfo;
import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.common.ActionLogConfig;
import com.dianping.actionlog.logger.ActionLogger;
import com.dianping.actionlog.logger.ActionLoggerBuilder;
import com.dianping.actionlog.logger.impl.NOPActionLogger;
import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ActionLogger容器
 *
 * @author jourrey
 */
public class LogContext {
    private static final Logger LOG = LoggerFactory.getLogger(LogContext.class);

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

    public void putLocalContext(String key, Object value) {
        localContext.get().put(key, value);
    }

    public Object getLocalContext(String key) {
        return localContext.get().get(key);
    }

    public void clearLocalContext() {
        localContext.get().clear();
        localContext.remove();
    }

    /**
     * 获取项目名
     *
     * @return
     */
    public String getAppName() {
        return ActionLogConfig.getAppName();
    }

    /**
     * 如果actionLogKey为null,则提供默认{@link DefaultActionLogKey#DEFAULT}
     *
     * @param actionLogKey 日志标记
     * @return
     */
    public ActionLogKey ifNullOfferDefault(ActionLogKey actionLogKey) {
        return MoreObjects.firstNonNull(actionLogKey, DefaultActionLogKey.DEFAULT);
    }

    /**
     * 如果handleType为null,则提供默认{@link HandleType#PARAM}
     *
     * @param handleType 日志类型
     * @return
     */
    public HandleType ifNullOfferDefault(HandleType handleType) {
        return MoreObjects.firstNonNull(handleType, HandleType.PARAM);
    }

    /**
     * 根据actionLogKey获取ActionLogger
     * 如果actionLogKey为null,则提供默认{@link DefaultActionLogKey#DEFAULT}
     *
     * @param actionLogKey the actionLogKey of the Logger to return
     */
    public ActionLogger getActionLogger(ActionLogKey actionLogKey) {
        ActionLogger actionLogger;
        try {
            actionLogKey = ifNullOfferDefault(actionLogKey);
            actionLogger = ActionLoggerBuilder.getLogger(actionLogKey);
        } catch (Exception e) {
            LOG.error("getActionLogger Exception", e);
            actionLogger = NOPActionLogger.NOP_ACTION_LOGGER;
        }
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
