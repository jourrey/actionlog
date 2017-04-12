package com.dianping.actionlog.context.log4j2;

import com.dianping.actionlog.context.ActionLogger;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author liuzheng03
 * @ClassName: Log4J2ActionLogger
 * @Description: Log4j2日志记录者
 * @date 2016-11-16 下午3:13:17
 */
public class Log4J2ActionLogger implements ActionLogger {
    private static final Logger LOG = LogManager.getLogger(Log4J2ActionLogger.class);

    private Logger logger;

    /**
     * 构建Logger
     *
     * @param loggerName
     * @param flow
     * @param action
     */
    public Log4J2ActionLogger(String loggerName, String flow, String action) {
        if (StringUtils.isBlank(loggerName)) {
            logger = LOG;
        } else {
            logger = Log4j2LoggerFactory.create(loggerName, flow, action).createLogger();
        }
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
