package com.dianping.actionlog.advice.filter;

import com.dianping.actionlog.advice.LogInfo;

/**
 * ActionLog拦截器
 * 拦截器会被同步调用,耗时操作请异步处理
 * Created by jourrey on 16/12/13.
 */
public interface LogFilter {

    /**
     * ActionLog过滤器,在执行ActionLog操作之前,同步执行
     * 返回True继续执行,返回False终止执行
     *
     * @param logInfo
     * @return
     */
    boolean doFilter(LogInfo logInfo);
}
