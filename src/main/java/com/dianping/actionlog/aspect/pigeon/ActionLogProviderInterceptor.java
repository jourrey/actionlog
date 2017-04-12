package com.dianping.actionlog.aspect.pigeon;

import com.dianping.actionlog.advice.HandleType;
import com.dianping.actionlog.advice.LogManual;
import com.dianping.actionlog.advice.LogType;
import com.dianping.actionlog.common.DefaultActionLogKey;
import com.dianping.actionlog.common.LogLevel;
import com.dianping.actionlog.common.SysConfig;
import com.dianping.actionlog.common.SysConstants;
import com.dianping.actionlog.context.LogContext;
import com.dianping.pigeon.remoting.common.util.ContextUtils;
import com.dianping.pigeon.remoting.provider.domain.ProviderContext;
import com.dianping.pigeon.remoting.provider.process.ProviderInterceptor;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * pigeon服务端拦截器
 * Created by jourrey on 16/12/11.
 */
public class ActionLogProviderInterceptor implements ProviderInterceptor {

    @Override
    public void preInvoke(ProviderContext providerContext) {
        //获取调用方传递的actionLogRequestId & actionLogRequestIp
        String actionLogRequestId = String.valueOf(ContextUtils.getGlobalContext(SysConstants.LOG_REQUEST_ID));
        String actionLogRequestIp = String.valueOf(ContextUtils.getGlobalContext(SysConstants.LOG_REQUEST_IP));
        //如果为空(没传/手动调4080接口),则actionLogRequestId & actionLogRequestIp都赋值pigeon的调用源IP
        actionLogRequestId = StringUtils.isBlank(actionLogRequestId) ? UUID.randomUUID().toString() : actionLogRequestId;
        actionLogRequestIp = StringUtils.isBlank(actionLogRequestIp) ?
                String.valueOf(ContextUtils.getGlobalContext(SysConstants.PIGEON_SOURCE_IP)) : actionLogRequestIp;

        LogContext.getInstance().putLocalContext(SysConstants.LOG_REQUEST_ID, actionLogRequestId);
        LogContext.getInstance().putLocalContext(SysConstants.LOG_REQUEST_IP, actionLogRequestIp);

        if (SysConfig.needPigeonLog()) {
            PigeonServiceLogManual.info(HandleType.PARAM, getExtendInfo(), "{}", providerContext);
        }
    }

    @Override
    public void postInvoke(ProviderContext providerContext) {
        if (SysConfig.needPigeonLog()) {
            PigeonServiceLogManual.info(HandleType.RETURN, getExtendInfo(), "{}", providerContext);
        }

        LogContext.getInstance().clearLocalContext();
    }

    private String getExtendInfo() {
        StringBuilder extendInfo = new StringBuilder();
        extendInfo.append(SysConstants.LOG_REQUEST_ID).append(SysConstants.KEY_VALUE_SEPARATOR)
                .append(ContextUtils.getGlobalContext(SysConstants.LOG_REQUEST_ID));
        extendInfo.append(SysConstants.EXTEND_INFO_SEPARATOR);
        extendInfo.append(SysConstants.LOG_REQUEST_IP).append(SysConstants.KEY_VALUE_SEPARATOR)
                .append(ContextUtils.getGlobalContext(SysConstants.LOG_REQUEST_IP));
        extendInfo.append(SysConstants.EXTEND_INFO_SEPARATOR);
        extendInfo.append(SysConstants.PIGEON_CLIENT_IP).append(SysConstants.KEY_VALUE_SEPARATOR)
                .append(ContextUtils.getLocalContext(SysConstants.PIGEON_CLIENT_IP));
        extendInfo.append(SysConstants.EXTEND_INFO_SEPARATOR);
        extendInfo.append(SysConstants.PIGEON_CLIENT_APP).append(SysConstants.KEY_VALUE_SEPARATOR)
                .append(ContextUtils.getLocalContext(SysConstants.PIGEON_CLIENT_APP));
        extendInfo.append(SysConstants.EXTEND_INFO_SEPARATOR);
        extendInfo.append(SysConstants.PIGEON_SOURCE_IP).append(SysConstants.KEY_VALUE_SEPARATOR)
                .append(ContextUtils.getGlobalContext(SysConstants.PIGEON_SOURCE_IP));
        extendInfo.append(SysConstants.EXTEND_INFO_SEPARATOR);
        extendInfo.append(SysConstants.PIGEON_SOURCE_APP).append(SysConstants.KEY_VALUE_SEPARATOR)
                .append(ContextUtils.getGlobalContext(SysConstants.PIGEON_SOURCE_APP));
        return extendInfo.toString();
    }

    private static class PigeonServiceLogManual extends LogManual {

        /**
         * 打印info级别日志
         *
         * @param handleType 操作类型
         * @param extendInfo 扩展信息
         * @param message    日志信息
         * @param params     打印参数
         */
        public static void info(HandleType handleType, String extendInfo, String message, Object... params) {
            printLog(LogType.PIGEON_SERVICE, LogLevel.INFO
                    , DefaultActionLogKey.PIGEON_SERVICE.flow(), DefaultActionLogKey.PIGEON_SERVICE.action()
                    , handleType, extendInfo, message, params);
        }

    }

}
