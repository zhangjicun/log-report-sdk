package cn.com.tvmore.logreport.executors;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *1->DiscardOldestPolicy;2->DiscardPolicy;3->AbortPolicy;4->CallerRunsPolicy
 */
public enum RejectedHandler {
	
	DISCARDOLDEST("1", new ThreadPoolExecutor.DiscardPolicy()),
	DISCARD("2", new ThreadPoolExecutor.DiscardPolicy()),
	ABORT("3", new ThreadPoolExecutor.AbortPolicy()),
	CALLERRUNS("4", new ThreadPoolExecutor.CallerRunsPolicy());
	
	private String code;
	private RejectedExecutionHandler handler;
	
	RejectedHandler(String code, RejectedExecutionHandler handler){
		this.code = code;
		this.handler = handler;
	}

	public String getCode() {
		return code;
	}

	public RejectedExecutionHandler getHandler() {
		return handler;
	}

	public static RejectedExecutionHandler getRejectedHandler(String code) {
		for (RejectedHandler aEnum : RejectedHandler.values()) {
			if(aEnum.getCode().equals(code)) {
				return aEnum.getHandler();
			}
		}
		return DISCARDOLDEST.getHandler();
	}
}
