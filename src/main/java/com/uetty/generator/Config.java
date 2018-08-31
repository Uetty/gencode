package com.uetty.generator;

import java.util.ResourceBundle;

public class Config {

	static ResourceBundle bundle = ResourceBundle.getBundle("config");
	
	public static String get(String key) {
		return bundle.getString(key);
	}
	
}
