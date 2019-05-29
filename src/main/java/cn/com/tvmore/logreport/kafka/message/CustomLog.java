package cn.com.tvmore.logreport.kafka.message;

import java.io.Serializable;

import cn.com.tvmore.logreport.model.SystemInfo;

/**
 * Send to kafka CustomLog obj
 */
public class CustomLog implements Serializable {
	private static final long serialVersionUID = -2706201093528047748L;

	private String sdk_type;
	
	private Integer code;
	
	private String method = "";
	
	private String dst = "";
	
	private String data;
	
	private Integer time_stamp = 0;
	
	private String src = "";
	
	private String ip = "";
	
	private Integer rule_id = 1;
	
	private String path = "";
	
	private Long request_time;
	
	private String domain = "";
	
	private String sdk_version;
	
	private String time = "";
	
	private String parameters = "";
	
	private String host_name;
	
	private SystemInfo systeminfo;

	public String getSdk_version() {
		return sdk_version;
	}

	public void setSdk_version(String sdk_version) {
		this.sdk_version = sdk_version;
	}

	public String getSdk_type() {
		return sdk_type;
	}

	public void setSdk_type(String sdk_type) {
		this.sdk_type = sdk_type;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(Integer time_stamp) {
		this.time_stamp = time_stamp;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getRule_id() {
		return rule_id;
	}

	public void setRule_id(Integer rule_id) {
		this.rule_id = rule_id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getRequest_time() {
		return request_time;
	}

	public void setRequest_time(Long request_time) {
		this.request_time = request_time;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getHost_name() {
		return host_name;
	}

	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}

	public SystemInfo getSysteminfo() {
		return systeminfo;
	}

	public void setSysteminfo(SystemInfo systeminfo) {
		this.systeminfo = systeminfo;
	}
}
