package com.dianping.actionlog.advice;

/**
 * Created by jourrey on 16/11/23.
 * 通知类型
 */
public enum HandleType {
    PARAM, //参数
    RETURN, //返回
    CATCH, //捕获
    THROW, //一异常
    FINALLY //末尾
}