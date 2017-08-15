package com.dianping.actionlog.logger.log4j;

import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.common.ActionLogConfig;
import com.dianping.actionlog.common.LogLevel;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
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

import java.io.File;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by jourrey on 16/11/21.
 */
public class Log4j2LoggerFactory {

    private static final String FILE_SEPARATOR = File.separator;
    private static final String NAME_SEPARATOR = "_";

    private static final String LOG_CHARSET_NAME = "UTF-8";

    private static final boolean IS_CURRENT_CONTEXT = false;

    private static final String FILE_SUFFIX = "";
    // 由于log4j2的RollingRandomAccessFileAppender时间戳策略问题,最后一个时间单位并不是当前的,所以只保证日期准确性
    private static final String ROLLING_FILE_SUFFIX = "_%d{yyyyMMdd_HH}_%i.gz";

    private static final int APPENDER_BUFFER_SIZE = 256 * 1024;
    private static final String APPENDER_IS_APPEND = "true";
    private static final String APPENDER_NAME_FORMAT = "{0}_{1}_{2}";

    /*日志key,一般是Class全名*/
    private String loggerName;
    /*日志流,基层目录*/
    private String flow;
    /*日志节点,文件名*/
    private String action;

    /**
     * 创建日志构造器
     *
     * @param loggerName
     * @param actionLogKey
     */
    public Log4j2LoggerFactory(String loggerName, ActionLogKey actionLogKey) {
        this.loggerName = loggerName;
        this.flow = actionLogKey.flow();
        this.action = actionLogKey.action();
    }

    /**
     * 创建日志对象
     *
     * @return
     */
    public org.apache.logging.log4j.Logger createLogger() {
        destroy();
        register();
        return LogManager.getLogger(loggerName);
    }

    private void register() {
        //为false时，返回多个LoggerContext对象; true：返回唯一的单例LoggerContext
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(IS_CURRENT_CONTEXT);
        final Configuration config = ctx.getConfiguration();
        //获取Appender
        List<Appender> appenderList = getAppender(config);
        //配置Logger
        AppenderRef[] appenderRefs = new AppenderRef[appenderList.size()];
        for (int i = 0; i < appenderRefs.length; i++) {
            appenderRefs[i] = AppenderRef.createAppenderRef(appenderList.get(i).getName(), null, null);
        }
        LoggerConfig loggerConfig = AsyncLoggerConfig.createLogger(Log4j2LoggerConfig.getLoggerAdditive()
                , loggerConfigLevel(), loggerName, Log4j2LoggerConfig.getLoggerIncludeLocation(), appenderRefs, null, config, null);
        //appender初始化
        for (Appender appender : appenderList) {
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
        List<Appender> appenderList = getAppender(config);
        //appender注销
        for (Appender appender : appenderList) {
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

    /**
     * 根据ActionLog的日志级别返回对应的Log4j2的日志级别
     *
     * @return
     */
    private Level loggerConfigLevel() {
        LogLevel level = ActionLogConfig.getLogLevel();
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

    /**
     * 获取Appender的集合,保证register和destroy一致性,Appender固定为INFO\WARN\ERROR三个级别
     *
     * @param config
     * @return
     */
    private List<Appender> getAppender(Configuration config) {
        List<Appender> appenderList = Lists.newArrayList();
        Level level = loggerConfigLevel();
        if (Level.DEBUG.isMoreSpecificThan(level)) {
            appenderList.add(getAppender(config, Level.DEBUG));
        }
        if (Level.INFO.isMoreSpecificThan(level)) {
            appenderList.add(getAppender(config, Level.INFO));
        }
        if (Level.WARN.isMoreSpecificThan(level)) {
            appenderList.add(getAppender(config, Level.WARN));
        }
        if (Level.ERROR.isMoreSpecificThan(level)) {
            appenderList.add(getAppender(config, Level.ERROR));
        }
        return appenderList;
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
        Layout layout = PatternLayout.createLayout(Log4j2LoggerConfig.getLogConversionPattern(), config, null
                , Charset.forName(LOG_CHARSET_NAME), true, false, null, null);
        //日志文件整理配置
        TimeBasedTriggeringPolicy ttp = TimeBasedTriggeringPolicy.createPolicy(Log4j2LoggerConfig.getLogFileTimeInterval()
                , Log4j2LoggerConfig.getLogFileTimeIntervalModulate());
        SizeBasedTriggeringPolicy stp = SizeBasedTriggeringPolicy.createPolicy(Log4j2LoggerConfig.getLogFileMaxSize());
        TriggeringPolicy tp = CompositeTriggeringPolicy.createPolicy(ttp, stp);
        RolloverStrategy strategy = DefaultRolloverStrategy.createStrategy(Log4j2LoggerConfig.getLogFileMaxNum()
                , null, null, null, config);
        //日志打印Appender,RollingRandomAccessFileAppender相比RollingFileAppender有很大的性能提升,官方宣称是20-200%。
        Appender appender = RollingRandomAccessFileAppender.createAppender(getFileName(level), getFilePattern(level)
                , APPENDER_IS_APPEND, getAppenderName(level), Log4j2LoggerConfig.getLogAppenderImmediateFlush()
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
        fileName.append(Log4j2LoggerConfig.getLogHomePath())
                .append(FILE_SEPARATOR)
                .append(ActionLogConfig.getAppName());
        if (StringUtils.isNotBlank(flow)) {
            fileName.append(FILE_SEPARATOR).append(flow);
        }
        fileName.append(FILE_SEPARATOR).append(action);
        if (level != null) {
            fileName.append(NAME_SEPARATOR).append(level.name());
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
        filePattern.append(Log4j2LoggerConfig.getLogHomePath())
                .append(FILE_SEPARATOR)
                .append(ActionLogConfig.getAppName());
        if (StringUtils.isNotBlank(flow)) {
            filePattern.append(FILE_SEPARATOR).append(flow);
        }
        filePattern.append(FILE_SEPARATOR).append(action);
        if (level != null) {
            filePattern.append(NAME_SEPARATOR).append(level.name());
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
        if (Level.DEBUG.equals(level)) {
            return LevelAppenderFilter.DEBUG.filter;
        }
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

        DEBUG(CompositeFilter.createFilters(new Filter[]{
                ThresholdFilter.createFilter(Level.INFO, Filter.Result.DENY, Filter.Result.NEUTRAL),
                ThresholdFilter.createFilter(Level.DEBUG, Filter.Result.ACCEPT, Filter.Result.DENY)
        })),

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
        ActionLogConfig.setLogLevel(LogLevel.OFF);
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            org.apache.logging.log4j.Logger logger = new Log4j2LoggerFactory(i + NAME_SEPARATOR + i, new ActionLogKey() {
                @Override
                public String flow() {
                    return "" + finalI;
                }

                @Override
                public String action() {
                    return "" + finalI;
                }
            }).createLogger();
            for (int j = 0; j < 5000; j++) {
                logger.debug("Testing testing testing 000");
                logger.info("Testing testing testing 111");
                logger.warn("Testing testing testing 222");
                logger.error("Testing testing testing 333");
            }
        }
    }
}
