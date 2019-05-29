package cn.com.tvmore.logreport.kafka.message;

import java.io.Serializable;

public class Data implements Serializable {
	private static final long serialVersionUID = -256067093394764914L;

	private String status;
	private String msg;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
