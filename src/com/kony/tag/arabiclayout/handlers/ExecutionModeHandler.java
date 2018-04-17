package com.kony.tag.arabiclayout.handlers;

public class ExecutionModeHandler {
	
	public static String EXEC_MODE_INCREMENTAL_CODE_GEN = "incerementalCodeGen";
	public static String EXEC_MODE_TYPE_CLEAN_BUILD = "cleanBuild";
	
	private static String executionMode = null;

	public static void setExecutionMode(String executionMode) {
		ExecutionModeHandler.executionMode = executionMode;
	}
	
	public static void init(){
		executionMode = null;
	}
	
	public static boolean isIncrementalCodeGenMode(){
		boolean mode = false;
		
		if (null != executionMode && 
				executionMode.equals(EXEC_MODE_INCREMENTAL_CODE_GEN)) {
			mode = true;
		}
		
		return mode;
	}
	
	public static boolean isCleanBuildMode(){
		boolean mode = false;
		
		if (null != executionMode && 
				executionMode.equals(EXEC_MODE_TYPE_CLEAN_BUILD)) {
			mode = true;
		}
		
		return mode;
	}	

}
