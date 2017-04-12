package com.dianping.actionlog.advice;

import com.dianping.actionlog.advice.filter.LogFilterHandler;
import com.dianping.actionlog.common.ActionLogKey;
import com.dianping.actionlog.common.DefaultActionLogKey;
import com.dianping.actionlog.common.LogLevel;
import com.dianping.actionlog.common.SysConfig;
import com.dianping.actionlog.common.SysConstants;
import com.dianping.actionlog.context.ActionLogger;
import com.dianping.actionlog.context.ActionLoggerHandler;
import com.dianping.actionlog.context.LogContext;
import com.dianping.shadow.context.AspectContext;
import com.dianping.shadow.util.ClassUtils;
import com.dianping.shadow.util.ReflectionUtils;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.reflect.Reflection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jourrey on 16/11/23.
 * ActionLog手动打印日志类,保持了与注解切面同一上下文
 * 使用ActionLogKey,而不是String flow,String action,目的是,期望这些业务流是固定的,且配置在一个地方
 * 另外一点是,传一个参数总比两个容易,减少错误的可能性
 */
public class LogManual {
    private static final Logger LOG = LogManager.getLogger(LogManual.class);

    /**
     * 打印debug级别日志,将日志输出到默认路径{@link DefaultActionLogKey}
     *
     * @param message 日志信息
     * @param params  打印参数
     */
    public static void debug(String message, Object... params) {
        printLog(LogType.MANUAL, LogLevel.DEBUG
                , DefaultActionLogKey.DEFAULT.flow(), DefaultActionLogKey.DEFAULT.action()
                , HandleType.PARAM, null, message, params);
    }

    /**
     * 打印info级别日志
     *
     * @param key        日志标记
     * @param handleType 操作类型
     * @param message    日志信息
     * @param params     打印参数
     */
    public static void info(ActionLogKey key, HandleType handleType, String message, Object... params) {
        printLog(LogType.MANUAL, LogLevel.INFO, ifNullOfferDefault(key).flow(), ifNullOfferDefault(key).action()
                , ifNullOfferDefault(handleType), null, message, params);
    }

    /**
     * 打印info级别日志
     *
     * @param key        日志标记
     * @param handleType 操作类型
     * @param extendInfo 扩展信息
     * @param message    日志信息
     * @param params     打印参数
     */
    public static void info(ActionLogKey key, HandleType handleType, String extendInfo, String message, Object... params) {
        printLog(LogType.MANUAL, LogLevel.INFO, ifNullOfferDefault(key).flow(), ifNullOfferDefault(key).action()
                , ifNullOfferDefault(handleType), extendInfo, message, params);
    }

    /**
     * 打印warn级别日志
     *
     * @param key        日志标记
     * @param handleType 操作类型
     * @param message    日志信息
     * @param params     打印参数
     */
    public static void warn(ActionLogKey key, HandleType handleType, String message, Object... params) {
        printLog(LogType.MANUAL, LogLevel.WARN, ifNullOfferDefault(key).flow(), ifNullOfferDefault(key).action()
                , ifNullOfferDefault(handleType), null, message, params);
    }

    /**
     * 打印warn级别日志
     *
     * @param key        日志标记
     * @param handleType 操作类型
     * @param extendInfo 扩展信息
     * @param message    日志信息
     * @param params     打印参数
     */
    public static void warn(ActionLogKey key, HandleType handleType, String extendInfo, String message, Object... params) {
        printLog(LogType.MANUAL, LogLevel.WARN, ifNullOfferDefault(key).flow(), ifNullOfferDefault(key).action()
                , ifNullOfferDefault(handleType), extendInfo, message, params);
    }

    /**
     * 打印error级别日志
     *
     * @param key     日志标记
     * @param message 日志信息
     * @param params  打印参数
     */
    public static void error(ActionLogKey key, String message, Object... params) {
        printLog(LogType.MANUAL, LogLevel.ERROR, ifNullOfferDefault(key).flow(), ifNullOfferDefault(key).action()
                , HandleType.THROW, null, message, params);
    }

    /**
     * 打印error级别日志
     *
     * @param key        日志标记
     * @param extendInfo 扩展信息
     * @param message    日志信息
     * @param params     打印参数
     */
    public static void error(ActionLogKey key, String extendInfo, String message, Object... params) {
        printLog(LogType.MANUAL, LogLevel.ERROR, ifNullOfferDefault(key).flow(), ifNullOfferDefault(key).action()
                , HandleType.THROW, extendInfo, message, params);
    }

    /**
     * 如果key为null,则提供默认{@link DefaultActionLogKey#DEFAULT}
     *
     * @param key
     * @return
     */
    protected static ActionLogKey ifNullOfferDefault(ActionLogKey key) {
        return MoreObjects.firstNonNull(key, DefaultActionLogKey.DEFAULT);
    }

