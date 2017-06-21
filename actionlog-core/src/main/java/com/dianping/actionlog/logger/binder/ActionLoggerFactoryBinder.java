package com.dianping.actionlog.logger.binder;

import com.dianping.actionlog.logger.ActionLoggerBuilder;
import com.dianping.actionlog.logger.ActionLoggerFactory;
import org.slf4j.ILoggerFactory;

/**
 * An internal interface which helps the static {@link ActionLoggerBuilder}
 * class bind with the appropriate {@link ActionLoggerFactory} instance.
 * <p/>
 * Created by jourrey on 16/11/16.
 */
public interface ActionLoggerFactoryBinder {

    /**
     * Return the instance of {@link ActionLoggerFactory} that
     * {@link com.dianping.actionlog.logger.ActionLoggerBuilder} class should bind to.
     *
     * @return the instance of {@link ILoggerFactory} that
     * {@link com.dianping.actionlog.logger.ActionLoggerBuilder} class should bind to.
     */
    ActionLoggerFactory getActionLoggerFactory();

    /**
     * The String form of the {@link ActionLoggerFactory} object that this
     * <code>ActionLoggerFactoryBinder</code> instance is <em>intended</em> to return.
     * <p/>
     * <p>This method allows the developer to intterogate this binder's intention
     * which may be different from the {@link ActionLoggerFactory} instance it is able to
     * yield in practice. The discrepency should only occur in case of errors.
     *
     * @return the class name of the intended {@link ActionLoggerFactory} instance
     */
    String getActionLoggerFactoryClassStr();

}
