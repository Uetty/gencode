package com.uetty.generator.types;

import java.math.BigDecimal;
import java.util.Date;

public enum JdbcType {

	bigint_n("bigint", Long.class, "getLong"),
	double_n("double", Double.class, "getDouble"),
	float_n("float", Float.class, "getFloat"),
	bigdecimal_n("decimal", BigDecimal.class, "getBigDecimal"),
	int_n("int", Integer.class, "getInt"),
	datetime_n("datetime", Date.class, "getTimestamp"),
	timestamp_n("timestamp", Date.class, "getTimestamp"),
	varchar_n("varchar", String.class, "getString"),
	enum_n("enum", String.class, "getString"),
	date_n("date", Date.class, "getTimestamp"),
	text_n("text", String.class, "getString"),
	text_l("longtext", String.class, "getString"),
	text_m("mediumtext", String.class, "getString"),
	text_s("tinytext", String.class, "getString"),
	json("json", String.class, "getString"),
	tinyint_n("tinyint", Byte.class, "getByte"),
	tinyint_1("tinyint", Boolean.class, "getBoolean"),
	bit("bit", Boolean.class, "getBoolean"),
	smallint_n("smallint", Short.class, "getShort"),
	blob("blob", Byte[].class, "getBlob"),
	blob_l("longblob", Byte[].class, "getBlob"),
	blob_m("mediumblob", Byte[].class, "getBlob"),
	blob_s("tinyblob", Byte[].class, "getBlob"),
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
