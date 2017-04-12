package com.dianping.actionlog.context;

import com.dianping.actionlog.common.LogLevel;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jourrey on 16/12/13.
 */
public abstract class ActionLoggerHandler {

    /**
     * 打印日志日志
     *
     * @param actionLogger
     * @param level
     * @param message      the message to log; the format depends on the message factory.
     * @param params       parameters to the message.
     */
    public static void printLog(ActionLogger actionLogger, LogLevel level, String message, Object... params) {
        checkNotNull(actionLogger, "actionLogger can't null");
        if (LogLevel.INFO.equals(level)) {
            actionLogger.info(message, params);
            return;
        }
        if (LogLevel.WARN.equals(level)) {
            actionLogger.warn(message, params);
            return;
        }
        if (LogLevel.ERROR.equals(level)) {
            actionLogger.error(message, params);
            return;
        }
        actionLogger.debug(message, params);
//        switch (level) {
//            case INFO:
//                actionLogger.info(message, params);
//                break;
//            case WARN:
//                actionLogger.warn(message, params);
//                break;
//            case ERROR:
//                actionLogger.error(message, params);
//                break;
//            default:
//                actionLogger.debug(message, params);
//        }
    }

}
