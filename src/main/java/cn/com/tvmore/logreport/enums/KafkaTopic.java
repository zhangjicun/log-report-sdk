package cn.com.tvmore.logreport.enums;

import cn.com.tvmore.logreport.constants.Constants;
import cn.com.tvmore.logreport.properties.PropBank;

/**
 * send message to kafka topic enum
 * if send kafka message need key， extend this enum
 */
public enum KafkaTopic {
	TOPIC_HTTPLOG(PropBank.getProperty(Constants.PROP_TOPIC_HTTPLOG)),
	TOPIC_REPORTLOG(PropBank.getProperty(Constants.PROP_TOPIC_REPORTLOG));
	
	private String topic;
	
	KafkaTopic(String topic) {
		this.topic = topic;
	}
	
	public String getTopic() {
		return topic;
	}
}
