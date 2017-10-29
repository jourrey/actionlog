
# Actionlog使用说明

### 1 概述
本文档主要记录具体业务端如何使用actionlog提供的工具及服务。
为了更好的理解文档，先了解几个概念：
+ HandleType：总共支持PARAM, RETURN, CATCH, THROW, FINALLY，五种类型，标记日志类型；
+ ActionLogKey：主要由两部分组成Flow和Action，用来表示一组业务日志；
+ Flow：用来获取业务流，体现形式是文件夹；
+ Action：用来获取业务流的具体节点，体现形式是文件。
+ LogLevel：总共支持DEBUG, INFO, WARN, ERROR, OFF，五个级别，控制日志打印级别；<br/>
***Flow＋Action＋LogLevel用来确定一个唯一的日志文件；***<br/>
***举个例子***<br/>
Flow＝order、Action＝create、LogLevel＝INFO，则日志文件为"logHomePath/order/create_INFO.log"的方式。（logHomePath为日志跟路径，可配置）<br/>
***注意：只会对应生成INFO, WARN, ERROR3个级别的日志文件，DEBUG是需要通过设置打印到Console的，OFF是关闭打印日志。***

### 2 使用actionlog
actionlog提供了API和Annotation两种方式，它们都是跟业务无关的。
#### 2.1 pom.xml配置
在你的pom.xml中加入如下dependency依赖配置：
```xml
<!-- start actionlog -->
<dependency>
    <groupId>com.dianping.orderdish</groupId>
    <artifactId>actionlog</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
<!-- end actionlog -->
```
#### 2.2 参数配置
参数配置均通过Lion实现
以下配置中，AppName代表项目中“META-INF/app.properties”内的值
##### 2.2.1 日志配置
[参考文档](http://logging.apache.org/log4j/2.x/)
<table border="1">
  <tr>
    <th>key</th>
    <th>类型</th>
    <th>默认值</th>
    <th>可选值</th>
    <th>意义</th>
  </tr>
  <tr>
    <td>AppName.ActionLog.Log.ConversionPattern</td>
    <td>String</td>
    <td>"%d{yyyy-MM-dd HH:mm:ss.SSS z} %-5level [%t] %msg%xEx%n"</td>
    <td>Log4J2支持的日志的打印格式</td>
    <td>具体看参考文档</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Log.HomePath</td>
    <td>String</td>
    <td>"/data/applogs"</td>
    <td>必须存在且有读写权限的目录</td>
    <td>日志打印根目录</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Log.File.MaxSize</td>
    <td>String</td>
    <td>"50M"</td>
    <td>Log4J2支持的单个日志文件的最大小</td>
    <td>具体看参考文档</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Log.File.MaxNum</td>
    <td>String</td>
    <td>"100"</td>
    <td>Log4J2支持的同名日志文件的最大保留数量</td>
    <td>具体看参考文档</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Log.File.TimeInterval</td>
    <td>String</td>
    <td>"24"</td>
    <td>Log4J2支持的单个日志文件的最大时间间隔</td>
    <td>具体看参考文档</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Log.File.TimeIntervalModulate</td>
    <td>String</td>
    <td>"true"</td>
    <td>"true"、"false"</td>
    <td>单个日志文件的最大时间间隔起始时间,"true"表示0点开始，“false”表示日志文件创建时间。</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Log.Appender.ImmediateFlush</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>是否立刻刷新日志到磁盘，建议不要，会影响性能50%左右</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Logger.Additive</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>是否将日志向上一级Logger传递，建议不要，会使性能降至1%左右，主要用来非线上环境配合DEBUG模式，调试使用。</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Logger.IncludeLocation</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>是否使用Log4J2的定位信息，建议不要，会使性能降至20%左右</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Logger.LogLevel</td>
    <td>String</td>
    <td>"INFO"</td>
    <td>"DEBUG"、"INFO"、"WARN"、"ERROR"、"OFF"</td>
    <td>日志级别，只打印此等级及更高级别日志。todo（后续可能需要对单独的flow和action设置，但现在不支持）</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.LogManual.IncludeLocation</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>是否使用ActionLog的定位信息，建议不要，会使性能降至50%左右</td>
  </tr>
</table>
##### 2.2.2	说明
默认配置，基本不用更改，即可直接使用。常用配置LogLevel、Additive、LogManual.IncludeLocation、ImmediateFlush
##### 2.2.3 CAT监控配置
<table border="1">
  <tr>
    <th>key</th>
    <th>类型</th>
    <th>默认值</th>
    <th>可选值</th>
    <th>意义</th>
  </tr>
  <tr>
    <td>AppName.ActionLog.Cat.Transaction.Switch</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>具体看参考文档</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Cat.Event.Switch</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>具体看参考文档</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Cat.MetricForCount.Switch</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>具体看参考文档</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Cat.MetricForDuration.Switch</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>具体看参考文档</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Cat.MetricForDuration.SlowThreshold</td>
    <td>String</td>
    <td>"10000"</td>
    <td>数字类型，单位：毫秒</td>
    <td>超时阈值，默认10秒，意味着，不启动超时预警，各业务线自行控制。todo（后续可能需要对单独的flow和action设置，但现在不支持）具体看参考文档</td>
  </tr>
</table>
##### 2.2.4	说明
+ Transaction和MetricForDuration仅支持Annotation方式，否则开启也不生效。
为了更好的监控项目，在不要求极高性能的前提下（当前普通业务线显然都是可以接受的，因为现阶段要求，稳定性和安全性，高于速度），通常建议全部开启。
推荐开启
+ Event：用来添加告警监控和成功率；
+ MetricForCount：统计节点的使用情况，基于报表，例如灰度功能；
尽量开启
+ Transaction：耗时分析。
+ MetricForDuration：耗时分析，基于报表。
##### 2.2.5 监听机制打印配置
<table border="1">
  <tr>
    <th>key</th>
    <th>类型</th>
    <th>默认值</th>
    <th>可选值</th>
    <th>意义</th>
  </tr>
  <tr>
    <td>AppName.ActionLog.Filter.Log.Switch</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>ActionLog Filter是否打印日志，由于现阶段多数项目使用Struts2，有时候找不到Action，那么请求不会被记录，开启此项配置可以记录访问的请求，但需要单独配置Filter，具体查看ServletActionLogFilter配置。</td>
  </tr>
  <tr>
    <td>AppName.ActionLog.Pigeon.Log.Switch</td>
    <td>String</td>
    <td>"false"</td>
    <td>"true"、"false"</td>
    <td>ActionLog Pigeon是否打印日志，监听Pigeon接口的请求和服务，并打印INFO级别日志，建议不开启，打印信息太多。</td>
  </tr>
</table>
##### 2.2.6	说明
默认配置，基本不用更改；如有需要可以开启Filter，但如无必要，请不要开启Pigeon。
#### 2.3 API介绍
API打点方法，全部使用com.dianping.actionlog.advice.LogManual类承载。
•	debug
```java
public static void debug(java.lang.String message, java.lang.Object... params)
```
打印debug级别日志,将日志输出到默认路径DefaultActionLogKey
参数:message - 日志信息  params - 打印参数

•	info
```java
public static void info(com.dianping.actionlog.common.ActionLogKey key,         com.dianping.actionlog.advice.HandleType handleType,         java.lang.String message,         java.lang.Object... params)
```
打印info级别日志
参数:key - 日志标记  handleType - 操作类型   message - 日志信息   params - 打印参数

•	info
```java
public static void info(com.dianping.actionlog.common.ActionLogKey key,         com.dianping.actionlog.advice.HandleType handleType,         java.lang.String extendInfo,         java.lang.String message,         java.lang.Object... params)
```
打印info级别日志
参数:key - 日志标记  handleType - 操作类型  extendInfo - 扩展信息  message - 日志信息  params - 打印参数

•	warn
```java
public static void warn(com.dianping.actionlog.common.ActionLogKey key,         com.dianping.actionlog.advice.HandleType handleType,         java.lang.String message,         java.lang.Object... params)
```
打印warn级别日志
参数:key - 日志标记  handleType - 操作类型   message - 日志信息  params - 打印参数

•	warn
```java
public static void warn(com.dianping.actionlog.common.ActionLogKey key,         com.dianping.actionlog.advice.HandleType handleType,         java.lang.String extendInfo,         java.lang.String message,         java.lang.Object... params)
```
打印warn级别日志
参数:key - 日志标记  handleType - 操作类型  extendInfo - 扩展信息  message - 日志信息  params - 打印参数

•	error
```java
public static void error(com.dianping.actionlog.common.ActionLogKey key,          java.lang.String message,          java.lang.Object... params)
```
打印error级别日志
参数:key - 日志标记  message - 日志信息  params - 打印参数

•	error
```java
public static void error(com.dianping.actionlog.common.ActionLogKey key,          java.lang.String extendInfo,          java.lang.String message,          java.lang.Object... params)
```
打印error级别日志
参数:key - 日志标记  extendInfo - 扩展信息  message - 日志信息  params - 打印参数
##### 2.3.1	参数说明
Message：Log4j2标准message。
Params: Log4j2标准参数。
##### 2.3.2	ActionLogKey说明
使用ActionLogKey,而不是String flow,String action,目的是,期望这些业务流是固定的,且配置在一个地方；
另外一点是,传一个参数总比两个容易,减少错误的可能性。
各自业务线需要定义自己的ActionLogKey，例子如下
```java
public enum MySelfActionLogKey implements ActionLogKey {
    ORDER_CREATE("order","create"),
    ORDER_PAY("order","pay");

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

    MySelfActionLogKey(String flow, String action) {
        this.flow = flow;
        this.action = action;
    }
}
```
#### 2.4 Annotation介绍
##### 2.4.1	使用方式
在你的项目web.xml中添加如下配置：
```xml
<listener>
    <listener-class>com.dianping.actionlog.context.init.ActionLogLoaderListener</listener-class>
</listener>

<!--ActionLog应用包跟路径-->
<context-param>
 <param-name>shadowBasePackage</param-name>
 <param-value>com.dianping.hobbit</param-value>
</context-param>
```
shadowBasePackage为必选项，值是Annotation，生效的最小包路径
##### 2.4.2 注解说明
###### @ActionLog
使用在方法和类上，对当前标记的方法和标记的类中的所有方法（基于1.0的实现，目前实际只能对public方法）有效。
注解有四个配置项
+ flow：用来获取业务流，就是ActionLogKey的Flow；
+ action：用来获取业务流的具体节点，就是ActionLogKey的Action；
+ behaviorType:日志打印行为，是否打日志；
+ extendInfo:扩展信息。

###### @InheritedActionLog
跟@ActionLog基本一样，不同的是可以被继承，深度优先遍历,优先级:自己,接口,注解,父类
1.被表述对象自己，在内部查找，如果没有执行2
2.在接口中查找，包含间接接口，直至最顶层，如果没有执行3
3.在注解中查找，包含注解的注解（非java.lang包下），直至最顶层，如果没有执行4
4.在父类中查找，包含间接父类，直至最顶层，如果没有，那就真没有
##### 2.4.3 注解使用限制
目前注解，仅支持两个框架Struts2和Spring。todo（未来版本支持更多，下一个迭代，AspectJ）
###### 2.4.3.1 Struts2 Interceptor 支持Annotation配置
你只需要在你的Struts2配置文件中配置AspectStruts2Interceptor即可，方式如下：
```xml
<interceptors>
    <interceptor name="aspectStruts2Interceptor" class="com.dianping.shadow.aspect.struts2.AspectStruts2Interceptor"></interceptor>
    <interceptor-stack name="myStack">
    <interceptor-ref name="aspectStruts2Interceptor"></interceptor-ref>
    <interceptor-ref name="defaultStack" />
    </interceptor-stack>
</interceptors>
<default-interceptor-ref name="myStack" />
```
值得特别注意的是，AspectStruts2Interceptor要配置在最后一个，否则按照Struts2的interceptor执行原理，可能直接避开了应答日志监控程序。
###### 2.4.3.2 Spring Interceptor 支持Annotation配置
这个切面需要手动设置,否则会有一些问题.例如设置 @Pointcut("execution(* *..*.*(..))") 时,被覆盖的类,Spring会为其构建一个Bean.
此时,如若这个类没有实现任意 接口 且又是 final 的,那么Bean会构建失败,导致应用无法启动,异常如下
org.springframework.aop.framework.AopConfigException: Could not generate CGLIB subclass of class [class xxx.xxx.xxx]

这里提供两个模版
+ 1.基于注解
```xml
<aop:aspectj-autoproxy/>
<bean class="com.dianping.shadow.aspect.spring.AnnotationAspectSpringInterceptor" />
```
+ 2.基于XML
```xml
<!-- 日志拦截声明bean ，此bean作为切面类使用  -->
<bean id="logInterceptor" class="com.dianping.shadow.aspect.spring.AspectSpringInterceptor"/>
<aop:config>
    <!-- 设置切面名，及切面类 -->
    <aop:aspect id="logAspect" ref="logInterceptor">
        <!-- 先设置切入点，待使用  -->
        <aop:pointcut id="logPointcut" expression="execution(public * com.dianping..service..*.*(..))"/>
        <!-- 运行前后方法配置，选择要执行的方法，参考预先设置好的切入点  -->
        <aop:around method="advice" pointcut-ref="logPointcut"/>
    </aop:aspect>
</aop:config>
```

###### 2.4.3.3 特别需要注意的是这里只能使用```<aop:around></aop:around>```
### 3 ServletActionLogFilter
#### 3.1 配置
在你的项目web.xml中添加如下配置即可：
```xml
<!-- actionlog filter -->
<filter>
    <filter-name>actionLogFilter</filter-name>
    <filter-class>com.dianping.actionlog.aspect.filter.ServletActionLogFilter</filter-class>
    <init-param>
        <param-name>bizStampName</param-name>
        <param-value>uuid</param-value>
    </init-param>
</filter>

<filter-mapping>
    <filter-name>actionLogFilter</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
    <dispatcher>FORWARD</dispatcher>
</filter-mapping>
```
bizStampName是业务系统指定的requestId，属于一个标记，可以根据这个标记，找到请求全链路信息。（日志染色）
***注意：*** <br/>
actionLogFilter，最好配置在前面，防止被其它Filter过滤掉，但最好在org.springframework.web.filter.CharacterEncodingFilter之后，避免Http乱码，导致日志乱码。

### 4 LogFilter
actionLog提供Filter机制，可以在日志打印之前，做一些操作，返回true继续执行，false停止并返回。举个例子，例如某些权限接口，一些高密级信息不能被打印出来，可以在这个接口做特定处理。
#### 4.1 实现
实现com.dianping.actionlog.advice.filter.LogFilter接口即可
#### 4.2 配置
项目根目录“src/main”下，创建“META-INF/services/”目录，再创建“com.dianping.actionlog.advice.filter.LogFilter”名称的文件，文件内容是自定义Filter实现的全类名，一行一个，按顺序存放。
实现方式，请参考java的ServiceLoader
