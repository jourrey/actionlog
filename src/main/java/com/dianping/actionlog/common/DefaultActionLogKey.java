package com.dianping.actionlog.common;

/**
 * Created by jourrey on 16/11/27.
 */
public enum DefaultActionLogKey implements ActionLogKey {
    FILTER(SysConstants.DEFAULT_FLOW, "Filter"),
    PIGEON_CLIENT(SysConstants.DEFAULT_FLOW, "PigeonClient"),
    PIGEON_SERVICE(SysConstants.DEFAULT_FLOW, "PigeonService"),
    DEFAULT(SysConstants.DEFAULT_FLOW, SysConstants.DEFAULT_ACTION);

    /**
     * 业务流名称
     */
    private String flow;

    /**
     * 业务功能名称
     */
    private String action;

    public String getFlow() {
        return flow;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String flow() {
        return getFlow();
    }

    @Override
    public String action() {
        return getAction();
    }

    DefaultActionLogKey(String flow, String action) {
        this.flow = flow;
        this.action = action;
    }
}
