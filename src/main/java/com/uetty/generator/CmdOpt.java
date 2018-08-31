package com.uetty.generator;

import com.uetty.generator.types.TypeGen;

public enum CmdOpt {

	DB_SERVER_OPT("-s", "数据库连接符"),
	DB_USER_OPT("-u", "数据库用户名"),
	DB_PASS_OPT("-p", "数据库密码"),
	FUNCTION_SELECT_OPT("--fs", "选择功能模式：" + Config.FUNCTION_MYBATIS_GEN
			+ "-mybatic gencode" + "，" + Config.FUNCTION_CUSTOM_GEN + "-custom gencode"),
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
	TABLE_NAME_OPT("--tb", "custom模式自动生成代码的表名"),
	
	OUT_FILE_OPT("-o", "mybatis模式输出文件夹地址"),
	PKG_ENTITY_OPT("-e", "mybatis模式Entity包名"),
	PKG_MAPPER_OPT("-m", "mybatis模式Mapper XML所在java包名"),
	PKG_DAO_OPT("-d", "mybatis模式Mapper Java所在包名"),
	PKG_BASE_OPT("-b", "mybatis模式base包名，设置之后entity、mapper、dao可不设置");
	
	public final String str;
	public final String desc;
	
	private CmdOpt(String str, String desc) {
		this.str = str;
		this.desc = desc;
	}
}
