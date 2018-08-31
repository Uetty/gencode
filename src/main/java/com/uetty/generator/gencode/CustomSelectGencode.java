package com.uetty.generator.gencode;

import java.util.List;

import com.uetty.generator.db.Column;
import com.uetty.generator.util.DbStringUtil;

public class CustomSelectGencode {

	public static String getJavaFillValueOfSelect (String javaObjName, String resultSetName, List<Column> cols) {
		javaObjName = javaObjName == null ? "obj" : javaObjName;
		resultSetName = resultSetName == null ? "rs" : resultSetName;
		String templateStr = javaObjName + ".{0}(" + resultSetName + ".{1}(\"{2}\"));";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cols.size(); i++) {
			String colName = cols.get(i).getName();
			String attrName = DbStringUtil.underLineToCamelStyle(colName);
			String setter = "set" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
			
			String javaCode = templateStr.replace("{0}", setter);
			
			String resultSetFunc = cols.get(i).getJdbcType().resultMethod();
			javaCode = javaCode.replace("{1}", resultSetFunc);
			javaCode = javaCode.replace("{2}", colName);
			sb.append(javaCode + "\n");
		}
		return sb.toString();
	}
}
