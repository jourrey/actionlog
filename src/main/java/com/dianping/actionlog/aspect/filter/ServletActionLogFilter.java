package com.dianping.actionlog.aspect.filter;

import com.dianping.actionlog.advice.HandleType;
import com.dianping.actionlog.advice.LogManual;
import com.dianping.actionlog.advice.LogType;
import com.dianping.actionlog.common.DefaultActionLogKey;
import com.dianping.actionlog.common.LogLevel;
import com.dianping.actionlog.common.SysConfig;
import com.dianping.actionlog.common.SysConstants;
import com.dianping.actionlog.context.LogContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ServletActionLogFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(ServletActionLogFilter.class);
    private static final String BIZ_STAMP_NAME = "bizStampName";

    // FilterConfig可用于访问Filter的配置信息
    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        LOG.debug("enter ServletActionLogFilter doFilter");
        try {
            String requestIp = getIpAddr((HttpServletRequest) request);
            String requestId;
            if (StringUtils.isBlank(filterConfig.getInitParameter(BIZ_STAMP_NAME))) {//请求参数里业务标记属性名称
                requestId = requestIp;
            } else {
                requestId = filterConfig.getInitParameter(BIZ_STAMP_NAME);
            }
            //设置本地上下文的actionLogRequestId & actionLogRequestIp
            LogContext.getInstance().putLocalContext(SysConstants.LOG_REQUEST_ID, requestId);
            LogContext.getInstance().putLocalContext(SysConstants.LOG_REQUEST_IP, requestIp);

            if (SysConfig.needFilterLog()) {
                FilterLogManual.info(HandleType.PARAM, getExtendInfo(), "{}", request.getParameterMap());
            }
        } catch (Throwable th) {
            LOG.error("{}", th);
        }
        try {
            chain.doFilter(request, response);
            try {
                if (SysConfig.needFilterLog()) {
                    FilterLogManual.info(HandleType.RETURN, getExtendInfo(), "{}", response);
                }
            } catch (Throwable th) {
                LOG.error("{}", th);
            }
        } catch (Throwable th) {
            if (SysConfig.needFilterLog()) {
                FilterLogManual.error(getExtendInfo(), "{}", th);
            }
            throw new ServletException(th);
        } finally {
            if (SysConfig.needFilterLog()) {
                FilterLogManual.info(HandleType.FINALLY, getExtendInfo(), "{}", response);
            }
            LogContext.getInstance().clearLocalContext();
        }

        LOG.debug("exit ServletActionLogFilter doFilter");
    }

    /**
     * 获取访问Ip
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getExtendInfo() {
        StringBuilder extendInfo = new StringBuilder();
        extendInfo.append(SysConstants.LOG_REQUEST_ID).append(SysConstants.KEY_VALUE_SEPARATOR)
                .append(LogContext.getInstance().getLocalContext(SysConstants.LOG_REQUEST_ID));
        extendInfo.append(SysConstants.EXTEND_INFO_SEPARATOR);
        extendInfo.append(SysConstants.LOG_REQUEST_IP).append(SysConstants.KEY_VALUE_SEPARATOR)
                .append(LogContext.getInstance().getLocalContext(SysConstants.LOG_REQUEST_IP));
        return extendInfo.toString();
    }

    public void destroy() {
        this.filterConfig = null;
    }

    private static class FilterLogManual extends LogManual {

        /**
         * 打印info级别日志
         *
         * @param handleType 操作类型
         * @param extendInfo 扩展信息
         * @param message    日志信息
         * @param params     打印参数
         */
        public static void info(HandleType handleType, String extendInfo, String message, Object... params) {
            printLog(LogType.FILTER, LogLevel.INFO
                    , DefaultActionLogKey.FILTER.flow(), DefaultActionLogKey.FILTER.action()
                    , handleType, extendInfo, message, params);
        }

        /**
         * 打印error级别日志
         *
         * @param extendInfo 扩展信息
         * @param message    日志信息
         * @param params     打印参数
         */
        public static void error(String extendInfo, String message, Object... params) {
            printLog(LogType.FILTER, LogLevel.ERROR
                    , DefaultActionLogKey.FILTER.flow(), DefaultActionLogKey.FILTER.action()
                    , HandleType.THROW, extendInfo, message, params);
        }

    }

}
