package com.uetty.generator.util;

import com.uetty.generator.CmdOpt;
import com.uetty.generator.CustomMybatisCodeGen;
import com.uetty.generator.db.Column;
import com.uetty.generator.db.Table;
import com.uetty.generator.types.KeyType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author : Vince
 * @date: 2019/9/12 18:03
 */
public class OptUtil {

    public static String readEntityTemplate(IHashMap<String, String> config) throws IOException {
        String templatePath = config.get(CmdOpt.TEMPLATE_ENTITY_OPT.str);
        if (templatePath != null && templatePath.trim().length() > 0) {
            return String.join("\n", FileTool.readLines(new File(templatePath)));
        }
        try (InputStream is = CustomMybatisCodeGen.class.getResourceAsStream("/template/entity.java.tmp")) {
            return String.join("\n", FileTool.readLines(is).toArray(new String[0]));
        }
    }

    public static String readMapperXmlTemplate(IHashMap<String, String> config) throws IOException {
        String templatePath = config.get(CmdOpt.TEMPLATE_MAPPER_XML_OPT.str);
        if (templatePath != null && templatePath.trim().length() > 0) {
            return String.join("\n", FileTool.readLines(new File(templatePath)));
        }
        try (InputStream is = CustomMybatisCodeGen.class.getResourceAsStream("/template/mapper.xml.tmp")) {
            return String.join("\n", FileTool.readLines(is).toArray(new String[0]));
        }
    }

    public static String readMapperJavaTemplate(IHashMap<String, String> config) throws IOException {
        String templatePath = config.get(CmdOpt.TEMPLATE_MAPPER_JAVA_OPT.str);
        if (templatePath != null && templatePath.trim().length() > 0) {
            return String.join("\n", FileTool.readLines(new File(templatePath)));
        }
        try (InputStream is = CustomMybatisCodeGen.class.getResourceAsStream("/template/mapper.java.tmp")) {
            return String.join("\n", FileTool.readLines(is).toArray(new String[0]));
        }
    }

    public static String getEntityName(Table tb, IHashMap<String, String> config) {
        String tableName = tb.getName();
        String prefix = config.get(CmdOpt.TABLE_PREFIX_OPT.str);
        String suffix = config.get(CmdOpt.TABLE_SUFFIX_OPT.str);
        if (prefix == null) prefix = "";
        prefix = prefix.trim();
        if (suffix == null) suffix = "";
        suffix = suffix.trim();
        if (prefix.length() > 0 && tableName.startsWith(prefix)) tableName = tableName.substring(prefix.length());
        if (suffix.length() > 0 && tableName.length() > 0 && tableName.endsWith(suffix)) tableName = tableName.substring(0, tableName.length() - suffix.length());
        return DbStringUtil.underLineToCamelStyle(tableName);
    }

    public static File getOutputFolder(IHashMap<String, String> config, String tableJavaName) {
        String outputFolder = config.get(CmdOpt.OUT_FILE_OPT.str);
        return new File(outputFolder, tableJavaName);
    }

    public static String getMapperPackage(IHashMap<String, String> config) {
        return config.get(CmdOpt.PKG_DAO_OPT.str);
    }

    public static String getMapperFullClass(Table tb, IHashMap<String, String> config) {
        String pkg = getMapperPackage(config);
        return pkg + "." + getMapperClass(tb,config);
    }

    public static String getMapperClass (Table tb, IHashMap<String, String> config) {
        String tableJavaName = getEntityClassName(tb, config);

        String mapperJavaSuffix = getMapperJavaSuffix(config);
        return tableJavaName + mapperJavaSuffix;
    }

    public static String getMapperJavaSuffix(IHashMap<String, String> config) {
        String mapperJavaSuffix = config.get(CmdOpt.DAO_SUFFIX_OPT.str);
        return getFirstUpperString(mapperJavaSuffix);
    }

    private static String getFirstUpperString(String str) {
        if (str == null) {
            return "";
        }
        str = str.trim();
        if (str.length() == 0) {
            return str;
        }
        str = str.toLowerCase();
        String firstrChar = str.charAt(0) + "";
        return firstrChar.toUpperCase() + str.substring(1);
    }

    public static String getEntityClassName (Table tb, IHashMap<String, String> config) {
        String tableName = getEntityName(tb,config);
        tableName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
        return tableName;
    }

    public static String getEndtityPackage(IHashMap<String, String> config) {
        return config.get(CmdOpt.PKG_ENTITY_OPT.str);
    }

    public static String getEntityFullClass(Table tb, IHashMap<String, String> config) {
        String pkg = getEndtityPackage(config);
        return pkg  + "." + getEntityClassName(tb,config);
    }

    public static Column getIdCol(Table tb) {
        List<Column> columns = tb.getColumns();
        for (Column col : columns) {
            if (col.getKeyType() == KeyType.pri) {
                return col;
            }
        }
        return null;
    }

    public static Class<?> getIdClass(Table tb) {
        Column idCo1 = getIdCol(tb);
        return idCo1 == null ? null : idCo1.getJdbcType().javaClass();
    }

    @SuppressWarnings("unused")
    public static String getIdClassName(Table tb) {
        Class<?> idClass = getIdClass(tb);
        if (idClass == null) return "";
        return idClass.getName();
    }

    public static String getIdClassFullName(Table tb) {
        Class<?> idClass = getIdClass(tb);
        if (idClass == null) return "";
        return idClass.getCanonicalName();
    }
}
