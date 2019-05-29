package cn.com.tvmore.logreport.report.service;

/**
 * Report Custom log
 */
public interface IReport {
	
	void exception(String appName, Throwable t);
	void exception(Throwable t);
	
	void error(String appName, String sceneCode, String msg);
	void error(String sceneCode, String msg);
	
	void warn(String appName, String sceneCode, String msg);
	void warn(String sceneCode, String msg);
	
	void info(String appName, String sceneCode, String msg);
	void info(String sceneCode, String msg);
}
