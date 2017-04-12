package com.dianping.actionlog.aspect.shadow;

import com.dianping.actionlog.advice.LogAdviceIntercept;
import com.dianping.actionlog.annotation.ActionLog;
import com.dianping.shadow.annotation.AspectConfig;

/**
 * Created by jourrey on 16/11/16.
 */
public interface ActionLogAdvisor {

    @AspectConfig(annotation = ActionLog.class, advice = LogAdviceIntercept.class, extendInfo = "ActionLog")
    void advisor();

}
