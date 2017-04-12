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
import com.dianping.pigeon.remoting.invoker.domain.InvokerContext;
import com.dianping.pigeon.remoting.invoker.process.InvokerInterceptor;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * pigeon客户端拦截器
 * Created by jourrey on 16/12/11.
 */
public class ActionLogInvokerInterceptor implements InvokerInterceptor {

    @Override
    public void preInvoke(InvokerContext invokerContext) {
        //获取本地上下文的actionLogRequestId & actionLogRequestIp
        String actionLogRequestId = LogContext.getInstance().getLocalContext(SysConstants.LOG_REQUEST_ID);
        String actionLogRequestIp = LogContext.getInstance().getLocalContext(SysConstants.LOG_REQUEST_IP);
        //如果为空(没传/手动调4080接口),则actionLogRequestId & actionLogRequestIp都赋值pigeon的调用源IP
        actionLogRequestId = StringUtils.isBlank(actionLogRequestId) ? UUID.randomUUID().toString() : actionLogRequestId;
        actionLogRequestIp = StringUtils.isBlank(actionLogRequestIp) ?
                String.valueOf(ContextUtils.getGlobalContext(SysConstants.PIGEON_SOURCE_IP)) : actionLogRequestIp;

        ContextUtils.putGlobalContext(SysConstants.LOG_REQUEST_ID, actionLogRequestId);
        ContextUtils.putGlobalContext(SysConstants.LOG_REQUEST_IP, actionLogRequestIp);

        if (SysConfig.needPigeonLog()) {
            PigeonClientLogManual.info(HandleType.PARAM, getExtendInfo(), "{}", invokerContext);
        }
    }

    @Override
    public void postInvoke(InvokerContext invokerContext) {
        if (SysConfig.needPigeonLog()) {
            PigeonClientLogManual.info(HandleType.RETURN, getExtendInfo(), "{}", invokerContext);
        }
    }

    @Override
    public void afterThrowing(InvokerContext invokerContext, Throwable throwable) {
        if (SysConfig.needPigeonLog()) {
            PigeonClientLogManual.error(getExtendInfo(), "{}", invokerContext, throwable);
        }
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

    private static class PigeonClientLogManual extends LogManual {

        /**
         * 打印info级别日志
         *
         * @param handleType 操作类型
         * @param extendInfo 扩展信息
         * @param message    日志信息
         * @param params     打印参数
         */
        public static void info(HandleType handleType, String extendInfo, String message, Object... params) {
            printLog(LogType.PIGEON_CLIENT, LogLevel.INFO
                    , DefaultActionLogKey.PIGEON_CLIENT.flow(), DefaultActionLogKey.PIGEON_CLIENT.action()
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
            printLog(LogType.PIGEON_CLIENT, LogLevel.ERROR
                    , DefaultActionLogKey.PIGEON_CLIENT.flow(), DefaultActionLogKey.PIGEON_CLIENT.action()
                    , HandleType.THROW, extendInfo, message, params);
        }

    }

}
