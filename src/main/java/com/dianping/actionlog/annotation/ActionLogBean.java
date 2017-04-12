package com.dianping.actionlog.annotation;

/**
 * Created by jourrey on 17/1/10.
 */
public class ActionLogBean {

    private String flow;
    private String action;
    private String extendInfo;
    private BehaviorType behaviorType;

    private ActionLogBean(String flow, String action, String extendInfo, BehaviorType behaviorType) {
        this.flow = flow;
        this.action = action;
        this.extendInfo = extendInfo;
        this.behaviorType = behaviorType;
    }

    public String getFlow() {
        return flow;
    }

    public String getAction() {
        return action;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public BehaviorType getBehaviorType() {
        return behaviorType;
    }

    public static class ActionLogBeanBuilder {
        private String flow;
        private String action;
        private String extendInfo;
        private BehaviorType behaviorType;

        public ActionLogBeanBuilder setFlow(String flow) {
            this.flow = flow;
            return this;
        }

        public ActionLogBeanBuilder setAction(String action) {
            this.action = action;
            return this;
        }

        public ActionLogBeanBuilder setExtendInfo(String extendInfo) {
            this.extendInfo = extendInfo;
            return this;
        }

        public ActionLogBeanBuilder setBehaviorType(BehaviorType behaviorType) {
            this.behaviorType = behaviorType;
            return this;
        }

        public ActionLogBean create() {
            return new ActionLogBean(flow, action, extendInfo, behaviorType);
        }
    }
}
