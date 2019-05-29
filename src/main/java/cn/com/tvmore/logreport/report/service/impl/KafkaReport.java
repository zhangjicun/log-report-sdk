package cn.com.tvmore.logreport.report.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.tvmore.logreport.constants.Constants;
import cn.com.tvmore.logreport.enums.KafkaTopic;
import cn.com.tvmore.logreport.enums.LogLevel;
import cn.com.tvmore.logreport.kafka.message.CustomLog;
import cn.com.tvmore.logreport.kafka.message.Data;
import cn.com.tvmore.logreport.properties.PropBank;
import cn.com.tvmore.logreport.report.KafkaReportTask;
import cn.com.tvmore.logreport.report.service.IReport;
import cn.com.tvmore.logreport.utils.ExceptionUtil;

public class KafkaReport implements IReport {
	
	@Override
	public void exception(String appName, Throwable t) {
		String msg = ExceptionUtil.printStackTrace(t);
		CustomLog log = createReportLog(appName, Constants.EXCEPTION_CODE, msg);
		
		KafkaReportTask.reportLog(log, KafkaTopic.TOPIC_REPORTLOG, LogLevel.EXCEPTION.getLevel(), appName);
	}

	@Override
	public void exception(Throwable t) {
		String msg = ExceptionUtil.printStackTrace(t);
		CustomLog log = createReportLog(null, Constants.EXCEPTION_CODE, msg);
		
		KafkaReportTask.reportLog(log, KafkaTopic.TOPIC_REPORTLOG, LogLevel.EXCEPTION.getLevel(), null);
	}

	@Override
	public void error(String appName, String sceneCode, String msg) {
		CustomLog log = createReportLog(appName, sceneCode, msg);
		/*log.setLevel(LogLevel.ERROR.getLevel());*/
		KafkaReportTask.reportLog(log, KafkaTopic.TOPIC_REPORTLOG, LogLevel.ERROR.getLevel(), appName);
	}

	@Override
	public void error(String sceneCode, String msg) {
		CustomLog log = createReportLog(null, sceneCode, msg);
		/*log.setLevel(LogLevel.ERROR.getLevel());*/
		KafkaReportTask.reportLog(log, KafkaTopic.TOPIC_REPORTLOG, LogLevel.ERROR.getLevel(), null);

	}

	@Override
	public void warn(String appName, String sceneCode, String msg) {
		CustomLog log = createReportLog(appName, sceneCode, msg);
		/*log.setLevel(LogLevel.WARN.getLevel());*/
		KafkaReportTask.reportLog(log, KafkaTopic.TOPIC_REPORTLOG, LogLevel.WARN.getLevel(), appName);
	}

	@Override
	public void warn(String sceneCode, String msg) {
		CustomLog log = createReportLog(null, sceneCode, msg);
		/*log.setLevel(LogLevel.WARN.getLevel());*/
		KafkaReportTask.reportLog(log, KafkaTopic.TOPIC_REPORTLOG, LogLevel.WARN.getLevel(), null);
	}

	@Override
	public void info(String appName, String sceneCode, String msg) {
		CustomLog log = createReportLog(appName, sceneCode, msg);
		/*log.setLevel(LogLevel.INFO.getLevel());*/
		KafkaReportTask.reportLog(log, KafkaTopic.TOPIC_REPORTLOG, LogLevel.INFO.getLevel(), appName);
	}

	@Override
	public void info(String sceneCode, String msg) {
		CustomLog log = createReportLog(null, sceneCode, msg);
		/*log.setLevel(LogLevel.INFO.getLevel());*/
		KafkaReportTask.reportLog(log, KafkaTopic.TOPIC_REPORTLOG, LogLevel.INFO.getLevel(), null);
	}
	
	private CustomLog createReportLog(String appName, String sceneCode, String msg) {
		CustomLog log = new CustomLog();
		/*if(appName == null) appName = PropBank.getProperty(Constants.PROP_APPNAME);*/
		log.setSdk_type(PropBank.getProperty(Constants.PROP_SDKTYPE));
		/*log.setApp(appName);*/
		int codeValue = 0;
		try {
			codeValue = Integer.parseInt(sceneCode);
		} catch (Exception e) {
		}
		/*log.setCode(Integer.parseInt(sceneCode));*/
		log.setCode(codeValue);
		Data data = new Data();
		data.setStatus(sceneCode);
		data.setMsg(msg);
		log.setData(JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue));
		log.setSdk_version(PropBank.getProperty(Constants.PROP_SDKVERSION));
		log.setRequest_time(System.currentTimeMillis()/1000);
		
		return log;
	}

}
