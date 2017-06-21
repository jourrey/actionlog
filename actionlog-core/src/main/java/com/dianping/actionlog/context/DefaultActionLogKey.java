package com.dianping.actionlog.context;

import com.dianping.actionlog.api.ActionLogKey;
import com.dianping.actionlog.common.ActionLogConstants;

/**
 * Created by jourrey on 16/11/27.
 */
public enum DefaultActionLogKey implements ActionLogKey {
    FILTER(ActionLogConstants.DEFAULT_FLOW, ActionLogConstants.FILTER_ACTION),
    DEFAULT(ActionLogConstants.DEFAULT_FLOW, ActionLogConstants.DEFAULT_ACTION);

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
