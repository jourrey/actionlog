package com.dianping.actionlog.common;

import com.dianping.actionlog.api.ActionLogKey;

/**
 * Created by jourrey on 16/11/27.
 */
public enum PigeonActionLogKey implements ActionLogKey {
    PIGEON_CLIENT(ActionLogConstants.DEFAULT_FLOW, "PigeonClient"),
    PIGEON_SERVICE(ActionLogConstants.DEFAULT_FLOW, "PigeonService");

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

    PigeonActionLogKey(String flow, String action) {
        this.flow = flow;
        this.action = action;
    }

}
