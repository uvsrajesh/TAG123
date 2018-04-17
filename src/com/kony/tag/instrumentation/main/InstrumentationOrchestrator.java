package com.kony.tag.instrumentation.main;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.main.ReviewHelper;
import com.kony.tag.util.TAGToolsUtil;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class InstrumentationOrchestrator implements ProjectConstants {
	
	public void performCodeInstrumentation(long startTime) {
			
		ProjectConfig projectConfig = null;
		InstrumentationHelper instruHelper = new InstrumentationHelper();
				
		JSReviewUtil.printToConsole("Reading Project Configuration");
		JSReviewUtil.setTaskName("Reading Project Configuration");
		// First get the configuration info for performing the review
		projectConfig = instruHelper.fetchReviewConfiguration(TAGToolsUtil.getChannelName());
		
		JSReviewUtil.printToConsole("Deleting any old files in the Instrumentation folders ");
		JSReviewUtil.setTaskName("Deleting old files");
		// Clean up the code review backup folders
		instruHelper.prepareReviewWorkspace(projectConfig);
		
		JSReviewUtil.printToConsole("Copying code to Instrumentation folder");
		JSReviewUtil.setTaskName("Copying code for instrumentation");
		// Copy the Necessary Files to a temporary location for code review
		instruHelper.copyFilesForInstrumentation(projectConfig);
		
		JSReviewUtil.printToConsole("Executing Code Instrumentation");
		JSReviewUtil.setTaskName("Executing Code Instrumentation");
		// Review All Java Script Files & prepare Review Report 
		instruHelper.executeCodeInstrumentation(projectConfig);
		
		// Print any errors & warnings encountered during review
		//reviewHelper.printErrorsWarnings();
		JSReviewUtil.printToConsole("\nSee 'codeInstrumentation/output' folder for instrumented files.");
		
		long endTime = System.currentTimeMillis();
		JSReviewUtil.printToConsole("\nTotal Time Taken for Instrumentation is...... "+((endTime-startTime)/1000) +" seconds");
	}
}

