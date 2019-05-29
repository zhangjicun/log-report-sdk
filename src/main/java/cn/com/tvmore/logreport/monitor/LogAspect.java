package cn.com.tvmore.logreport.monitor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.tvmore.logreport.constants.Constants;
import cn.com.tvmore.logreport.enums.KafkaTopic;
import cn.com.tvmore.logreport.kafka.message.HttpLog;
import cn.com.tvmore.logreport.properties.PropBank;
import cn.com.tvmore.logreport.report.KafkaReportTask;
import cn.com.tvmore.logreport.utils.HttpUtil;
//import javassist.ClassClassPath;
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtMethod;
//import javassist.Modifier;
//import javassist.NotFoundException;
//import javassist.bytecode.CodeAttribute;
//import javassist.bytecode.LocalVariableAttribute;
//import javassist.bytecode.MethodInfo;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Aspect for Controller(Http Reqeust and Response)[Can only be used by SpringMVC.]
 */
@Aspect
@Component
public class LogAspect {

    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private static final String URI_ERROR = "/error";
    
    /**
     * 定义日志切入点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
//    @Pointcut("@annotation(cn.com.tvmore.logreport.annotation.LogMonitor)")
    public void logAspect() {}

    @Around("logAspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        Object result = null;
        if(attributes == null) {
        	result = point.proceed(args);
        } else {
        	HttpServletRequest request = attributes.getRequest();
        	
            /*ReqInfo reqInfo = HttpUtil.createReqInfo(request);*/
            LogInfo logInfo = new LogInfo(point, request);
        	/*reqInfo.setParams(logInfo.nameAndArgs);*/
            logInfo.beforeLog();
            
            result = point.proceed(args);
            
