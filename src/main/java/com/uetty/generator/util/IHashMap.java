package com.uetty.generator.util;

import java.util.HashMap;

public class IHashMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 1L;
	public void putIfNotExist(K key, V value) {
		if (get(key) == null) {
			put(key, value);
		}
	}
}