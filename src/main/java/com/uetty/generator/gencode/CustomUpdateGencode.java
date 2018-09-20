package com.uetty.generator.gencode;

import java.util.List;

import com.uetty.generator.db.Column;
import com.uetty.generator.db.Table;
import com.uetty.generator.types.KeyType;
import com.uetty.generator.util.DbStringUtil;

public class CustomUpdateGencode {
	
	public static String getJdbcUpdateSql(Table tb) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + DbStringUtil.toSqlField(tb.getName()) + " SET ");
		
		List<Column> cols = tb.getColumns();
		int count = 0;
		for (int i = 0; i < cols.size(); i++) {
			if (cols.get(i).getKeyType() == KeyType.pri) {
				continue;
			}
			if (count > 0) {
				sql.append(", ");
			}
			sql.append(DbStringUtil.toSqlField(cols.get(i).getName()) + " = ?");
			count++;
		}
		sql.append(" WHERE ");
		count = 0;
		for (int i = 0; i < cols.size(); i++) {
			if (cols.get(i).getKeyType() != KeyType.pri) {
				continue;
			}
			if (count > 0) {
				sql.append(" AND ");
			}
			sql.append(DbStringUtil.toSqlField(cols.get(i).getName()) + " = ?");
			count++;
		}
		return sql.toString();
	}
	
	public static String getJavaUpdateStatement(String stmtName, String javaObjName, List<Column> cols) {
		stmtName = stmtName == null ? "pstmt" : stmtName;
		javaObjName = javaObjName == null ? "obj" : javaObjName;
		
		StringBuilder sb = new StringBuilder();
		String templateStr = "StatementUtil.setValue(" + stmtName + ", index++, " + javaObjName + ".{0});";
		
		for (int i = 0; i < cols.size(); i++) {
			if (cols.get(i).getKeyType() == KeyType.pri) {
				continue;
			}
			String colName = cols.get(i).getName();
			String attrName = DbStringUtil.underLineToCamelStyle(colName);
			String getter = "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1) + "()";
			String genLine = templateStr.replace("{0}", getter);
			sb.append(genLine + "\n");
		}
		for (int i = 0; i < cols.size(); i++) {
			if (cols.get(i).getKeyType() != KeyType.pri) {
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
