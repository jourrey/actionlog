package com.dianping.actionlog.impl;

import com.dianping.actionlog.logger.impl.Log4J2MessageFactory;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 * 容器初始化
 * Created by jourrey on 16/11/10.
 */
public class Log4j2MessageFactoryTest {

    @Test
    public void testGetFormattedMessage() {
        String msg = Log4J2MessageFactory.getFormattedMessage("{}", "as", 12);

        Assert.assertThat(msg, is("[as, 12]"));

        msg = Log4J2MessageFactory.getFormattedMessage("{} {}", "as", 12);

        Assert.assertThat(msg, is("as 12"));

        msg = Log4J2MessageFactory.getFormattedMessage("{} {}", "as", 12, 78);

        Assert.assertThat(msg, is("as 12"));

        msg = Log4J2MessageFactory.getFormattedMessage("{} {}", "as", 12, new RuntimeException("ceshi"));

        System.out.println(msg);
        Assert.assertThat(msg, is("as 12"));
    }

}
