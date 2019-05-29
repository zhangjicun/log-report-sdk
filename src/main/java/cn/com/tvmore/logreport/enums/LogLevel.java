package cn.com.tvmore.logreport.enums;

/**
 * report log level enum
 */
public enum LogLevel {
	EXCEPTION(0, "exception"),
	ERROR(1, "error"),
	WARN(2, "warn"),
	INFO(3, "info");
	
	LogLevel(int code, String level) {
		this.code = code;
		this.level = level;
	}
	
	private int code;
	private String level;
	
	public int getCode() {
		return code;
	}
	
	public String getLevel() {
		return level;
	}
}
