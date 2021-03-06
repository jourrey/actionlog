package com.dianping.actionlog.logger;

import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.logger.binder.StaticActionLoggerBinder;
import com.dianping.actionlog.logger.impl.DefaultActionLoggerFactory;
import com.dianping.actionlog.logger.impl.NOPActionLoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by jourrey on 16/12/13.
 */
public final class ActionLoggerBuilder {

    // We need to use the name of the StaticLoggerBinder class, but we can't
    // reference
    // the class itself.
    private static String STATIC_LOGGER_BINDER_PATH = "com/dianping/actionlog/logger/binder/StaticActionLoggerBinder.class";

    static final int UNINITIALIZED = 0;
    static final int ONGOING_INITIALIZATION = 1;
    static final int FAILED_INITIALIZATION = 2;
    static final int SUCCESSFUL_INITIALIZATION = 3;
    static final int NOP_FALLBACK_INITIALIZATION = 4;
    static volatile int INITIALIZATION_STATE = UNINITIALIZED;

    static final String UNSUCCESSFUL_INIT_MSG = "com.dianping.actionlog.logger.ActionLoggerBuilder could not be successfully initialized.";

    static ActionLoggerFactory SUBST_FACTORY = new DefaultActionLoggerFactory();
    static ActionLoggerFactory NOP_FALLBACK_FACTORY = new NOPActionLoggerFactory();

    // private constructor prevents instantiation
    private ActionLoggerBuilder() {
    }

    /**
     * Force ActionLoggerBuilder to consider itself uninitialized.
     * <p/>
     * <p/>
     * This method is intended to be called by classes (in the same package) for
     * testing purposes. This method is internal. It can be modified, renamed or
     * removed at any time without notice.
     * <p/>
     * <p/>
     * You are strongly discouraged from calling this method in production code.
     */
    static void reset() {
        INITIALIZATION_STATE = UNINITIALIZED;
    }

    private final static void performInitialization() {
        bind();
    }

    private static boolean messageContainsOrgSlf4jImplStaticLoggerBinder(String msg) {
        if (msg == null)
            return false;
        if (msg.contains("com/dianping/actionlog/logger/binder/StaticActionLoggerBinder"))
            return true;
        if (msg.contains("com.dianping.actionlog.logger.binder.StaticActionLoggerBinder"))
            return true;
        return false;
    }

    private final static void bind() {
        try {
            Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
            reportMultipleBindingAmbiguity(staticLoggerBinderPathSet);
            // the next line does the binding
            StaticActionLoggerBinder.getSingleton();
            INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
            reportActualBinding(staticLoggerBinderPathSet);
        } catch (NoClassDefFoundError ncde) {
            String msg = ncde.getMessage();
            if (messageContainsOrgSlf4jImplStaticLoggerBinder(msg)) {
                INITIALIZATION_STATE = NOP_FALLBACK_INITIALIZATION;
                Util.report("Failed to load class \"com.dianping.actionlog.logger.binder.StaticActionLoggerBinder\".");
                Util.report("Defaulting to no-operation (NOP) logger implementation");
            } else {
                failedBinding(ncde);
                throw ncde;
            }
        } catch (java.lang.NoSuchMethodError nsme) {
            String msg = nsme.getMessage();
            if (msg != null && msg.contains("com.dianping.actionlog.logger.binder.StaticActionLoggerBinder.getSingleton()")) {
                INITIALIZATION_STATE = FAILED_INITIALIZATION;
                Util.report("actionlog-core 1.1.x (or later) is incompatible with this binding.");
                Util.report("Upgrade your binding to version.");
            }
            throw nsme;
        } catch (Exception e) {
            failedBinding(e);
            throw new IllegalStateException("Unexpected initialization failure", e);
        }
    }


    static void failedBinding(Throwable t) {
        INITIALIZATION_STATE = FAILED_INITIALIZATION;
        Util.report("Failed to instantiate actionlog-core ActionLoggerFactory", t);
    }

    static Set<URL> findPossibleStaticLoggerBinderPathSet() {
        // use Set instead of list in order to deal with bug #138
        // LinkedHashSet appropriate here because it preserves insertion order
        // during iteration
        Set<URL> staticLoggerBinderPathSet = new LinkedHashSet<URL>();
        try {
            ClassLoader loggerFactoryClassLoader = ActionLoggerBuilder.class.getClassLoader();
            Enumeration<URL> paths;
            if (loggerFactoryClassLoader == null) {
                paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
            } else {
                paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            }
            while (paths.hasMoreElements()) {
                URL path = paths.nextElement();
                staticLoggerBinderPathSet.add(path);
            }
        } catch (IOException ioe) {
            Util.report("Error getting resources from path", ioe);
        }
        return staticLoggerBinderPathSet;
    }

