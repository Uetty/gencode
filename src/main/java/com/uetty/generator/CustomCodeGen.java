package com.uetty.generator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.uetty.generator.constant.Config;
import com.uetty.generator.db.Column;
import com.uetty.generator.db.Table;
import com.uetty.generator.db.TableScanner;
import com.uetty.generator.gencode.CustomInsertGencode;
import com.uetty.generator.gencode.CustomSelectGencode;
import com.uetty.generator.gencode.CustomUpdateGencode;
import com.uetty.generator.types.TypeGen;
import com.uetty.generator.util.DbStringUtil;
import com.uetty.generator.util.IHashMap;

public class CustomCodeGen {

	final static String driverClassName = "com.mysql.jdbc.Driver";
	
	final static String GET_CURRENT_DATABASE = "SELECT database();";
	
	@SuppressWarnings("ConstantConditions")
	private static Connection getConnection(IHashMap<String, String> params) throws ClassNotFoundException, SQLException {
		String driver = params.get(CmdOpt.DB_DRIVER_OPT.str);
		Class.forName(driver.trim());
		String url = params.get(CmdOpt.DB_SERVER_OPT.str);
		String username = params.get(CmdOpt.DB_USER_OPT.str);
		String password = params.get(CmdOpt.DB_PASS_OPT.str);
		return DriverManager.getConnection(url, username, password);
	}
	
	private static String getCurrentDatabase(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(GET_CURRENT_DATABASE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		String dbname = rs.getString(1);
		rs.close();
		pstmt.close();
		return dbname;
	}
	
	private static IHashMap<String, String> getParams(String[] args) {
		IHashMap<String, String> cmap = new IHashMap<String, String>();
		for (int i = 1; i < args.length; i+=2) {
			cmap.put(args[i-1].toLowerCase(), args[i]);
		}
		cmap.putIfNotExist(CmdOpt.DB_DRIVER_OPT.str, Config.get("database.driver"));
		cmap.putIfNotExist(CmdOpt.DB_SERVER_OPT.str, Config.get("database.url"));
		cmap.putIfNotExist(CmdOpt.DB_USER_OPT.str, Config.get("database.username"));
		cmap.putIfNotExist(CmdOpt.DB_PASS_OPT.str, Config.get("database.password"));
		cmap.putIfNotExist(CmdOpt.TYPE_TINYINT_OPT.str, TypeGen.TYPE_NO_BOOLEAN + "");
		cmap.putIfNotExist(CmdOpt.GEN_TYPE_OPT.str, Config.GEN_TYPE_INSERT + "");
		return cmap;
	}
	
	public static void main(String[] args) {
		IHashMap<String,String> params = getParams(args);
		
		Connection conn = null;
		try {
			
			conn = getConnection(params);
			int genType = Integer.parseInt(params.get(CmdOpt.GEN_TYPE_OPT.str));
			int typeOfTinyint = Integer.parseInt(params.get(CmdOpt.TYPE_TINYINT_OPT.str));
			String stmtName = params.get(CmdOpt.STMT_NAME_OPT.str);
			String javaObjName = params.get(CmdOpt.JAVA_OBJ_NAME_OPT.str);
			String resultSetName = params.get(CmdOpt.RESULT_SET_NAME_OPT.str);
			String tableName = params.get(CmdOpt.TABLE_NAME_OPT.str);
			System.out.println("-------------");
			
			TypeGen typeGen = null;
			if (typeOfTinyint == TypeGen.TYPE_NO_BOOLEAN) {
				typeGen = TypeGen.noBooleanGen();
			} else if (typeOfTinyint == TypeGen.TYPE_WITH_BOOLEAN) {
				typeGen = TypeGen.withBooleanGen();
			}
			
			String dbname = getCurrentDatabase(conn);
			
			List<Column> cols = TableScanner.searchColumn(conn, dbname, tableName, typeGen);
			Table tb = new Table();
			tb.setName(tableName);
			tb.setColumns(cols);
			
			if ((genType & Config.GEN_TYPE_INSERT) > 0) {
				String insertSql = CustomInsertGencode.getJdbcInsertSql(tb);
				System.out.println("insert SQL ==> ");
				System.out.println(insertSql + "\n");
				String insertCode = CustomInsertGencode.getJavaInsertStatement(stmtName, javaObjName, cols);
				System.out.println("insert code ==> ");
				System.out.println(insertCode + "\n\n");
			}
			
			if ((genType & Config.GEN_TYPE_UPDATE) > 0) {
				String updateSql = CustomUpdateGencode.getJdbcUpdateSql(tb);
				System.out.println("update SQL ==> ");
				System.out.println(updateSql + "\n");
				String updateCode = CustomUpdateGencode.getJavaUpdateStatement(stmtName, javaObjName, cols);
				System.out.println("update code ==> ");
				System.out.println(updateCode + "\n\n");
			}
			
			if ((genType & Config.GEN_TYPE_SELECT) > 0) {
				String selectFillCode = CustomSelectGencode.getJavaFillValueOfSelect(javaObjName, resultSetName, cols);
				System.out.println("select fill object code ==> ");
				System.out.println(selectFillCode + "\n\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
