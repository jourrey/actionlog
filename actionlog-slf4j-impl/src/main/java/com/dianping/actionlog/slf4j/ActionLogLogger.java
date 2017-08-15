/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package com.dianping.actionlog.slf4j;

import com.dianping.actionlog.advice.LogManual;
import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.common.ActionLogConfig;
import com.dianping.actionlog.common.LogLevel;
import com.dianping.actionlog.context.DefaultActionLogKey;
import com.dianping.actionlog.logger.ActionLogger;
import com.dianping.actionlog.logger.ActionLoggerBuilder;
import com.dianping.actionlog.logger.impl.NOPActionLogger;
import com.dianping.shadow.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * SLF4J logger implementation that uses Log4j.
 */
public class ActionLogLogger extends LogManual implements Logger, Serializable {
    private static final long serialVersionUID = 7869000638091304316L;
    private static final String ACTION_LOG_PACKAGE = "com.dianping.actionlog";
    private static final String SHADOW_PACKAGE = "com.dianping.shadow";

    private final String name;
    private transient ActionLogger internalLogger;

    public ActionLogLogger(String name) {
        this.name = name;
        internalLogger();
    }

    @Override
    public void trace(final String format) {
        return;
    }

    @Override
    public void trace(final String format, final Object o) {
        return;
    }

    @Override
    public void trace(final String format, final Object arg1, final Object arg2) {
        return;
    }

    @Override
    public void trace(final String format, final Object... args) {
        return;
    }

