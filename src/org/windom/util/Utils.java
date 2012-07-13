package org.windom.util;

import java.io.InputStream;

import javax.swing.UIManager;

public class Utils {

	public static InputStream getClassPathResource(String filePath) {
		return Utils.class.getResourceAsStream("/" + filePath);
	}

	public static String capitalize(String s) {
		if (s == null || s.length() == 0) return s;
		else return s.substring(0,1).toUpperCase() + s.substring(1);
	}

	public static void initUiLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