    /**
     * 如果type为null,则提供默认{@link HandleType#PARAM}
     *
     * @param type
     * @return
     */
    protected static HandleType ifNullOfferDefault(HandleType type) {
        return MoreObjects.firstNonNull(type, HandleType.PARAM);
    }

    /**
     * 打印日志
     *
     * @param logType    日志类型
     * @param level      日志级别
     * @param flow       业务流
     * @param action     业务功能
     * @param handleType 操作类型
     * @param extendInfo 扩展信息
     * @param message    日志信息
     * @param params     打印参数
     */
    protected static void printLog(LogType logType, LogLevel level, String flow, String action
            , HandleType handleType, String extendInfo, String message, Object... params) {
        LOG.debug("logType {}, level {}, flow {}, action {}, handleType {}, extendInfo {}, message {}, params {}"
                , logType, level, flow, action, handleType, extendInfo, message, params);
        try {
            LogInfo logInfo = createLogInfo(logType, flow, action, handleType, extendInfo, message, params);
            if (!LogFilterHandler.filter(logInfo)) {
                return;
            }

            ActionLogger actionLogger = LogContext.getInstance().getActionLogger(flow, action);
            ActionLoggerHandler.printLog(actionLogger, level
                    , logInfo.format(SysConstants.LOG_INFO_PREFIX, SysConstants.LOG_INFO_SUFFIX), params);
        } catch (Throwable th) {
            LOG.error("printLog", th);
        }
    }

    /**
     * {@link Reflection#getCallerClass(int)} int = 3,是当前方法被调用的深度
     *
     * @return
     */
    private static LogInfo createLogInfo(LogType logType, String flow, String action, HandleType handleType
            , String extendInfo, String message, Object... params) throws ClassNotFoundException {
        // 构建日志
        LogInfo logInfo = LogContext.getInstance().getLogInfo();
        // 调用信息填充
        if (Boolean.valueOf(SysConfig.logManualIncludeLocation())) {
            StackTraceElement element = ReflectionUtils.getStackTraceElement(3);
            logInfo.setCallerClass(ClassUtils.loadClass(element.getClassName()));
            logInfo.setCallerLineNumber(element.getLineNumber());
            logInfo.setCallerMethodName(element.getMethodName());
        }
        // 设置LogType
        logInfo.setLogType(logType);
        // 填充切面上下文信息, 填充parameters result throwable
        fillLogContext(logInfo, handleType, message, params);
        // 日志内容填充
        logInfo.setFlow(flow).setAction(action).setHandleType(handleType).setExtendInfo(extendInfo);
        return logInfo;
    }

    private static void fillLogContext(LogInfo logInfo, HandleType handleType, String message, Object... params) {
        // 填充切面上下文信息
        logInfo.setToken(AspectContext.getInstance().getToken());
        logInfo.setHierarchy(AspectContext.getInstance().getCurrentHierarchy());
        logInfo.setSequence(AspectContext.getInstance().getAspectSequenceAndIncrement());
        // 填充parameters result throwable
        if (HandleType.RETURN.equals(handleType)) {
            logInfo.setResult(message);
        } else {
            logInfo.setResult(null);
            logInfo.getParameters().put(HandleType.PARAM.name(), message);
        }
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

    public static void main(String[] args) {
        LogManual.debug("0test{} {}", 123, 456, 789);
        LogManual.info(null, HandleType.PARAM, "0test{} {}", 123, 456, 789);
        LogManual.info(null, HandleType.RETURN, "0test{} {}", 123, 456, 789);
        LogManual.info(null, HandleType.FINALLY, "0test{} {}", 123, 456, 789);
        LogManual.warn(null, null, "0test{} {}", 123, 456, 789);
        LogManual.error(null, null, "0test{} {}", 123, 456, 789, new RuntimeException());
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(2);
        cachedThreadPool.execute(new Runnable() {
            public void run() {
                LogManual.debug("1test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.PARAM, "1test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.RETURN, "1test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.FINALLY, "1test{} {}", 123, 456, 789);
                LogManual.warn(null, null, "1test{} {}", 123, 456, 789);
                LogManual.error(null, null, "1test{} {}", 123, 456, 789, new RuntimeException("789"));
            }
        });
        cachedThreadPool.execute(new Runnable() {
            public void run() {
                LogManual.debug("2test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.PARAM, "2test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.RETURN, "2test{} {}", 123, 456, 789);
                LogManual.info(null, HandleType.FINALLY, "2test{} {}", 123, 456, 789);
                LogManual.warn(null, null, "2test{} {}", 123, 456, 789);
                LogManual.error(null, null, "2test{} {}", 123, 456, 789, new RuntimeException("789"));
            }
        });
        cachedThreadPool.execute(new Runnable() {
            public void run() {
                LogManual.debug("3test{} {}", 123, 456, 789);
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
