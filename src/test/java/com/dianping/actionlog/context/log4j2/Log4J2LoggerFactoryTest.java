package com.dianping.actionlog.context.log4j2;

import com.dianping.actionlog.advice.HandleType;
import com.dianping.actionlog.advice.LogManual;
import com.dianping.actionlog.advice.filter.LogFilter;
import com.dianping.actionlog.common.DefaultActionLogKey;
import com.dianping.ed.logger.EDLogger;
import com.dianping.ed.logger.LoggerConfig;
import com.dianping.ed.logger.LoggerLevel;
import com.dianping.ed.logger.LoggerManager;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.core.Is.is;

/**
 * 容器初始化
 * Created by jourrey on 16/11/10.
 */
public class Log4J2LoggerFactoryTest {
    private static final Logger LOG = LogManager.getLogger(Log4J2LoggerFactoryTest.class);

//    @Test
    @Ignore
    public void testCreate() {
        Logger logger = Log4j2LoggerFactory.create(LogFilter.class.getName(), "", "").createLogger();
        Assert.assertThat(logger.getName(), is(LogFilter.class.getName()));
        logger.debug("I am Log4j2LoggerFactory");
    }

//    @Test
    @Ignore
    public void testPressure() throws InterruptedException {
        long consume = 0;
        int num = 10;
        int count = 1000;
        int threadNum = 1;
        for (int i = 0; i < num; i++) {
            consume += testPressure(count, threadNum);
        }
        BigDecimal oneConsume = BigDecimal.valueOf(consume).divide(BigDecimal.valueOf(num * count));
        System.out.println("consume:"+oneConsume);
//        原理：每天80%的访问集中在20%的时间里，这20%时间叫做峰值时间
//        公式：( 总PV数 * 80% ) / ( 每天秒数 * 20% ) = 峰值时间每秒请求数(QPS) 搜索
        System.out.println("QPS:" + BigDecimal.valueOf(num * count * 1000).divide(BigDecimal.valueOf(consume), 2, RoundingMode.HALF_UP));
    }

    private long testPressure(int count, int threadNum) throws InterruptedException {
        LoggerConfig loggerConfig = new LoggerConfig();
        loggerConfig.setApp("actionlog");
        loggerConfig.setCategory("ActionLog");
        loggerConfig.setName("log4j");
        loggerConfig.setLevel(LoggerLevel.ERROR);
        loggerConfig.setDaily(true);
        loggerConfig.setPerm(false);
        loggerConfig.setError(true);
        final EDLogger ERROR = LoggerManager.getLogger(loggerConfig);
        LoggerConfig loggerConfig1 = new LoggerConfig();
        loggerConfig1.setApp("actionlog");
        loggerConfig1.setCategory("ActionLog");
        loggerConfig1.setName("log4j");
        loggerConfig1.setLevel(LoggerLevel.INFO);
        loggerConfig1.setDaily(true);
        loggerConfig1.setPerm(false);
        loggerConfig1.setError(false);
        final EDLogger INFO = LoggerManager.getLogger(loggerConfig1);
//        final StringBuffer logMsg = new StringBuffer();
//        final StringBuilder logMsg = new StringBuilder();

        final CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(threadNum);
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            cachedThreadPool.execute(new Runnable() {
                public void run() {
//                    LogContext.getInstance().getLogInfo();
//                    new LogInfo();
//                    String s = "flow" + "_" + "action";
//                    String.format("%s_%s", "flow", "action");
//                    MessageFormat.format("{0}_{1}", "flow", "action");
//                    logMsg.append("sdfsd");

//                logger.debug("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456//789);
//                    LogManual.debug("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
//                    INFO.info("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值" + "123" + "456");
//                    logger.info("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
//                    LogManual.info(null, null, "log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
//                logger.warn("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
//                LogManual.warn(null, null, "log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
//                    ERROR.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值" + "123" + "456");
//                    logger1.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值" + 123 + 456);
//                    LOG.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
//                    logger.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
                    ERROR.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值" + 123 + 456, new RuntimeException("789"));
//                    logger1.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值" + 123 + 456, new RuntimeException("789"));
//                    LOG.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789, new RuntimeException("789"));
//                logger.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789, new RuntimeException("789"));
//                    LogManual.error(null, null, "log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789, new RuntimeException("789"));
                    //执行子任务完毕之后，countDown减少一个点
                    countDownLatch.countDown();
                }
            });
        }