    private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> binderPathSet) {
        return binderPathSet.size() > 1;
    }

    /**
     * Prints a warning message on the console if multiple bindings were found
     * on the class path. No reporting is done otherwise.
     */
    private static void reportMultipleBindingAmbiguity(Set<URL> binderPathSet) {
        if (isAmbiguousStaticLoggerBinderPathSet(binderPathSet)) {
            Util.report("Class path contains multiple actionLoggerBuilder bindings.");
            for (URL path : binderPathSet) {
                Util.report("Found binding in [" + path + "]");
            }
        }
    }


    private static void reportActualBinding(Set<URL> binderPathSet) {
        // binderPathSet can be null under Android
        if (binderPathSet != null && isAmbiguousStaticLoggerBinderPathSet(binderPathSet)) {
            Util.report("Actual binding is of type [" + StaticActionLoggerBinder.getSingleton().getActionLoggerFactoryClassStr() + "]");
        }
    }

    /**
     * Return a actionLogger actionLogKey according to the name parameter using the
     * statically bound {@link ActionLoggerFactory} instance.
     *
     * @param actionLogKey The actionLogKey of the actionLogger.
     * @return actionLogger
     */
    public static ActionLogger getLogger(ActionLogKey actionLogKey) {
        ActionLoggerFactory actionLoggerFactory = getActionLoggerFactory();
        return actionLoggerFactory.getLogger(actionLogKey);
    }

    /**
     * Return the {@link ActionLoggerFactory} instance in use.
     * <p/>
     * <p/>
     * ActionLoggerFactory instance is bound with this class at compile time.
     *
     * @return the ActionLoggerFactory instance in use
     */
    public static ActionLoggerFactory getActionLoggerFactory() {
        if (INITIALIZATION_STATE == UNINITIALIZED) {
            synchronized (ActionLoggerBuilder.class) {
                if (INITIALIZATION_STATE == UNINITIALIZED) {
                    INITIALIZATION_STATE = ONGOING_INITIALIZATION;
                    performInitialization();
                }
            }
        }
        switch (INITIALIZATION_STATE) {
            case SUCCESSFUL_INITIALIZATION:
                return StaticActionLoggerBinder.getSingleton().getActionLoggerFactory();
            case NOP_FALLBACK_INITIALIZATION:
                return NOP_FALLBACK_FACTORY;
            case FAILED_INITIALIZATION:
                throw new IllegalStateException(UNSUCCESSFUL_INIT_MSG);
            case ONGOING_INITIALIZATION:
                return SUBST_FACTORY;
        }
        throw new IllegalStateException("Unreachable code");
    }

    /**
     * An internal utility class.
     *
     * @author Alexander Dorokhine
     * @author Ceki G&uuml;lc&uuml;
     */
    private static final class Util {

        private Util() {
        }

        public static String safeGetSystemProperty(String key) {
            if (key == null)
                throw new IllegalArgumentException("null input");

            String result = null;
            try {
                result = System.getProperty(key);
            } catch (java.lang.SecurityException sm) {
                ; // ignore
            }
            return result;
        }

        public static boolean safeGetBooleanSystemProperty(String key) {
            String value = safeGetSystemProperty(key);
            if (value == null)
                return false;
            else
                return value.equalsIgnoreCase("true");
        }

        /**
         * In order to call {@link SecurityManager#getClassContext()}, which is a
         * protected method, we add this wrapper which allows the method to be visible
         * inside this package.
         */
        private static final class ClassContextSecurityManager extends SecurityManager {
            protected Class<?>[] getClassContext() {
                return super.getClassContext();
            }
        }

        private static ClassContextSecurityManager SECURITY_MANAGER;
        private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;

        private static ClassContextSecurityManager getSecurityManager() {
            if (SECURITY_MANAGER != null)
                return SECURITY_MANAGER;
            else if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED)
                return null;
            else {
                SECURITY_MANAGER = safeCreateSecurityManager();
                SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
                return SECURITY_MANAGER;
            }
        }

        private static ClassContextSecurityManager safeCreateSecurityManager() {
            try {
                return new ClassContextSecurityManager();
            } catch (java.lang.SecurityException sm) {
                return null;
            }
        }

        /**
         * Returns the name of the class which called the invoking method.
         *
         * @return the name of the class which called the invoking method.
         */
        public static Class<?> getCallingClass() {
            ClassContextSecurityManager securityManager = getSecurityManager();
            if (securityManager == null)
                return null;
            Class<?>[] trace = securityManager.getClassContext();
            String thisClassName = Util.class.getName();

            // Advance until Util is found
            int i;
            for (i = 0; i < trace.length; i++) {
                if (thisClassName.equals(trace[i].getName()))
                    break;
            }

            // trace[i] = Util; trace[i+1] = caller; trace[i+2] = caller's caller
            if (i >= trace.length || i + 2 >= trace.length) {
                throw new IllegalStateException("Failed to find com.dianping.actionlog.logger.Util or its caller in the stack; " + "this should not happen");
            }

            return trace[i + 2];
        }

        static final public void report(String msg, Throwable t) {
            System.err.println(msg);
            System.err.println("Reported exception:");
            t.printStackTrace();
        }

        static final public void report(String msg) {
            System.err.println("ActionLog: " + msg);
        }

    }

}
