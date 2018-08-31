package com.uetty.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.mybatis.generator.api.ShellRunner;

import com.uetty.generator.util.IHashMap;

/**
 * Hello world!
 *
 */
public class MybaticCodeGen {

	final static String driverClassName = "com.mysql.jdbc.Driver";
	final static String searchTablesSql = "show tables;";
	
	
	
	private static IHashMap<String, String> getParams(String[] args) {
		IHashMap<String, String> cmap = new IHashMap<String, String>();
		
		for (int i = 1; i < args.length; i+=2) {
			if (CmdOpt.PKG_BASE_OPT.str.equals(args[i-1].toLowerCase())) {
				cmap.put(args[i-1].toLowerCase(), args[i]);
				cmap.put(CmdOpt.PKG_DAO_OPT.str, cmap.get(CmdOpt.PKG_BASE_OPT.str) + ".mapper");
				cmap.put(CmdOpt.PKG_MAPPER_OPT.str, cmap.get(CmdOpt.PKG_BASE_OPT.str) + ".mapper");
				cmap.put(CmdOpt.PKG_ENTITY_OPT.str, cmap.get(CmdOpt.PKG_BASE_OPT.str) + ".entity");
			}
		}
		for (int i = 1; i < args.length; i+=2) {
			cmap.put(args[i-1].toLowerCase(), args[i]);
		}
		
		cmap.putIfNotExist(CmdOpt.DB_SERVER_OPT.str, Config.get("database.url"));
		cmap.putIfNotExist(CmdOpt.DB_USER_OPT.str, Config.get("database.username"));
		cmap.putIfNotExist(CmdOpt.DB_PASS_OPT.str, Config.get("database.password"));
		cmap.putIfNotExist(CmdOpt.PKG_ENTITY_OPT.str, Config.get("package.entity"));
		cmap.putIfNotExist(CmdOpt.PKG_MAPPER_OPT.str, Config.get("package.mapper"));
		cmap.putIfNotExist(CmdOpt.PKG_DAO_OPT.str, Config.get("package.dao"));
		cmap.putIfNotExist(CmdOpt.OUT_FILE_OPT.str, Config.get("output.basefile"));
		
		return cmap;
	}
	
	public static void main(String[] args) {

		IHashMap<String, String> cmap = getParams(args);
		
		Connection conn = null;
		try {
			Class<?> driverClass = Class.forName(driverClassName);
			String driverJarPath = getDriverJarPath(driverClass);
			
			conn = DriverManager.getConnection(cmap.get(CmdOpt.DB_SERVER_OPT.str),
					cmap.get(CmdOpt.DB_USER_OPT.str), cmap.get(CmdOpt.DB_PASS_OPT.str));
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(searchTablesSql);
			if (resultSet.first()) {
				File file = new File(cmap.get(CmdOpt.OUT_FILE_OPT.str)+"/xml/");
				if(!file.exists()){
					file.mkdirs();
				}
				String template = readTemplate();
				template = template.replaceAll("\\$\\{database.driver\\}", driverClassName);
				template = template.replaceAll("\\$\\{database.url\\}", cmap.get(CmdOpt.DB_SERVER_OPT.str));
				template = template.replaceAll("\\$\\{database.username\\}", cmap.get(CmdOpt.DB_USER_OPT.str));
				template = template.replaceAll("\\$\\{database.password\\}", cmap.get(CmdOpt.DB_PASS_OPT.str));
				template = template.replaceAll("\\$\\{package.entity\\}", cmap.get(CmdOpt.PKG_ENTITY_OPT.str));
				template = template.replaceAll("\\$\\{output.basefile\\}", cmap.get(CmdOpt.OUT_FILE_OPT.str));
				template = template.replaceAll("\\$\\{package.mapper\\}", cmap.get(CmdOpt.PKG_MAPPER_OPT.str));
				template = template.replaceAll("\\$\\{package.dao\\}", cmap.get(CmdOpt.PKG_DAO_OPT.str));
				template = template.replaceAll("\\$\\{mysql.connector.path\\}", driverJarPath);
				
				do {
					String tableName = resultSet.getString(1);
					File outFile = writeConfig(template, tableName, cmap.get(CmdOpt.OUT_FILE_OPT.str));
					ShellRunner.main(new String[]{"-configfile",outFile.getAbsolutePath(),"-overwrite"});
				} while (resultSet.next());
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static String getDriverJarPath(Class<?> driverClass) {
		URL resource = driverClass.getResource("");
		String path = resource.getPath();
		if (path.startsWith("file:")) {
			path = path.substring(5);
		}
		int indexOf = path.indexOf(".jar!");
		if (indexOf > 0) {
			path = path.substring(0, indexOf + 4);
		}
		return path;
	}
	
	public static String readTemplate(){
		InputStream is = MybaticCodeGen.class.getResourceAsStream("/template/generatorConfig.xml");
		StringBuffer sb = new StringBuffer();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static File writeConfig(String template,String tableName, String outPutDir) throws IOException{
		String entityName = getEntityName(tableName);
		File configFile = new File(outPutDir + "/xml/" + entityName + "-config.xml");
		template = template.replaceAll("\\$\\{name.table\\}", tableName);
		template = template.replaceAll("\\$\\{name.entity\\}", entityName);
		configFile.createNewFile();
		OutputStream os = new FileOutputStream(configFile);
		os.write(template.getBytes("utf-8"));
		os.flush();
		os.close();
		return configFile;
	}
	
	public static String getEntityName(String tableName){
		String entityName = tableName;
		entityName = entityName.toLowerCase();
		if(entityName.length() > 0){
			entityName = entityName.substring(0,1).toUpperCase()+entityName.substring(1);
		}
		int index = 0;
		boolean after_ = false;
		while (index < entityName.length()) {
			if (entityName.substring(index, index + 1).equals("_")) {
				after_ = true;
				entityName = entityName.substring(0, index) + entityName.substring(index + 1);
			} else {
				if (after_) {
					entityName = entityName.substring(0, index)
							+ entityName.substring(index, index + 1).toUpperCase()
							+ entityName.substring(index + 1);
					after_ = false;
				}
				index++;
			}
		}
		System.out.println(entityName);
		return entityName;
	}
}
