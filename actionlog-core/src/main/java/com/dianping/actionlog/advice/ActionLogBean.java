package com.dianping.actionlog.advice;

import com.dianping.actionlog.annotation.BehaviorType;
import com.dianping.actionlog.api.ActionLogKey;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by jourrey on 17/1/10.
 */
public class ActionLogBean implements ActionLogKey {

    private String flow;
    private String action;
    private String extendInfo;
    private BehaviorType behaviorType;

    public String getFlow() {
        return flow;
    }

    protected void setFlow(String flow) {
        this.flow = flow;
    }

    public String getAction() {
        return action;
    }

    protected void setAction(String action) {
        this.action = action;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    protected void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public BehaviorType getBehaviorType() {
        return behaviorType;
    }

    protected void setBehaviorType(BehaviorType behaviorType) {
        this.behaviorType = behaviorType;
    }

    public ActionLogBean clear() {
        this.flow = null;
        this.action = null;
        this.extendInfo = null;
        this.behaviorType = null;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ActionLogBean that = (ActionLogBean) o;

        return new EqualsBuilder()
                .append(flow, that.flow)
                .append(action, that.action)
                .append(extendInfo, that.extendInfo)
                .append(behaviorType, that.behaviorType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(flow)
                .append(action)
                .append(extendInfo)
                .append(behaviorType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("flow", flow)
                .append("action", action)
                .append("extendInfo", extendInfo)
                .append("behaviorType", behaviorType)
                .toString();
    }

    @Override
    public String flow() {
        return getFlow();
    }

    @Override
    public String action() {
        return getAction();
    }

}
