package com.dianping.actionlog.logger;

import com.dianping.actionlog.api.ActionLogKey;

/**
 * 子类请不要使用SLF4J API,否则ActionLog使用支持SLF4J API时,会引起死循环
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
