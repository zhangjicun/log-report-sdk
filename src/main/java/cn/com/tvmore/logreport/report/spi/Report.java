package cn.com.tvmore.logreport.report.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

import cn.com.tvmore.logreport.report.service.IReport;
import cn.com.tvmore.logreport.report.service.impl.SimpleReport;

public class Report {
	
	private static IReport report = null;
	
	/**
	 * Load report Service Provider
	 */
	static{
		ServiceLoader<IReport> load = ServiceLoader.load(IReport.class);
		Iterator<IReport> reports = load.iterator();
		
		if(reports.hasNext()) {
			report = reports.next();
		}
		
		if(report == null) report = new SimpleReport();
	}
	
	public static void exception(String appName, Throwable t) {
		report.exception(appName, t);
	}
	
	public static void exception(Throwable t) {
		report.exception(t);
	}
	
	public static void error(String appName, String sceneCode, String msg){
		report.error(appName, sceneCode, msg);
	}
	
	public static void error(String sceneCode, String msg){
		report.error(sceneCode, msg);
	}
	
	public static void warn(String appName, String sceneCode, String msg){
		report.warn(appName, sceneCode, msg);
	}
	
	public static void warn(String sceneCode, String msg){
		report.error(sceneCode, msg);
	}
	
	public static void info(String appName, String sceneCode, String msg){
		report.error(appName, sceneCode, msg);
	}
	
	public static void info(String sceneCode, String msg){
		report.error(sceneCode, msg);
	}
	
}
