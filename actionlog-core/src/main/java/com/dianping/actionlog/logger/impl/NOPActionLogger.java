package com.dianping.actionlog.logger.impl;

import com.dianping.actionlog.logger.ActionLogger;

/**
 * A direct no operation ActionLogger.
 * <p/>
 * Created by jourrey on 16/11/16.
 */
public class NOPActionLogger implements ActionLogger {
    public static final ActionLogger NOP_ACTION_LOGGER = new NOPActionLogger();

    private NOPActionLogger() {
    }

    @Override
    public void debug(String message, Object... params) {
    }

    @Override
    public void info(String message, Object... params) {
    }

    @Override
    public void warn(String message, Object... params) {
    }

    @Override
    public void error(String message, Object... params) {
    }

}
