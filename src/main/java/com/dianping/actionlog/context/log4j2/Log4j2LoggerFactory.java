package com.dianping.actionlog.context.log4j2;

import com.dianping.actionlog.common.LogLevel;
import com.dianping.actionlog.common.SysConfig;
import com.dianping.actionlog.common.SysConstants;
import com.dianping.actionlog.context.LogContext;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by jourrey on 16/11/21.
 */
public class Log4j2LoggerFactory {
    private static final Logger LOG = LogManager.getLogger(Log4j2LoggerFactory.class);

    private static final boolean IS_CURRENT_CONTEXT = false;

    private static final String FILE_SUFFIX = ".log";
    private static final String ROLLING_FILE_SUFFIX = "_%d{yyyyMMdd}_%i.gz";

    private static final int APPENDER_BUFFER_SIZE = 256 * 1024;
    private static final String APPENDER_IS_APPEND = "true";
    private static final String APPENDER_NAME_FORMAT = "{0}_{1}_{2}";

    /*日志key,一般是Class全名*/
    private String loggerName;
    /*日志流,基层目录*/
    private String flow;
    /*日志节点,文件名*/
    private String action;

    private Log4j2LoggerFactory(String loggerName, String flow, String action) {
        this.loggerName = loggerName;
        this.flow = flow;
        this.action = action;
    }

    /**
     * 创建日志构造器
     *
     * @param loggerName 日志key,一般是Class全名
     * @param flow       日志流,基层目录
     * @param action     日志节点,文件名
     * @return
     */
    public static Log4j2LoggerFactory create(String loggerName, String flow, String action) {
        Log4j2LoggerFactory factory = null;
        try {
            flow = StringUtils.isBlank(flow) ? SysConstants.DEFAULT_FLOW : flow;
            action = StringUtils.isBlank(action) ? SysConstants.DEFAULT_ACTION : action;
            factory = new Log4j2LoggerFactory(loggerName, flow, action);
        } catch (Exception e) {
            LOG.error("create error", e);
        }
        return factory;
    }

    /**
     * 创建日志对象
     *
     * @return
     */
    public Logger createLogger() {
        destroy();
        register();
        return LogManager.getLogger(loggerName);
    }

    private void register() {
        //为false时，返回多个LoggerContext对象; true：返回唯一的单例LoggerContext
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(IS_CURRENT_CONTEXT);
        final Configuration config = ctx.getConfiguration();
        //获取Appender
        List<Appender> appenders = getAppender(config);
        //配置Logger
        AppenderRef[] appenderRefs = new AppenderRef[appenders.size()];
        for (int i = 0; i < appenderRefs.length; i++) {
            appenderRefs[i] = AppenderRef.createAppenderRef(appenders.get(i).getName(), null, null);
        }
        LoggerConfig loggerConfig = AsyncLoggerConfig.createLogger(SysConfig.loggerConfigAdditive()
                , loggerConfigLevel(), loggerName, SysConfig.loggerIncludeLocation(), appenderRefs, null, config, null);
        //appender初始化
        for (Appender appender : appenders) {
            //启动appender
            appender.start();
            //appender加入上下文
            config.addAppender(appender);
            //appender加入Logger
            loggerConfig.addAppender(appender, null, null);
        }
        //Logger加入上下文
        config.addLogger(loggerConfig.getName(), loggerConfig);
        //更新上下文
        ctx.updateLoggers();
    }

    private void destroy() {
        //为false时，返回多个LoggerContext对象; true：返回唯一的单例LoggerContext
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(IS_CURRENT_CONTEXT);
        final Configuration config = ctx.getConfiguration();
        //获取Appender
        List<Appender> appenders = getAppender(config);
        //appender注销
        for (Appender appender : appenders) {
            //停止appender
            if (config.getAppender(appender.getName()) != null) {
                config.getAppender(appender.getName()).stop();
            }
            //appender移出上下文
            config.getAppenders().remove(appender.getName());
            //appender移出Logger
            config.getLoggerConfig(loggerName).removeAppender(appender.getName());
        }
        //Logger移出上下文
        config.removeLogger(loggerName);
        //更新上下文
        ctx.updateLoggers();
    }

    private Level transformLevel(LogLevel level) {
        if (LogLevel.DEBUG.equals(level)) {
            return Level.DEBUG;
        }
        if (LogLevel.INFO.equals(level)) {
            return Level.INFO;
        }
        if (LogLevel.WARN.equals(level)) {
            return Level.WARN;
        }
        if (LogLevel.ERROR.equals(level)) {
            return Level.ERROR;
        }
        return Level.OFF;
    }

    private Level loggerConfigLevel() {
        return transformLevel(SysConfig.loggerConfigLogLevel());
    }

    /**
     * 获取Appender的集合,保证register和destroy一致性,Appender固定为INFO\WARN\ERROR三个级别
     *
     * @param config
     * @return
     */
    private List<Appender> getAppender(Configuration config) {
        List<Appender> appenders = Lists.newArrayList();
        appenders.add(getAppender(config, Level.INFO));
        appenders.add(getAppender(config, Level.WARN));
        appenders.add(getAppender(config, Level.ERROR));
        return appenders;
    }

