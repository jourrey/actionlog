package com.dianping.actionlog.aspect.pigeon;

import com.alibaba.fastjson.JSON;
import com.dianping.actionlog.advice.LogManual;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.common.ActionLogConstants;
import com.dianping.actionlog.common.DpSupportConfig;
import com.dianping.actionlog.common.DpSupportConstants;
import com.dianping.actionlog.common.LogType;
import com.dianping.actionlog.common.PigeonActionLogKey;
import com.dianping.actionlog.common.PigeonLogType;
import com.dianping.actionlog.context.LogContext;
import com.dianping.pigeon.remoting.common.util.ContextUtils;
import com.dianping.pigeon.remoting.invoker.domain.InvokerContext;
import com.dianping.pigeon.remoting.invoker.process.InvokerInterceptor;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

/**
 * pigeon客户端拦截器
 * Created by jourrey on 16/12/11.
 */
public class ActionLogInvokerInterceptor implements InvokerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(ActionLogInvokerInterceptor.class);

    @Override
    public void preInvoke(InvokerContext invokerContext) {
        //获取本地上下文的actionLogRequestId & actionLogRequestIp
        String actionLogRequestId = (String) LogContext.getInstance().getLocalContext(ActionLogConstants.LOG_REQUEST_ID);
        String actionLogRequestIp = (String) LogContext.getInstance().getLocalContext(ActionLogConstants.LOG_REQUEST_IP);
        //如果为空(没传/手动调4080接口),则actionLogRequestId & actionLogRequestIp都赋值pigeon的调用源IP
        actionLogRequestId = StringUtils.isBlank(actionLogRequestId) ? UUID.randomUUID().toString() : actionLogRequestId;
        actionLogRequestIp = StringUtils.isBlank(actionLogRequestIp) ?
                String.valueOf(ContextUtils.getGlobalContext(DpSupportConstants.PIGEON_SOURCE_IP)) : actionLogRequestIp;

        ContextUtils.putGlobalContext(ActionLogConstants.LOG_REQUEST_ID, actionLogRequestId);
        ContextUtils.putGlobalContext(ActionLogConstants.LOG_REQUEST_IP, actionLogRequestIp);

        if (DpSupportConfig.needPigeonLog()) {
            PigeonClientLogManual.info(PigeonActionLogKey.PIGEON_CLIENT, HandleType.PARAM, getExtendInfo(), invokerContext);
        }
    }

    @Override
    public void postInvoke(InvokerContext invokerContext) {
        if (DpSupportConfig.needPigeonLog()) {
            PigeonClientLogManual.info(PigeonActionLogKey.PIGEON_CLIENT, HandleType.RETURN, getExtendInfo(), invokerContext);
        }
    }

    @Override
    public void afterThrowing(InvokerContext invokerContext, Throwable throwable) {
        if (DpSupportConfig.needPigeonLog()) {
            PigeonClientLogManual.error(PigeonActionLogKey.PIGEON_CLIENT, getExtendInfo(), invokerContext, throwable);
        }
    }

    private String getExtendInfo() {
        Map<String, Object> extendInfo = Maps.newHashMap();
        extendInfo.put(ActionLogConstants.LOG_REQUEST_ID, ContextUtils.getGlobalContext(ActionLogConstants.LOG_REQUEST_ID));
        extendInfo.put(ActionLogConstants.LOG_REQUEST_IP, ContextUtils.getGlobalContext(ActionLogConstants.LOG_REQUEST_IP));
        extendInfo.put(DpSupportConstants.PIGEON_CLIENT_IP, ContextUtils.getLocalContext(DpSupportConstants.PIGEON_CLIENT_IP));
        extendInfo.put(DpSupportConstants.PIGEON_CLIENT_APP, ContextUtils.getLocalContext(DpSupportConstants.PIGEON_CLIENT_APP));
        extendInfo.put(DpSupportConstants.PIGEON_SOURCE_IP, ContextUtils.getGlobalContext(DpSupportConstants.PIGEON_SOURCE_IP));
        extendInfo.put(DpSupportConstants.PIGEON_SOURCE_APP, ContextUtils.getGlobalContext(DpSupportConstants.PIGEON_SOURCE_APP));
        return JSON.toJSONString(extendInfo) + ActionLogConstants.SLF4J_PLACEHOLDER;
    }

    private static class PigeonClientLogManual extends LogManual {

        protected LogType getLogType() {
            return PigeonLogType.PIGEON_CLIENT;
        }

    }

}
