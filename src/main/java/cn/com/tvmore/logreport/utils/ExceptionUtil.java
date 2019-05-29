package cn.com.tvmore.logreport.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	
	public static String printStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
	
}
