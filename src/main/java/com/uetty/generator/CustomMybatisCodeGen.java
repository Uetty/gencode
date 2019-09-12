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

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Vince
 * @date: 2019/9/12 14:16
 */
public class CustomMybatisCodeGen {

    final static String GET_CURRENT_DATABASE = "SELECT database();";
    final static String SEARCH_TABLE_SQL = "show tables;";

    @SuppressWarnings("Duplicates")
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
        cmap.putIfNotExist(CmdOpt.TYPE_TINYINT_OPT.str, TypeGen.TYPE_WITH_BOOLEAN + "");
        return cmap;
    }

    @SuppressWarnings("Duplicates")
    private static Connection getConnection(IHashMap<String, String> params) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
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

    private static List<Table> getTableList(Connection conn, TypeGen typeGen) throws SQLException {
        String dbname = getCurrentDatabase(conn);
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(SEARCH_TABLE_SQL);
        List<Table> list = new ArrayList<>();

        if (resultSet.first()) {
            do {
                String tableName = resultSet.getString(1);
                Table tb = new Table();
                tb.setName(tableName);
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

        Connection conn = null;
        try {

            conn = getConnection(params);

            int typeOfTinyint = Integer.parseInt(params.get(CmdOpt.TYPE_TINYINT_OPT.str));
            TypeGen typeGen = null;
            if (typeOfTinyint == TypeGen.TYPE_NO_BOOLEAN) {
                typeGen = TypeGen.noBooleanGen();
            } else if (typeOfTinyint == TypeGen.TYPE_WITH_BOOLEAN) {
                typeGen = TypeGen.withBooleanGen();
            }


            List<Table> tableList = getTableList(conn, typeGen);
            String mapperXmlTemp = readMapperXmlTemplate();
            String mapperJavaTemp = readMapperJavaTemplate();
            String entityJavaTemp = readEntityJavaTemplate();
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
        try (InputStream is = MybaticCodeGen.class.getResourceAsStream("/template/mapper.xml.tmp")) {
            return String.join("\n",FileTool.readLines(is).toArray(new String[0]));
        }
    }
    private static String readMapperJavaTemplate() throws IOException {
        try (InputStream is = MybaticCodeGen.class.getResourceAsStream("/template/mapper.java.tmp")) {
            return String.join("\n", FileTool.readLines(is).toArray(new String[0]));
        }
    }
    private static String readEntityJavaTemplate() throws IOException {
        try (InputStream is = MybaticCodeGen.class.getResourceAsStream("/template/entity.java.tmp")) {
            return String.join("\n", FileTool.readLines(is).toArray(new String[0]));
        }
    }
}
