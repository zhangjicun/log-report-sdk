package cn.com.tvmore.logreport.report.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tvmore.logreport.report.service.IReport;
import cn.com.tvmore.logreport.utils.ExceptionUtil;

public class SimpleReport implements IReport {
	private static final Logger logger = LoggerFactory.getLogger(SimpleReport.class);
	
	@Override
	public void exception(String appName, Throwable t) {
		logger.info("SimpleReport.exception --> [appName:{}, exception:{}]", appName, ExceptionUtil.printStackTrace(t));
	}

	@Override
	public void exception(Throwable t) {
		logger.info("SimpleReport.exception --> [appName:{},exception:{}]", null, ExceptionUtil.printStackTrace(t));
	}

	@Override
	public void error(String appName, String sceneCode, String msg) {
		logger.info("SimpleReport-->[{},{},{}]",appName, sceneCode, msg);
	}

	@Override
	public void error(String sceneCode, String msg) {
		logger.info("SimpleReport-->[{},{},{}]",null, sceneCode, msg);
	}

	@Override
	public void warn(String appName, String sceneCode, String msg) {
		logger.info("SimpleReport-->[{},{},{}]",appName, sceneCode, msg);
	}

	@Override
	public void warn(String sceneCode, String msg) {
		logger.info("SimpleReport-->[{},{},{}]",null, sceneCode, msg);
	}

	@Override
	public void info(String appName, String sceneCode, String msg) {
		logger.info("SimpleReport-->[{},{},{}]",appName, sceneCode, msg);
	}

	@Override
	public void info(String sceneCode, String msg) {
		logger.info("SimpleReport-->[{},{},{}]",null, sceneCode, msg);
	}
}
