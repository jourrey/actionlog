package com.dianping.actionlog.advice;

import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.context.DefaultActionLogKey;
import com.dianping.actionlog.logger.ActionLogger;
import com.dianping.actionlog.logger.impl.Log4j2ActionLoggerFactory;
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

/**
 * 容器初始化
 * Created by jourrey on 16/11/10.
 */
public class LogManualTest {

    //    @Test
    @Ignore
    public void testCreate() {
        ActionLogger actionLogger = new Log4j2ActionLoggerFactory().getLogger(DefaultActionLogKey.DEFAULT);
        Assert.assertNotNull(actionLogger);
        actionLogger.debug("I am Log4j2ActionLoggerFactory");
    }

    @Test
    public void testPressure() throws InterruptedException {
        final org.slf4j.Logger logger = LoggerFactory.getLogger(LogManualTest.class);
        final Logger logger2 = LogManager.getLogger(LogManualTest.class);
        final ActionLogger actionLogger = new Log4j2ActionLoggerFactory().getLogger(DefaultActionLogKey.DEFAULT);

        Runnable log4j = new Runnable() {
            @Override
            public void run() {
//                logger.info("log4j2之所以快过其它日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值" + "123" + "456");
                logger.error("log4j2之所以快过其它日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值" + "123" + "456");
            }
        };
        Runnable log4j2 = new Runnable() {
            @Override
            public void run() {
//                logger2.info("log4j2之所以快过其它日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
                logger2.error("log4j2之所以快过其它日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
            }
        };
        Runnable log4j2Impl = new Runnable() {
            @Override
            public void run() {
//                actionLogger.info("log4j2之所以快过其它日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
                actionLogger.error("log4j2之所以快过其它日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
            }
        };
        Runnable actionLog = new Runnable() {
            @Override
            public void run() {
//                LogManual.info(DefaultActionLogKey.DEFAULT, HandleType.PARAM, "log4j2之所以快过其它日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
                LogManual.error(DefaultActionLogKey.DEFAULT, "log4j2之所以快过其它日志组件，是因为它支持异步输出日志，虽然这里没有使用异步,正在测试参数赋值{} {}", 123, 456, 789);
            }
        };

        List<String> result = Lists.newLinkedList();
        int num = 10;
//        result.add(testPressure(num, 1000, 1, context));
//        result.add(testPressure(num, 1000, 1, context));
//        result.add(testPressure(num, 10000, 1, context));
//        result.add(testPressure(num, 100000, 1, context));
//        result.add(testPressure(num, 100000, 5, context));
//        result.add(testPressure(num, 100000, 25, context));
//        result.add(testPressure(num, 1000, 1, log4j2));
//        result.add(testPressure(num, 1000, 1, log4j2));
//        result.add(testPressure(num, 10000, 1, log4j2));
//        result.add(testPressure(num, 100000, 1, log4j2));
//        result.add(testPressure(num, 100000, 5, log4j2));
//        result.add(testPressure(num, 100000, 25, log4j2));
//        result.add(testPressure(num, 1000, 1, log4j2Impl));
//        result.add(testPressure(num, 1000, 1, log4j2Impl));
//        result.add(testPressure(num, 10000, 1, log4j2Impl));
//        result.add(testPressure(num, 100000, 1, log4j2Impl));
//        result.add(testPressure(num, 100000, 5, log4j2Impl));
//        result.add(testPressure(num, 100000, 25, log4j2Impl));
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
