package com.uetty.generator;

import com.uetty.generator.constant.Config;
import com.uetty.generator.types.TypeGen;

public enum CmdOpt {
	
	FUNCTION_SELECT_OPT("-m", "\u9009\u62e9\u529f\u80fd\u6a21\u5f0f\uff08\u5fc5\u987b\u4f5c\u4e3a\u7b2c\u4e00\u53c2\u6570\uff09\uff1a"
			+ Config.FUNCTION_MYBATIS_GEN + "-mybatic gencode" + "\uff0c" + Config.FUNCTION_CUSTOM_GEN + "-custom gencode"),
	DB_DRIVER_OPT("-d", "\u6570\u636e\u5e93\u9a71\u52a8\u7c7b"),
	DB_SERVER_OPT("-s", "\u6570\u636e\u5e93\u8fde\u63a5\u7b26"),
	DB_USER_OPT("-u", "\u6570\u636e\u5e93\u7528\u6237\u540d"),
	DB_PASS_OPT("-p", "\u6570\u636e\u5e93\u5bc6\u7801"),
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
	DAO_SUFFIX_OPT("--ds", "mybatis\u6a21\u5f0fMapper Java\u7c7b\u540e\u7f00\u540d"),
	PKG_DAO_OPT("--pd", "mybatis\u6a21\u5f0fMapper Java\u6240\u5728\u5305\u540d"),
	PKG_BASE_OPT("--pb", "mybatis\u6a21\u5f0fbase\u5305\u540d\uff0c\u8bbe\u7f6e\u4e4b\u540eentity\u3001mapper\u3001dao\u53ef\u4e0d\u8bbe\u7f6e"),
	TABLE_PREFIX_OPT("--pf", "mybatis\u6a21\u5f0f\u8868\u540d\u524d\u7f00\uff0c\u8bbe\u7f6e\u4e4b\u540e\u5b9e\u4f53\u540d\u4e0d\u5305\u542b\u524d\u7f00\u90e8\u5206"),
	;
	
	public final String str;
	public final String desc;
	
	private CmdOpt(String str, String desc) {
		this.str = str;
		this.desc = desc;
	}
}
