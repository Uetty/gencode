package com.uetty.generator.gencode;

import com.uetty.generator.db.Column;
import com.uetty.generator.db.Table;
import com.uetty.generator.util.DbStringUtil;
import com.uetty.generator.util.FileTool;
import com.uetty.generator.util.IHashMap;
import com.uetty.generator.util.OptUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author : Vince
 * @date: 2019/9/12 19:09
 */
public class MybatisMapperJavaGencode {

    private static File getOutputFile(IHashMap<String, String> config, String tableJavaName) {
        String mapperJavaSuffix = OptUtil.getMapperJavaSuffix(config);
        return new File(OptUtil.getOutputFolder(config, tableJavaName), tableJavaName + mapperJavaSuffix + ".java");
    }

    public static void generate(Table tb, String tempalateStr, IHashMap<String, String> config) throws IOException {
        String tableJavaName = OptUtil.getEntityClassName(tb, config);

        String data = tempalateStr;
        data = data.replace("@{mapperPackage}", OptUtil.getMapperPackage(config));
        data = data.replace("@{importPackage}", "\nimport " + OptUtil.getEntityFullClass(tb,config) + ";\n");
        data = data.replace("@{mapperClass}",OptUtil.getMapperClass(tb,config));
        data = data.replace("@{entityClass}",OptUtil.getEntityClassName(tb,config));
        data = data.replace("@{entityName}",OptUtil.getEntityName(tb,config));
        Column idCol = OptUtil.getIdCol(tb);
        data = data.replace("@{idClass}", idCol != null ? idCol.getJdbcType().javaClass().getName() : "");
        data = data.replace("@{idProp}", idCol == null ? "" : DbStringUtil.underLineToCamelStyle(idCol.getName()));

        File outputFile = getOutputFile(config, tableJavaName);
        FileTool.writeString(outputFile, data);
    }
}
