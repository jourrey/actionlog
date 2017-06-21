package com.dianping.actionlog.logger.impl;

import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.logger.ActionLogger;
import com.dianping.actionlog.logger.ActionLoggerFactory;

/**
 * Created by jourrey on 16/11/16.
 */
public class DefaultActionLoggerFactory implements ActionLoggerFactory {

    @Override
    public ActionLogger getLogger(ActionLogKey actionLogKey) {
        return DefaultActionLogger.DEFAULT_ACTION_LOGGER;
    }

}
