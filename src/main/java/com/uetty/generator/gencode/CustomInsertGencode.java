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
		int insertCount = 0;
		for (int i = 0; i < columns.size(); i++) {
			Column col = columns.get(i);
			if (col.getAutoIncrement()) {
				continue;
			}
			if (insertCount > 0) {
				sql.append(",");
			}
			sql.append(DbStringUtil.toSqlField(col.getName()));
			insertCount++;
		}
		sql.append(") VALUES(");
		insertCount = 0;
		for (int i = 0; i < columns.size(); i++) {
			Column col = columns.get(i);
			if (col.getAutoIncrement()) {
				continue;
			}
			if (insertCount > 0) {
				sql.append(",");
			}
			sql.append("?");
			insertCount++;
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
			if (cols.get(i).getAutoIncrement()) {
				continue;
			}
			String colName = cols.get(i).getName();
			String attrName = DbStringUtil.underLineToCamelStyle(colName);
			String getter = "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1) + "()";
			String genLine = templateStr.replace("{0}", getter);
			sb.append(genLine + "\n");
		}
		return sb.toString();
	}
}
