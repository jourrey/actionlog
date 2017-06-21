package com.dianping.actionlog.logger.binder;

import com.dianping.actionlog.logger.ActionLoggerBuilder;
import com.dianping.actionlog.logger.ActionLoggerFactory;
import com.dianping.actionlog.logger.impl.DefaultActionLoggerFactory;

/**
 * The binding of {@link ActionLoggerBuilder} class with an actual instance of
 * {@link ActionLoggerFactory} is performed using information returned by this class.
 * <p/>
 * This class is meant to provide a dummy StaticActionLoggerBinder to the actionlog module.
 * Real implementations are found in each actionlog binding project, e.g. actionlog-nop,
 * actionlog-log4j12 etc.
 * <p/>
 * Created by jourrey on 16/11/16.
 */
public class StaticActionLoggerBinder implements ActionLoggerFactoryBinder {
    private static final String ACTION_LOGGER_FACTORY_CLASS_STR = DefaultActionLoggerFactory.class.getName();

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
        actionLoggerFactory = new DefaultActionLoggerFactory();
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
