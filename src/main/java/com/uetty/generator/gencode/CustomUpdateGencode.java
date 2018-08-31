package com.uetty.generator.gencode;

import java.util.List;

import com.uetty.generator.scan.Column;
import com.uetty.generator.scan.Table;
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
		}
		return sql.toString();
	}
	
	
}
