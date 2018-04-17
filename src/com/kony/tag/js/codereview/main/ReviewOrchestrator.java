package com.kony.tag.js.codereview.main;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.util.ReportsUtil;
import com.kony.tag.util.RuleFactory;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ReviewOrchestrator implements ProjectConstants {
	
	public void performCodeReview(long startTime) {
		
		ProjectConfig codeReviewConfig = null;
		ReviewHelper reviewHelper = new ReviewHelper();
		List<ReviewComment> comments = null;
		
		// Clean up all cached instances of Rules
		RuleFactory.init();
		
		JSReviewUtil.printToConsole("Reading Review Configuration");
		JSReviewUtil.setTaskName("Reading Review Configuration");
		// First get the configuration info for performing the review
		codeReviewConfig = reviewHelper.fetchReviewConfiguration();
		
		JSReviewUtil.printToConsole("Deleting any old files in the code review folders ");
		JSReviewUtil.setTaskName("Deleting old files");
		// Clean up the code review backup folders
		//reviewHelper.prepareReviewWorkspace(codeReviewConfig);
		
		JSReviewUtil.printToConsole("Copying code to code review folder");
		JSReviewUtil.setTaskName("Copying code for review");
		// Copy the Necessary Files to a temporary location for code review
		//reviewHelper.copyFilesForReview(codeReviewConfig);
		
		JSReviewUtil.printToConsole("Executing Javascript Code Review");
		JSReviewUtil.setTaskName("Executing Javascript Code Review");
		// Review All Java Script Files & prepare Review Report 
		comments = reviewHelper.executeJavaScriptReview(codeReviewConfig);
		
		JSReviewUtil.setTaskName("Preparing Javascript Review Report");
		//ReportsUtil.getInstance().prepareJSReviewReport(comments);
		
		JSReviewUtil.printToConsole("Executing Form Review");
		JSReviewUtil.setTaskName("Executing Form Review");
		// Review All Java Script Files & prepare Review Report 
		//comments = reviewHelper.executeFormReview(codeReviewConfig);		
		
		JSReviewUtil.setTaskName("Preparing Form Review Report");
		//ReportsUtil.getInstance().prepareFormReviewReport(comments);
		
		// Print Review Summary as a dash-board
		JSReviewUtil.setTaskName("Preparing Summary Report");
		//ReportsUtil.getInstance().prepareReviewSummaryReport();
		
		// Print any errors & warnings encountered during review
		//reviewHelper.printErrorsWarnings();
		//JSReviewUtil.printToConsole("\nSee 'output' folder for 'SUMMARY' Report and All Review Comments.");
		
		long endTime = System.currentTimeMillis();
		//JSReviewUtil.printToConsole("\nTotal Time Taken for the Review is...... "+((endTime-startTime)/1000) +" seconds");
	}
}
