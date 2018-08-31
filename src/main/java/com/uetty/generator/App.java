package com.uetty.generator;

public class App {

	private static final String HELP_OPT = "--help";
	
	private static final String FUNCTION_SELECT_OPT = "--fs";
	
	public static void main(String[] args) {
//		if (args.length == 0) {
//			printHelp();
//		}
		if (args.length >= 1) {
			if (HELP_OPT.equals(args[0])) {
				printHelp();
			}
		}
		if (args.length >= 2) {
			if (FUNCTION_SELECT_OPT.equals(args[0])) {
				if (Config.FUNCTION_MYBATIS_GEN.equals(args[1])) {
					MybaticCodeGen.main(args);
				}
				if (Config.FUNCTION_CUSTOM_GEN.equals(args[0])) {
					CustomCodeGen.main(args);
				}
				return;
			}
		}
		MybaticCodeGen.main(args);
	}
	
	private static void printOptHelp(String opt, String desc) {
		System.out.println(opt + "  \t" + desc);
	}
	
	private static void printHelp() {
		System.out.println("HELP");
		for (CmdOpt opt : CmdOpt.values()) {
			printOptHelp(opt.str, opt.desc);
		}
	}
}
