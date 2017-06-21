package com.dianping.actionlog.aspect.shadow;

import com.dianping.actionlog.annotation.ActionLog;
import com.dianping.actionlog.annotation.InheritedActionLog;
import com.dianping.shadow.context.strategy.AspectAnnotationStrategy;
import com.google.common.base.Optional;

import java.lang.annotation.Annotation;

/**
 * Created by jourrey on 17/2/6.
 */
public class ActionLogAspectAnnotationStrategy implements AspectAnnotationStrategy {

    @Override
    public Optional<? extends Class<? extends Annotation>> choose(Class<? extends Annotation> annotationType) {
        return Optional.fromNullable(ActionLog.class.equals(annotationType) ? InheritedActionLog.class : null);
    }

}
