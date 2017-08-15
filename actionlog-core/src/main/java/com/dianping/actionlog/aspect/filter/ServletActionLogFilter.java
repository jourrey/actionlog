package com.dianping.actionlog.aspect.filter;

import com.alibaba.fastjson.JSON;
import com.dianping.actionlog.advice.LogManual;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.common.ActionLogConfig;
import com.dianping.actionlog.common.ActionLogConstants;
import com.dianping.actionlog.common.DefaultLogType;
import com.dianping.actionlog.common.LogType;
import com.dianping.actionlog.context.DefaultActionLogKey;
import com.dianping.actionlog.context.LogContext;
import com.google.common.collect.Maps;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ServletActionLogFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(ServletActionLogFilter.class);
    private static final String BIZ_STAMP_NAME = "bizStampName";
    private static final String JSON_CONTENT_TYPE = "application/json";

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
            LogContext.getInstance().putLocalContext(ActionLogConstants.LOG_REQUEST_ID, requestId);
            LogContext.getInstance().putLocalContext(ActionLogConstants.LOG_REQUEST_IP, requestIp);

            if (ActionLogConfig.isNeedFilterLog()) {
                FilterLogManual.info(DefaultActionLogKey.FILTER, HandleType.PARAM, getExtendInfo(), request.getParameterMap());
            }
        } catch (Throwable th) {
            LOG.error("ServletActionLogFilter doFilter Exception", th);
        }
        try {
            chain.doFilter(request, response);
            try {
                if (ActionLogConfig.isNeedFilterLog()) {
                    if (response.getContentType().indexOf(JSON_CONTENT_TYPE) > -1) {
                        FilterLogManual.info(DefaultActionLogKey.FILTER, HandleType.RETURN, getExtendInfo(), ((HttpServletResponse) response).getStatus());
                    } else {
                        FilterLogManual.info(DefaultActionLogKey.FILTER, HandleType.RETURN, getExtendInfo());
                    }
                }
            } catch (Throwable th) {
                LOG.error("ServletActionLogFilter doFilter Exception", th);
            }
        } catch (Throwable th) {
            if (ActionLogConfig.isNeedFilterLog()) {
                FilterLogManual.error(DefaultActionLogKey.FILTER, getExtendInfo(), th);
            }
            throw new ServletException(th);
        } finally {
            if (ActionLogConfig.isNeedFilterLog()) {
                FilterLogManual.info(DefaultActionLogKey.FILTER, HandleType.FINALLY, getExtendInfo());
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
        Map<String, Object> extendInfo = Maps.newHashMap();
        extendInfo.put(ActionLogConstants.LOG_REQUEST_ID, LogContext.getInstance().getLocalContext(ActionLogConstants.LOG_REQUEST_ID));
        extendInfo.put(ActionLogConstants.LOG_REQUEST_IP, LogContext.getInstance().getLocalContext(ActionLogConstants.LOG_REQUEST_IP));
        return JSON.toJSONString(extendInfo) + ActionLogConstants.SLF4J_PLACEHOLDER;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    private static class FilterLogManual extends LogManual {

        protected LogType getLogType() {
            return DefaultLogType.FILTER;
        }

    }

}
