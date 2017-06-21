package com.dianping.actionlog.advice.filter;

import com.dianping.actionlog.advice.LogInfo;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.common.CatConfig;
import com.dianping.actionlog.context.LogContext;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;

import java.text.MessageFormat;

/**
 * Cat监控打点Event & MetricForCount
 * <p/>
 * Created by jourrey on 17/1/10.
 */
public class CatLogFilter implements LogFilter {
    private static final String CAT_TRANSACTION_NAME_FORMAT = "CAT_TRANSACTION_{0}_{1}_{2}";
    private static final String CAT_METRIC_DURATION_NAME_FORMAT = "CAT_METRIC_DURATION_{0}_{1}_{2}";

    @Override
    public boolean doFilter(LogInfo logInfo) {
        if (logInfo == null) {
            return false;
        }
        Transaction transaction;
        Long startMillis;
        if (HandleType.PARAM.equals(logInfo.getHandleType())) {
            if (CatConfig.needMetricForCount()) {
                Cat.logMetricForCount(CatConfig.getMetricCountKey(logInfo.getFlow(), logInfo.getAction()));
            }
            if (CatConfig.needMetricForDuration()) {
                startMillis = System.currentTimeMillis();
                LogContext.getInstance().putLocalContext(getCatMetricDurationName(logInfo), startMillis);
            }
            // 当前AOP依赖于Shadow,而Shadow的切面支持是第三方可配的,并不能保证Around属性,CAT的Transaction不commit会导致内存益处,所以注释掉这个功能
//            if (DefaultLogType.AOP.equals(logInfo.getLogType()) && CatConfig.needTransaction()) {
//                transaction = Cat.newTransaction(logInfo.getFlow(), logInfo.getAction());
//                LogContext.getInstance().putLocalContext(getCatTransactionName(logInfo), transaction);
//            }
            return true;
        }
        // Transaction
        transaction = (Transaction) LogContext.getInstance().getLocalContext(getCatTransactionName(logInfo));
        if (transaction != null) {
            if (HandleType.RETURN.equals(logInfo.getHandleType())) {
                transaction.setStatus(Transaction.SUCCESS);
            }
            if (HandleType.THROW.equals(logInfo.getHandleType())) {
                if (logInfo.getThrowable() != null) {
                    transaction.setStatus(logInfo.getThrowable());
                } else {
                    transaction.setStatus(HandleType.THROW.name());
                }
            }
            if (HandleType.FINALLY.equals(logInfo.getHandleType())) {
                transaction.complete();
            }
        }
        // MetricSlowThreshold & MetricForDuration
        startMillis = (Long) LogContext.getInstance().getLocalContext(getCatMetricDurationName(logInfo));
        if (startMillis != null && HandleType.FINALLY.equals(logInfo.getHandleType())) {
            Cat.setMetricSlowThreshold(CatConfig.getMetricDurationKey(logInfo.getFlow(), logInfo.getAction())
                    , CatConfig.getMetricForDurationSlowThreshold());
            Cat.logMetricForDuration(CatConfig.getMetricDurationKey(logInfo.getFlow(), logInfo.getAction())
                    , System.currentTimeMillis() - startMillis);
        }
        // Event
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

    private String getCatTransactionName(LogInfo logInfo) {
        return MessageFormat.format(CAT_TRANSACTION_NAME_FORMAT
                , logInfo.getFlow(), logInfo.getAction(), logInfo.getHierarchy());
    }

    private String getCatMetricDurationName(LogInfo logInfo) {
        return MessageFormat.format(CAT_METRIC_DURATION_NAME_FORMAT
                , logInfo.getFlow(), logInfo.getAction(), logInfo.getHandleType());
    }

}
