package com.uetty.generator.types;

import java.util.Date;

public enum JdbcType {

	bigint_n("bigint", Long.class, "getLong"),
	int_n("int", Integer.class, "getInt"),
	datetime_n("datetime", Date.class, "getTimestamp"),
	timestamp_n("timestamp", Date.class, "getTimestamp"),
	varchar_n("varchar", String.class, "getString"),
	enum_n("enum", String.class, "getString"),
	date_n("date", Date.class, "getTimestamp"),
	text_n("text", String.class, "getString"),
	tinyint_n("tinyint", Byte.class, "getByte"),
	tinyint_1("tinyint", Boolean.class, "getBoolean"),
	smallint_n("smallint", Short.class, "getShort")
	;
	
	private JdbcType(String dataType, Class<?> javaClass, String resultMethod) {
		this.dataType = dataType;
		this.javaClass = javaClass;
		this.resultMethod = resultMethod;
	}
	
	String dataType;
	Class<?> javaClass;
	String resultMethod;
	
	public String dataType() { return this.dataType; }
	public Class<?> javaClass() { return this.javaClass; }
	public String resultMethod() { return this.resultMethod; }
}
