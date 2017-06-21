package com.dianping.actionlog.advice;

import com.dianping.actionlog.advice.filter.LogFilterHandler;
import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.common.ActionLogConfig;
import com.dianping.actionlog.common.DefaultLogType;
import com.dianping.actionlog.common.LogType;
import com.dianping.actionlog.context.DefaultActionLogKey;
import com.dianping.actionlog.context.LogContext;
import com.dianping.shadow.context.AspectContext;
import com.dianping.shadow.util.ClassUtils;
import com.dianping.shadow.util.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by jourrey on 16/11/23.
 * ActionLog手动打印日志类,保持了与注解切面同一上下文
 * 使用ActionLogKey,而不是String flow,String action,目的是,期望这些业务流是固定的,且配置在一个地方
 * 另外一点是,传一个参数总比两个容易,减少错误的可能性
 */
public class LogManual {
    private static final Logger LOG = LoggerFactory.getLogger(LogManual.class);

    /**
     * 打印debug级别日志
     *
     * @param actionLogKey 日志标记
     * @param handleType   操作类型
     * @param message      日志信息
     * @param params       打印参数
     */
    public static boolean debug(ActionLogKey actionLogKey, HandleType handleType, String message, Object... params) {
        try {
            actionLogKey = ifNullOfferDefault(actionLogKey);
            handleType = ifNullOfferDefault(handleType);
            LogInfo logInfo = logInfoFilter(DefaultLogType.MANUAL, actionLogKey, handleType, message, params);
            if (logInfo == null) {
                return false;
            }
            if (DefaultActionLogKey.DEFAULT.equals(actionLogKey)) {
                return false;
            }
            LogContext.getInstance().getActionLogger(actionLogKey).debug(message, params);
            return true;
        } catch (Throwable e) {
            LOG.error("LogManual Exception", e);
            return false;
        }
    }

    /**
     * 打印info级别日志
     *
     * @param actionLogKey 日志标记
     * @param handleType   操作类型
     * @param message      日志信息
     * @param params       打印参数
     */
    public static boolean info(ActionLogKey actionLogKey, HandleType handleType, String message, Object... params) {
        try {
            actionLogKey = ifNullOfferDefault(actionLogKey);
            handleType = ifNullOfferDefault(handleType);
            LogInfo logInfo = logInfoFilter(DefaultLogType.MANUAL, actionLogKey, handleType, message, params);
            if (logInfo == null) {
                return false;
            }
            if (DefaultActionLogKey.DEFAULT.equals(actionLogKey)) {
                return false;
            }
            LogContext.getInstance().getActionLogger(actionLogKey).info(message, params);
            return true;
        } catch (Throwable e) {
            LOG.error("LogManual Exception", e);
            return false;
        }
    }

    /**
     * 打印warn级别日志
     *
     * @param actionLogKey 日志标记
     * @param handleType   操作类型
     * @param message      日志信息
     * @param params       打印参数
     */
    public static boolean warn(ActionLogKey actionLogKey, HandleType handleType, String message, Object... params) {
        try {
            actionLogKey = ifNullOfferDefault(actionLogKey);
            handleType = ifNullOfferDefault(handleType);
            LogInfo logInfo = logInfoFilter(DefaultLogType.MANUAL, actionLogKey, handleType, message, params);
            if (logInfo == null) {
                return false;
            }
            if (DefaultActionLogKey.DEFAULT.equals(actionLogKey)) {
                return false;
            }
            LogContext.getInstance().getActionLogger(actionLogKey).warn(message, params);
            return true;
        } catch (Throwable e) {
            LOG.error("LogManual Exception", e);
            return false;
        }
    }

    /**
     * 打印error级别日志
     *
     * @param actionLogKey 日志标记
     * @param message      日志信息
     * @param params       打印参数
     */
    public static boolean error(ActionLogKey actionLogKey, String message, Object... params) {
        try {
            actionLogKey = ifNullOfferDefault(actionLogKey);
            LogInfo logInfo = logInfoFilter(DefaultLogType.MANUAL, actionLogKey, HandleType.THROW, message, params);
            if (logInfo == null) {
                return false;
            }
            if (DefaultActionLogKey.DEFAULT.equals(actionLogKey)) {
                return false;
            }
            LogContext.getInstance().getActionLogger(actionLogKey).error(message, params);
            return true;
        } catch (Throwable e) {
            LOG.error("LogManual Exception", e);
            return false;
        }
    }

    /**
     * LogInfo过滤器
     *
     * @param logType      日志类型
     * @param actionLogKey 日志标记
     * @param handleType   操作类型
     * @param message      日志信息
     * @param params       打印参数
     */
    protected static LogInfo logInfoFilter(LogType logType, ActionLogKey actionLogKey, HandleType handleType
            , String message, Object... params) {
        LOG.debug("logType {}, flow {}, action {}, handleType {}, message {}, params {}"
                , logType, actionLogKey.flow(), actionLogKey.action(), handleType, message, params);
        checkArgument(DefaultLogType.AOP.equals(logType), "Only internal logType set DefaultLogType.AOP are allowed!");
        try {
            LogInfo logInfo = createLogInfo(logType, actionLogKey, handleType, message, params);
            if (!LogFilterHandler.filter(logInfo)) {
                return null;
            }
            return logInfo;
        } catch (Throwable th) {
            LOG.error("logInfoFilter Exception", th);
            return null;
        }
    }