            String result_JSON = JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue);
            logInfo.afterLog(result_JSON);
            
            /*HttpLog httpLog = new HttpLog(reqInfo, result);*/
            HttpLog httpLog = HttpUtil.createHttpLog(request);
            /*httpLog.getReq().setParams(logInfo.nameAndArgs);*/
            /*httpLog.setParameters(mapToStringParameters(logInfo.nameAndArgs));*/
            httpLog.setParameters(getQueryString(request));
            httpLog.setData(result_JSON);
            
            httpLog.setTime(logInfo.getCostMilliseconds() + "ms");
            
            if(needReport(result_JSON, httpLog)){
            	KafkaReportTask.reportHttpLog(httpLog, KafkaTopic.TOPIC_HTTPLOG);
            } else {
            	//请求响应超时  上报 日志
            	if(logInfo.getCostMilliseconds() > Long.parseLong(PropBank.getProperty(Constants.PROP_MONITOR_RESPONSE_TIMEOUT))) {
            		logger.info(">>>>>>>>>http response timeout<<<<<<<<<<");
            		httpLog.setCode(Constants.RESPONSE_TIMEOUT_CODE); // 请求响应超时
            		KafkaReportTask.reportHttpLog(httpLog, KafkaTopic.TOPIC_HTTPLOG);
            	}
            }
        }
        
        return result;
    }
    
    /** 根据返回的Code码判断是否需要上报日志 */
    private boolean needReport(String result, HttpLog httpLog) {
    	String monitorCodes = PropBank.getProperty(Constants.PROP_MONITORCODES);
    	logger.debug("log.report.monitor.codes={}", monitorCodes);
    	/*if("all".equals(monitorCodes)) return true;*/
    	if(monitorCodes == null || "".equals(monitorCodes)) return false;
    	
    	String[] codes = monitorCodes.split(",");
    	List<String> codesList = Arrays.asList(codes);
    	
    	JSONObject obj = null;
    	try {
    		obj = JSONObject.parseObject(result);
		} catch (Exception e) {
			logger.error("JSONObject.parseObject({}) --> ERROR: [{}]",result, e.getMessage());
		}
    	
    	String field = PropBank.getProperty(Constants.PROP_MONITORFIELD);
    	/*logger.debug("log.report.monitor.field={}", field);*/
    	if(obj == null || obj.getString(field) == null) {
    		logger.debug("Got [{}] value from Response was [{}]", field, null);
    		return false; 
    	}
    	
    	String code = obj.getString(field);
    	logger.debug("Got [{}] value from Response was [{}]", field, code);
    	if("all".equals(monitorCodes) || codesList.contains(code)) {
    		String excludeCodes = PropBank.getProperty(Constants.PROP_MONITOR_EXCLUDE_CODES);
    		if(excludeCodes != null && !"".equals(excludeCodes)){
    			String[] excludes = excludeCodes.split(",");
    			List<String> excludeList = Arrays.asList(excludes);
    			if(excludeList.contains(code)) return false;
    		}
    		int codeValue = 0;
    		try {
    			codeValue = Integer.parseInt(code);
    		} catch (Exception e) {
    			logger.warn("report.monitor.code is not numeric string {}", code);
    		}
    		httpLog.setCode(codeValue);
    		return true;
    	}
    	
    	return false;
    }
    
    private String getQueryString(HttpServletRequest request) {
    	if("GET".equals(request.getMethod())) return request.getQueryString();
    	if("POST".equals(request.getMethod())) {
    		StringBuilder sb = new StringBuilder();
    		@SuppressWarnings("unchecked")
			Map<String, String[]> map =  request.getParameterMap();
    		for (Entry<String, String[]> entry : map.entrySet()) {
    			for (String value : entry.getValue()) {
    				sb.append(entry.getKey()).append("=").append(value).append("&");
				}
			}
    		
    		String queryString = sb.toString();
    		if(queryString == null || queryString.length() == 0) return queryString; 
    		return queryString.substring(0, queryString.length() -1);
    	}
    	return "";
    }
    
    /*private String mapToStringParameters(Map<String, Object> args) {
    	StringBuilder sb = new StringBuilder();
    	for (Entry<String, Object> entry : args.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
    	String result = sb.toString();
    	if(result == null || !result.contains("&")) return result;
    	return sb.toString().substring(0, sb.lastIndexOf("&"));
    }*/

	/**
     * 日志信息
     */
    public class LogInfo {
        @SuppressWarnings("rawtypes")
		private Class clazz;
        private String methodName;
        private String uri;
        private Object[] params;
        /*private Map<String, Object> nameAndArgs;*/
        private String method;
        private Object result;
        private long beginTime;
        private long costMilliseconds;

        public LogInfo(JoinPoint point, HttpServletRequest request) {
            this.clazz  = point.getTarget().getClass();
            this.methodName = point.getSignature().getName();
            this.uri        = request.getRequestURI();
            this.method     = request.getMethod();
            this.params     = point.getArgs();
            //获取参数名称和值  
            /*try {
				nameAndArgs = this.getFieldsName(this.getClass(), clazz.getName(), this.methodName, this.params);
			} catch (NotFoundException e) {
				nameAndArgs = null;
			}*/ 
            this.beginTime  = System.currentTimeMillis();
        }
        
        /*@SuppressWarnings("rawtypes")
		private Map<String,Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws NotFoundException {
            Map<String,Object > map=new HashMap<String,Object>();
            
            ClassPool pool = ClassPool.getDefault();
            //ClassClassPath classPath = new ClassClassPath(this.getClass());
            ClassClassPath classPath = new ClassClassPath(cls);
            pool.insertClassPath(classPath);
                
            CtClass cc = pool.get(clazzName);
            CtMethod cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);    
            if (attr == null) {
                // exception
            }
           // String[] paramNames = new String[cm.getParameterTypes().length];
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < cm.getParameterTypes().length; i++){
                map.put( attr.variableName(i + pos),args[i]);//paramNames即参数名
            }
              
            //Map<>
            return map;
        }*/

        public String getClazzName() {
            return clazz.getSimpleName();
        }

        @SuppressWarnings("rawtypes")
		public Class getClazz() {
            return clazz;
        }

        @SuppressWarnings("rawtypes")
		public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getParams() {
            return Arrays.toString(params);
        }

        public void setParams(Object[] params) {
            this.params = params;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public long getCostMilliseconds() {
            return costMilliseconds;
        }

        public void setCostMilliseconds(long costMilliseconds) {
            this.costMilliseconds = costMilliseconds;
        }

        public void beforeLog() {
            if (URI_ERROR.equals(uri)) {
                return;
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append("LOG [Request  ][").append(uri).append("][").append(method).append("] ");
            buffer.append(getClazzName()).append(".").append(methodName);
            buffer.append("() 参数:").append(getParams());
            logger.info(buffer.toString());
        }

        public void afterLog(Object result) {
            if (URI_ERROR.equals(uri)) {
                return;
            }
            setResult(result);
            setCostMilliseconds(System.currentTimeMillis() - getBeginTime());
            StringBuffer buffer = new StringBuffer();
            buffer.append("LOG [Response ][").append(uri).append("][").append(method).append("] ");
            buffer.append(getClazzName()).append(".").append(methodName);
            /*buffer.append("() 响应结果：").append(result).append(" 耗时：").append(costMilliseconds).append("ms");*/
            buffer.append("() 响应结果：").append(JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue)).append(" 耗时：").append(costMilliseconds).append("ms");
            logger.info(buffer.toString());
        }
        
        public void afterLog(String result) {
        	if (URI_ERROR.equals(uri)) {
                return;
            }
        	setCostMilliseconds(System.currentTimeMillis() - getBeginTime());
            StringBuffer buffer = new StringBuffer();
            buffer.append("LOG [Response ][").append(uri).append("][").append(method).append("] ");
            buffer.append(getClazzName()).append(".").append(methodName);
            buffer.append("() 响应结果：").append(result).append(" 耗时：").append(costMilliseconds).append("ms");
            logger.info(buffer.toString());
        }

        public void throwLog(String message, Throwable e) {
            setCostMilliseconds(System.currentTimeMillis() - getBeginTime());
            StringBuffer buffer = new StringBuffer();
            buffer.append("LOG [Exception][").append(uri).append("][").append(method).append("] ");
            buffer.append(getClazzName()).append(".").append(methodName);
            buffer.append("() 参数:").append(getParams());
            buffer.append(" 耗时：").append(costMilliseconds).append("ms");
            buffer.append(" 异常信息：").append(message);
            logger.error(buffer.toString(), e);
        }
    }

}
