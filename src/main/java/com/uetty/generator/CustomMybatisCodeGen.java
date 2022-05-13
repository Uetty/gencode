package com.uetty.generator;

import com.uetty.generator.constant.Config;
import com.uetty.generator.db.Column;
import com.uetty.generator.db.Table;
import com.uetty.generator.db.TableScanner;
import com.uetty.generator.gencode.MybatisEntityGencode;
import com.uetty.generator.gencode.MybatisMapperJavaGencode;
import com.uetty.generator.gencode.MybatisMapperXmlGencode;
import com.uetty.generator.types.TypeGen;
import com.uetty.generator.util.FileTool;
import com.uetty.generator.util.IHashMap;
import com.uetty.generator.util.OptUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Vince
 * @date: 2019/9/12 14:16
 */
public class CustomMybatisCodeGen {

    final static String GET_CURRENT_DATABASE = "SELECT database();";
    final static String SEARCH_TABLE_SQL = "SELECT * FROM `information_schema`.`TABLES` WHERE `TABLE_SCHEMA` = ?";

    @SuppressWarnings("Duplicates")
    private static IHashMap<String, String> getParams(String[] args) {
        IHashMap<String, String> cmap = new IHashMap<String, String>();

        for (int i = 1; i < args.length; i+=2) {
            if (CmdOpt.PKG_BASE_OPT.str.equals(args[i-1].toLowerCase())) {
                cmap.put(args[i-1].toLowerCase(), args[i]);
                cmap.put(CmdOpt.PKG_DAO_OPT.str, cmap.get(CmdOpt.PKG_BASE_OPT.str) + ".mapper");
                cmap.put(CmdOpt.PKG_ENTITY_OPT.str, cmap.get(CmdOpt.PKG_BASE_OPT.str) + ".entity");
            }
        }
        for (int i = 1; i < args.length; i+=2) {
            cmap.put(args[i-1].toLowerCase(), args[i]);
        }
        cmap.putIfNotExist(CmdOpt.DB_DRIVER_OPT.str, Config.get("database.driver"));
        cmap.putIfNotExist(CmdOpt.DB_SERVER_OPT.str, Config.get("database.url"));
        cmap.putIfNotExist(CmdOpt.DB_USER_OPT.str, Config.get("database.username"));
        cmap.putIfNotExist(CmdOpt.DB_PASS_OPT.str, Config.get("database.password"));
        cmap.putIfNotExist(CmdOpt.PKG_ENTITY_OPT.str, Config.get("package.entity"));
        cmap.putIfNotExist(CmdOpt.PKG_DAO_OPT.str, Config.get("package.dao"));
        cmap.putIfNotExist(CmdOpt.DAO_SUFFIX_OPT.str, Config.get("mapper.suffix"));
        cmap.putIfNotExist(CmdOpt.OUT_FILE_OPT.str, Config.get("mybatis.output.folder"));
        cmap.putIfNotExist(CmdOpt.TYPE_TINYINT_OPT.str, TypeGen.TYPE_WITH_BOOLEAN + "");
        return cmap;
    }

    @SuppressWarnings({"Duplicates", "ConstantConditions"})
    private static Connection getConnection(IHashMap<String, String> params) throws ClassNotFoundException, SQLException {
        String driver = params.get(CmdOpt.DB_DRIVER_OPT.str);
        Class.forName(driver.trim());
        String url = params.get(CmdOpt.DB_SERVER_OPT.str);
        String username = params.get(CmdOpt.DB_USER_OPT.str);
        String password = params.get(CmdOpt.DB_PASS_OPT.str);
        return DriverManager.getConnection(url, username, password);
    }

    private static String getCurrentDatabase(Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(GET_CURRENT_DATABASE);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        String dbname = rs.getString(1);
        rs.close();
        pstmt.close();
        return dbname;
    }

    private static List<Table> getTableList(Connection conn, TypeGen typeGen, IHashMap<String, String> params) throws SQLException {
        String dbname = getCurrentDatabase(conn);
        System.out.println(dbname);
        PreparedStatement preparedStatement = conn.prepareStatement(SEARCH_TABLE_SQL);
        preparedStatement.setString(1, dbname);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Table> list = new ArrayList<>();

        String prefix = params.get(CmdOpt.TABLE_PREFIX_OPT.str);
        String suffix = params.get(CmdOpt.TABLE_SUFFIX_OPT.str);

        if (resultSet.first()) {
            do {
                String tableName = resultSet.getString("TABLE_NAME");
                if (prefix != null && !tableName.startsWith(prefix)) {
                    System.out.println("ignore table " + tableName);
                    continue;
                }
                if (suffix != null && !tableName.endsWith(suffix)) {
                    System.out.println("ignore table " + tableName);
                    continue;
                }
                String tableComment = resultSet.getString("TABLE_COMMENT");
                Table tb = new Table();
                tb.setName(tableName);
                tb.setComment(tableComment);
                list.add(tb);
            } while (resultSet.next());

            for (Table table : list) {
                String tableName = table.getName();
                List<Column> cols = TableScanner.searchColumn(conn, dbname, tableName, typeGen);
                table.setColumns(cols);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        IHashMap<String,String> params = getParams(args);

        Connection conn;
        try {

            conn = getConnection(params);

            int typeOfTinyint = Integer.parseInt(params.get(CmdOpt.TYPE_TINYINT_OPT.str));
            TypeGen typeGen = null;
            if (typeOfTinyint == TypeGen.TYPE_NO_BOOLEAN) {
                typeGen = TypeGen.noBooleanGen();
            } else if (typeOfTinyint == TypeGen.TYPE_WITH_BOOLEAN) {
                typeGen = TypeGen.withBooleanGen();
            }


            List<Table> tableList = getTableList(conn, typeGen, params);
            String mapperXmlTemp = OptUtil.readMapperXmlTemplate(params);
            String mapperJavaTemp = OptUtil.readMapperJavaTemplate(params);
            String entityJavaTemp = OptUtil.readEntityTemplate(params);
            for (Table table : tableList) {
                MybatisEntityGencode.generate(table,entityJavaTemp, params);
                MybatisMapperJavaGencode.generate(table, mapperJavaTemp, params);
                MybatisMapperXmlGencode.generate(table,mapperXmlTemp,params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readMapperXmlTemplate() throws IOException {
        try (InputStream is = CustomMybatisCodeGen.class.getResourceAsStream("/template/mapper.xml.tmp")) {
            return String.join("\n",FileTool.readLines(is).toArray(new String[0]));
        }
    }
    private static String readMapperJavaTemplate() throws IOException {
        try (InputStream is = CustomMybatisCodeGen.class.getResourceAsStream("/template/mapper.java.tmp")) {
            return String.join("\n", FileTool.readLines(is).toArray(new String[0]));
        }
    }
    private static String readEntityJavaTemplate() throws IOException {
        try (InputStream is = CustomMybatisCodeGen.class.getResourceAsStream("/template/entity.java.tmp")) {
            return String.join("\n", FileTool.readLines(is).toArray(new String[0]));
        }
    }
}
