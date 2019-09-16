package com.uetty.generator.gencode;

import com.uetty.generator.db.Column;
import com.uetty.generator.db.Table;
import com.uetty.generator.util.DbStringUtil;
import com.uetty.generator.util.FileTool;
import com.uetty.generator.util.IHashMap;
import com.uetty.generator.util.OptUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author : Vince
 * @date: 2019/9/12 17:13
 */
public class MybatisEntityGencode {

    private static final String TAB = "    ";
    private static final String DTAB = TAB + TAB;
    private static final String NLINE = "\n";

    private static boolean hasDateType(Table tb) {
        List<Column> columns = tb.getColumns();
        for (Column column : columns) {
            if (column.getJdbcType().javaClass() == Date.class) {
                return true;
            }
        }
        return false;
    }

    private static String buildGetAndSetter(Column col) {
        StringBuilder sb = new StringBuilder();
        String javaName = DbStringUtil.underLineToCamelStyle(col.getName());
        String setterName = "set" + javaName.substring(0,1).toUpperCase() + javaName.substring(1);
        String getterName = "get" + javaName.substring(0,1).toUpperCase() + javaName.substring(1);

        sb.append(TAB).append("public ").append(col.getJdbcType().javaClass().getSimpleName());
        sb.append(" ").append(getterName).append(" () {");
        sb.append(NLINE).append(DTAB);
        sb.append("return this.").append(javaName).append(";");
        sb.append(NLINE).append(TAB).append("}");

        sb.append(NLINE);
        sb.append(TAB).append("public void ").append(setterName).append(" (");
        sb.append(col.getJdbcType().javaClass().getSimpleName()).append(" ").append(javaName);
        sb.append(") {");
        sb.append(NLINE).append(DTAB);
        sb.append("this.").append(javaName).append(" = ").append(javaName).append(";");
        sb.append(NLINE).append(TAB).append("}");

        return sb.toString();
    }

    private static String getEntityMembersCode(Table tb) {
        List<Column> columns = tb.getColumns();
        StringBuilder sb = new StringBuilder();
        for (Column col : columns) {
            sb.append(NLINE).append(TAB);
            sb.append("private ").append(col.getJdbcType().javaClass().getSimpleName());
            sb.append(" ").append(DbStringUtil.underLineToCamelStyle(col.getName()));
            sb.append(";");
        }
        sb.append(NLINE);

        for (Column col : columns) {
            sb.append(NLINE);
            String getterSetter = buildGetAndSetter(col);
            sb.append(getterSetter);
        }

        return sb.toString();
    }

    private static File getOutputFile(IHashMap<String, String> config, String tableJavaName) {
        return new File(OptUtil.getOutputFolder(config, tableJavaName), tableJavaName + ".java");
    }

    public static void generate(Table tb, String tempalateStr, IHashMap<String, String> config) throws IOException {
        String entityClass = OptUtil.getEntityClassName(tb, config);
        String endtityPackage = OptUtil.getEndtityPackage(config);
        boolean hasDate = hasDateType(tb);

        String data = tempalateStr;
        data = data.replace("${entityPackage-}", endtityPackage);
        data = data.replace("${importPackage-}", hasDate ? "import java.util.Date;" : "");
        data = data.replace("${className-}", entityClass);

        data = data.replace("${classMembers-}", getEntityMembersCode(tb));


        File outputFile = getOutputFile(config, OptUtil.getEntityClassName(tb, config));
        FileTool.writeString(outputFile, data);
    }

}
