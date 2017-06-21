package com.dianping.actionlog.context.init;

import com.dianping.actionlog.aspect.shadow.ActionLogAdvisor;
import com.dianping.shadow.context.init.ContextLoaderListener;
import com.dianping.shadow.context.init.InitApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

/**
 * Created by jourrey on 16/11/10.
 */
public class ActionLogLoaderListener extends ContextLoaderListener {
    private static final Logger LOG = LoggerFactory.getLogger(ActionLogLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            //加载ActionLog的AspectConfig
            InitApplicationContext.scan(ActionLogAdvisor.class.getPackage().getName());
            super.contextInitialized(servletContextEvent);
        } catch (Throwable th) {
            LOG.error("contextInitialized Exception", th);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
    }
}
