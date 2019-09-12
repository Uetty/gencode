package com.uetty.generator.util;

import java.io.UnsupportedEncodingException;

public class UnicodeCoder {

	public static String encode(String str) {
		StringBuilder sb = new StringBuilder();
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			sb.append(charToUnicodeString(c));
		}
		return sb.toString();
	}
	
	public static String decode(String str) throws UnsupportDecodeException {

		StringBuilder sb = new StringBuilder();
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (c == '\\') {
				if (i == charArray.length - 1) {
					throw new UnsupportDecodeException();
				}
				if (charArray[i + 1] == '\\') {
					sb.append("\\");
					i++;
				} else if (charArray[i + 1] == 'u') {
					if (i >= charArray.length - 5 || !isHexChar(charArray[i + 2])
							 || !isHexChar(charArray[i + 3]) || !isHexChar(charArray[i + 4])
							 || !isHexChar(charArray[i + 5])) {
						throw new UnsupportDecodeException();
					}
					String hexInt = "" + charArray[i + 2] + charArray[i + 3] + charArray[i + 4] + charArray[i + 5];
					char v = (char) Integer.valueOf(hexInt, 16).intValue();
					sb.append(v);
					i += 5;
				} else {
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	private static boolean isHexChar(char c) {
		if (c >= '0' && c <= '9') return true;
		if (c >= 'a' && c <= 'f') return true;
		if (c >= 'A' && c <= 'F') return true;
		return false;
	}
	
	private static String charToUnicodeString(char c) {
		if (c < 0x100) {
			if (c == '\\') {
				return "\\\\";
			} else {
				return c + "";
			}
		}
		String hex = Integer.toHexString(c);
		if (c >= 0x1000) {
			return "\\u" + hex;
		} else {
			return "\\u0" + hex;
		}
	}
	
	public static class UnsupportDecodeException extends Exception {
		private static final long serialVersionUID = 1L;

		public UnsupportDecodeException() {
		}
	}
	
	public static void main(String[] args) throws UnsupportDecodeException, UnsupportedEncodingException {
		printEncode("mybatis模式表名前缀，设置之后实体名不包含前缀部分");
	}

	private static void printEncode(String str) {
		String encode = encode(str);
		System.out.println("\"" + encode + "\"");
	}
}
