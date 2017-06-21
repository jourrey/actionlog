package com.dianping.actionlog;

import com.dianping.actionlog.annotation.ActionLog;

/**
 * Created by jourrey on 16/8/20.
 */
public class ClassAttachActionLog implements InterfaceAttachActionLog {

    public String getString00(String s) {
        return ClassAttachActionLog.class.getName();
    }

    @ActionLog(flow = "0", action = "0", extendInfo = "ClassMethod{}")
    public String getString01(String s) {
        return ClassAttachActionLog.class.getName();
    }

    public String getString02(String s) {
        return ClassAttachActionLog.class.getName();
    }

    @ActionLog(flow = "0", action = "0", extendInfo = "ClassMethod{}")
    public String getString10(String s) {
        return ClassAttachActionLog.class.getName();
    }

    public String getString11(String s) {
        return ClassAttachActionLog.class.getName();
    }

    public String getString12(String s) {
        return ClassAttachActionLog.class.getName();
    }
}
