package com.dianping.actionlog.logger.binder;

import com.dianping.actionlog.logger.ActionLoggerFactory;
import com.dianping.actionlog.logger.impl.Log4j2ActionLoggerFactory;

/**
 * actionlog-core ActionLoggerFactoryBinder implementation using actionlog-core. This class is part of the required classes used to specify an
 * actionlog-core actionLogger provider implementation.
 * <p/>
 * Created by jourrey on 16/11/16.
 */
public class StaticActionLoggerBinder implements ActionLoggerFactoryBinder {
    private static final String ACTION_LOGGER_FACTORY_CLASS_STR = Log4j2ActionLoggerFactory.class.getName();

    /**
     * The unique instance of this class.
     */
    private static final StaticActionLoggerBinder SINGLETON = new StaticActionLoggerBinder();

    /**
     * The ActionLoggerFactory instance returned by the {@link #getActionLoggerFactory}
     * method should always be the same object
     */
    private final ActionLoggerFactory actionLoggerFactory;

    /**
     * Private constructor to prevent instantiation
     */
    private StaticActionLoggerBinder() {
        actionLoggerFactory = new Log4j2ActionLoggerFactory();
    }

    /**
     * Returns the singleton of this class.
     *
     * @return the StaticLoggerBinder singleton
     */
    public static StaticActionLoggerBinder getSingleton() {
        return SINGLETON;
    }

    /**
     * Returns the builder.
     *
     * @return the builder.
     */
    @Override
    public ActionLoggerFactory getActionLoggerFactory() {
        return actionLoggerFactory;
    }

    /**
     * Returns the class name.
     *
     * @return the class name;
     */
    @Override
    public String getActionLoggerFactoryClassStr() {
        return ACTION_LOGGER_FACTORY_CLASS_STR;
    }

}
