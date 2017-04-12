package com.dianping.actionlog.context;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by jourrey on 16/11/16.
 */
public class DefaultActionLogger implements ActionLogger {

    @Override
    public void debug(String message, Object... params) {
        System.out.println(message);
        if (ArrayUtils.isNotEmpty(params)) {
            Object o = params[params.length - 1];
            if (o instanceof Throwable) {
                ((Throwable) o).printStackTrace();
            }
        }
    }

    @Override
    public void info(String message, Object... params) {
        System.out.println(message);
        if (ArrayUtils.isNotEmpty(params)) {
            Object o = params[params.length - 1];
            if (o instanceof Throwable) {
                ((Throwable) o).printStackTrace();
            }
        }
    }

    @Override
    public void warn(String message, Object... params) {
        System.out.println(message);
        if (ArrayUtils.isNotEmpty(params)) {
            Object o = params[params.length - 1];
            if (o instanceof Throwable) {
                ((Throwable) o).printStackTrace();
            }
        }
    }

    @Override
    public void error(String message, Object... params) {
        System.out.println(message);
        if (ArrayUtils.isNotEmpty(params)) {
            Object o = params[params.length - 1];
            if (o instanceof Throwable) {
                ((Throwable) o).printStackTrace();
            }
        }
    }
}
