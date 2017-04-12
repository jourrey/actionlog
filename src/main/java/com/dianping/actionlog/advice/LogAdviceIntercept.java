package com.dianping.actionlog.advice;

import com.dianping.actionlog.advice.filter.LogFilterHandler;
import com.dianping.actionlog.annotation.ActionLog;
import com.dianping.actionlog.annotation.ActionLogBean;
import com.dianping.actionlog.annotation.ActionLogBean.ActionLogBeanBuilder;
import com.dianping.actionlog.annotation.BehaviorType;
import com.dianping.actionlog.annotation.InheritedActionLog;
import com.dianping.actionlog.common.CatConfig;
import com.dianping.actionlog.common.LogLevel;
import com.dianping.actionlog.common.SysConstants;
import com.dianping.actionlog.context.ActionLogger;
import com.dianping.actionlog.context.ActionLoggerHandler;
import com.dianping.actionlog.context.LogContext;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.dianping.shadow.advice.AdviceIntercept;
import com.dianping.shadow.advice.AspectParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jourrey on 16/11/16.
 */
public class LogAdviceIntercept implements AdviceIntercept {
    private static final Logger LOG = LogManager.getLogger(LogAdviceIntercept.class);
    private Transaction transaction;
    private Long startMillis;

    @Override
    public void before(AspectParam param) {
        executeAdvice(LogLevel.INFO, HandleType.PARAM, param);
    }

    @Override
    public void afterReturning(AspectParam param) {
        executeAdvice(LogLevel.INFO, HandleType.RETURN, param);
    }

    @Override
    public void afterThrowing(AspectParam param) {
        executeAdvice(LogLevel.ERROR, HandleType.THROW, param);
    }

    @Override
    public void afterFinally(AspectParam param) {
        executeAdvice(LogLevel.INFO, HandleType.FINALLY, param);
    }

    /**
     * 执行通知(advice)
     *
     * @param level      日志级别
     * @param handleType 操作类型
     * @param param      打印参数
     */
    private void executeAdvice(LogLevel level, HandleType handleType, AspectParam param) {
        try {
            ActionLogBean actionLogBean = getActionLogBean(param);
            if (!validateActionLogBean(actionLogBean)) {
                return;
            }

            LogInfo logInfo = createLogInfo(handleType, param, actionLogBean);
            if (!LogFilterHandler.filter(logInfo)) {
                return;
            }

            executeCatMonitor(logInfo);

            ActionLogger actionLogger = getActionLogger(actionLogBean);
            ActionLoggerHandler.printLog(actionLogger, level
                    , logInfo.format(SysConstants.LOG_INFO_PREFIX, SysConstants.LOG_INFO_SUFFIX)
                    , param.getThrowable());
        } catch (Throwable th) {
            LOG.error("executeAdvice", th);
        }
    }

    /**
     * 获取切面的ActionLog注解
     *
     * @param param
     * @return
     */
    private ActionLogBean getActionLogBean(AspectParam param) {
        ActionLogBean actionLogBean = null;
        if (param.getPointcutBean().getAnnotation() instanceof ActionLog) {
            ActionLog actionLog = (ActionLog) param.getPointcutBean().getAnnotation();
            actionLogBean = new ActionLogBeanBuilder()
                    .setFlow(actionLog.flow())
                    .setAction(actionLog.action())
                    .setExtendInfo(actionLog.extendInfo())
                    .setBehaviorType(actionLog.behaviorType())
                    .create();
        } else if (param.getPointcutBean().getAnnotation() instanceof InheritedActionLog) {
            InheritedActionLog inheritedActionLog = (InheritedActionLog) param.getPointcutBean().getAnnotation();
            actionLogBean = new ActionLogBeanBuilder()
                    .setFlow(inheritedActionLog.flow())
                    .setAction(inheritedActionLog.action())
                    .setExtendInfo(inheritedActionLog.extendInfo())
                    .setBehaviorType(inheritedActionLog.behaviorType())
                    .create();
        }
        return actionLogBean;
    }

    /**
     * 校验ActionLogBean是否有效
     *
     * @param actionLogBean
     * @return
     */
    private boolean validateActionLogBean(ActionLogBean actionLogBean) {
        if (actionLogBean == null) {
            return false;
        }
        if (BehaviorType.NO_DO_LOG.equals(actionLogBean.getBehaviorType())) {
            return false;
        }
        return true;
    }

    /**
     * 生成切面日志信息
     *
     * @param handleType
     * @param param
     * @param actionLogBean
     * @return
     */
    private LogInfo createLogInfo(HandleType handleType, AspectParam param, ActionLogBean actionLogBean) {
        LogInfo logInfo = LogContext.getInstance().getLogInfo();
        if (param.getParameters() != null) {
            logInfo.getParameters().putAll(param.getParameters());
        }
        logInfo.setLogType(LogType.AOP).setToken(param.getToken())
                .setFlow(StringUtils.isBlank(actionLogBean.getFlow()) ? SysConstants.DEFAULT_FLOW : actionLogBean.getFlow())
                .setAction(StringUtils.isBlank(actionLogBean.getAction()) ? SysConstants.DEFAULT_ACTION : actionLogBean.getAction())
                .setHandleType(handleType)
                .setResult(param.getResult()).setThrowable(param.getThrowable())
                .setHierarchy(param.getHierarchy()).setSequence(param.getSequence())
                .setExtendInfo(actionLogBean.getExtendInfo())
                .setCallerClass(param.getJoinPointBean().getDeclaringClass())
                .setCallerMethodName(param.getJoinPointBean().getMember().getName());
        return logInfo;
    }

    /**
     * 获取切面对应的Logger
     *
     * @param actionLogBean
     * @return
     */
    private ActionLogger getActionLogger(ActionLogBean actionLogBean) {
        return LogContext.getInstance().getActionLogger(actionLogBean.getFlow(), actionLogBean.getAction());
    }

    /**
     * 执行Cat的监控
     *
     * @param logInfo
     */
    private void executeCatMonitor(LogInfo logInfo) {
        try {
            if (HandleType.PARAM.equals(logInfo.getHandleType())) {
                if (CatConfig.needTransaction()) {
                    transaction = Cat.newTransaction(logInfo.getFlow(), logInfo.getAction());
                }
                if (CatConfig.needMetricForDuration()) {
                    startMillis = System.currentTimeMillis();
                }
                return;
            }
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
            if (startMillis != null && HandleType.FINALLY.equals(logInfo.getHandleType())) {
                Cat.setMetricSlowThreshold(CatConfig.getMetricDurationKey(logInfo.getFlow(), logInfo.getAction())
                        , CatConfig.getMetricForDurationSlowThreshold());
                Cat.logMetricForDuration(CatConfig.getMetricDurationKey(logInfo.getFlow(), logInfo.getAction())
                        , System.currentTimeMillis() - startMillis);
            }
        } catch (Throwable th) {
            LOG.error("executeCatMonitor", th);
        }
    }

}
