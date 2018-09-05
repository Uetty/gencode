package com.uetty.generator.db;

import com.uetty.generator.types.JdbcType;
import com.uetty.generator.types.KeyType;

public class Column {

	String name;
	
	KeyType keyType;
	
	JdbcType jdbcType;
	
	String comment;
	
	boolean autoIncrement;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public KeyType getKeyType() {
		return keyType;
	}

	public void setKeyType(KeyType keyType) {
		this.keyType = keyType;
	}

	public JdbcType getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}

	public boolean getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
