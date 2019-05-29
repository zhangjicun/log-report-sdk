package cn.com.tvmore.logreport.executors;

public enum TimeUnit {
	
	SECONDS("1",java.util.concurrent.TimeUnit.SECONDS),
	MILLISECONDS("2",java.util.concurrent.TimeUnit.MILLISECONDS);
	
	private String code;
	private java.util.concurrent.TimeUnit timeUnit;
	
	TimeUnit(String code, java.util.concurrent.TimeUnit timeUnit){
		this.code = code;
		this.timeUnit = timeUnit;
	}

	public String getCode() {
		return code;
	}

	public java.util.concurrent.TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
	public static java.util.concurrent.TimeUnit getTimeUnitByCode(String code) {
		for (TimeUnit aEnum : TimeUnit.values()) {
			if(aEnum.getCode().equals(code)) {
				return aEnum.getTimeUnit();
			}
		}
		return SECONDS.getTimeUnit();
	}

}
