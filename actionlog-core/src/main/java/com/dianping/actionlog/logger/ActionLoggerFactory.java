package com.dianping.actionlog.logger;

import com.dianping.actionlog.api.ActionLogKey;

/**
 * Created by jourrey on 16/11/16.
 */
public interface ActionLoggerFactory {

    /**
     * 根据actionLogKey获取ActionLogger
     *
     * @param actionLogKey the actionLogKey of the Logger to return
     */
    ActionLogger getLogger(ActionLogKey actionLogKey);

}
