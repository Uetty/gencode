package com.uetty.generator.types;

public enum KeyType {

	none,
	pri,
	mul;
	
	public static KeyType fromName(String name) {
		for (KeyType type : KeyType.values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
