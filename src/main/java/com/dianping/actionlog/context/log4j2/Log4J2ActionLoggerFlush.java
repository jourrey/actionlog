package com.dianping.actionlog.context.log4j2;

import com.dianping.actionlog.common.LionKeys;
import com.dianping.actionlog.common.SysConfig;
import com.dianping.actionlog.util.LionUtils;
import com.dianping.lion.client.ConfigEvent;
import com.dianping.lion.client.ConfigListener;

/**
 * @author liuzheng03
 * @ClassName: Log4J2ActionLogger
 * @Description: Log4j2日志记录者
 * @date 2016-11-16 下午3:13:17
 */
public class Log4J2ActionLoggerFlush implements ConfigListener {

    @Override
    public void configChanged(ConfigEvent configEvent) {
        LionUtils.getInstance().addConfigListener(SysConfig.lionKey(LionKeys.LOGGER_ADDITIVE)
                , new Log4J2ActionLoggerFlush());
    }
}
