package com.dianping.actionlog.context;

import org.junit.Assert;
import org.junit.Test;

/**
 * 容器初始化
 * Created by jourrey on 16/11/10.
 */
public class LogContextTest {

    @Test
    public void testGetActionLogger() {
        ActionLogger logger = LogContext.getInstance().getActionLogger("", "");
        Assert.assertNotNull(logger);
        logger.info("I am LogContext", null);
    }

}
