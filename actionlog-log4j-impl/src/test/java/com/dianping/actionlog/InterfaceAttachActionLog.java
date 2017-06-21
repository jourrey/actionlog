package com.dianping.actionlog;

import com.dianping.actionlog.annotation.ActionLog;
import com.dianping.actionlog.annotation.InheritedActionLog;

/**
 * Created by jourrey on 16/8/20.
 */
@InheritedActionLog(flow = "0", action = "0", extendInfo = "Interface{}")
public interface InterfaceAttachActionLog {

    @ActionLog(flow = "0", action = "0", extendInfo = "InterfaceMethod{}")
    String getString00(String s);

    String getString01(String s);

    String getString02(String s);
}
