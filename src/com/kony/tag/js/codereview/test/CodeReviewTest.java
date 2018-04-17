package com.kony.tag.js.codereview.test;

import java.util.Date;
import java.util.LinkedHashSet;

import com.kony.tag.arabiclayout.handlers.ExecutionModeHandler;
import com.kony.tag.arabiclayout.main.ArabicLayoutOrchestrator;
import com.kony.tag.arabiclayout.util.ArabicLayoutPropertiesUtil;
import com.kony.tag.arabiclayout.util.BuildPropertiesUtil;
import com.kony.tag.js.codereview.config.ReviewModeConstants;
import com.kony.tag.js.codereview.main.ReviewOrchestrator;
import com.kony.tag.js.codereview.tasks.ant.CodeReviewAntTask;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class CodeReviewTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("START REVIEW "+args);
		try {
			executeCodeReview();
			//executeRTLTool();
			
 		}catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}

		System.out.println("DONE REVIEW");
	}
	
	public static void executeCodeReview() {

		try {
			
			String[] args1 = {ReviewModeConstants.TEST_LOCATION_PROJECT_PATH};
			ReviewModeConstants.INSTRUMENT_ON = "forms";
			//ReviewModeConstants.INSTRUMENT_ON = "modules\\js";
			//ReviewModeConstants.INSTRUMENT_ON = "spa";
			CodeReviewAntTask.main(args1);
			
 		}catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}

		System.out.println("COMPLETED CODE REVIEW");
	}
	
	public static void executeRTLTool() {

		System.out.println("STARTED TRANSFORMING FILES");
		try {
			String[] args = {ReviewModeConstants.TEST_LOCATION_PROJECT_PATH}; 
			ExecutionModeHandler.init();
			ExecutionModeHandler.setExecutionMode(ExecutionModeHandler.EXEC_MODE_INCREMENTAL_CODE_GEN);
			ArabicLayoutPropertiesUtil.setChannelName("Android");
			//ArabicLayoutPropertiesUtil.setChannelName("IPhone");
			//ArabicLayoutPropertiesUtil.setChannelName("SPA-IPhone");
			
			ArabicLayoutOrchestrator reviewOrchestrator =  new ArabicLayoutOrchestrator();
			
			if (args != null && args.length>0 && args[0] != null && args[0].trim().length()>0) {
				System.out.println("PROJECT FOLDER : " + args[0]);
			} else {
				System.out.println("Unable to locate the Project !! Project Folder should be passed as an input arg to this task !! ");
				return;
			}
			
			ReviewModeConstants.TESTING_MODE_FLAG = true;
			
			reviewOrchestrator.performCodeReview((new Date()).getTime());
 		}catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}

		System.out.println("COMPLETED TRANSFORMING FILES");
	}
	
	public static void executeCleanBuild(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("STARTED TRANSFORMING FILES");
		try {
			
			ArabicLayoutOrchestrator reviewOrchestrator =  new ArabicLayoutOrchestrator();
			
			ReviewModeConstants.TESTING_MODE_FLAG = true;
			
			if (args != null && args.length>0 && args[0] != null && args[0].trim().length()>0) {
				System.out.println("PROJECT FOLDER : " + args[0]);
			} else {
				System.out.println("Unable to locate the Project !! Project Folder should be passed as an input arg to this task !! ");
				return;
			}
			
			BuildPropertiesUtil.init(args[0]);
			
			LinkedHashSet<String> channelList = BuildPropertiesUtil.getSelectedChannelList();
			if(channelList.size() == 0) {
				System.out.println("Invalid/No Platforms Platforms selected. Please check your build.properties file");
				return;
			}
			
			int status = BuildPropertiesUtil.executeBuild(args[0]);
			
			if(status == 0){
				System.out.println("\n**************  Build Execution is successful. **************\n");
			} else {
				System.out.println("\n**************  Build Execution Failed. Please check the console Logs. **************\n");
				return;
			}
			
			for(String channel : channelList){	
				reviewOrchestrator.performCodeReview((new Date()).getTime());
				
				int buildStatus = BuildPropertiesUtil.executeBuild(args[0]);
				
				if(buildStatus == 0){
					JSReviewUtil.printToConsole("\n**************  Build execution for Channel "+channel+" is successful **********\n");
				}
			}
			
 		}catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}

		System.out.println("COMPLETED TRANSFORMING FILES");
	}
}
