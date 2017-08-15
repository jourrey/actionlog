package com.dianping.actionlog.lion;

import com.dianping.actionlog.common.ActionLogConfig;
import com.dianping.actionlog.common.DpSupportConfig;
import com.dianping.actionlog.common.LionKeys;
import com.dianping.actionlog.util.AppInfoUtils;
import com.dianping.lion.client.ConfigEvent;
import com.dianping.lion.client.ConfigListener;
import com.dianping.lion.client.Lion;
import com.dianping.lion.client.spring.LionConfigListener;
import com.google.common.base.MoreObjects;

/**
 * @author jourrey.liu
 * @ClassName: ActionLogConfigListener
 * @Description: 刷新 {@link ActionLogConfig}
 * @date 2016-11-16 下午3:13:17
 */
@LionConfigListener
public class ActionLogConfigListener implements ConfigListener {

    static {
        Lion.addConfigListener(new ActionLogConfigListener());

        flushActionLogConfig();
    }

    @Override
    public void configChanged(ConfigEvent configEvent) {
        flushActionLogConfig();
    }

    private static void flushActionLogConfig() {
        ActionLogConfig.setAppName(MoreObjects.firstNonNull(AppInfoUtils.getAppName(), ActionLogConfig.getAppName()));
        ActionLogConfig.setNeedFilterLog(MoreObjects.firstNonNull(Lion.getBoolean(DpSupportConfig.lionKey(LionKeys.FILTER_LOG_SWITCH_KEY)), ActionLogConfig.isNeedFilterLog()));
        // LogManual配置
        ActionLogConfig.setLogManualIncludeLocation(MoreObjects.firstNonNull(Lion.getBoolean(DpSupportConfig.lionKey(LionKeys.LOG_MANUAL_INCLUDE_LOCATION)), ActionLogConfig.isLogManualIncludeLocation()));
    }

}
