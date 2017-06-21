package com.dianping.actionlog.aspect.pigeon;

import com.alibaba.fastjson.JSON;
import com.dianping.actionlog.advice.LogInfo;
import com.dianping.actionlog.advice.LogManual;
import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.common.ActionLogConstants;
import com.dianping.actionlog.common.DpSupportConfig;
import com.dianping.actionlog.common.DpSupportConstants;
import com.dianping.actionlog.common.PigeonActionLogKey;
import com.dianping.actionlog.common.PigeonLogType;
import com.dianping.actionlog.context.LogContext;
import com.dianping.pigeon.remoting.common.util.ContextUtils;
import com.dianping.pigeon.remoting.provider.domain.ProviderContext;
import com.dianping.pigeon.remoting.provider.process.ProviderInterceptor;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

/**
 * pigeon服务端拦截器
 * Created by jourrey on 16/12/11.
 */
public class ActionLogProviderInterceptor implements ProviderInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(ActionLogProviderInterceptor.class);

    @Override
    public void preInvoke(ProviderContext providerContext) {
        //获取调用方传递的actionLogRequestId & actionLogRequestIp
        String actionLogRequestId = String.valueOf(ContextUtils.getGlobalContext(ActionLogConstants.LOG_REQUEST_ID));
        String actionLogRequestIp = String.valueOf(ContextUtils.getGlobalContext(ActionLogConstants.LOG_REQUEST_IP));
        //如果为空(没传/手动调4080接口),则actionLogRequestId & actionLogRequestIp都赋值pigeon的调用源IP
        actionLogRequestId = StringUtils.isBlank(actionLogRequestId) ? UUID.randomUUID().toString() : actionLogRequestId;
        actionLogRequestIp = StringUtils.isBlank(actionLogRequestIp) ?
                String.valueOf(ContextUtils.getGlobalContext(DpSupportConstants.PIGEON_SOURCE_IP)) : actionLogRequestIp;

        LogContext.getInstance().putLocalContext(ActionLogConstants.LOG_REQUEST_ID, actionLogRequestId);
        LogContext.getInstance().putLocalContext(ActionLogConstants.LOG_REQUEST_IP, actionLogRequestIp);

        if (DpSupportConfig.needPigeonLog()) {
            PigeonServiceLogManual.info(HandleType.PARAM, getExtendInfo(), providerContext);
        }
    }

    @Override
    public void postInvoke(ProviderContext providerContext) {
        if (DpSupportConfig.needPigeonLog()) {
            PigeonServiceLogManual.info(HandleType.RETURN, getExtendInfo(), providerContext);
        }

        LogContext.getInstance().clearLocalContext();
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

    private static class PigeonServiceLogManual extends LogManual {

        /**
         * 打印info级别日志
         *
         * @param handleType 操作类型
         * @param message    日志信息
         * @param params     打印参数
         */
        public static void info(HandleType handleType, String message, Object... params) {
            try {
                ActionLogKey actionLogKey = PigeonActionLogKey.PIGEON_SERVICE;
                handleType = LogContext.getInstance().ifNullOfferDefault(handleType);
                LogInfo logInfo = logInfoFilter(PigeonLogType.PIGEON_SERVICE, actionLogKey, handleType, message, params);
                if (logInfo == null) {
                    return;
                }
                LogContext.getInstance().getActionLogger(actionLogKey).info(message, params);
            } catch (Exception e) {
                LOG.error("PigeonServiceLogManual Exception", e);
            }
        }

    }

}
