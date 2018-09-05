package com.uetty.generator.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uetty.generator.coderely.StatementUtil;
import com.uetty.generator.types.KeyType;
import com.uetty.generator.types.TypeGen;

public class TableScanner {

	private static final String GET_TABLE_COLUMN = "SELECT `COLUMN_NAME`,`COLUMN_KEY`,`DATA_TYPE`,`COLUMN_TYPE`,`EXTRA`,`COLUMN_COMMENT` FROM information_schema.`COLUMNS` WHERE `TABLE_SCHEMA` = ? AND `TABLE_NAME` = ?";
	
	public static List<Column> searchColumn(Connection conn, String databaseName, String tableName,
			TypeGen typeGen) {
		List<Column> list = new ArrayList<Column>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean autoCommit = true;
		try {
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(true);
			pstmt = conn.prepareStatement(GET_TABLE_COLUMN);
			int index = 1;
			StatementUtil.setValue(pstmt, index++, databaseName);
			StatementUtil.setValue(pstmt, index++, tableName);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				String dataType = rs.getString("DATA_TYPE");
				String columnType = rs.getString("COLUMN_TYPE");
				String columnKey = rs.getString("COLUMN_KEY");
				String extra = rs.getString("EXTRA");
				String comment = rs.getString("COLUMN_COMMENT");
				Column col = new Column();
				col.setName(columnName);
				col.setJdbcType(typeGen.getJdbcType(dataType, columnType));
				col.setKeyType(KeyType.fromName(columnKey));
				col.setAutoIncrement("auto_increment".equalsIgnoreCase(extra));
				col.setComment(comment);
				list.add(col);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				conn.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeCloseable(rs);
			closeCloseable(pstmt);
		}
		return list;
		
	}
	
	private static void closeCloseable(AutoCloseable con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
