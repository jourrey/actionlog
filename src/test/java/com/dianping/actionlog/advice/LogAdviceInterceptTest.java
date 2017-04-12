package com.dianping.actionlog.advice;

import com.dianping.actionlog.MethodAttachActionLog;
import com.dianping.actionlog.annotation.ActionLog;
import com.dianping.actionlog.annotation.InheritedActionLog;
import com.dianping.shadow.advice.AspectParam;
import com.dianping.shadow.context.AspectContext;
import com.dianping.shadow.context.JoinPointBeanFactory;
import com.dianping.shadow.context.PointcutBeanFactory;
import com.dianping.shadow.context.init.InitApplicationContext;
import com.dianping.shadow.util.AnnotationUtils;
import com.dianping.shadow.util.ReflectionUtils;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AOP的切面
 *
 * @author jourrey
 */
public class LogAdviceInterceptTest {

    @Before
    public void cleanerAspectContext() {
        Field aspectResolverCache = ReflectionUtils.findField(AspectContext.class, "aspectResolverCache");
        Field joinPointResolverCache = ReflectionUtils.findField(AspectContext.class, "joinPointResolverCache");
        ReflectionUtils.makeAccessible(aspectResolverCache);
        ReflectionUtils.makeAccessible(joinPointResolverCache);
        ReflectionUtils.setField(aspectResolverCache, AspectContext.getInstance(), Lists.newLinkedList());
        ReflectionUtils.setField(joinPointResolverCache, AspectContext.getInstance(), LinkedHashMultimap.create());
    }

//    @Test
    @Ignore
    public void testAdviceInterceptor() throws Exception {
        InitApplicationContext.scan("com.dianping.actionlog");

        final LogAdviceIntercept interceptor = new LogAdviceIntercept();

        Method method00 = ReflectionUtils.findMethod(MethodAttachActionLog.class, "getString00", String.class);
        Method method01 = ReflectionUtils.findMethod(MethodAttachActionLog.class, "getString01", String.class);
        Method method02 = ReflectionUtils.findMethod(MethodAttachActionLog.class, "getString02", String.class);
        Method method10 = ReflectionUtils.findMethod(MethodAttachActionLog.class, "getString10", String.class);
        Method method11 = ReflectionUtils.findMethod(MethodAttachActionLog.class, "getString11", String.class);
        Method method12 = ReflectionUtils.findMethod(MethodAttachActionLog.class, "getString12", String.class);
        Method method20 = ReflectionUtils.findMethod(MethodAttachActionLog.class, "getString20", String.class);
        Method method21 = ReflectionUtils.findMethod(MethodAttachActionLog.class, "getString21", String.class);
        Method method22 = ReflectionUtils.findMethod(MethodAttachActionLog.class, "getString22");

        final AspectParam aspectParam00 = AspectParam.create(PointcutBeanFactory.create(AnnotationUtils.parseMethod(method00, InheritedActionLog.class))
                , JoinPointBeanFactory.create(method00));
        final AspectParam aspectParam01 = AspectParam.create(PointcutBeanFactory.create(AnnotationUtils.parseMethod(method01, ActionLog.class))
                , JoinPointBeanFactory.create(method01));
        final AspectParam aspectParam02 = AspectParam.create(PointcutBeanFactory.create(AnnotationUtils.parseMethod(method02, InheritedActionLog.class))
                , JoinPointBeanFactory.create(method02));
        final AspectParam aspectParam10 = AspectParam.create(PointcutBeanFactory.create(AnnotationUtils.parseMethod(method10, ActionLog.class))
                , JoinPointBeanFactory.create(method10));
        final AspectParam aspectParam11 = AspectParam.create(PointcutBeanFactory.create(AnnotationUtils.parseMethod(method11, ActionLog.class))
                , JoinPointBeanFactory.create(method11));
        final AspectParam aspectParam12 = AspectParam.create(PointcutBeanFactory.create(AnnotationUtils.parseMethod(method12, InheritedActionLog.class))
                , JoinPointBeanFactory.create(method12));
        final AspectParam aspectParam20 = AspectParam.create(PointcutBeanFactory.create(AnnotationUtils.parseMethod(method20, ActionLog.class))
                , JoinPointBeanFactory.create(method20));
        final AspectParam aspectParam21 = AspectParam.create(PointcutBeanFactory.create(AnnotationUtils.parseMethod(method21, InheritedActionLog.class))
                , JoinPointBeanFactory.create(method21));
        final AspectParam aspectParam22 = AspectParam.create(PointcutBeanFactory.create(AnnotationUtils.parseMethod(method22, ActionLog.class))
                , JoinPointBeanFactory.create(method22));

        Runnable runnable00 = new Runnable() {
            @Override
            public void run() {
                interceptor.before(aspectParam00);
                interceptor.afterReturning(aspectParam00);
                interceptor.afterThrowing(aspectParam00);
                interceptor.afterFinally(aspectParam00);
            }
        };
        Runnable runnable01 = new Runnable() {
            @Override
            public void run() {
                interceptor.before(aspectParam01);
                interceptor.afterReturning(aspectParam01);
                interceptor.afterThrowing(aspectParam01);
                interceptor.afterFinally(aspectParam01);
            }
        };
        Runnable runnable02 = new Runnable() {
            @Override
            public void run() {
                interceptor.before(aspectParam02);
                interceptor.afterReturning(aspectParam02);
                interceptor.afterThrowing(aspectParam02);
                interceptor.afterFinally(aspectParam02);
            }
        };
        Runnable runnable10 = new Runnable() {
            @Override
            public void run() {
                interceptor.before(aspectParam10);
                interceptor.afterReturning(aspectParam10);
                interceptor.afterThrowing(aspectParam10);
                interceptor.afterFinally(aspectParam10);
            }
        };
        Runnable runnable11 = new Runnable() {
            @Override
            public void run() {
                interceptor.before(aspectParam11);
                interceptor.afterReturning(aspectParam11);
                interceptor.afterThrowing(aspectParam11);
                interceptor.afterFinally(aspectParam11);
            }
        };
        Runnable runnable12 = new Runnable() {
            @Override
            public void run() {
                interceptor.before(aspectParam12);
                interceptor.afterReturning(aspectParam12);
                interceptor.afterThrowing(aspectParam12);
                interceptor.afterFinally(aspectParam12);
            }
        };
        Runnable runnable20 = new Runnable() {
            @Override
            public void run() {
                interceptor.before(aspectParam20);
                interceptor.afterReturning(aspectParam20);
                interceptor.afterThrowing(aspectParam20);
                interceptor.afterFinally(aspectParam20);
            }
        };
        Runnable runnable21 = new Runnable() {
            @Override
            public void run() {
                interceptor.before(aspectParam21);
                interceptor.afterReturning(aspectParam21);
                interceptor.afterThrowing(aspectParam21);
                interceptor.afterFinally(aspectParam21);
            }
        };
        Runnable runnable22 = new Runnable() {
            @Override
            public void run() {
                interceptor.before(aspectParam22);
                interceptor.afterReturning(aspectParam22);
                interceptor.afterThrowing(aspectParam22);
                interceptor.afterFinally(aspectParam22);
            }
        };

        List<String> result = Lists.newLinkedList();
        int num = 10;
        result.add(testPressure(num, 1000, 1, runnable00));
        result.add(testPressure(num, 1000, 1, runnable01));
        result.add(testPressure(num, 1000, 1, runnable02));
        result.add(testPressure(num, 1000, 1, runnable10));
        result.add(testPressure(num, 1000, 1, runnable11));
        result.add(testPressure(num, 1000, 1, runnable12));
        result.add(testPressure(num, 1000, 1, runnable20));
        result.add(testPressure(num, 1000, 1, runnable21));
        result.add(testPressure(num, 1000, 1, runnable22));
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