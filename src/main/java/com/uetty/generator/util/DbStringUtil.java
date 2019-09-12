package com.uetty.generator.util;

public class DbStringUtil {

	public static String toSqlField (String str) {
		return "`" + str + "`";
	}
	
	/**
	 * 下划线命名转驼峰
	 */
	public static String underLineToCamelStyle (String str) {
		StringBuilder sb = new StringBuilder();
		boolean shift = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '_') {
				shift = true;
			} else {
				if (shift) {
					if ((c >= 'a' && c <= 'z')) {
						c = (char) (c - 32);
					}
					shift = false;
				}
				sb.append(c);
			}
		}
		if (sb.length() > 0) {
			sb.replace(0, 1, sb.substring(0, 1).toLowerCase());
		}
		return sb.toString();
	}
	
	/**
	 * 驼峰命名转下划线
	 * @param str
	 * @return
	 */
	public static String camelToUnderLineStyle (String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				sb.append("_");
				c = (char) (c + 32);
			}
			sb.append(c);
		}
		while (sb.length() > 0 && sb.charAt(0) == '_') {
			sb.delete(0, 1);
		}
		return sb.toString();
	}
}
