package com.uetty.generator;

import com.uetty.generator.constant.Config;
import com.uetty.generator.types.TypeGen;

public enum CmdOpt {

//	DB_SERVER_OPT("-s", "数据库连接符"),
//	DB_USER_OPT("-u", "数据库用户名"),
//	DB_PASS_OPT("-p", "数据库密码"),
//	FUNCTION_SELECT_OPT("--fs", "选择功能模式：" + Config.FUNCTION_MYBATIS_GEN
//			+ "-mybatic gencode" + "，" + Config.FUNCTION_CUSTOM_GEN + "-custom gencode"),
//	GEN_TYPE_OPT("--gt", "custom模式执行功能（可累加）：" 
//			+ Config.GEN_TYPE_INSERT + " - 生成插入相关jdbc语句、"
//			+ Config.GEN_TYPE_UPDATE + " - 生成更新相关jdbc语句、"
//			+ Config.GEN_TYPE_SELECT + " - 生成查询相关jdbc语句"),
//	STMT_NAME_OPT("--stmt", "custom模式PrepareStatement变量名"),
//	JAVA_OBJ_NAME_OPT("--jo", "custom模式实体变量名"),
//	RESULT_SET_NAME_OPT("--rs", "custom模式ResultSet变量名"),
//	TYPE_TINYINT_OPT("--ti", "custom模式TINYINT(1)是否转为Boolean："
//			+ TypeGen.TYPE_NO_BOOLEAN + " - 否、"
//			+ TypeGen.TYPE_WITH_BOOLEAN + " - 是"),
//	TABLE_NAME_OPT("--tb", "custom模式自动生成代码的表名(必填项)"),
//	
//	OUT_FILE_OPT("-o", "mybatis模式输出文件夹地址"),
//	PKG_ENTITY_OPT("-e", "mybatis模式Entity包名"),
//	PKG_MAPPER_OPT("-m", "mybatis模式Mapper XML所在java包名"),
//	PKG_DAO_OPT("-d", "mybatis模式Mapper Java所在包名"),
//	PKG_BASE_OPT("-b", "mybatis模式base包名，设置之后entity、mapper、dao可不设置");
//	
//	public final String str;
//	public final String desc;
//	
//	private CmdOpt(String str, String desc) {
//		this.str = str;
//		this.desc = desc;
//	}
	
	DB_SERVER_OPT("-s", "\u6570\u636e\u5e93\u8fde\u63a5\u7b26"),
	DB_USER_OPT("-u", "\u6570\u636e\u5e93\u7528\u6237\u540d"),
	DB_PASS_OPT("-p", "\u6570\u636e\u5e93\u5bc6\u7801"),
	FUNCTION_SELECT_OPT("-m", "\u9009\u62e9\u529f\u80fd\u6a21\u5f0f\uff1a" + Config.FUNCTION_MYBATIS_GEN
			+ "-mybatic gencode" + "\uff0c" + Config.FUNCTION_CUSTOM_GEN + "-custom gencode"),
	TABLE_NAME_OPT("--tb", "custom\u6a21\u5f0f\u81ea\u52a8\u751f\u6210\u4ee3\u7801\u7684\u8868\u540d(\u5fc5\u586b\u9879)"),
	GEN_TYPE_OPT("--gt", "custom\u6a21\u5f0f\u6267\u884c\u529f\u80fd\uff08\u53ef\u7d2f\u52a0\uff09\uff1a" 
			+ Config.GEN_TYPE_INSERT + " - \u751f\u6210\u63d2\u5165\u76f8\u5173jdbc\u8bed\u53e5\u3001"
			+ Config.GEN_TYPE_UPDATE + " - \u751f\u6210\u66f4\u65b0\u76f8\u5173jdbc\u8bed\u53e5\u3001"
			+ Config.GEN_TYPE_SELECT + " - \u751f\u6210\u67e5\u8be2\u76f8\u5173jdbc\u8bed\u53e5"),
	STMT_NAME_OPT("--stmt", "custom\u6a21\u5f0fPrepareStatement\u53d8\u91cf\u540d"),
	JAVA_OBJ_NAME_OPT("--jo", "custom\u6a21\u5f0f\u5b9e\u4f53\u53d8\u91cf\u540d"),
	RESULT_SET_NAME_OPT("--rs", "custom\u6a21\u5f0fResultSet\u53d8\u91cf\u540d"),
	TYPE_TINYINT_OPT("--ti", "custom\u6a21\u5f0fTINYINT(1)\u662f\u5426\u8f6c\u4e3aBoolean\uff1a"
			+ TypeGen.TYPE_NO_BOOLEAN + " - \u5426\u3001"
			+ TypeGen.TYPE_WITH_BOOLEAN + " - \u662f"),
	
	OUT_FILE_OPT("--of", "mybatis\u6a21\u5f0f\u8f93\u51fa\u6587\u4ef6\u5939\u5730\u5740"),
	PKG_ENTITY_OPT("--pe", "mybatis\u6a21\u5f0fEntity\u5305\u540d"),
	PKG_MAPPER_OPT("--pm", "mybatis\u6a21\u5f0fMapper XML\u6240\u5728java\u5305\u540d"),
	PKG_DAO_OPT("--pd", "mybatis\u6a21\u5f0fMapper Java\u6240\u5728\u5305\u540d"),
	PKG_BASE_OPT("--pb", "mybatis\u6a21\u5f0fbase\u5305\u540d\uff0c\u8bbe\u7f6e\u4e4b\u540eentity\u3001mapper\u3001dao\u53ef\u4e0d\u8bbe\u7f6e");
	
	public final String str;
	public final String desc;
	
	private CmdOpt(String str, String desc) {
		this.str = str;
		this.desc = desc;
	}
}
