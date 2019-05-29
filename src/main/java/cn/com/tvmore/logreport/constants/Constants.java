package cn.com.tvmore.logreport.constants;

public interface Constants {
	/** common properties */
	String PROP_SDKTYPE = "log.report.sdktype";
	String PROP_APPNAME = "log.report.app-name";
	String PROP_SDKVERSION = "log.report.sdkversion";
	String PROP_MONITORFIELD = "log.report.monitor.field";
	String PROP_MONITORCODES = "log.report.monitor.codes";
	String PROP_MONITOR_EXCLUDE_CODES = "log.report.monitor.exclude.codes";
	String PROP_MONITOR_RESPONSE_TIMEOUT = "log.report.monitor.response.timeout";
	
	/** executors properties */
	String PROP_EXECUTORS_CORESIZE_KEY = "log.report.executors.core-size";
	String PROP_EXECUTORS_MAXSIZE_KEY = "log.report.executors.max-size";
	String PROP_EXECUTORS_ALIVETIME_KEY = "log.report.executors.alive-time";
	String PROP_EXECUTORS_TIMEUNIT_KEY = "log.report.executors.time-unit";
	String PROP_EXECUTORS_QUEUECAPACITY_KEY = "log.report.executors.queue-capacity";
	String PROP_EXECUTORS_REJECTEDEXECUTIONHANDLER_KEY = "log.report.executors.RejectedExecutionHandler";
	
	/** kafka properties */
	String PROP_KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap.servers";
	String PROP_KAFKA_ACKS = "kafka.acks";
	String PROP_KAFKA_RETRIES = "kafka.retries";
	String PROP_KAFKA_BATCHSIZE = "kafka.batch.size";
    String PROP_KAFKA_LINGERMS = "kafka.linger.ms";
    String PROP_KAFKA_BUUFFER = "kafka.buffer.memory";
    
    String PROP_TOPIC_HTTPLOG = "kafka.topic.httplog";
    String PROP_TOPIC_REPORTLOG = "kafka.topic.reportlog";
    
    String PROP_APP_DOMAIN = "log.report.app-domain";
    
    String EXCEPTION_CODE = "500";
    
    int RESPONSE_TIMEOUT_CODE = 408;
}
