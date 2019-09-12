package com.uetty.generator.types;

import java.math.BigDecimal;
import java.util.Date;

public enum JdbcType {

	bigint_n("bigint", Long.class, "BIGINT"),
	double_n("double", Double.class, "DOUBLE"),
	float_n("float", Float.class, "FLOAT"),
	bigdecimal_n("decimal", BigDecimal.class, "DECIMAL"),
	int_n("int", Integer.class, "INTEGER"),
	datetime_n("datetime", Date.class, "TIMESTAMP"),
	timestamp_n("timestamp", Date.class, "TIMESTAMP"),
	varchar_n("varchar", String.class, "VARCHAR"),
	enum_n("enum", String.class, "VARCHAR"),
	date_n("date", Date.class, "TIMESTAMP"),
	text_n("text", String.class, "VARCHAR"),
	text_l("longtext", String.class, "VARCHAR"),
	text_m("mediumtext", String.class, "VARCHAR"),
	text_s("tinytext", String.class, "VARCHAR"),
	json("json", String.class, "VARCHAR"),
	tinyint_n("tinyint", Byte.class, "TINYINT"),
	tinyint_1("tinyint", Boolean.class, "TINYINT"),
	bit("bit", Boolean.class, "BIT"),
	smallint_n("smallint", Short.class, "SMALLINT"),
	blob("blob", Byte[].class, "BLOB"),
	blob_l("longblob", Byte[].class, "BLOB"),
	blob_m("mediumblob", Byte[].class, "BLOB"),
	blob_s("tinyblob", Byte[].class, "BLOB"),
	;
	
	private JdbcType(String dataType, Class<?> javaClass, String name) {
		this.dataType = dataType;
		this.javaClass = javaClass;
		this.name = name;
	}
	
	String dataType;
	Class<?> javaClass;
	String name;
	
	public String dataType() { return this.dataType; }
	public Class<?> javaClass() { return this.javaClass; }
	public String names() { return this.name; }
}
