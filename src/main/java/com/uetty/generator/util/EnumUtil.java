package com.uetty.generator.util;

public class EnumUtil {
	
	public interface Codeable {

		byte code();
	}

	public static <T extends Enum<T> & Codeable> T valueOf(Byte code, Class<T> clz) {
		if (code == null) return null;
		return valueOf(code.intValue(), clz);
	}
	
	public static <T extends Enum<T> & Codeable> T valueOf(Integer code, Class<T> clz) {
		if (code == null) return null;
		
		T[] values = clz.getEnumConstants();
	
		for (T item : values) {
			if (item.code() == code) {
				return item;
			}
		}
		return null;
	}
	
	public static <T extends Enum<T>> T valueOf(String name, Class<T> clz) {
		if (name == null) return null;
		T[] values = clz.getEnumConstants();
		for (T item : values) {
			if (item.name().equals(name)) {
				return item;
			}
		}
		return null;
	}
}
