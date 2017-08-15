package com.dianping.actionlog.advice;

import com.dianping.actionlog.advice.filter.LogFilter;
import com.dianping.actionlog.api.HandleType;
import com.dianping.actionlog.common.LogType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jourrey on 16/11/23.
 * 这里没有单独构建builder类是因为,每new一次创建2个对象的代价,对于原生的日志工具,性能损耗占比太大,最然总体不到毫秒
 */
public class LogInfo implements Serializable {
    private static final long serialVersionUID = -6976400465268471183L;

    private LogType logType;
    private String token;
    private String flow;
    private String action;
    private HandleType handleType;
    private Map<String, Object> parameters;
    private Object result;
    private Throwable throwable;
    private Integer hierarchy;//调用层级
    private Integer sequence;//调用顺序
    private String extendInfo;
    /**
     * ActionLogConfig.isLogManualIncludeLocation()==false拿不到
     */
    private Class callerClass;//调用类
    /**
     * logType==DefaultLogType.AOP和ActionLogConfig.isLogManualIncludeLocation()==false拿不到
     */
    private Integer callerLineNumber;//调用行数
    /**
     * ActionLogConfig.isLogManualIncludeLocation()==false拿不到
     */
    private String callerMethodName;//调用方法名

    public LogInfo() {
        this.parameters = new HashMap();
    }

    public LogType getLogType() {
        return logType;
    }

    protected LogInfo setLogType(LogType logType) {
        this.logType = logType;
        return this;
    }

    public String getToken() {
        return token;
    }

    protected LogInfo setToken(String token) {
        this.token = token;
        return this;
    }

    public String getFlow() {
        return flow;
    }

    protected LogInfo setFlow(String flow) {
        this.flow = flow;
        return this;
    }

    public String getAction() {
        return action;
    }

    protected LogInfo setAction(String action) {
        this.action = action;
        return this;
    }

    public HandleType getHandleType() {
        return handleType;
    }

    protected LogInfo setHandleType(HandleType handleType) {
        this.handleType = handleType;
        return this;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Object getResult() {
        return result;
    }

    protected LogInfo setResult(Object result) {
        this.result = result;
        return this;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    protected LogInfo setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    protected LogInfo setHierarchy(Integer hierarchy) {
        this.hierarchy = hierarchy;
        return this;
    }

    public Integer getSequence() {
        return sequence;
    }

    protected LogInfo setSequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    protected LogInfo setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
        return this;
    }

    public Class getCallerClass() {
        return callerClass;
    }

    protected LogInfo setCallerClass(Class callerClass) {
        this.callerClass = callerClass;
        return this;
    }

    public Integer getCallerLineNumber() {
        return callerLineNumber;
    }

    protected LogInfo setCallerLineNumber(Integer callerLineNumber) {
        this.callerLineNumber = callerLineNumber;
        return this;
    }

    public String getCallerMethodName() {
        return callerMethodName;
    }

    protected LogInfo setCallerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
        return this;
    }

    /**
     * 清空上下文
     * 内部使用,{@link LogFilter}实现内不要调用,否则后续操作可能空指针
     *
     * @return
     */
    public LogInfo clear() {
        this.logType = null;
        this.token = null;
        this.flow = null;
        this.action = null;
        this.handleType = null;
        this.parameters.clear();
        this.result = null;
        this.throwable = null;
        this.hierarchy = null;
        this.sequence = null;
        this.extendInfo = null;
        this.callerClass = null;
        this.callerLineNumber = null;
        this.callerMethodName = null;
        return this;
    }

}
