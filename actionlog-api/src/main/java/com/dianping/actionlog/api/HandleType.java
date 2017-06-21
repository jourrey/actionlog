package com.dianping.actionlog.api;

/**
 * Created by jourrey on 16/11/23.
 * 通知类型
 */
public enum HandleType {
    PARAM, //参数
    RETURN, //返回
    CATCH, //捕获
    THROW, //异常
    FINALLY //结束
}