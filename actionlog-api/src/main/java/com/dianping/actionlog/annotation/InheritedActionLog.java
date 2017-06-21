package com.dianping.actionlog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解，被标注的类或者方法将执行日志记录
 *
 * @author jourrey
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InheritedActionLog {

    /**
     * 日志所属业务流
     */
    String flow() default "";

    /**
     * 日志所属业务功能
     */
    String action() default "";

    /**
     * 日志扩展信息，默认是无扩展信息
     *
     * @return String 返回类型
     * @throws
     */
    String extendInfo() default "";

    /**
     * 注解的行为类型，默认是记录日志，{@link BehaviorType}
     *
     * @returnType: BehaviorType
     */
    BehaviorType behaviorType() default BehaviorType.DO_LOG;

}
