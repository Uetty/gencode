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

    private static String resolveIfId(String tempStr, Column col) {
        String ifIdStr = "@ifId(){";
        String doneStr = "@dIfi}";
        int index = tempStr.indexOf(ifIdStr);
        if (index < 0) {
            return tempStr;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(tempStr, 0, index);

        tempStr = tempStr.substring(index + ifIdStr.length());
        tempStr = trimPrefix(tempStr);

        int doneIndex = tempStr.indexOf(doneStr);

        if (col.getKeyType() == KeyType.pri) {
            sb.append(tempStr, 0, doneIndex);
        }

        tempStr = tempStr.substring(doneIndex + doneStr.length());
        tempStr = trimPrefix(tempStr);

        if (tempStr.length() > 0) {
            sb.append(resolveIfId(tempStr, col));
        }

        return sb.toString();
    }

    private static String resolveIfHasComment(String tempStr, Column col) {
        String commentExStr = "@cmEx(){";
        String doneStr = "@xEmc}";
        int index = tempStr.indexOf(commentExStr);
        if (index < 0) {
            return tempStr;
        }

        boolean hasComment = col.getComment() != null && col.getComment().trim().length() > 0;

        StringBuilder sb = new StringBuilder();
        sb.append(tempStr, 0, index);

        tempStr = tempStr.substring(index + commentExStr.length());
        tempStr = trimPrefix(tempStr);

        int doneIndex = tempStr.indexOf(doneStr);

        if (hasComment) {
            sb.append(tempStr, 0, doneIndex);
        }

        tempStr = tempStr.substring(doneIndex + doneStr.length());
        tempStr = trimPrefix(tempStr);

        if (tempStr.length() > 0) {
            sb.append(resolveIfHasComment(tempStr, col));
        }

        return sb.toString();
    }

    private static String trimPrefix(String str) {
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == ' ' || c == '\t' || c == '\r') {
                continue;
            }
            if (c == '\n') {
                index = i + 1;
            }
            break;
        }
        return str.substring(index);
    }

    private static String getOnColumnLoop(String tempStr, Table tb) {
        String forStr = "@for(columns){";
        String doneStr = "@rof}";
        int index = tempStr.indexOf(forStr);
        if (index < 0) {
            return tempStr;
        }
        StringBuilder sb = new StringBuilder(tempStr.substring(0, index));
        tempStr = tempStr.substring(index + forStr.length());

        int doneIndex = tempStr.indexOf(doneStr);
        String endStr = tempStr.substring(doneIndex + doneStr.length());
        endStr = trimPrefix(endStr);

        tempStr = tempStr.substring(0, doneIndex);
        tempStr = trimPrefix(tempStr);

        List<Column> columns = tb.getColumns();
        for (Column col : columns) {
            String fieldTempStr = tempStr;

            fieldTempStr = resolveIfId(fieldTempStr, col);
            fieldTempStr = resolveIfHasComment(fieldTempStr, col);
            if (fieldTempStr.contains("@{javaFieldDeclare}")) {
                String fieldDeclareLine = getFieldDeclare(col);
                fieldTempStr = fieldTempStr.replace("@{javaFieldDeclare}", fieldDeclareLine);
            }
            if (fieldTempStr.contains("@{javaField}")) {
                fieldTempStr = fieldTempStr.replace("@{javaField}", DbStringUtil.underLineToCamelStyle(col.getName()));
            }
            if (fieldTempStr.contains("@{upperCaseJavaField}")) {
                String upperCaseJavaField = DbStringUtil.underLineToCamelStyle(col.getName());
                upperCaseJavaField = upperCaseJavaField.substring(0, 1).toUpperCase() + upperCaseJavaField.substring(1);
                fieldTempStr = fieldTempStr.replace("@{upperCaseJavaField}", upperCaseJavaField);
            }
            if (fieldTempStr.contains("@{dataField}")) {
                fieldTempStr = fieldTempStr.replace("@{dataField}", col.getName());
            }
            if (fieldTempStr.contains("@{javaFieldIfId}")) {
                String idName = col.getKeyType() == KeyType.pri ? DbStringUtil.underLineToCamelStyle(col.getName()) : "";
                fieldTempStr = fieldTempStr.replace("@{javaFieldIfId}", idName);
            }
            if (fieldTempStr.contains("@{javaType}")) {
                String fieldTypeName = col.getJdbcType().javaClass().getSimpleName();
                fieldTempStr = fieldTempStr.replace("@{javaType}", fieldTypeName);
            }
            if (fieldTempStr.contains("@{dataType}")) {
                String fieldTypeName = col.getJdbcType().dataType();
                fieldTempStr = fieldTempStr.replace("@{dataType}", fieldTypeName);
            }
            if (fieldTempStr.contains("@{colComment}")) {
                String colComment = col.getComment();
                fieldTempStr = fieldTempStr.replace("@{colComment}", colComment);
            }
            sb.append(fieldTempStr);
        }

        if (endStr.length() > 0) {
            sb.append(getOnColumnLoop(endStr, tb));
        }
        return sb.toString();
    }

    private static String getFieldDeclare(Column col) {
        return "private " + col.getJdbcType().javaClass().getSimpleName() +
                " " + DbStringUtil.underLineToCamelStyle(col.getName()) +
                ";";
    }

    private static String getOnClassMembers(String tempStr, Table tb) {
        if (!tempStr.contains("@{classMembers}")) {
            return tempStr;
        }
        List<Column> columns = tb.getColumns();
        StringBuilder sb = new StringBuilder();
        for (Column col : columns) {
            sb.append(NLINE).append(TAB);
            String fieldDeclare = getFieldDeclare(col);
            sb.append(fieldDeclare);
        }
        sb.append(NLINE);
        return tempStr.replace("@{classMembers}", sb.toString());
    }

    private static String resolveNbr(String data) {
        String nbrStr = "@nbr{}";
        int index = data.indexOf(nbrStr);
        if (index < 0) {
            return data;
        }

        int backLookIndex = index;
        for (int i = index - 1; i >= 0; i--) {
            char c = data.charAt(i);
            if (c == ' ' || c == '\t' || c == '\r') {
                continue;
            }
            if (c == '\n') {
                backLookIndex = i + 1;
            }
            break;
        }
        int forwardLookIndex = index + nbrStr.length();
        int length = data.length();
        for (int i = forwardLookIndex; i < length; i++) {
            char c = data.charAt(i);
            if (c == ' ' || c == '\t' || c == '\r') {
                if (i == length - 1) {
                    forwardLookIndex = i + 1;
                }
                continue;
            }
            if (c == '\n') {
                forwardLookIndex = i + 1;
            }
            break;
        }

        String sb = data.substring(0, backLookIndex) +
                data.substring(forwardLookIndex);
        return resolveNbr(sb);
    }

    private static String getOnGetterSetters(String tempStr, Table tb) {
        if (!tempStr.contains("@{getterSetters}")) {
            return tempStr;
        }
        List<Column> columns = tb.getColumns();
        StringBuilder sb = new StringBuilder();
        for (Column col : columns) {
            sb.append(NLINE);
            String getterSetter = buildGetAndSetter(col);
            sb.append(getterSetter);
        }
        sb.append(NLINE);
        return tempStr.replace("@{getterSetters}", sb.toString());
    }

    private static File getOutputFile(IHashMap<String, String> config, String tableJavaName) {
        return new File(OptUtil.getOutputFolder(config, tableJavaName), tableJavaName + ".java");
    }

    public static void generate(Table tb, String tempalateStr, IHashMap<String, String> config) throws IOException {
        String entityClass = OptUtil.getEntityClassName(tb, config);
        String endtityPackage = OptUtil.getEndtityPackage(config);
        boolean hasDate = hasDateType(tb);

        String data = tempalateStr;
        data = data.replace("@{entityPackage}", endtityPackage);
        data = data.replace("@{importPackage}", hasDate ? "\nimport java.util.Date;\n" : "");
        data = data.replace("@{tableName}", tb.getName());
        data = data.replace("@{className}", entityClass);
        data = data.replace("@{tableComment}", tb.getComment());

        data = getOnColumnLoop(data, tb);
        data = getOnClassMembers(data, tb);
        data = getOnGetterSetters(data, tb);

        data = resolveNbr(data);

        File outputFile = getOutputFile(config, OptUtil.getEntityClassName(tb, config));
        FileTool.writeString(outputFile, data);
    }

}
