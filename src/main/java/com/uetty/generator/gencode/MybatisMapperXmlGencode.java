package com.uetty.generator.gencode;

import com.uetty.generator.db.Column;
import com.uetty.generator.db.Table;
import com.uetty.generator.types.KeyType;
import com.uetty.generator.util.DbStringUtil;
import com.uetty.generator.util.FileTool;
import com.uetty.generator.util.IHashMap;
import com.uetty.generator.util.OptUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MybatisMapperXmlGencode {

    private static final String TAB = "    ";
    private static final String DTAB = TAB + TAB;
    private static final String NLINE = "\n";

    private static File getOutputFile(IHashMap<String, String> config, String tableJavaName) {
        String mapperJavaSuffix = OptUtil.getMapperJavaSuffix(config);
        return new File(OptUtil.getOutputFolder(config, tableJavaName), tableJavaName + mapperJavaSuffix + ".xml");
    }

    public static void generate(Table tb, String tempalateStr, IHashMap<String, String> config) throws IOException {
        String tableJavaName = OptUtil.getEntityClassName(tb, config);

        String data = tempalateStr;
        data = data.replace("${mapperFullClass-}", OptUtil.getMapperFullClass(tb,config));
        data = data.replace("${entityFullClass-}",OptUtil.getEntityFullClass(tb,config));
        data = data.replace("${propertyList-}", getPropertyStrings(tb));
        data = data.replace("${baseColumnList-}",getBaseColumnSql(tb));
        data = data.replace("${idFullClass-}",OptUtil.getIdClassFullName(tb));
        data = data.replace("${tableName-}",tb.getName());
        Column idCol = OptUtil.getIdCol(tb);
        data = data.replace("${idColumn-}", idCol == null ? "" : idCol.getName());
        data = data.replace("${idProp-}", idCol == null ? "" : DbStringUtil.underLineToCamelStyle(idCol.getName()));
        boolean genKey = idCol != null && idCol.getAutoIncrement();
        String genKeySetting = "";
        if (genKey) {
            genKeySetting = " useGeneratedKeys=\"true\" keyProperty=\"" + idCol.getName() + "\"";
        }
        data = data.replace("${generateKey-}", genKeySetting);
        data = data.replace("${insertSQL-}", getInsertSql(tb));
        data = data.replace("${updateSQL-}", getUpdateSql(tb));

        File outputFile = getOutputFile(config, tableJavaName);
        FileTool.writeString(outputFile, data);
    }

    private static String getInsertSql(Table tb) {
        StringBuilder sql = new StringBuilder();
        sql.append(DTAB).append("INSERT INTO ").append(DbStringUtil.toSqlField(tb.getName())).append(NLINE);
        sql.append(DTAB).append("(");
        List<Column> columns = tb.getColumns();
        int insertCount = 0;
        for (Column col : columns) {
            if (col.getAutoIncrement()) {
                continue;
            }
            if (insertCount > 0) {
                sql.append(", ");
                if (insertCount % 4 == 0) sql.append(NLINE).append(DTAB);
            }
            sql.append(DbStringUtil.toSqlField(col.getName()));
            insertCount++;
        }
        sql.append(")").append(NLINE);
        sql.append(DTAB).append("VALUES (");
        insertCount = 0;
        for (Column col : columns) {
            if (col.getAutoIncrement()) {
                continue;
            }
            if (insertCount > 0) {
                sql.append(", ");
                if (insertCount % 4 == 0) sql.append(NLINE).append(DTAB);
            }
            sql.append("#{").append(DbStringUtil.underLineToCamelStyle(col.getName())).append("}");
            insertCount++;
        }
        sql.append(")");
        return sql.toString();
    }

    private static String getBaseColumnSql(Table tb) {
        StringBuilder sb = new StringBuilder();
        sb.append(DTAB);
        List<Column> columns = tb.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Column col = columns.get(i);
            if (i > 0) {
                sb.append(", ");
                if (i % 6 == 0) sb.append(NLINE).append(DTAB);
            }
            sb.append(DbStringUtil.toSqlField(col.getName()));
        }
        return sb.toString();
    }

    private static String getUpdateSql(Table tb) {
        StringBuilder sql = new StringBuilder();
        sql.append(DTAB).append("UPDATE ").append(DbStringUtil.toSqlField(tb.getName())).append(NLINE);
        sql.append(DTAB).append("SET");
        List<Column> columns = tb.getColumns();
        Column identify = null;
        int insertCount = 0;
        for (Column col : columns) {
            if (col.getKeyType() == KeyType.pri) {
                identify = col;
                continue;
            }
            if (insertCount > 0) sql.append(", ");
            sql.append(NLINE).append(DTAB);
            sql.append(DbStringUtil.toSqlField(col.getName()));
            sql.append(" = #{").append(DbStringUtil.underLineToCamelStyle(col.getName())).append("}");
            insertCount++;
        }
        sql.append(NLINE).append(DTAB);
        if (identify != null) {
            sql.append("WHERE ");
            sql.append(DbStringUtil.toSqlField(identify.getName()));
            sql.append(" = #{").append(DbStringUtil.underLineToCamelStyle(identify.getName())).append("}");
        }
        return sql.toString();
    }

    private static String getPropertyStrings(Table tb) {
        StringBuilder sb = new StringBuilder();
        List<Column> columns = tb.getColumns();
        Column identify = null;
        for (Column col : columns) {
            if (col.getKeyType() == KeyType.pri) {
                identify = col;
            }
        }
        if (identify != null) {
            sb.append(DTAB);
            sb.append("<id column=\"").append(identify.getName()).append("\" jdbcType=\"");
            sb.append(identify.getJdbcType().names().toUpperCase()).append("\" property=\"");
            sb.append(DbStringUtil.underLineToCamelStyle(identify.getName())).append("\"/>");
        }
        for (Column col : columns) {
            if (col.getKeyType() == KeyType.pri) {
                continue;
            }
            sb.append(NLINE).append(DTAB);
            sb.append("<result column=\"").append(col.getName()).append("\" jdbcType=\"");

            sb.append(col.getJdbcType().names().toUpperCase()).append("\" property=\"");
            sb.append(DbStringUtil.underLineToCamelStyle(col.getName())).append("\"/>");
        }
        return sb.toString();
    }
}
