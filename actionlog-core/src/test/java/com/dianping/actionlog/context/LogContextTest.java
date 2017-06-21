package com.dianping.actionlog.context;

import com.dianping.actionlog.logger.ActionLogger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * 容器初始化
 * Created by jourrey on 16/11/10.
 */
public class LogContextTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetActionLogger() {
//        exception.expect(ExceptionInInitializerError.class);
        ActionLogger logger = LogContext.getInstance().getActionLogger(DefaultActionLogKey.DEFAULT);
        Assert.assertNotNull(logger);
        logger.info("I am LogContext", null);
    }

}
