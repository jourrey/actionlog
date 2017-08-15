package com.dianping.actionlog.common;

/**
 * Created by jourrey on 16/08/09.
 */
public interface ActionLogConstants {

    // 常用配置
    String DEFAULT_APP_NAME = "ActionLog";
    String DEFAULT_FLOW = "ActionLog";
    String DEFAULT_ACTION = "ActionLog";
    String FILTER_ACTION = "Filter";

    // slf4j打印日志的参数占位符
    String SLF4J_PLACEHOLDER = ": {}";

    // 入口ID和IP
    String LOG_REQUEST_ID = "ACTION_LOG_REQUEST_ID";
    String LOG_REQUEST_IP = "ACTION_LOG_REQUEST_IP";

}
