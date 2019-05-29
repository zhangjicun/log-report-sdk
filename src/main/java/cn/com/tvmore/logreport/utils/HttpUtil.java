package cn.com.tvmore.logreport.utils;

import javax.servlet.http.HttpServletRequest;

import cn.com.tvmore.logreport.kafka.message.HttpLog;
/**
 * HttpUtil
 */
public class HttpUtil {
	
	/**
	 * Create HttpLog from httpServletRequest
	 * @param request
	 * @return
	 */
	public static HttpLog createHttpLog(HttpServletRequest request) {
		Long currentTime = System.currentTimeMillis() / 1000;
		
		HttpLog httpLog = new HttpLog();
		httpLog.setMethod(request.getMethod());
		String requestPath = request.getRequestURI();
		if(requestPath != null && requestPath.startsWith("//")) requestPath = requestPath.substring(1);
		httpLog.setPath(requestPath);
		httpLog.setRequest_time(currentTime);
		httpLog.setDomain(request.getServerName());
		/*httpLog.setUrl(request.getRequestURL().toString());*/
		httpLog.setSrc(getIpAddress(request));
		
		return httpLog;
	}
	
	/**
	 * Get IP address
	 * @param request
	 * @return ip
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
