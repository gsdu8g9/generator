package org.windom.generator.util;

import java.io.InputStream;

public class Utils {

	public static InputStream getClassPathResource(String filePath) {
		return Utils.class.getResourceAsStream("/" + filePath);
	}

}
