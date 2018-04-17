package com.kony.tag.instrumentation.main;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ConfigVO;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.instrumentation.tasks.CleanupTask;
import com.kony.tag.instrumentation.tasks.CopyFilesTask;
import com.kony.tag.instrumentation.tasks.JSReviewEngine;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.util.eclipse.JSReviewUtil;


public class InstrumentationHelper implements ProjectConstants {
	
	protected ProjectConfig fetchReviewConfiguration(String channel) {
		ProjectConfig projectConfig = null;
		String projectLocation = null;
		
		projectLocation = JSReviewUtil.getProjectPath();
		
		// Read all this config information from projects..for now it is hard coded
		ConfigVO configVo = new ConfigVO();
		configVo.setChannel(channel);
		configVo.setProjectPath(projectLocation);
		configVo.setCodeBackupPath(projectLocation + CODE_INSTRUMENTATION_PATH + CODE_INSTRUMENTATION_BACKUP_PATH);
		configVo.setReviewOutputPath(projectLocation + CODE_INSTRUMENTATION_PATH + CODE_INSTURMENTATION_OUTPUT_PATH);
		
		// Create the config Object and keep it ready
		projectConfig = ProjectConfig.getCodeReviewConfig(configVo);
//		
//		// Read Project Properties
//		ProjectPropertiesReader.readProjectProperties(projectLocation);
//		
//		// Reset the review report
// 		ReportsUtil.initNewInstance(codeReviewConfig);
// 		CodeReviewStatus.reset();
		return projectConfig;
	}
	
	protected void copyFilesForInstrumentation(ProjectConfig projectConfig) {
		JSReviewUtil.printDetailedLog("copyFilesForReview START");
		CopyFilesTask copyFilesTask = new CopyFilesTask(projectConfig);
		copyFilesTask.run();
		JSReviewUtil.printDetailedLog("copyFilesForReview END");
	}
	
	protected void prepareReviewWorkspace(ProjectConfig projectConfig) {
		// Clean up the necessary temp directories needed for review
		CleanupTask cleanupTask = new CleanupTask(projectConfig);
		cleanupTask.run();
	}

	public void printErrorsWarnings() {
		
		// Print any warnings encountered during review
		if (CodeReviewStatus.getInstance().getWarningMessages() != null && 
				CodeReviewStatus.getInstance().getWarningMessages().size()>0) {
			JSReviewUtil.printToConsole("\n >>>>>>  WARNINGS : <<<<<<");
			for (String message : CodeReviewStatus.getInstance().getWarningMessages()) {
				JSReviewUtil.printToConsole(":: " + message);
			}
			JSReviewUtil.printToConsole(" <<<<<<  Done with WARNINGS : <<<<<< \n");
		}
		
		if (CodeReviewStatus.getInstance().getErrorMessages() != null && 
				CodeReviewStatus.getInstance().getErrorMessages().size()>0) {
			JSReviewUtil.printToConsole("\n >>>>>>  ERRORS : <<<<<<");
			for (String message : CodeReviewStatus.getInstance().getErrorMessages()) {
				JSReviewUtil.printToConsole("## " + message);
			}
			JSReviewUtil.printToConsole(" <<<<<<  Done with ERRORS : <<<<<< \n");
			JSReviewUtil.printToConsole("REVIEW COMPLETED with one or more Errors. See the Error log printed above !!");
		} else {
			JSReviewUtil.printToConsole("SUCCESS !! Transformation of Arabic Modules is Completed.");
		}
		
	}
	
	protected List<ReviewComment> executeCodeInstrumentation(ProjectConfig codeReviewConfig) {
		// First Review All JS Files
		ReviewUtil reviewUtil = new ReviewUtil();
		File[] jsFiles = null;
		List<ReviewComment> allComments = new ArrayList<ReviewComment>();
		List<ReviewComment> comments = null; 
		List<String> jsFileNames = new ArrayList<String>();
		
		JSReviewEngine jsReviewEngine = new JSReviewEngine(codeReviewConfig);
		
		 // This is a JS project. Review All JS Files
		    String[] fileLocationArray = {LOCATION_JS_GEN_SRC_FOLDER, LOCATION_JS_NONGEN_SRC_FOLDER};
			for(int k=0; k<2;k++){
				jsFiles = reviewUtil.fetchFiles(codeReviewConfig.getCodeBackupPath() + fileLocationArray[k]);
				System.out.println("jsfiles length: "+jsFiles.length);
				if(jsFiles.length <= 0){
					continue;
				}
				ArrayList<String> callBacks = new ArrayList<String>();
				codeReviewConfig.setCallBackArray(callBacks);
				
				codeReviewConfig.setInstrumentMode(k == 0 ? "forms" : "modules");
				
				for (int passNumber=1; passNumber<=2; passNumber++) {
					for(File reviewFile : jsFiles) {
						
						if (passNumber == 1) {
							jsFileNames.add(reviewFile.getName());
						}
						
						// Review Each Java Script file for all JS Rules
						try {

							// Rule #JS-001 : No. of lines in a function should be within a threshold limit
							jsReviewEngine.execute(reviewFile, passNumber);
						
						} catch (TagToolException cdrExcp) {
							CodeReviewStatus.getInstance().addErrorMessage(cdrExcp,"JS Review Pass# " + passNumber + " : " + cdrExcp.getErrorMessage());
						} catch (Exception excp) {
							CodeReviewStatus.getInstance().addErrorMessage(excp,"JS Review Pass# " + passNumber + " : " + excp.toString());
						}					
					}
				}
			}
			
		return allComments;
	}
}