    /**
     * 获取Appender
     *
     * @param config
     * @param level
     * @return
     */
    private Appender getAppender(Configuration config, Level level) {
        //创建日志打印样式
        Layout layout = PatternLayout.createLayout(SysConfig.logConversionPattern(), config, null
                , Charset.forName("UTF-8"), true, false, null, null);
        //日志文件整理配置
        TimeBasedTriggeringPolicy ttp = TimeBasedTriggeringPolicy.createPolicy(SysConfig.logFileTimeInterval()
                , SysConfig.logFileTimeIntervalModulate());
        SizeBasedTriggeringPolicy stp = SizeBasedTriggeringPolicy.createPolicy(SysConfig.logFileMaxSize());
        TriggeringPolicy tp = CompositeTriggeringPolicy.createPolicy(ttp, stp);
        RolloverStrategy strategy = DefaultRolloverStrategy.createStrategy(SysConfig.logFileMaxNum()
                , null, null, null, config);
        //日志打印Appender
        Appender appender = RollingRandomAccessFileAppender.createAppender(getFileName(level), getFilePattern(level)
                , APPENDER_IS_APPEND, getAppenderName(level), SysConfig.logAppenderImmediateFlush()
                , String.valueOf(APPENDER_BUFFER_SIZE), tp, strategy, layout, getAppenderFilter(level)
                , null, null, null, config);
        return appender;
    }

    /**
     * 日志文件名
     *
     * @param level
     * @return
     */
    private String getFileName(Level level) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(SysConfig.logHomePrefix()).append(SysConstants.FILE_SEPARATOR)
                .append(LogContext.getInstance().getAppName());
        if (StringUtils.isNotBlank(flow)) {
            fileName.append(SysConstants.FILE_SEPARATOR).append(flow);
        }
        fileName.append(SysConstants.FILE_SEPARATOR).append(action);
        if (level != null) {
            fileName.append(SysConstants.NAME_SEPARATOR).append(level.name());
        }
        fileName.append(FILE_SUFFIX);
        return fileName.toString();
    }

    /**
     * 备份日志文件名
     *
     * @param level
     * @return
     */
    private String getFilePattern(Level level) {
        StringBuilder filePattern = new StringBuilder();
        filePattern.append(SysConfig.logHomePrefix()).append(SysConstants.FILE_SEPARATOR)
                .append(LogContext.getInstance().getAppName());
        if (StringUtils.isNotBlank(flow)) {
            filePattern.append(SysConstants.FILE_SEPARATOR).append(flow);
        }
        filePattern.append(SysConstants.FILE_SEPARATOR).append(action);
        if (level != null) {
            filePattern.append(SysConstants.NAME_SEPARATOR).append(level.name());
        }
        filePattern.append(ROLLING_FILE_SUFFIX);
        return filePattern.toString();
    }

    /**
     * Appender元素的定义名称
     *
     * @param level
     * @return
     */
    private String getAppenderName(Level level) {
        return MessageFormat.format(APPENDER_NAME_FORMAT, flow, action, level == null ? "" : level.name());
    }

    /**
     * Appender的Filter
     *
     * @param level
     * @return
     */
    private Filter getAppenderFilter(Level level) {
        if (Level.INFO.equals(level)) {
            return LevelAppenderFilter.INFO.filter;
        }
        if (Level.WARN.equals(level)) {
            return LevelAppenderFilter.WARN.filter;
        }
        if (Level.ERROR.equals(level)) {
            return LevelAppenderFilter.ERROR.filter;
        }
        return null;
    }

    /**
     * Filter是顺序执行,一个满足即可,所以先使用Filter.Result.DENY过滤,否则Filter.Result.ACCEPT会直接放行
     * ERROR 级别以上均为严重错误,视同 ERROR 一起打印,防止丢失重要异常信息
     */
    public enum LevelAppenderFilter {

        INFO(CompositeFilter.createFilters(new Filter[]{
                ThresholdFilter.createFilter(Level.WARN, Filter.Result.DENY, Filter.Result.NEUTRAL),
                ThresholdFilter.createFilter(Level.INFO, Filter.Result.ACCEPT, Filter.Result.DENY)
        })),

        WARN(CompositeFilter.createFilters(new Filter[]{
                ThresholdFilter.createFilter(Level.ERROR, Filter.Result.DENY, Filter.Result.NEUTRAL),
                ThresholdFilter.createFilter(Level.WARN, Filter.Result.ACCEPT, Filter.Result.DENY)
        })),

        ERROR(CompositeFilter.createFilters(new Filter[]{
                ThresholdFilter.createFilter(Level.ERROR, Filter.Result.ACCEPT, Filter.Result.DENY)
        }));

        private final CompositeFilter filter;

        LevelAppenderFilter(CompositeFilter filter) {
            this.filter = filter;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Logger logger = Log4j2LoggerFactory.create("", "" + i, "" + i).createLogger();
            for (int j = 0; j < 500; j++) {
                logger.info("Testing testing testing 111");
                logger.debug("Testing testing testing 222");
                logger.error("Testing testing testing 333");
            }
        }
    }
}
