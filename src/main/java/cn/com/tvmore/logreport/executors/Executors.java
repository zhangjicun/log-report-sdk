package cn.com.tvmore.logreport.executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import cn.com.tvmore.logreport.constants.Constants;
import cn.com.tvmore.logreport.properties.PropBank;

/**
 * Custom thread pool(Executors.newThreadPool)
 */
public class Executors {
	private static final Logger logger = LoggerFactory.getLogger(Executors.class);
	
	private static final int CORE_SIZE = Integer.parseInt(PropBank.getProperty(Constants.PROP_EXECUTORS_CORESIZE_KEY,"10"));
	
	private static final int MAX_SIEZE = Integer.parseInt(PropBank.getProperty(Constants.PROP_EXECUTORS_MAXSIZE_KEY,"500"));
	
	private static final TimeUnit TIME_UNIT = cn.com.tvmore.logreport.executors.TimeUnit.getTimeUnitByCode(PropBank.getProperty(Constants.PROP_EXECUTORS_TIMEUNIT_KEY,"1"));
	
	private static final long ALIVE_TIME = Long.parseLong(PropBank.getProperty(Constants.PROP_EXECUTORS_ALIVETIME_KEY,"10"));
	
	private static final int QUEUE_CAPACITY = Integer.parseInt(PropBank.getProperty(Constants.PROP_EXECUTORS_QUEUECAPACITY_KEY,"1000"));
	
	private static final RejectedExecutionHandler REJECTED_HANDLER = RejectedHandler.getRejectedHandler(PropBank.getProperty(Constants.PROP_EXECUTORS_REJECTEDEXECUTIONHANDLER_KEY,"1"));
	
	public static ExecutorService newThreadPool(String name) {
		return newThreadPool(CORE_SIZE, MAX_SIEZE, ALIVE_TIME, QUEUE_CAPACITY, name);
	}
	
	public static ExecutorService newThreadPool(int coreSize, int maxSize, long time, int queueCapacity, String name) {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(coreSize, maxSize, time, TIME_UNIT, new ArrayBlockingQueue<Runnable>(queueCapacity), 
				new ThreadFactoryBuilder().setDaemon(true).setNameFormat("pool-" + name + "-%d").setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
					public void uncaughtException(Thread t, Throwable e) {
						logger.error("Handler Uncaugth Exception in thread:{}, Error:{}", t.getName(), e);
					}
				}).build());
		pool.setRejectedExecutionHandler(REJECTED_HANDLER);
		return pool;
	}
}
