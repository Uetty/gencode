package com.uetty.generator;

import com.uetty.generator.db.Column;
import com.uetty.generator.db.TableScanner;
import com.uetty.generator.types.TypeGen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommonCodeGen {

	String url;
	String user;
	String password;
	String tableName;
	String codeTemplate;
	String colPlaceholder;
	String tablePlaceholder;
	String datatypePlaceholder;
	String resultMethodPlaceholder;
	String commentPlaceholder;
	PlaceholderAdapter colAdapter;
	PlaceholderAdapter tableAdapter;
	PlaceholderAdapter datatypeAdapter;
	PlaceholderAdapter resultMethodAdapter;
	PlaceholderAdapter commentAdapter;
	final static String GET_CURRENT_DATABASE = "SELECT database();";
	
	
	public static interface PlaceholderAdapter {
		
		public String transfer(String name);
	}
	
	public CommonCodeGen setUrl(String url) {
		this.url = url;
		return this;
	}
	
	public CommonCodeGen setUser(String user) {
		this.user = user;
		return this;
	}
	
	public CommonCodeGen setPassword(String password) {
		this.password = password;
		return this;
	}
	
	public CommonCodeGen setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public CommonCodeGen setCodeTemplate(String template) {
		this.codeTemplate = template;
		return this;
	}
	
	public CommonCodeGen setColumnTrans(String colPlaceholder, PlaceholderAdapter colAdapter) {
		this.colPlaceholder = colPlaceholder;
		this.colAdapter = colAdapter;
		return this;
	}
	
	public CommonCodeGen setTableTrans(String tablePlaceholder, PlaceholderAdapter tableAdapter) {
		this.tablePlaceholder = tablePlaceholder;
		this.tableAdapter = tableAdapter;
		return this;
	}
	
	public CommonCodeGen setDatatypeTrans(String datatypePlaceholder, PlaceholderAdapter datatypeAdapter) {
		this.datatypePlaceholder = datatypePlaceholder;
		this.datatypeAdapter = datatypeAdapter;
		return this;
	}
	
	public CommonCodeGen setResultMethodTrans(String resultPlaceholder, PlaceholderAdapter methodAdapter) {
		this.resultMethodPlaceholder = resultPlaceholder;
		this.resultMethodAdapter = methodAdapter;
		return this;
	}
	
	public CommonCodeGen setCommentTrans(String commentPlaceholder, PlaceholderAdapter commentAdapter) {
		this.commentPlaceholder = commentPlaceholder;
		this.commentAdapter = commentAdapter;
		return this;
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(url, user, password);
		return connection;
	}
	
	private String getCurrentDatabase(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(GET_CURRENT_DATABASE);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		String dbname = rs.getString(1);
		rs.close();
		pstmt.close();
		return dbname;
	}
	
	
	public List<Column> readColumn() throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		String database = getCurrentDatabase(conn);
		List<Column> list = TableScanner.searchColumn(conn, database, tableName, TypeGen.noBooleanGen());
		return list;
	}
	
	
	public List<String> gencode() throws ClassNotFoundException, SQLException {
		List<String> list = new ArrayList<String>(); 
		List<Column> cols = readColumn();
		System.out.println(cols);
		if (codeTemplate == null) {
			throw new NullPointerException("codeTemplate is null");
		}
		for (Column col : cols) {
			String temp = codeTemplate;
			
			if (resultMethodPlaceholder != null) {
				temp = temp.replaceAll(resultMethodPlaceholder, resultMethodAdapter.transfer(col.getJdbcType().names()));
			}
			
			if (datatypePlaceholder != null) {
				temp = temp.replaceAll(datatypePlaceholder, datatypeAdapter.transfer(col.getJdbcType().dataType()));
			}
			
			if (colPlaceholder != null) {
				temp = temp.replaceAll(colPlaceholder, colAdapter.transfer(col.getName()));
			}
			
			if (tablePlaceholder != null) {
				temp = temp.replaceAll(tablePlaceholder, tableAdapter.transfer(tableName));
			}
			
			if (commentPlaceholder != null) {
				temp = temp.replaceAll(commentPlaceholder, commentAdapter.transfer(col.getComment()));
			}
			list.add(temp);
		}
		return list;
	}

	
}
