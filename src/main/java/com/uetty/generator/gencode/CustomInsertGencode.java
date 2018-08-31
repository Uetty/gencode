package com.uetty.generator.gencode;

import java.util.List;

import com.uetty.generator.db.Column;
import com.uetty.generator.db.Table;
import com.uetty.generator.util.DbStringUtil;

public class CustomInsertGencode {

	public static String getJdbcInsertSql(Table tb) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO " + DbStringUtil.toSqlField(tb.getName()) + "(");
		List<Column> columns = tb.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			Column col = columns.get(i);
			if (i > 0) {
				sql.append(",");
			}
			if (col.getAutoIncrement()) {
				continue;
			}
			sql.append(DbStringUtil.toSqlField(col.getName()));
		}
		sql.append(") VALUES(");
		for (int i = 0; i < columns.size(); i++) {
			Column col = columns.get(i);
			if (i > 0) {
				sql.append(",");
			}
			if (col.getAutoIncrement()) {
				continue;
			}
			sql.append("?");
		}
		sql.append(")");
		return sql.toString();
	}
	
	public static String getJavaInsertStatement (String stmtName, String javaObjName, List<Column> cols) {
		stmtName = stmtName == null ? "pstmt" : stmtName;
		javaObjName = javaObjName == null ? "obj" : javaObjName;
		StringBuilder sb = new StringBuilder();
		String templateStr = "StatementUtil.setValue(" + stmtName + ", index++, " + javaObjName + ".{0});";
		for (int i = 0; i < cols.size(); i++) {
			String colName = cols.get(i).getName();
			String attrName = DbStringUtil.underLineToCamelStyle(colName);
			String getter = "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1) + "()";
			String genLine = templateStr.replace("{0}", getter);
			sb.append(genLine + "\n");
		}
		return sb.toString();
	}
}
