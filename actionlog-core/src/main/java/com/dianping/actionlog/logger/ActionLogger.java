package com.dianping.actionlog.logger;

/**
 * Created by jourrey on 16/11/16.
 */
public interface ActionLogger {

    /**
     * debug级别日志
     *
     * @param message the message to log; the format depends on the message factory.
     * @param params  parameters to the message.
     */
    void debug(String message, Object... params);

    /**
     * info级别日志
     *
     * @param message the message to log; the format depends on the message factory.
     * @param params  parameters to the message.
     */
    void info(String message, Object... params);

    /**
     * warn级别日志
     *
     * @param message the message to log; the format depends on the message factory.
     * @param params  parameters to the message.
     */
    void warn(String message, Object... params);

    /**
     * error级别日志
     *
     * @param message the message to log; the format depends on the message factory.
     * @param params  parameters to the message.
     */
    void error(String message, Object... params);
    
}
