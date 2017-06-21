package com.dianping.actionlog.advice;

import com.dianping.actionlog.advice.filter.LogFilterHandler;
import com.dianping.actionlog.annotation.ActionLog;
import com.dianping.actionlog.annotation.BehaviorType;
import com.dianping.actionlog.annotation.InheritedActionLog;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.common.ActionLogConstants;
import com.dianping.actionlog.common.DefaultLogType;
import com.dianping.actionlog.context.LogContext;
import com.dianping.shadow.advice.AdviceIntercept;
import com.dianping.shadow.aspect.AspectParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jourrey on 16/11/16.
 */
public class LogAdviceIntercept implements AdviceIntercept {
    private static final Logger LOG = LoggerFactory.getLogger(LogAdviceIntercept.class);

    @Override
    public void before(AspectParam param) {
        try {
            ActionLogBean actionLogBean = getActionLogBean(param);
            if (!validateActionLogBean(actionLogBean)) {
                return;
            }
            LogInfo logInfo = logInfoFilter(actionLogBean, HandleType.PARAM, param);
            if (logInfo == null) {
                return;
            }
            LogContext.getInstance().getActionLogger(actionLogBean).info(logInfo.getExtendInfo(), logInfo.getParameters());
        } catch (Throwable e) {
            LOG.error("before Exception", e);
        }
    }

    @Override
    public void afterReturning(AspectParam param) {
        try {
            ActionLogBean actionLogBean = getActionLogBean(param);
            if (!validateActionLogBean(actionLogBean)) {
                return;
            }
            LogInfo logInfo = logInfoFilter(actionLogBean, HandleType.RETURN, param);
            if (logInfo == null) {
                return;
            }
            LogContext.getInstance().getActionLogger(actionLogBean).info(logInfo.getExtendInfo(), logInfo.getResult());
        } catch (Throwable e) {
            LOG.error("afterReturning Exception", e);
        }
    }

    @Override
    public void afterThrowing(AspectParam param) {
        try {
            ActionLogBean actionLogBean = getActionLogBean(param);
            if (!validateActionLogBean(actionLogBean)) {
                return;
            }
            LogInfo logInfo = logInfoFilter(actionLogBean, HandleType.THROW, param);
            if (logInfo == null) {
                return;
            }
            LogContext.getInstance().getActionLogger(actionLogBean).error(logInfo.getExtendInfo(), param.getThrowable());
        } catch (Throwable e) {
            LOG.error("afterThrowing Exception", e);
        }
    }

    @Override
    public void afterFinally(AspectParam param) {
        try {
            ActionLogBean actionLogBean = getActionLogBean(param);
            if (!validateActionLogBean(actionLogBean)) {
                return;
            }
            LogInfo logInfo = logInfoFilter(actionLogBean, HandleType.FINALLY, param);
            if (logInfo == null) {
                return;
            }
            LogContext.getInstance().getActionLogger(actionLogBean).info(logInfo.getExtendInfo());
        } catch (Throwable e) {
            LOG.error("afterFinally Exception", e);
        }
    }

    /**
     * 获取切面的ActionLog注解
     *
     * @param aspectParam 切面参数
     * @return
     */
    private ActionLogBean getActionLogBean(AspectParam aspectParam) {
        ActionLogBean actionLogBean = LogContext.getInstance().getActionLogBean();
        if (aspectParam.getPointcutBean().getAnnotation() instanceof ActionLog) {
            ActionLog actionLog = (ActionLog) aspectParam.getPointcutBean().getAnnotation();
            actionLogBean.setFlow(actionLog.flow());
            actionLogBean.setAction(actionLog.action());
            actionLogBean.setExtendInfo(actionLog.extendInfo());
            actionLogBean.setBehaviorType(actionLog.behaviorType());
        } else if (aspectParam.getPointcutBean().getAnnotation() instanceof InheritedActionLog) {
            InheritedActionLog inheritedActionLog = (InheritedActionLog) aspectParam.getPointcutBean().getAnnotation();
            actionLogBean.setFlow(inheritedActionLog.flow());
            actionLogBean.setAction(inheritedActionLog.action());
            actionLogBean.setExtendInfo(inheritedActionLog.extendInfo());
            actionLogBean.setBehaviorType(inheritedActionLog.behaviorType());
        }
        // 默认值处理
        actionLogBean.setFlow(StringUtils.isBlank(actionLogBean.getFlow()) ? ActionLogConstants.DEFAULT_FLOW : actionLogBean.getFlow());
        actionLogBean.setAction(StringUtils.isBlank(actionLogBean.getAction()) ? ActionLogConstants.DEFAULT_ACTION : actionLogBean.getAction());
        actionLogBean.setExtendInfo(StringUtils.isBlank(actionLogBean.getExtendInfo()) ? ActionLogConstants.SLF4J_PLACEHOLDER : actionLogBean.getExtendInfo());
        actionLogBean.setBehaviorType(actionLogBean.getBehaviorType() == null ? BehaviorType.NO_DO_LOG : actionLogBean.getBehaviorType());
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
     * LogInfo过滤器
     *
     * @param actionLogBean
     * @param handleType    操作类型
     * @param aspectParam   切面参数
     */
    private LogInfo logInfoFilter(ActionLogBean actionLogBean, HandleType handleType, AspectParam aspectParam) {
        LOG.debug("actionLogBean {}, handleType {}, aspectParam {}", actionLogBean, handleType, aspectParam);
        try {
            LogInfo logInfo = createLogInfo(actionLogBean, handleType, aspectParam);
            if (!LogFilterHandler.filter(logInfo)) {
                return null;
            }
            return logInfo;
        } catch (Throwable th) {
            LOG.error("logInfoFilter Exception", th);
            return null;
        }
    }

    /**
     * 生成切面日志信息
     *
     * @param actionLogBean
     * @param handleType
     * @param aspectParam
     * @return
     */
    private LogInfo createLogInfo(ActionLogBean actionLogBean, HandleType handleType, AspectParam aspectParam) {
        LogInfo logInfo = LogContext.getInstance().getLogInfo();
        if (aspectParam.getParameters() != null) {
            logInfo.getParameters().putAll(aspectParam.getParameters());
        }
        logInfo.setLogType(DefaultLogType.AOP).setToken(aspectParam.getToken())
                .setFlow(actionLogBean.getFlow()).setAction(actionLogBean.getAction()).setHandleType(handleType)
                .setResult(aspectParam.getResult()).setThrowable(aspectParam.getThrowable())
                .setHierarchy(aspectParam.getHierarchy()).setSequence(aspectParam.getSequence())
                .setExtendInfo(actionLogBean.getExtendInfo())
                .setCallerClass(aspectParam.getJoinPointBean().getDeclaringClass())
                .setCallerMethodName(aspectParam.getJoinPointBean().getMember().getName());
        return logInfo;
    }

}
