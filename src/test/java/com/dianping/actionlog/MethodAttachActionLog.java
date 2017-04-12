package com.dianping.actionlog;

import com.dianping.actionlog.annotation.ActionLog;

/**
 * Created by jourrey on 16/8/20.
 */
public class MethodAttachActionLog extends ClassAttachActionLog {

    @ActionLog(flow = "0", action = "0", extendInfo = "Method")
    public String getString11(String s) {
        return MethodAttachActionLog.class.getName();
    }

    @ActionLog(flow = "0", action = "0", extendInfo = "Method")
    public String getString20(String s) {
        return MethodAttachActionLog.class.getName();
    }

    public String getString21(String s) {
        return MethodAttachActionLog.class.getName();
    }

    @ActionLog(flow = "0", action = "0", extendInfo = "Method")
    public void getString22() {
    }

}
