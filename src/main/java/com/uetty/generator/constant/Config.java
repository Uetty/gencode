package com.uetty.generator.constant;

import java.util.ResourceBundle;

public class Config {

	static ResourceBundle bundle = ResourceBundle.getBundle("config");
	
	public static String get(String key) {
		return bundle.getString(key);
	}
	
	
	public static final int GEN_TYPE_INSERT = 0x0001;
	public static final int GEN_TYPE_UPDATE = 0x0002;
	public static final int GEN_TYPE_SELECT = 0x0004;
	
	
	public static final String FUNCTION_MYBATIS_GEN = "1";
	
	public static final String FUNCTION_CUSTOM_GEN = "2";
}
