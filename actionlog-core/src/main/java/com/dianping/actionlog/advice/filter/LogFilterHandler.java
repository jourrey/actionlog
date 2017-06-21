package com.dianping.actionlog.advice.filter;

import com.dianping.actionlog.advice.LogInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by jourrey on 16/12/13.
 */
public class LogFilterHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LogFilterHandler.class);

    /**
     * 过滤,是否需要记录日志
     *
     * @param logInfo
     * @return
     */
    public static boolean filter(LogInfo logInfo) {
        // 执行过滤器
        List<LogFilter> filters = LogFilterLoader.getFilters();
        for (LogFilter filter : filters) {
            try {
                if (filter == null || filter.getClass().equals(logInfo.getCallerClass())) {
                    LOG.debug("filter {} validate skip {}", filter, logInfo.getCallerClass());
                    continue;
                }
                if (!filter.doFilter(logInfo)) {
                    LOG.debug("filter {} validate refuse", filter.getClass().getName());
                    return false;
                }
            } catch (Throwable th) {
                LOG.error("filter {} exception", filter.getClass().getName(), th);
                continue;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        LOG.debug("", new RuntimeException());
        LOG.info("", new RuntimeException());
        LOG.warn("", new RuntimeException());
        LOG.error("", new RuntimeException());
        LOG.error("{}", 213, new RuntimeException());
    }

}
