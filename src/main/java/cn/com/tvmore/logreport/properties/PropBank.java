package cn.com.tvmore.logreport.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Load properties from file[log-report.properties]
 */
public class PropBank {
	private static final Logger logger = LoggerFactory.getLogger(PropBank.class);
	
	private static final String PROP_FILE_NAME = "log-report.properties";
	
	private static Properties props;
	
	static{
		loadProps();
	}
	
	synchronized static private void loadProps() {
		logger.info("Start loading properties file:[{}]...", PROP_FILE_NAME);
		props = new Properties();
		InputStream class_in = null;
		InputStream classLoader_in = null;
		
		try {
			class_in = PropBank.class.getResourceAsStream(PROP_FILE_NAME);
			classLoader_in = PropBank.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME);
			if(class_in != null) props.load(class_in);
			if(classLoader_in != null) props.load(classLoader_in);
		} catch(FileNotFoundException e) {
			logger.error("File[{}] not found", PROP_FILE_NAME);
		} catch (IOException e) {
			logger.error("Load File[{}] catch IOException", PROP_FILE_NAME);
		} finally {
			try {
				if(class_in != null) {
					class_in.close();
				}
				if(classLoader_in != null) {
					classLoader_in.close();
				}
			} catch (IOException e) {
				logger.error("Close File[{}] catch IOException", PROP_FILE_NAME);
			}
		}
		logger.info("Load properties File[{}] Success", PROP_FILE_NAME);
	}
	
	public static String getProperty(String key) {
		if(null == props) loadProps();
		return props.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue) {
		if(null == props) loadProps();
		return props.getProperty(key, defaultValue);
	}
}