        try {
            //调用await方法阻塞当前线程，等待子线程完成后在继续执行
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        long consume = end - start;
        return consume;
    }

//    @Test
    @Ignore
    public void testPressure2() throws InterruptedException {
        LoggerConfig loggerConfig1 = new LoggerConfig();
        loggerConfig1.setApp("actionlog");
        loggerConfig1.setCategory("ActionLog");
        loggerConfig1.setName("log4j");
        loggerConfig1.setLevel(LoggerLevel.INFO);
        loggerConfig1.setDaily(true);
        loggerConfig1.setPerm(false);
        loggerConfig1.setError(false);
        final EDLogger INFO = LoggerManager.getLogger(loggerConfig1);
        LoggerConfig loggerConfig = new LoggerConfig();
        loggerConfig.setApp("actionlog");
        loggerConfig.setCategory("ActionLog");
        loggerConfig.setName("log4j");
        loggerConfig.setLevel(LoggerLevel.ERROR);
        loggerConfig.setDaily(true);
        loggerConfig.setPerm(false);
        loggerConfig.setError(true);
        final EDLogger ERROR = LoggerManager.getLogger(loggerConfig);
        final org.slf4j.Logger logger1 = LoggerFactory.getLogger(Log4J2LoggerFactoryTest.class);
        final Logger logger = Log4j2LoggerFactory.create(LogFilter.class.getName(), "", "").createLogger();

        Runnable log4j = new Runnable() {
            @Override
            public void run() {
//                INFO.info("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值" + "123" + "456");
                logger1.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值" + "123" + "456");
            }
        };
        Runnable log4j2 = new Runnable() {
            @Override
            public void run() {
//                logger.info("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
                logger.error("log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
            }
        };
        Runnable actionLog = new Runnable() {
            @Override
            public void run() {
//                LogManual.info(DefaultActionLogKey.DEFAULT, HandleType.PARAM, "log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
                LogManual.error(DefaultActionLogKey.DEFAULT, "log4j2之所以能秒杀一切日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
            }
        };

        List<String> result = Lists.newLinkedList();
        int num = 10;
        result.add(testPressure(num, 1000, 1, log4j));
        result.add(testPressure(num, 1000, 1, log4j));
        result.add(testPressure(num, 10000, 1, log4j));
        result.add(testPressure(num, 100000, 1, log4j));
        result.add(testPressure(num, 100000, 5, log4j));
        result.add(testPressure(num, 100000, 25, log4j));
        result.add(testPressure(num, 1000, 1, log4j2));
        result.add(testPressure(num, 1000, 1, log4j2));
        result.add(testPressure(num, 10000, 1, log4j2));
        result.add(testPressure(num, 100000, 1, log4j2));
        result.add(testPressure(num, 100000, 5, log4j2));
        result.add(testPressure(num, 100000, 25, log4j2));
        result.add(testPressure(num, 1000, 1, actionLog));
        result.add(testPressure(num, 1000, 1, actionLog));
        result.add(testPressure(num, 10000, 1, actionLog));
        result.add(testPressure(num, 100000, 1, actionLog));
        result.add(testPressure(num, 100000, 5, actionLog));
        result.add(testPressure(num, 100000, 25, actionLog));

        for (String s : result) {
            System.out.println(s);
        }
    }

    private String testPressure(int num, int count, int threadNum, Runnable runnable) throws InterruptedException {
        long consume = 0;
        for (int i = 0; i < num; i++) {
            consume += testPressure(count, threadNum, runnable);
        }
        BigDecimal oneConsume = BigDecimal.valueOf(consume).divide(BigDecimal.valueOf(num * count));
//        原理：每天80%的访问集中在20%的时间里，这20%时间叫做峰值时间
//        公式：( 总PV数 * 80% ) / ( 每天秒数 * 20% ) = 峰值时间每秒请求数(QPS) 搜索
        BigDecimal qps = BigDecimal.valueOf(num * count * 1000).divide(BigDecimal.valueOf(consume), 2, RoundingMode.HALF_UP);
        return "num:" + num + " count:" + count + " threadNum:" + threadNum + " consume:" + oneConsume + " qps:" + qps;
    }

    private long testPressure(int count, int threadNum, final Runnable runnable) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(threadNum);
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            cachedThreadPool.execute(new Runnable() {
                public void run() {
                    runnable.run();
                    //执行子任务完毕之后，countDown减少一个点
                    countDownLatch.countDown();
                }
            });
        }

        try {
            //调用await方法阻塞当前线程，等待子线程完成后在继续执行
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        long consume = end - start;
        return consume;
    }

}
