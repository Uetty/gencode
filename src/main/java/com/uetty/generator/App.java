package com.uetty.generator;

public class App {

	private static final String FUNCTION_SELECT_OPT = "--fs";
	
	private static final String FUNCTION_MYBATIS_GEN = "mysbatis-gen";
	
	public static void main(String[] args) {
		
		for (int i = 1; i < args.length; i+=2) {
			if (FUNCTION_SELECT_OPT.equals(args[i-1])) {
				if (FUNCTION_MYBATIS_GEN.equals(args[i])) {
					MybaticCodeGen.main(args);
				}
				return;
			}
		}
		MybaticCodeGen.main(args);
	}
}
