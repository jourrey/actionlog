package com.dianping.actionlog.logger.impl;

import com.dianping.actionlog.logger.ActionLogger;
import org.slf4j.Logger;

/**
 * @author liuzheng03
 * @ClassName: Log4J2ActionLogger
 * @Description: Log4j2日志记录者
 * @date 2016-11-16 下午3:13:17
 */
public class Log4J2ActionLogger implements ActionLogger {

    private Logger logger;

    public Log4J2ActionLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String message, Object... params) {
        logger.debug(message, params);
    }

    @Override
    public void info(String message, Object... params) {
        logger.info(message, params);
    }

    @Override
    public void warn(String message, Object... params) {
        logger.warn(message, params);
    }

    @Override
    public void error(String message, Object... params) {
        logger.error(message, params);
    }

}
