package com.uetty.generator;

import com.uetty.generator.constant.Config;
import com.uetty.generator.types.TypeGen;

public enum CmdOpt {

	/**
	 *
	 */
	FUNCTION_SELECT_OPT("-m", "选择功能模式（必须作为第一参数）："
			+ Config.FUNCTION_MYBATIS_GEN + "-mybatic gencode" + "，" + Config.FUNCTION_CUSTOM_GEN + "-custom gencode"),
	DB_DRIVER_OPT("-d", "数据库驱动类"),
	DB_SERVER_OPT("-s", "数据库连接符"),
	DB_USER_OPT("-u", "数据库用户名"),
	DB_PASS_OPT("-p", "数据库密码"),
	TABLE_NAME_OPT("--tb", "custom模式自动生成代码的表名(必填项)"),
	GEN_TYPE_OPT("--gt", "custom模式执行功能（可累加）："
			+ Config.GEN_TYPE_INSERT + " - 生成插入相关jdbc语句、"
			+ Config.GEN_TYPE_UPDATE + " - 生成更新相关jdbc语句、"
			+ Config.GEN_TYPE_SELECT + " - 生成查询相关jdbc语句"),
	STMT_NAME_OPT("--stmt", "custom模式PrepareStatement变量名"),
	JAVA_OBJ_NAME_OPT("--jo", "custom模式实体变量名"),
	RESULT_SET_NAME_OPT("--rs", "custom模式ResultSet变量名"),
	TYPE_TINYINT_OPT("--ti", "custom模式TINYINT(1)是否转为Boolean："
			+ TypeGen.TYPE_NO_BOOLEAN + " - 否、"
			+ TypeGen.TYPE_WITH_BOOLEAN + " - 是"),

	OUT_FILE_OPT("--of", "mybatis模式输出文件夹地址"),
	PKG_ENTITY_OPT("--pe", "mybatis模式Entity包名"),
	DAO_SUFFIX_OPT("--ds", "mybatis模式Mapper Java类后缀名"),
	PKG_DAO_OPT("--pd", "mybatis模式Mapper Java所在包名"),
	PKG_BASE_OPT("--pb", "mybatis模式base包名，设置之后entity、mapper、dao可不设置"),
	TABLE_PREFIX_OPT("--pf", "表名前缀，设置之后实体名不包含前缀部分"),
	TABLE_SUFFIX_OPT("--sf", "表名后缀，设置之后实体名不包含前缀部分"),
	TEMPLATE_ENTITY_OPT("--tme", "实体类模版"),
	TEMPLATE_MAPPER_JAVA_OPT("--tmmj", "mapper类模版"),
	TEMPLATE_MAPPER_XML_OPT("--tmmx", "mapperXml模版"),
	;

	public final String str;
	public final String desc;

	private CmdOpt(String str, String desc) {
		this.str = str;
		this.desc = desc;
	}
}
