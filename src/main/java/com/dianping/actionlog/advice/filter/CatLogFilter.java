package com.dianping.actionlog.advice.filter;

import com.dianping.actionlog.advice.HandleType;
import com.dianping.actionlog.advice.LogInfo;
import com.dianping.actionlog.common.CatConfig;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;

/**
 * Cat监控打点Event & MetricForCount
 * <p/>
 * Created by jourrey on 17/1/10.
 */
public class CatLogFilter implements LogFilter {

    @Override
    public boolean doFilter(LogInfo logInfo) {
        if (logInfo == null) {
            return false;
        }
        if (CatConfig.needMetricForCount()) {
            Cat.logMetricForCount(CatConfig.getMetricCountKey(logInfo.getFlow(), logInfo.getAction()));
        }
        if (CatConfig.needEvent()) {
            //THROW是失败,其它类型都是成功,这个决定Cat上的错误率统计
            if (HandleType.THROW.equals(logInfo.getHandleType())) {
                Cat.logEvent(CatConfig.getEventKey(logInfo.getFlow(), logInfo.getAction())
                        , logInfo.getHandleType().name(), logInfo.getHandleType().name(), logInfo.getToken());
            } else {
                Cat.logEvent(CatConfig.getEventKey(logInfo.getFlow(), logInfo.getAction())
                        , logInfo.getHandleType().name(), Event.SUCCESS, logInfo.getToken());
            }
        }
        return true;
    }
}
