package cn.com.tvmore.logreport.model;

import java.io.Serializable;

import cn.com.tvmore.logreport.enums.LogLevel;

/**
 * System info sent to kafka
 */
public class SystemInfo implements Serializable {
	private static final long serialVersionUID = -1053554122269638022L;

	private String jdkVersion = "";
	private String systemName = "";
	private String localHost = "0.0.0.0";
	private String app = "";
	private String level = LogLevel.INFO.getLevel();

	public String getJdkVersion() {
		return jdkVersion;
	}

	public void setJdkVersion(String jdkVersion) {
		this.jdkVersion = jdkVersion;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getLocalHost() {
		return localHost;
	}

	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{jdkVersion:").append(jdkVersion).append("; ");
		buffer.append("systemName:").append(systemName).append("; ");
		buffer.append("localHost:").append(localHost).append("}");
		return buffer.toString();
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
