package com.uetty.generator;

import com.uetty.generator.constant.Config;

public class App {

	private static final String HELP_OPT = "--help";
	
	public static void main(String[] args) {
		if (args.length == 0) {
			printHelp();
			return;
		}
		if (args.length >= 1) {
			if (HELP_OPT.equals(args[0])) {
				printHelp();
				return;
			}
		}
		if (args.length >= 2) {
			if (CmdOpt.FUNCTION_SELECT_OPT.str.equals(args[0])) {
				if (Config.FUNCTION_MYBATIS_GEN.equals(args[1])) {
					CustomMybatisCodeGen.main(args);
					return;
				}
				if (Config.FUNCTION_CUSTOM_GEN.equals(args[1])) {
					CustomCodeGen.main(args);
					return;
				}
				System.out.println(CmdOpt.FUNCTION_SELECT_OPT.str + " value invalid");
				return;
			}
		}
		CustomMybatisCodeGen.main(args);
	}
	
	private static void printOptHelp(String opt, String desc) {
		System.out.println(opt + "\t\t" + desc);
	}
	
	private static void printHelp() {
		System.out.println("HELP");
		for (CmdOpt opt : CmdOpt.values()) {
			printOptHelp(opt.str, opt.desc);
		}
	}
}
