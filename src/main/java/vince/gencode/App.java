package vince.gencode;

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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.mybatis.generator.api.ShellRunner;

/**
 * Hello world!
 *
 */
public class App {

	final static String driverClassName = "com.mysql.jdbc.Driver";
	final static String searchTablesSql = "show tables;";
	final static String OUT_FILE_OPT = "-o";
	final static String DB_SERVER_OPT = "-s";
	final static String DB_USER_OPT = "-u";
	final static String DB_PASS_OPT = "-p";
	final static String PKG_ENTITY_OPT = "-e";
	final static String PKG_MAPPER_OPT = "-m";
	final static String PKG_DAO_OPT = "-d";
	final static String PKG_BASE_OPT = "-b";

	static class IHashMap<K, V> extends HashMap<K, V> {
		private static final long serialVersionUID = 1L;
		public void putIfNotExist(K key, V value) {
			if (get(key) == null) {
				put(key, value);
			}
		}
	}
	
	public static void main(String[] args) {

		ResourceBundle bundle = ResourceBundle.getBundle("config");
		IHashMap<String, String> cmap = new IHashMap<String, String>();
		
		for (int i = 1; i < args.length; i+=2) {
			if (PKG_BASE_OPT.equals(args[i-1].toLowerCase())) {
				cmap.put(args[i-1].toLowerCase(), args[i]);
				cmap.put(PKG_DAO_OPT, cmap.get(PKG_BASE_OPT) + ".mapper");
				cmap.put(PKG_MAPPER_OPT, cmap.get(PKG_BASE_OPT) + ".mapper");
				cmap.put(PKG_ENTITY_OPT, cmap.get(PKG_BASE_OPT) + ".entity");
			}
		}
		for (int i = 1; i < args.length; i+=2) {
			cmap.put(args[i-1].toLowerCase(), args[i]);
		}
		
		cmap.putIfNotExist(DB_SERVER_OPT, bundle.getString("database.url"));
		cmap.putIfNotExist(DB_USER_OPT, bundle.getString("database.username"));
		cmap.putIfNotExist(DB_PASS_OPT, bundle.getString("database.password"));
		cmap.putIfNotExist(PKG_ENTITY_OPT, bundle.getString("package.entity"));
		cmap.putIfNotExist(PKG_MAPPER_OPT, bundle.getString("package.mapper"));
		cmap.putIfNotExist(PKG_DAO_OPT, bundle.getString("package.dao"));
		cmap.putIfNotExist(OUT_FILE_OPT, bundle.getString("output.basefile"));
		
		Connection conn = null;
		try {
			Class<?> driverClass = Class.forName(driverClassName);
			String driverJarPath = getDriverJarPath(driverClass);
			
			conn = DriverManager.getConnection(cmap.get(DB_SERVER_OPT), cmap.get(DB_USER_OPT), cmap.get(DB_PASS_OPT));
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(searchTablesSql);
			if (resultSet.first()) {
				File file = new File(cmap.get(OUT_FILE_OPT)+"/xml/");
				if(!file.exists()){
					file.mkdirs();
				}
				String template = readTemplate();
				template = template.replaceAll("\\$\\{database.driver\\}", driverClassName);
				template = template.replaceAll("\\$\\{database.url\\}", cmap.get(DB_SERVER_OPT));
				template = template.replaceAll("\\$\\{database.username\\}", cmap.get(DB_USER_OPT));
				template = template.replaceAll("\\$\\{database.password\\}", cmap.get(DB_PASS_OPT));
				template = template.replaceAll("\\$\\{package.entity\\}", cmap.get(PKG_ENTITY_OPT));
				template = template.replaceAll("\\$\\{output.basefile\\}", cmap.get(OUT_FILE_OPT));
				template = template.replaceAll("\\$\\{package.mapper\\}", cmap.get(PKG_MAPPER_OPT));
				template = template.replaceAll("\\$\\{package.dao\\}", cmap.get(PKG_DAO_OPT));
				template = template.replaceAll("\\$\\{mysql.connector.path\\}", driverJarPath);
				
				do {
					String tableName = resultSet.getString(1);
					File outFile = writeConfig(template, tableName, cmap.get(OUT_FILE_OPT));
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
		InputStream is = App.class.getResourceAsStream("/template/generatorConfig.xml");
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
