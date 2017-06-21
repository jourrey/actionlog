package com.dianping.actionlog.logger.impl;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jourrey on 17/1/10.
 */
public class Log4J2MessageFactory {
    private static final Logger LOG = LoggerFactory.getLogger(Log4J2MessageFactory.class);
    private static volatile MessageFactory messageFactory = null;

    private Log4J2MessageFactory() {
    }

    static {
        try {
            messageFactory = ParameterizedMessageFactory.class.newInstance();
        } catch (final InstantiationException e) {
            LOG.error("InstantiationException", e);
        } catch (final IllegalAccessException e) {
            LOG.error("IllegalAccessException", e);
        }
    }

    public static String getFormattedMessage(String message, Object... params) {
        Message msg = messageFactory.newMessage(message, params);
        return msg.getFormattedMessage();
    }
}
