package org.windom.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Utils {

	public static InputStream getClassPathResource(String filePath) {
		return Utils.class.getResourceAsStream("/" + filePath);
	}

	private static Map<String,Boolean> boolMap = new HashMap<String,Boolean>();
	static {
		boolMap.put("on", true);
		boolMap.put("off", false);
		boolMap.put("true", true);
		boolMap.put("false", false);
		boolMap.put("1", true);
		boolMap.put("0", false);
	}
	
	public static Boolean parseBool(Object obj, Boolean defValue) {
		if (obj == null) return defValue;
		String str = (obj instanceof String) ? (String) obj : obj.toString();
		Boolean result = boolMap.get(str.trim().toLowerCase());
		return (result != null) ? result : defValue;
	}
	
}
