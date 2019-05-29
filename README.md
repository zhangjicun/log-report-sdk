# log-report-sdk
日志处理/上报，默认对接kafka上报日志，可根据spi自定义对接上报逻辑

# 构建/发布sdk包到本地/内网 中央库
clean install / clean deploy

# 上报方式
## 1.HTTP日志上报（Controller日志）
**描述：**  
&emsp;基于切面的方式，监控业务代码“请求/返回”JSON日志  
&emsp;根据返回JSON对象的返回字段、返回码  决定是否上报Controller日志  
&emsp;默认返回字段“status”（支持配置）  
&emsp;默认返回码“all”  全部上报（支持配置）  

**接入方式：**  
（1）基于配置  
&emsp;引入配置文件 classpath:application-log.xml  
（2）基于注解  
&emsp;添加包扫描  cn.com.tvmore.logreport.monitor

**切入逻辑：**  
&emsp;切入所有被@RequestMapping 注解的方法（可自定义注解用于切入）

## 2.主动日志上报
**描述：**  
&emsp;业务系统可根据情况，调用上报接口  
&emsp;主动上报业务日志  
&emsp;cn.com.tvmore.logreport.report.spi.Report

**接口类别：**  
（1）上报普通信息日志 Report.info  
&emsp;Report.info(String code, String msg)  
&emsp;Report.info(String appName, String code, String msg)  
&emsp;appName:业务系统名；code: 业务码；msg:日志信息  
（2）上报警告日志 Report.warn  
&emsp;Report.warn(String code, String msg)  
&emsp;Report.warn(String appName, String code, String msg)  
&emsp;appName:业务系统名；code: 业务码；msg:日志信息  
（3）上报错误日志 Report.error  
&emsp;Report.error(String code, String msg)  
&emsp;Report.error(String appName, String code, String msg)  
&emsp;appName:业务系统名；code: 业务码；msg:日志信息  
（4）上报异常信息 Report.excepiton  
&emsp;Report.exception(Throwable t)  
&emsp;Report.exception(String appName, Throwable t)  
&emsp;appName:业务系统名；t:异常对象  
  
# 配置文件
***业务系统需要新增配置文件  log-report.properties

**内容：**  
&emsp;log.report.app-name= #上报日志的业务系统名  
&emsp;log.report.app-domain= #业务系统域名  

&emsp;log.report.monitor.field=status   
&emsp;log.report.monitor.codes=all  
&emsp;log.report.monitor.exclude.codes=200,401,102  

&emsp;kafka.bootstrap.servers=XX.XX.XXX.XXX:XXXX,XX.XX.XXX.XXX:XXXX,XX.XX.XXX.XXX:XXXX
