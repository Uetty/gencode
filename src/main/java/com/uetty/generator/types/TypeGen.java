package com.uetty.generator.types;

public class TypeGen {

	int typeOfTinyint;
	
	public static int TYPE_NO_BOOLEAN = 1;
	public static int TYPE_WITH_BOOLEAN = 2;
	
	private TypeGen (int typeOfTinyint) {
		this.typeOfTinyint = typeOfTinyint;
	}
	
	private static class TypeGenHolder {
		private static TypeGen NO_BOOLEAN_GEN = new TypeGen(TYPE_NO_BOOLEAN);
		private static TypeGen WITH_BOOLEAN_GEN = new TypeGen(TYPE_WITH_BOOLEAN);
	}
	
	public static TypeGen noBooleanGen() {
		return TypeGenHolder.NO_BOOLEAN_GEN;
	}
	
	public static TypeGen withBooleanGen() {
		return TypeGenHolder.WITH_BOOLEAN_GEN;
	}
	
	public JdbcType getJdbcType(String dataType, String columnType) {
		if (JdbcType.tinyint_n.dataType().equals(dataType)) {
			if (this.typeOfTinyint == TYPE_NO_BOOLEAN) {
				return JdbcType.tinyint_n;
			} else {
				if ("tinyint(1)".equals(columnType)) {
					return JdbcType.tinyint_1;
				} else {
					return JdbcType.tinyint_n;
				}
			}
		}
		for (JdbcType type : JdbcType.values()) {
			if (type.dataType().equals(dataType)) {
				return type;
			}
		}
		return null;
	}
}
