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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jourrey on 16/11/23.
 * ActionLog手动打印日志类,保持了与注解切面同一上下文
 * 使用ActionLogKey,而不是String flow,String action,目的是扩展性好.
 * 另外一点是,传一个参数总比两个容易,减少错误的可能性.且期望这些业务流是固定的,且配置在一个地方
 */
public class LogManual {
    private final static LogManual LOG_MANUAL = new LogManual();

    /**
     * 打印debug级别日志
     * 只调用过滤器和获取Logger打印.因为支持SLF4J-API,逻辑太多容易死循环,保证这个过程不使用SLF4J-API
     *
     * @param actionLogKey 日志标记
     * @param handleType   操作类型
     * @param message      日志信息
     * @param params       打印参数
     */
    public final static void debug(ActionLogKey actionLogKey, HandleType handleType, String message, Object... params) {
        try {
            if (!LOG_MANUAL.logInfoFilter(actionLogKey, handleType, message, params)) {
                return;
            }
            LogContext.getInstance().getActionLogger(actionLogKey).debug(message, params);
        } catch (Throwable e) {
            LogContext.getInstance().getActionLogger(actionLogKey).error("debug Exception", e);
        }
    }

    /**
     * 打印info级别日志
     * 只调用过滤器和获取Logger打印.因为支持SLF4J-API,逻辑太多容易死循环,保证这个过程不使用SLF4J-API
     *
     * @param actionLogKey 日志标记
     * @param handleType   操作类型
     * @param message      日志信息
     * @param params       打印参数
     */
    public final static void info(ActionLogKey actionLogKey, HandleType handleType, String message, Object... params) {
        try {
            if (!LOG_MANUAL.logInfoFilter(actionLogKey, handleType, message, params)) {
                return;
            }
            LogContext.getInstance().getActionLogger(actionLogKey).info(message, params);
        } catch (Throwable e) {
            LogContext.getInstance().getActionLogger(actionLogKey).error("info Exception", e);
        }
    }

    /**
     * 打印warn级别日志
     * 只调用过滤器和获取Logger打印.因为支持SLF4J-API,逻辑太多容易死循环,保证这个过程不使用SLF4J-API
     *
     * @param actionLogKey 日志标记
     * @param handleType   操作类型
     * @param message      日志信息
     * @param params       打印参数
     */
    public final static void warn(ActionLogKey actionLogKey, HandleType handleType, String message, Object... params) {
        try {
            if (!LOG_MANUAL.logInfoFilter(actionLogKey, handleType, message, params)) {
                return;
            }
            LogContext.getInstance().getActionLogger(actionLogKey).warn(message, params);
        } catch (Throwable e) {
            LogContext.getInstance().getActionLogger(actionLogKey).error("warn Exception", e);
        }
    }

    /**
     * 打印error级别日志
     * 只调用过滤器和获取Logger打印.因为支持SLF4J-API,逻辑太多容易死循环,保证这个过程不使用SLF4J-API
     *
     * @param actionLogKey 日志标记
     * @param message      日志信息
     * @param params       打印参数
     */
    public final static void error(ActionLogKey actionLogKey, String message, Object... params) {
        try {
            if (!LOG_MANUAL.logInfoFilter(actionLogKey, HandleType.THROW, message, params)) {
                return;
            }
            LogContext.getInstance().getActionLogger(actionLogKey).error(message, params);
        } catch (Throwable e) {
            LogContext.getInstance().getActionLogger(actionLogKey).error("error Exception", e);
        }
    }

    /**
     * LogInfo过滤器
     *
     * @param actionLogKey 日志标记
     * @param handleType   操作类型
     * @param message      日志信息
     * @param params       打印参数
     * @return
     */
    private boolean logInfoFilter(ActionLogKey actionLogKey, HandleType handleType
            , String message, Object... params) {
        try {
            actionLogKey = ActionLogConfig.ifNullOfferDefault(actionLogKey);
            // ActionLog自身也使用了SLF4J-API,ActionLogKey不传默认DefaultActionLogKey.DEFAULT
            // 避免死循环,所以必须跳过,提升性能直接跳过
            if (DefaultActionLogKey.DEFAULT.equals(actionLogKey)) {
                return true;
            }
            LogContext.getInstance().getActionLogger(actionLogKey)
                    .debug("actionLogKey {}, handleType {}, message {}, params {}"
                            , actionLogKey, handleType, message, params);
            handleType = ActionLogConfig.ifNullOfferDefault(handleType);
            LogInfo logInfo = createLogInfo(actionLogKey, handleType, message, params);
            return LogFilterHandler.filter(logInfo);
        } catch (Throwable th) {
            LogContext.getInstance().getActionLogger(actionLogKey).error("logInfoFilter Exception", th);
            return false;
        }
    }

    /**
     * 生成切面日志信息
     *
     * @param actionLogKey 日志标记
     * @param handleType   操作类型
     * @param message      日志信息
     * @param params       打印参数
     * @return
     */
    private LogInfo createLogInfo(ActionLogKey actionLogKey, HandleType handleType
            , String message, Object... params) throws ClassNotFoundException {
        // 构建日志
        LogInfo logInfo = LogContext.getInstance().getLogInfo();
        // 设置日志类型
        logInfo.setLogType(getLogType());
        // 设置标记信息
        logInfo.setFlow(actionLogKey.flow()).setAction(actionLogKey.action())
                .setHandleType(handleType).setExtendInfo(message);
        // 填充参数 结果 异常
        if (HandleType.RETURN.equals(logInfo.getHandleType())) {
            logInfo.setResult(params);
        } else {
            logInfo.getParameters().put(logInfo.getExtendInfo(), params);
        }
        logInfo.setThrowable(getThrowable(params));
        // 填充切面上下文信息
        logInfo.setToken(AspectContext.getInstance().getToken());
        logInfo.setHierarchy(AspectContext.getInstance().getCurrentHierarchy());
        logInfo.setSequence(AspectContext.getInstance().getAspectSequenceAndIncrement());
        // 填充调用信息
        if (ActionLogConfig.isLogManualIncludeLocation()) {
            StackTraceElement element = getStackTraceElement();
            logInfo.setCallerClass(ClassUtils.loadClass(element.getClassName()));
            logInfo.setCallerLineNumber(element.getLineNumber());
            logInfo.setCallerMethodName(element.getMethodName());
        }
        return logInfo;
    }

    /**
     * 获取日志类型
     *
     * @return
     */
    protected LogType getLogType() {
        return DefaultLogType.MANUAL;
    }

    /**
     * 获取异常信息
     *
     * @param params 打印参数
     */
    protected Throwable getThrowable(Object... params) {
        if (ArrayUtils.isNotEmpty(params) && params[params.length - 1] instanceof Throwable) {
            return (Throwable) params[params.length - 1];
        }
        return null;
    }

    /**
     * 获取调用信息
     * {@link sun.reflect.Reflection#getCallerClass(int)} int = 4,是当前方法被调用的深度
     *
     * @throws ClassNotFoundException
     */
    protected StackTraceElement getStackTraceElement() throws ClassNotFoundException {
        return ReflectionUtils.getStackTraceElement(4);
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
