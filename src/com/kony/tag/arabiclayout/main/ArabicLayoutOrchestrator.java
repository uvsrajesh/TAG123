package com.kony.tag.arabiclayout.main;

import java.util.LinkedHashSet;

import com.kony.tag.arabiclayout.handlers.ExecutionModeHandler;
import com.kony.tag.arabiclayout.impl.ArLayoutManager;
import com.kony.tag.arabiclayout.util.BuildPropertiesUtil;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.util.RuleFactory;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ArabicLayoutOrchestrator implements ProjectConstants {
	
	private void performCodeReview(long startTime, String channel) {
		
		ProjectConfig projectConfig = null;
		ArabicLayoutHelper reviewHelper = new ArabicLayoutHelper();
		
		JSReviewUtil.printToConsole("Reading Project Configuration Values");
		JSReviewUtil.setTaskName("Reading Configuration");
		// First get the configuration info for performing the review
		projectConfig = reviewHelper.fetchReviewConfiguration(channel);
		
		JSReviewUtil.printToConsole("Validating if JS Modules are latest");
		JSReviewUtil.setTaskName("Validating ...");
		reviewHelper.validateCodeGen(ArLayoutManager.getChannel(), projectConfig);
		
		if(channel != null) {
			JSReviewUtil.printToConsole("Creating channel specific folders ");
			JSReviewUtil.setTaskName("Creating temporary channel folders");
			
			ReviewUtil reviewUtil = new ReviewUtil();
			reviewUtil.createDirectory(projectConfig.getReviewOutputPath() + FILE_DELIMITER + channel);
			reviewUtil.createDirectory(projectConfig.getCodeBackupPath() + FILE_DELIMITER + channel);
		} else {
			JSReviewUtil.printToConsole("Deleting any old temporary folders that store transformed files ");
			JSReviewUtil.setTaskName("Deleting temp folders");
			// Clean up the code review backup folders
			reviewHelper.prepareReviewWorkspace(projectConfig);
		}
		
		JSReviewUtil.printToConsole("Copying JS Modules to the 'arLayout' folder");
		JSReviewUtil.setTaskName("Copying JS Modules");
		// Copy the Necessary Files to a temporary location for code review
		reviewHelper.copyFilesForReview(projectConfig);
		
		JSReviewUtil.printToConsole("Transforming UI Forms for Reverse Layout");
		JSReviewUtil.setTaskName("Transforming UI Forms");
		reviewHelper.executeArabicLayoutReversal(projectConfig);
		
		JSReviewUtil.printToConsole("Verifying if Arabic theme is required");
		JSReviewUtil.setTaskName("Arabic Theme verification");
		reviewHelper.createArabicTheme(projectConfig);
		
		JSReviewUtil.printToConsole("Provisioning for Custom Duplicated Forms");
		JSReviewUtil.setTaskName("Provisioning for Duplicated Forms");
		// Copy the Necessary Files to a temporary location for code review
		reviewHelper.provisionForDuplicateForms(projectConfig);

		JSReviewUtil.printToConsole("Generating Latest Version of Layout Manager Module");
		JSReviewUtil.setTaskName("Generating Layout Manager Module");
		ArLayoutManager.createAutogenModule();
		
		JSReviewUtil.printToConsole("Copying the transformed JS Modules to the project ");
		JSReviewUtil.setTaskName("Updating JS Modules");
		ArLayoutManager.copyTransformedJSModules(projectConfig);
		
		JSReviewUtil.printToConsole("\n\nWARNING COUNT: " + ArLayoutManager.getWarnings().size());
		JSReviewUtil.setTaskName("Printing Warnings");
		ArLayoutManager.printWarnings();		
		
		// Print any errors & warnings encountered during review
		reviewHelper.printErrorsWarnings();
		JSReviewUtil.printToConsole("\nDone with Transformation of UI & Non UI Modules needed to support Reverse Layout .");
		
		long endTime = System.currentTimeMillis();
		JSReviewUtil.printToConsole("\nTotal Time Taken for generating Transformed UI & Non UI Modules :: "+((endTime-startTime)/1000) +" seconds");
		
	}
	
	public void performCodeReview(long startTime) {
		// Clean up all cached instances of Rules
		RuleFactory.init();
		ArLayoutManager.init();
		
		if(ExecutionModeHandler.isIncrementalCodeGenMode()){
			performCodeReview(startTime, null);
		} else if(ExecutionModeHandler.isCleanBuildMode()){

			JSReviewUtil.printToConsole("SEE-PERF-LOG: step - 3A CLEAN BUILD START  ALL CHANNELS" + (new java.util.Date()).toString());
			String projectLocation = JSReviewUtil.getProjectPath();
			
			LinkedHashSet<String> channelList = BuildPropertiesUtil.getSelectedChannelList();
			
			for(String channel : channelList){
				JSReviewUtil.printToConsole("SEE-PERF-LOG: step - 3A START for '" + channel + "' : " + (new java.util.Date()).toString());
				JSReviewUtil.setTaskName("Incremental Code Gen for channel "+channel);
				performCodeReview(startTime, channel);
				
				if(channelList.size() > 1) {
					JSReviewUtil.setTaskName("Updating build.properties");
					BuildPropertiesUtil.updateBuildProperties(channel, projectLocation);
				}
				JSReviewUtil.setTaskName("Incremental build for channel '"+channel+"'");
				JSReviewUtil.printToConsole("\n**************  Building application for Channel '"+channel+"' **********\n");
				
				JSReviewUtil.printToConsole("SEE-PERF-LOG: step - 3A FOR LOOP START" + (new java.util.Date()).toString());
				int status = BuildPropertiesUtil.executeBuild(projectLocation);
				JSReviewUtil.printToConsole("SEE-PERF-LOG: step - 3A FOR LOOP END" + (new java.util.Date()).toString());
				
				if(status == 0){
					JSReviewUtil.printToConsole("\n**************  Build execution for Channel '"+channel+"' is successful **********\n");
				}
				JSReviewUtil.printToConsole("SEE-PERF-LOG: step - 3A END for " + channel + " : " +  (new java.util.Date()).toString());
			}
			
			JSReviewUtil.printToConsole("SEE-PERF-LOG: step - 3A CLEAN BUILD END  ALL CHANNELS" + (new java.util.Date()).toString());
			
		}
		
		
		
		
	}
}