    /**
     * {@link sun.reflect.Reflection#getCallerClass(int)} int = 3,是当前方法被调用的深度
     *
     * @return
     */
    private static LogInfo createLogInfo(LogType logType, ActionLogKey actionLogKey, HandleType handleType
            , String message, Object... params) throws ClassNotFoundException {
        // 构建日志
        LogInfo logInfo = LogContext.getInstance().getLogInfo();
        // 填充切面上下文信息
        logInfo.setToken(AspectContext.getInstance().getToken());
        logInfo.setHierarchy(AspectContext.getInstance().getCurrentHierarchy());
        logInfo.setSequence(AspectContext.getInstance().getAspectSequenceAndIncrement());
        // 设置LogType
        logInfo.setLogType(logType);
        // 设置actionLogKey
        logInfo.setFlow(actionLogKey.flow()).setAction(actionLogKey.action())
                .setHandleType(handleType).setExtendInfo(message);
        // 调用信息填充
        if (ActionLogConfig.isLogManualIncludeLocation()) {
            StackTraceElement element = ReflectionUtils.getStackTraceElement(3);
            logInfo.setCallerClass(ClassUtils.loadClass(element.getClassName()));
            logInfo.setCallerLineNumber(element.getLineNumber());
            logInfo.setCallerMethodName(element.getMethodName());
        }
        // 填充切面上下文信息, 填充parameters result throwable
        fillLogInfoContext(logInfo, handleType, message, params);
        return logInfo;
    }

    private static void fillLogInfoContext(LogInfo logInfo, HandleType handleType, Object... params) {
        // 填充parameters/result
        if (HandleType.RETURN.equals(handleType)) {
            logInfo.setResult(params);
        } else {
            logInfo.setResult(null);
            logInfo.getParameters().put("0", params);
        }
        // 填充throwable
        if (ArrayUtils.isNotEmpty(params)) {
            Object o = params[params.length - 1];
            if (o instanceof Throwable) {
                logInfo.setThrowable((Throwable) o);
            } else {
                logInfo.setThrowable(null);
            }
        } else {
            logInfo.setThrowable(null);
        }
    }

    /**
     * 检查actionLogKey,为空继续检查参数第一位,再取默认
     *
     * @param actionLogKey 日志标记
     * @param params       日志参数
     * @return
     */
    private static ActionLogKey ifNullOfferDefault(ActionLogKey actionLogKey, Object... params) {
        if (actionLogKey != null) {
            return actionLogKey;
        }
        if (params != null && params.length > 1 && params[0] instanceof ActionLogKey) {
            actionLogKey = (ActionLogKey) params[0];
        }
        return LogContext.getInstance().ifNullOfferDefault(actionLogKey);
    }

    /**
     * 检查handleType,为空继续检查参数第二位,再取默认
     *
     * @param handleType 日志类型
     * @param params     日志参数
     * @return
     */
    private static HandleType ifNullOfferDefault(HandleType handleType, Object... params) {
        if (handleType != null) {
            return handleType;
        }
        if (params != null && params.length > 2 && params[0] instanceof ActionLogKey && params[1] instanceof HandleType) {
            handleType = (HandleType) params[0];
        }
        return LogContext.getInstance().ifNullOfferDefault(handleType);
    }

    public static void main(String[] args) {
        LogManual.debug(null, null, "0test{} {}", 123, 456, 789);
        LogManual.info(null, HandleType.PARAM, "0test{} {}", 123, 456, 789);
        LogManual.info(null, HandleType.RETURN, "0test{} {}", 123, 456, 789);
        LogManual.info(null, HandleType.FINALLY, "0test{} {}", 123, 456, 789);
        LogManual.warn(null, null, "0test{} {}", 123, 456, 789);
        LogManual.error(null, null, "0test{} {}", 123, 456, 789, new RuntimeException());
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(2);
        cachedThreadPool.execute(new Runnable() {
            public void run() {
                LogManual.debug(null, null, "1test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.PARAM, "1test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.RETURN, "1test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.FINALLY, "1test{} {}", 123, 456, 789);
                LogManual.warn(null, null, "1test{} {}", 123, 456, 789);
                LogManual.error(null, null, "1test{} {}", 123, 456, 789, new RuntimeException("789"));
            }
        });
        cachedThreadPool.execute(new Runnable() {
            public void run() {
                LogManual.debug(null, null, "2test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.PARAM, "2test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.RETURN, "2test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.FINALLY, "2test{} {}", 123, 456, 789);
                LogManual.warn(null, null, "2test{} {}", 123, 456, 789);
                LogManual.error(null, null, "2test{} {}", 123, 456, 789, new RuntimeException("789"));
            }
        });
        cachedThreadPool.execute(new Runnable() {
            public void run() {
                LogManual.debug(null, null, "3test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.PARAM, "3test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.RETURN, "3test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.FINALLY, "3test{} {}", 123, 456, 789);
                LogManual.warn(null, null, "3test{} {}", 123, 456, 789);
                LogManual.error(null, null, "3test{} {}", 123, 456, 789, new RuntimeException());
            }
        });
        cachedThreadPool.shutdown();
    }
}