    @Override
    public void trace(final String format, final Throwable t) {
        return;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public boolean isTraceEnabled(final Marker marker) {
        return false;
    }

    @Override
    public void trace(final Marker marker, final String s) {
        return;
    }

    @Override
    public void trace(final Marker marker, final String s, final Object o) {
        return;
    }

    @Override
    public void trace(final Marker marker, final String s, final Object o, final Object o1) {
        return;
    }

    @Override
    public void trace(final Marker marker, final String s, final Object... objects) {
        return;
    }

    @Override
    public void trace(final Marker marker, final String s, final Throwable throwable) {
        return;
    }

    @Override
    public void debug(final String format) {
        if (isInternal()) {
            internalLogger.debug(null, null, format, null);
            return;
        }
        LogManual.debug(null, null, format, null);
    }

    @Override
    public void debug(final String format, final Object o) {
        if (isInternal()) {
            internalLogger.debug(null, null, format, o);
            return;
        }
        if (o instanceof ActionLogKey) {
            LogManual.debug((ActionLogKey) o, null, format, o);
        } else {
            LogManual.debug(null, null, format, o);
        }
    }

    @Override
    public void debug(final String format, final Object arg1, final Object arg2) {
        if (isInternal()) {
            internalLogger.debug(null, null, format, arg1, arg2);
            return;
        }
        if (arg1 instanceof ActionLogKey && arg2 instanceof HandleType) {
            LogManual.debug((ActionLogKey) arg1, (HandleType) arg2, format, arg1, arg2);
        } else if (arg1 instanceof ActionLogKey) {
            LogManual.debug((ActionLogKey) arg1, null, format, arg1, arg2);
        } else {
            LogManual.debug(null, null, format, arg1, arg2);
        }
    }

    @Override
    public void debug(final String format, final Object... args) {
        if (isInternal()) {
            internalLogger.debug(null, null, format, args);
            return;
        }
        if (args != null && args.length > 1 && args[0] instanceof ActionLogKey && args[1] instanceof HandleType) {
            LogManual.debug((ActionLogKey) args[0], (HandleType) args[1], format, args);
        } else if (args != null && args.length > 0 && args[0] instanceof ActionLogKey) {
            LogManual.debug((ActionLogKey) args[0], null, format, args);
        } else {
            LogManual.debug(null, null, format, args);
        }
    }

    @Override
    public void debug(final String format, final Throwable t) {
        if (isInternal()) {
            internalLogger.debug(null, null, format, t);
            return;
        }
        LogManual.debug(null, null, format, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return LogLevel.DEBUG.equals(ActionLogConfig.getLogLevel());
    }

    @Override
    public boolean isDebugEnabled(final Marker marker) {
        return LogLevel.DEBUG.equals(ActionLogConfig.getLogLevel());
    }

    @Override
    public void debug(final Marker marker, final String s) {
        if (isInternal()) {
            internalLogger.debug(null, null, s, null);
            return;
        }
        LogManual.debug(null, null, s, null);
    }

    @Override
    public void debug(final Marker marker, final String s, final Object o) {
        if (isInternal()) {
            internalLogger.debug(null, null, s, o);
            return;
        }
        if (o instanceof ActionLogKey) {
            LogManual.debug((ActionLogKey) o, null, s, o);
        } else {
            LogManual.debug(null, null, s, o);
        }
    }

    @Override
    public void debug(final Marker marker, final String s, final Object o, final Object o1) {
        if (isInternal()) {
            internalLogger.debug(null, null, s, o, o1);
            return;
        }
        if (o instanceof ActionLogKey && o1 instanceof HandleType) {
            LogManual.debug((ActionLogKey) o, (HandleType) o1, s, o, o1);
        } else if (o instanceof ActionLogKey) {
            LogManual.debug((ActionLogKey) o, null, s, o, o1);
        } else {
            LogManual.debug(null, null, s, o, o1);
        }
    }

    @Override
    public void debug(final Marker marker, final String s, final Object... objects) {
        if (isInternal()) {
            internalLogger.debug(null, null, s, objects);
            return;
        }
        if (objects != null && objects.length > 1 && objects[0] instanceof ActionLogKey && objects[1] instanceof HandleType) {
            LogManual.debug((ActionLogKey) objects[0], (HandleType) objects[1], s, objects);
        } else if (objects != null && objects.length > 0 && objects[0] instanceof ActionLogKey) {
            LogManual.debug((ActionLogKey) objects[0], null, s, objects);
        } else {
            LogManual.debug(null, null, s, objects);
        }
    }

    @Override
    public void debug(final Marker marker, final String s, final Throwable throwable) {
        if (isInternal()) {
            internalLogger.debug(null, null, s, throwable);
            return;
        }
        LogManual.debug(null, null, s, throwable);
    }

    @Override
    public void info(final String format) {
        if (isInternal()) {
            internalLogger.info(null, null, format, null);
            return;
        }
        LogManual.info(null, null, format, null);
    }

    @Override
    public void info(final String format, final Object o) {
        if (isInternal()) {
            internalLogger.info(null, null, format, o);
            return;
        }
        if (o instanceof ActionLogKey) {
            LogManual.info((ActionLogKey) o, null, format, o);
        } else {
            LogManual.info(null, null, format, o);
        }
    }

    @Override
    public void info(final String format, final Object arg1, final Object arg2) {
        if (isInternal()) {
            internalLogger.info(null, null, format, arg1, arg2);
            return;
        }
        if (arg1 instanceof ActionLogKey && arg2 instanceof HandleType) {
            LogManual.info((ActionLogKey) arg1, (HandleType) arg2, format, arg1, arg2);
        } else if (arg1 instanceof ActionLogKey) {
            LogManual.info((ActionLogKey) arg1, null, format, arg1, arg2);
        } else {
            LogManual.info(null, null, format, arg1, arg2);
        }
    }

    @Override
    public void info(final String format, final Object... args) {
        if (isInternal()) {
            internalLogger.info(null, null, format, args);
            return;
        }
        if (args != null && args.length > 1 && args[0] instanceof ActionLogKey && args[1] instanceof HandleType) {
            LogManual.info((ActionLogKey) args[0], (HandleType) args[1], format, args);
        } else if (args != null && args.length > 0 && args[0] instanceof ActionLogKey) {
            LogManual.info((ActionLogKey) args[0], null, format, args);
        } else {
            LogManual.info(null, null, format, args);
        }
    }

    @Override
    public void info(final String format, final Throwable t) {
        if (isInternal()) {
            internalLogger.info(null, null, format, t);
            return;
        }
        LogManual.info(null, null, format, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return LogLevel.INFO.equals(ActionLogConfig.getLogLevel());
    }

    @Override
    public boolean isInfoEnabled(final Marker marker) {
        return LogLevel.INFO.equals(ActionLogConfig.getLogLevel());
    }

    @Override
    public void info(final Marker marker, final String s) {
        if (isInternal()) {
            internalLogger.info(null, null, s, null);
            return;
        }
        LogManual.info(null, null, s, null);
    }

    @Override
    public void info(final Marker marker, final String s, final Object o) {
        if (isInternal()) {
            internalLogger.info(null, null, s, o);
            return;
        }
        if (o instanceof ActionLogKey) {
            LogManual.info((ActionLogKey) o, null, s, o);
        } else {
            LogManual.info(null, null, s, o);
        }
    }

    @Override
    public void info(final Marker marker, final String s, final Object o, final Object o1) {
        if (isInternal()) {
            internalLogger.info(null, null, s, o, o1);
            return;
        }
        if (o instanceof ActionLogKey && o1 instanceof HandleType) {
            LogManual.info((ActionLogKey) o, (HandleType) o1, s, o, o1);
        } else if (o instanceof ActionLogKey) {
            LogManual.info((ActionLogKey) o, null, s, o, o1);
        } else {
            LogManual.info(null, null, s, o, o1);
        }
    }

    @Override
    public void info(final Marker marker, final String s, final Object... objects) {
        if (isInternal()) {
            internalLogger.info(null, null, s, objects);
            return;
        }
        if (objects != null && objects.length > 1 && objects[0] instanceof ActionLogKey && objects[1] instanceof HandleType) {
            LogManual.info((ActionLogKey) objects[0], (HandleType) objects[1], s, objects);
        } else if (objects != null && objects.length > 0 && objects[0] instanceof ActionLogKey) {
            LogManual.info((ActionLogKey) objects[0], null, s, objects);
        } else {
            LogManual.info(null, null, s, objects);
        }
    }

    @Override
    public void info(final Marker marker, final String s, final Throwable throwable) {
        if (isInternal()) {
            internalLogger.info(null, null, s, throwable);
            return;
        }
        LogManual.info(null, null, s, throwable);
    }

    @Override
    public void warn(final String format) {
        if (isInternal()) {
            internalLogger.warn(null, null, format, null);
            return;
        }
        LogManual.warn(null, null, format, null);
    }

    @Override
    public void warn(final String format, final Object o) {
        if (isInternal()) {
            internalLogger.warn(null, null, format, o);
            return;
        }
        if (o instanceof ActionLogKey) {
            LogManual.warn((ActionLogKey) o, null, format, o);
        } else {
            LogManual.warn(null, null, format, o);
        }
    }

    @Override
    public void warn(final String format, final Object arg1, final Object arg2) {
        if (isInternal()) {
            internalLogger.warn(null, null, format, arg1, arg2);
            return;
        }
        if (arg1 instanceof ActionLogKey && arg2 instanceof HandleType) {
            LogManual.warn((ActionLogKey) arg1, (HandleType) arg2, format, arg1, arg2);
        } else if (arg1 instanceof ActionLogKey) {
            LogManual.warn((ActionLogKey) arg1, null, format, arg1, arg2);
        } else {
            LogManual.warn(null, null, format, arg1, arg2);
        }
    }

    @Override
    public void warn(final String format, final Object... args) {
        if (isInternal()) {
            internalLogger.warn(null, null, format, args);
            return;
        }
        if (args != null && args.length > 1 && args[0] instanceof ActionLogKey && args[1] instanceof HandleType) {
            LogManual.warn((ActionLogKey) args[0], (HandleType) args[1], format, args);
        } else if (args != null && args.length > 0 && args[0] instanceof ActionLogKey) {
            LogManual.warn((ActionLogKey) args[0], null, format, args);
        } else {
            LogManual.warn(null, null, format, args);
        }
    }

    @Override
    public void warn(final String format, final Throwable t) {
        if (isInternal()) {
            internalLogger.warn(null, null, format, t);
            return;
        }
        LogManual.warn(null, null, format, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return LogLevel.WARN.equals(ActionLogConfig.getLogLevel());
    }

    @Override
    public boolean isWarnEnabled(final Marker marker) {
        return LogLevel.WARN.equals(ActionLogConfig.getLogLevel());
    }

    @Override
    public void warn(final Marker marker, final String s) {
        if (isInternal()) {
            internalLogger.warn(null, null, s, null);
            return;
        }
        LogManual.warn(null, null, s, null);
    }

    @Override
    public void warn(final Marker marker, final String s, final Object o) {
        if (isInternal()) {
            internalLogger.warn(null, null, s, o);
            return;
        }
        if (o instanceof ActionLogKey) {
            LogManual.warn((ActionLogKey) o, null, s, o);
        } else {
            LogManual.warn(null, null, s, o);
        }
    }

    @Override
    public void warn(final Marker marker, final String s, final Object o, final Object o1) {
        if (isInternal()) {
            internalLogger.warn(null, null, s, o, o1);
            return;
        }
        if (o instanceof ActionLogKey && o1 instanceof HandleType) {
            LogManual.warn((ActionLogKey) o, (HandleType) o1, s, o, o1);
        } else if (o instanceof ActionLogKey) {
            LogManual.warn((ActionLogKey) o, null, s, o, o1);
        } else {
            LogManual.warn(null, null, s, o, o1);
        }
    }

    @Override
    public void warn(final Marker marker, final String s, final Object... objects) {
        if (isInternal()) {
            internalLogger.warn(null, null, s, objects);
            return;
        }
        if (objects != null && objects.length > 1 && objects[0] instanceof ActionLogKey && objects[1] instanceof HandleType) {
            LogManual.warn((ActionLogKey) objects[0], (HandleType) objects[1], s, objects);
        } else if (objects != null && objects.length > 0 && objects[0] instanceof ActionLogKey) {
            LogManual.warn((ActionLogKey) objects[0], null, s, objects);
        } else {
            LogManual.warn(null, null, s, objects);
        }
    }

    @Override
    public void warn(final Marker marker, final String s, final Throwable throwable) {
        if (isInternal()) {
            internalLogger.warn(null, null, s, throwable);
            return;
        }
        LogManual.warn(null, null, s, throwable);
    }

    @Override
    public void error(final String format) {
        if (isInternal()) {
            internalLogger.error(null, null, format, null);
            return;
        }
        LogManual.error(null, null, format, null);
    }

    @Override
    public void error(final String format, final Object o) {
        if (isInternal()) {
            internalLogger.error(null, null, format, o);
            return;
        }
        if (o instanceof ActionLogKey) {
            LogManual.error((ActionLogKey) o, null, format, o);
        } else {
            LogManual.error(null, null, format, o);
        }
    }

    @Override
    public void error(final String format, final Object arg1, final Object arg2) {
        if (isInternal()) {
            internalLogger.error(null, null, format, arg1, arg2);
            return;
        }
        if (arg1 instanceof ActionLogKey) {
            LogManual.error((ActionLogKey) arg1, null, format, arg1, arg2);
        } else {
            LogManual.error(null, null, format, arg1, arg2);
        }
    }

    @Override
    public void error(final String format, final Object... args) {
        if (isInternal()) {
            internalLogger.error(null, null, format, args);
            return;
        }
        if (args != null && args.length > 0 && args[0] instanceof ActionLogKey) {
            LogManual.error((ActionLogKey) args[0], null, format, args);
        } else {
            LogManual.error(null, null, format, args);
        }
    }

    @Override
    public void error(final String format, final Throwable t) {
        if (isInternal()) {
            internalLogger.error(null, null, format, t);
            return;
        }
        LogManual.error(null, null, format, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return LogLevel.ERROR.equals(ActionLogConfig.getLogLevel());
    }

    @Override
    public boolean isErrorEnabled(final Marker marker) {
        return LogLevel.ERROR.equals(ActionLogConfig.getLogLevel());
    }

    @Override
    public void error(final Marker marker, final String s) {
        if (isInternal()) {
            internalLogger.error(null, null, s, null);
            return;
        }
        LogManual.error(null, null, s, null);
    }

    @Override
    public void error(final Marker marker, final String s, final Object o) {
        if (isInternal()) {
            internalLogger.error(null, null, s, o);
            return;
        }
        if (o instanceof ActionLogKey) {
            LogManual.error((ActionLogKey) o, null, s, o);
        } else {
            LogManual.error(null, null, s, o);
        }
    }

    @Override
    public void error(final Marker marker, final String s, final Object o, final Object o1) {
        if (isInternal()) {
            internalLogger.error(null, null, s, o, o1);
            return;
        }
        if (o instanceof ActionLogKey) {
            LogManual.error((ActionLogKey) o, null, s, o, o1);
        } else {
            LogManual.error(null, null, s, o, o1);
        }
    }

    @Override
    public void error(final Marker marker, final String s, final Object... objects) {
        if (isInternal()) {
            internalLogger.error(null, null, s, objects);
            return;
        }
        if (objects != null && objects.length > 0 && objects[0] instanceof ActionLogKey) {
            LogManual.error((ActionLogKey) objects[0], null, s, objects);
        } else {
            LogManual.error(null, null, s, objects);
        }
    }

    @Override
    public void error(final Marker marker, final String s, final Throwable throwable) {
        if (isInternal()) {
            internalLogger.error(null, null, s, throwable);
            return;
        }
        LogManual.error(null, null, s, throwable);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected StackTraceElement getStackTraceElement() throws ClassNotFoundException {
        return ReflectionUtils.getStackTraceElement(5);
    }

    /**
     * 判断是否是内部包
     *
     * @return
     */
    private boolean isInternal() {
        return name.startsWith(ACTION_LOG_PACKAGE) || name.startsWith(SHADOW_PACKAGE);
    }

    /**
     * 获取ActionLogger
     *
     * @return
     */
    private void internalLogger() {
        try {
            internalLogger = ActionLoggerBuilder.getLogger(DefaultActionLogKey.DEFAULT);
        } catch (Exception e) {
            internalLogger = NOPActionLogger.NOP_ACTION_LOGGER;
        }
    }

    /**
     * Always treat de-serialization as a full-blown constructor, by validating the final state of
     * the de-serialized object.
     */
    private void readObject(final ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
        // always perform the default de-serialization first
        aInputStream.defaultReadObject();
        internalLogger();
    }

    /**
     * This is the default implementation of writeObject. Customise if necessary.
     */
    private void writeObject(final ObjectOutputStream aOutputStream) throws IOException {
        // perform the default serialization for all non-transient, non-static fields
        aOutputStream.defaultWriteObject();
    }

}
