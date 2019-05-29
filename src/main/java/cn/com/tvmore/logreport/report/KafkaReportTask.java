package cn.com.tvmore.logreport.report;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.tvmore.logreport.constants.Constants;
import cn.com.tvmore.logreport.enums.KafkaTopic;
import cn.com.tvmore.logreport.executors.Executors;
import cn.com.tvmore.logreport.kafka.KafkaSender;
import cn.com.tvmore.logreport.kafka.message.HttpLog;
import cn.com.tvmore.logreport.kafka.message.CustomLog;
import cn.com.tvmore.logreport.model.SystemInfo;
import cn.com.tvmore.logreport.properties.PropBank;

/**
 * AsyncTask for sent log to kafka
 */
public class KafkaReportTask {
	private static final Logger logger = LoggerFactory.getLogger(KafkaReportTask.class);
	
	private static SystemInfo info = new SystemInfo();
	private static String localIP = "";
	private static String host_name = "";
	
	/**
	 * Initial System Information
	 */
	static{
		Properties sysProperty = System.getProperties();
		info.setJdkVersion(sysProperty.getProperty("java.version"));
		info.setSystemName(sysProperty.getProperty("os.name"));
		info.setApp(PropBank.getProperty(Constants.PROP_APPNAME));
		/*InetAddress localHost = null;
		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
		info.setLocalHost(localHost==null?"":localHost.getHostAddress());*/
		String localHostAddress = "";
		String localHostName = "";
		try{
			Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			outterLoop: while( ifaces.hasMoreElements()) {
			      NetworkInterface iface = ifaces.nextElement();
			      Enumeration<InetAddress> addresses = iface.getInetAddresses();

			      while( addresses.hasMoreElements()) {
			        InetAddress addr = addresses.nextElement();
			        if( addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
			          localHostAddress = addr.getHostAddress();
			          localHostName = addr.getHostName();
			          break outterLoop;
			        }
			      }
			    }
			if(localHostAddress == null || "".equals(localHostAddress)) localHostAddress = InetAddress.getLocalHost().getHostName();
			if(localHostName == null || "".equals(localHostName)) localHostName = localHostAddress;
		} catch (Exception e) {
		}
		info.setLocalHost(localHostAddress);
		localIP = localHostAddress;
		/*host_name = sysProperty.getProperty("user.name");*/
		host_name = localHostName;
	}
	
	// ThreadPool for report log
	private static final ExecutorService customLogThreadPool = Executors.newThreadPool("report-log-task");
	// ThreadPool for http log
	private static final ExecutorService httpLogThreadPool = Executors.newThreadPool("http-log-task");
	
	/**
	 * Report custom log
	 * @param reportLog
	 * @param topic
	 */
	public static void reportLog(final CustomLog customLog, final KafkaTopic topic, final String level, final String appName) {
		customLogThreadPool.submit(new Runnable() {
			public void run() {
				report(customLog, topic, level, appName);
			}
		});
	}
	
	/**
	 * Report monitor log
	 * @param httpLog
	 * @param topic
	 */
	public static void reportHttpLog(final HttpLog httpLog, final KafkaTopic topic) {
		httpLogThreadPool.submit(new Runnable() {
			public void run() {
				report(httpLog, topic);
			}
		});
	}
	
	/**
	 * Do report for httpLog
	 * @param httpLog
	 * @param topic
	 */
	private static void report(final HttpLog httpLog, final KafkaTopic topic) {
		httpLog.setSysteminfo(info);
		httpLog.setDst(localIP);
		httpLog.setIp(localIP);
		httpLog.setHost_name(host_name);
		/*httpLog.setApp(PropBank.getProperty(Constants.PROP_APPNAME));*/
		httpLog.setSdk_version(PropBank.getProperty(Constants.PROP_SDKVERSION));
		httpLog.setSdk_type(PropBank.getProperty(Constants.PROP_SDKTYPE));
		String message = JSONObject.toJSONString(httpLog, SerializerFeature.WriteMapNullValue);
		logger.info("Send message to kafka[topic:{}, message:{}]", topic.getTopic(), message);
		
		KafkaSender.send(topic.getTopic(), message);
	}
	
	/**
	 * Do report for customLog
	 * @param reportLog
	 * @param topic
	 */
	private static void report(final CustomLog customLog, final KafkaTopic topic, final String level, final String appName) {
		if(appName != null) info.setApp(appName);
		info.setLevel(level);
		customLog.setSysteminfo(info);
		customLog.setDst(localIP);
		customLog.setIp(localIP);
		customLog.setHost_name(host_name);
		customLog.setDomain(PropBank.getProperty(Constants.PROP_APP_DOMAIN));
		String message = JSONObject.toJSONString(customLog, SerializerFeature.WriteMapNullValue);
		logger.info("Send message to kafka[topic:{}, message:{}]", topic.getTopic(), message);
		
		KafkaSender.send(topic.getTopic(), message);
	}
}
