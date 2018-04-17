package com.kony.tag.js.codereview.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.js.codereview.config.ReportSummaryVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ReportsUtil implements ProjectConstants {
	
	private static ProjectConfig codeReviewConfig = null;
	private static List<ReportSummaryVo> reportSummaryList = null;
	private static ReportSummaryVo searchReportSummaryVo = null;
	private static Map<String,String> reportCategoryMap = null;
	private static List <String> enabledCommentCategories = null;
	private static final String NIL = "-";
	
	private static ReportsUtil reportsUtil= null;
	
	protected ReportsUtil() {
		// Do not use
	}
	
	public static ReportsUtil initNewInstance(ProjectConfig reviewConfig) {
		reportsUtil = new ReportsUtil();
		
		codeReviewConfig = reviewConfig;
		reportSummaryList = new ArrayList<ReportSummaryVo>();
		searchReportSummaryVo = new ReportSummaryVo(null, null, false);
		reportCategoryMap = new HashMap<String, String>();
		 
		initEnabledCommentCatgeories();

		return reportsUtil;
	}
	
	public static ReportsUtil getInstance() {
		return reportsUtil;
	}
	
	public void initReportSummary(String ruleId, String[] ruleDescriptions, boolean isEnabled) {
		
		if (ruleDescriptions == null || ruleDescriptions.length==0) {
			getReportSummaryVo(ruleId, BLANK,isEnabled);
		} else {
			for (String ruleDesc : ruleDescriptions) {
				getReportSummaryVo(ruleId, ruleDesc,isEnabled);
			}
		}
	}

	private ReportSummaryVo getReportSummaryVo(String ruleId, String ruleDesc) {
		return getReportSummaryVo(ruleId, ruleDesc, true);
	}
	
	private ReportSummaryVo getReportSummaryVo(String ruleId, String ruleDesc, boolean isEnabled) {
		ReportSummaryVo reportSummaryVo = null;

		int index = -1;
		searchReportSummaryVo.setRuleId(ruleId);
		searchReportSummaryVo.setRuleDesc(ruleDesc);
		
		index = reportSummaryList.indexOf(searchReportSummaryVo);
		
		if (index >=0) {
			reportSummaryVo = reportSummaryList.get(index);
		} else {
			reportSummaryVo = new ReportSummaryVo(ruleId, ruleDesc, isEnabled);
			reportSummaryList.add(reportSummaryVo);
		}
		
		return reportSummaryVo;
		
	}
	
	public String getReviewCommentCategory(String ruleId, String reviewDirectory) {
		String category = null;
		
		if (null == reviewDirectory || reviewDirectory.trim().length()==0 || ruleId==null || ruleId.trim().length()==0) {
			category = COMMENT_CATEGORY_MISC;
		}
		
		category = reportCategoryMap.get(ruleId+reviewDirectory);
		
		if (category == null || category.trim().length() == 0) {
			if (reviewDirectory.startsWith(LOCATION_JS_MODULES) && ruleId.startsWith(JS)) {
				category = COMMENT_CATEGORY_JS;
			} else if (reviewDirectory.indexOf(MOBILE) >=0 && ruleId.startsWith(FRM)) {
				if (reviewDirectory.endsWith(MOBILE_SEPARATOR)) {
					category = COMMENT_CATEGORY_MOBILE_APP;
				} else {
					category = COMMENT_CATEGORY_FORKED_MOBILE_APP;
				}
			} else if (reviewDirectory.indexOf(TABLET) >=0 && ruleId.startsWith(FRM)) {
				if (reviewDirectory.endsWith(TABLET_SEPARATOR)) {
					category = COMMENT_CATEGORY_TABLET_APP;
				} else {
					category = COMMENT_CATEGORY_FORKED_TABLET_APP;
				}
				
			} else if (reviewDirectory.indexOf(DESKTOP) >=0 && ruleId.startsWith(FRM)) {
				category = COMMENT_CATEGORY_DESKTOP_APP;
			} else {
				category = COMMENT_CATEGORY_MISC; 
			}
			
			reportCategoryMap.put(ruleId+reviewDirectory, category);
		}
		
		return category;
	}	
	
	public void prepareJSReviewReport(List<ReviewComment> reviewComments)  {
		if (isValidReport(reviewComments)) {
			if (reviewComments == null || reviewComments.size()==0) {
				JSReviewUtil.printToConsole("No Review comments for JavaScript files");
				return;
			}
			
			JSReviewUtil.printToConsole("Creating Report for Java Script Review Comments" );

			prepareSingleFileReport(reviewComments, JS_REVIEW_COMMENTS_FILE);
		}
		
	}
	
	public void prepareFormReviewReport(List<ReviewComment> reviewComments)  {
		if (isValidReport(reviewComments)) {
			if (reviewComments == null || reviewComments.size()==0) {
				JSReviewUtil.printToConsole("No Review comments for UI Forms");
				return;
			}
			
			JSReviewUtil.printToConsole("Creating Report for UI Form Review Comments" );
			
			prepareFormReviewReports(reviewComments);
		}
	}

	public void prepareMiscCommentsReport(List<ReviewComment> reviewComments)  {
		if (isValidReport(reviewComments)) {
			if (reviewComments == null || reviewComments.size()==0) {
				JSReviewUtil.printToConsole("No Misc Comments");
				return;
			}
			
			JSReviewUtil.printToConsole("Creating Report for Misc Comments" );
			
			prepareSingleFileReport(reviewComments, MISC_COMMENTS_FILE);

		}
	}
	
	public void prepareUnusedFunctionsReport(List<ReviewComment> reviewComments)  {
		if (isValidReport(reviewComments)) {
			if (reviewComments == null || reviewComments.size()==0) {
				JSReviewUtil.printToConsole("No Unused Functions found");
				return;
			}
			
			JSReviewUtil.printToConsole("Creating Report for Unused Functions" );
			
			prepareSingleFileReport(reviewComments, UNUSED_FUNCTIONS_COMMENTS_FILE);

		}
	}
	
	public void prepareSecurityReviewReport(List<ReviewComment> reviewComments)  {
		if (isValidReport(reviewComments)) {
			if (reviewComments == null || reviewComments.size()==0) {
				JSReviewUtil.printToConsole("No Security issues found");
				return;
			}
			
			JSReviewUtil.printToConsole("Creating Report for Security Review" );
			
			prepareSingleFileReport(reviewComments, SECURITY_REVIEW_COMMENTS_FILE);

		}
	}
	
	public void prepareUnusedImagesReport(List<ReviewComment> reviewComments)  {
		if (isValidReport(reviewComments)) {
			if (reviewComments == null || reviewComments.size()==0) {
				JSReviewUtil.printToConsole("No Unused Images found");
				return;
			}
			
			JSReviewUtil.printToConsole("Creating Report for Unused Images" );
			
			prepareSingleFileReport(reviewComments, UNUSED_IMAGES_COMMENTS_FILE);
		}		
	}

	public void prepareUnusedSkinsReport(List<ReviewComment> reviewComments)  {
		if (isValidReport(reviewComments)) {
			if (reviewComments == null || reviewComments.size()==0) {
				JSReviewUtil.printToConsole("No Unused Skins found");
				return;
			}
			
			JSReviewUtil.printToConsole("Creating Report for Unused Skins" );
			
			prepareSingleFileReport(reviewComments, UNUSED_SKINS_COMMENTS_FILE);
		}		
	}
	
	public void prepareUnusedI18Report(List<ReviewComment> reviewComments)  {
		if (isValidReport(reviewComments)) {
			if (reviewComments == null || reviewComments.size()==0) {
				JSReviewUtil.printToConsole("No Unused I18 found");
				return;
			}
			
			JSReviewUtil.printToConsole("Creating Report for Unused I18 Keys" );
			
			prepareSingleFileReport(reviewComments, UNUSED_I18KEYS_COMMENTS_FILE);
		}		
	}

	private void prepareFormReviewReports(List<ReviewComment> reviewComments)  {
		
		List<ReviewComment> reportComments = new ArrayList<ReviewComment>();

		/*
	} else if (reviewDirectory.indexOf(MOBILE) >=0 && ruleId.startsWith(FRM)) {
		if (reviewDirectory.endsWith(MOBILE_SEPARATOR)) {
			category = COMMENT_CATEGORY_MOBILE_APP;
		} else {
			category = COMMENT_CATEGORY_FORKED_MOBILE_APP;
		}
	} else if (reviewDirectory.indexOf(TABLET) >=0 && ruleId.startsWith(FRM)) {
		if (reviewDirectory.endsWith(TABLET_SEPARATOR)) {
			category = COMMENT_CATEGORY_TABLET_APP;
		} else {
			category = COMMENT_CATEGORY_FORKED_TABLET_APP;
		}
		
	} else if (reviewDirectory.indexOf(DESKTOP) >=0 && ruleId.startsWith(FRM)) {
		category = COMMENT_CATEGORY_DESKTOP_APP;
	} else {
		
		*/
		
		// Prepare the report for all Mobile App forms, dialogs and templates (forked forms not covered)
		reportComments.clear();
		for (ReviewComment comment: reviewComments) {
			if (comment.getFileLocation() == null || comment.getFileLocation().length()==0) { 
				continue;
			}
		
			if (comment.getFileLocation().indexOf(MOBILE) >=0 && comment.getRuleId().startsWith(FRM) 
					&& comment.getFileLocation().endsWith(MOBILE_SEPARATOR)) { 
				reportComments.add(comment);
			}
			
		}
		prepareSingleFileReport(reportComments, MOBILE_FORM_REVIEW_COMMENTS_FILE);
		
		// Prepare the report for all forked forms of Mobile App including forked dialogs
		reportComments.clear();
		for (ReviewComment comment: reviewComments) {
			if (comment.getFileLocation() == null) { 
				continue;
			}
		
			if (comment.getFileLocation().indexOf(MOBILE) >=0 && comment.getRuleId().startsWith(FRM) 
					&& !comment.getFileLocation().endsWith(MOBILE_SEPARATOR)) { 
				reportComments.add(comment);
			}
		}
		prepareSingleFileReport(reportComments, FORKED_MOBILE_FORM_REVIEW_COMMENTS_FILE);
		
		// Prepare the report for all Tablet App forms, dialogs and templates (forked forms not covered)
		reportComments.clear();
		for (ReviewComment comment: reviewComments) {
			if (comment.getFileLocation() == null) { 
				continue;
			}
		
			if (comment.getFileLocation().indexOf(TABLET) >=0 && comment.getRuleId().startsWith(FRM) 
					&& comment.getFileLocation().endsWith(TABLET_SEPARATOR)) { 
				reportComments.add(comment);
			}
			
		}
		prepareSingleFileReport(reportComments, TABLET_FORM_REVIEW_COMMENTS_FILE);
		
		// Prepare the report for all forked forms of Tablet App including forked dialogs
		reportComments.clear();
		for (ReviewComment comment: reviewComments) {
			if (comment.getFileLocation() == null) { 
				continue;
			}
		
			if (comment.getFileLocation().indexOf(TABLET) >=0 && comment.getRuleId().startsWith(FRM) 
					&& !comment.getFileLocation().endsWith(TABLET_SEPARATOR)) { 
				reportComments.add(comment);
			}
		}
		prepareSingleFileReport(reportComments, FORKED_TABLET_FORM_REVIEW_COMMENTS_FILE);
		
		// Prepare the report for all Desktop App forms, dialogs and templates (forked forms not covered)
		reportComments.clear();
		for (ReviewComment comment: reviewComments) {
			if (comment.getFileLocation() == null) { 
				continue;
			}
		
			if (comment.getFileLocation().indexOf(DESKTOP) >=0 && comment.getRuleId().startsWith(FRM)) { 
				reportComments.add(comment);
			}
			
		}
		prepareSingleFileReport(reportComments, DESKTOP_FORM_REVIEW_COMMENTS_FILE);

		
	}
	
	private void prepareSingleFileReport(List<ReviewComment> reviewComments, String reportFileName)  {
				
		List<String> formattedComments = null;
	    
		if (null == reviewComments || reviewComments.size() == 0) {
			//JSReviewUtil.printToConsole("No Review Comments");
			return;
		}

		Collections.sort(reviewComments);
		formattedComments = fetchFormattedComments(reviewComments);
		createReportFile(formattedComments, reportFileName);
 	}
	
	private void createReportFile(List<String> comments, String reportFileName)  {
		
		File file = new File(codeReviewConfig.getReviewOutputPath()+reportFileName);
	    Writer output = null;
	    
	    if (comments == null || comments.size()==0) {
	    	return;
	    }
		
		try {
			output = new BufferedWriter(new FileWriter(file, true));

			for (String comment : comments) {
				//System.out.println(comment);
				//output.append(comment);
				output.write(comment);
				output.write(NEW_LINE);
			}
		} catch (IOException excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, "Error Preparing Report : " + excp.toString());
		}  catch (Exception e) {
			CodeReviewStatus.getInstance().addErrorMessage(e,e.toString());
		}
		
		finally {
			try {
				if (output != null) {
					output.flush();
					output.close();
				}
			} catch (IOException excp) {
				CodeReviewStatus.getInstance().addErrorMessage(excp,"Error Preparing Report : " + excp.toString());
			}  catch (Exception e) {
				CodeReviewStatus.getInstance().addErrorMessage(e,e.toString());
			}
		}
		
 	}
	
	
	private List<String> fetchFormattedComments(List<ReviewComment> reviewComments) {
		List<String> formattedComments = new ArrayList<String>();
		String comment = null;
		String category = null;
		ReportSummaryVo reportSummaryVo = null;
		String lineNum;

		//First add the header
		comment = fetchFormattedComment("RULE-ID", "Comment", "FileName","File Location", "Line Number", "Severity", "Comment Type", "Suggestion" , "Code Reference");
		formattedComments.add(comment);
		
		if (null == reviewComments || reviewComments.size()==0) {
			comment = fetchFormattedComment(NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE);
			formattedComments.add(comment);
		} else {
			for(ReviewComment reviewComment : reviewComments) {
				
				if (reviewComment.getLineNumber()<=0) {
					lineNum = NIL;
				} else {
					lineNum = BLANK + reviewComment.getLineNumber();
				}
				comment = fetchFormattedComment(
						reviewComment.getRuleId(), 
						reviewComment.getShortDescripton(),
						reviewComment.getFileName(),
						reviewComment.getFileLocation(), 
						lineNum, 
						reviewComment.getSeverity(), 
						reviewComment.fetchCommentType(),
						reviewComment.getReviewComment(),
						reviewComment.getReferenceData());
				formattedComments.add(comment);
				
				reportSummaryVo = getReportSummaryVo(reviewComment.getRuleId(), reviewComment.getShortDescripton());
				category = getReviewCommentCategory(reviewComment.getRuleId(), reviewComment.getFileLocation());
				
				if (null != reportSummaryVo && category != null) {
					reportSummaryVo.updateCommentCount(category);
				} else {
					CodeReviewStatus.getInstance().addErrorMessage("Unable to report for the Rule : " + reviewComment.getRuleId() + reviewComment.getShortDescripton());
				}
			}
		}
		
		return formattedComments;
	}	

	private String fetchFormattedComment(String ruleId, String shortDesc, String fileName, String fileLocation, String lineNumber,String severity, String commentType, String reviewComment, String referenceData) {

		// Strp all commas because the report is a CSV. Unwanted Commas are not welcome
		StringBuffer comment = new StringBuffer();
		comment.append(stripComma(ruleId));

		comment.append(COMMA);
		comment.append(stripComma(shortDesc));

		comment.append(COMMA);
		comment.append(stripComma(fileName));

		comment.append(COMMA);
		comment.append(stripComma(fileLocation));
		
		comment.append(COMMA);
		comment.append(stripComma(lineNumber));
		
		comment.append(COMMA);
		comment.append(stripComma(severity));
		
		comment.append(COMMA);
		comment.append(stripComma(commentType));
		
		comment.append(COMMA);
		comment.append(stripComma(reviewComment));
		
		comment.append(COMMA);
		comment.append(stripComma(referenceData));

		
		return comment.toString();
	}	

	private boolean isValidReport(List<ReviewComment> reviewComments) {
		if (codeReviewConfig == null || codeReviewConfig.getReviewOutputPath()== null) {
			// JSReviewUtil.printToConsole("Code Review Configuraition Not found. Unable to identify the output folder for writing Review Comments");
			return false;
		}
		
		return true;
	}
	
	private String stripComma(String attribute) {
		String updatedVar = null;
		
		if (null == attribute || attribute.length()==0) {
			return attribute;
		}
		
		updatedVar = attribute.replace(CHAR_COMMA, CHAR_SEMI_COLON);
		return updatedVar;
	}
	
	public void prepareReviewSummaryReport () {
		List<String> summaryReportComments = null;
			
		Collections.sort(reportSummaryList);
		summaryReportComments = fetchAllFormattedSummaryReportLines(reportSummaryList);
		
		JSReviewUtil.printToConsole("Printing Summary Report" );
		
		createReportFile(summaryReportComments, EXECUTIVE_SUMMARY_FILE);
	}
	
	private String fetchFormattedSummaryReportHeader() {
		StringBuffer comment = new StringBuffer();
		comment.append("Rule ID");

		comment.append(COMMA);
		comment.append("Rule Description");
		
		comment.append(COMMA);
		comment.append("Is Rule Enabled?");

		comment.append(COMMA);
		comment.append("#comments: js modules");

		comment.append(COMMA);
		comment.append("#comments: mobile app (excluding forked forms)");

		comment.append(COMMA);
		comment.append("#comments: forked forms (mobile app) ");

		comment.append(COMMA);
		comment.append("#comments: tablet app (excluding forked forms)");

		comment.append(COMMA);
		comment.append("#comments: forked forms (tablet app)");

		comment.append(COMMA);
		comment.append("#comments: desktop app");

		comment.append(COMMA);
		comment.append("#comments: misc");
		
		return comment.toString();
		
	}

	private List<String> fetchAllFormattedSummaryReportLines(List<ReportSummaryVo> reportSummaryList) {
		List<String> formattedComments = new ArrayList<String>();
		String comment = null;

		//First add the header
		comment = fetchFormattedSummaryReportHeader();
		formattedComments.add(comment);
		
		for(ReportSummaryVo reportSummaryVo : reportSummaryList) {
				
			comment = fetchFormattedSummaryReportLine(reportSummaryVo);
			formattedComments.add(comment);
		}
		
		return formattedComments;
	}	
	
	private String fetchFormattedSummaryReportLine(ReportSummaryVo reportSummaryVo) {

		// Strp all commas because the report is a CSV. Unwanted Commas are not welcome
		StringBuffer comment = new StringBuffer();
		comment.append(stripComma(reportSummaryVo.getRuleId()));

		comment.append(COMMA);
		comment.append(stripComma(reportSummaryVo.getRuleDesc()));

		comment.append(COMMA);
		if (reportSummaryVo.isRuleEnabled()) {
			comment.append(YES);
		} else {
			comment.append(NO);
		}

		comment.append(COMMA);
		if (enabledCommentCategories.contains(COMMENT_CATEGORY_JS)) {
			if (reportSummaryVo.isRuleEnabled() && reportSummaryVo.getRuleId() != null 
					&& reportSummaryVo.getRuleId().toLowerCase().startsWith(JS.toLowerCase())) {
				comment.append(reportSummaryVo.getCommentCount(COMMENT_CATEGORY_JS));
			} else {
				comment.append(NIL);
			}
		} else {
			comment.append(NIL);
		}

		comment.append(COMMA);
		if (enabledCommentCategories.contains(COMMENT_CATEGORY_MOBILE_APP)) {
			if (reportSummaryVo.isRuleEnabled() && reportSummaryVo.getRuleId() != null 
					&& reportSummaryVo.getRuleId().toLowerCase().startsWith(FRM.toLowerCase())) {
				comment.append(reportSummaryVo.getCommentCount(COMMENT_CATEGORY_MOBILE_APP));
			} else {
				comment.append(NIL);
			}
		} else {
			comment.append(NIL);
		}

		comment.append(COMMA);
		if (enabledCommentCategories.contains(COMMENT_CATEGORY_FORKED_MOBILE_APP)) {
			if (reportSummaryVo.isRuleEnabled() && reportSummaryVo.getRuleId() != null 
					&& reportSummaryVo.getRuleId().toLowerCase().startsWith(FRM.toLowerCase())) {
				comment.append(reportSummaryVo.getCommentCount(COMMENT_CATEGORY_FORKED_MOBILE_APP));
			} else {
				comment.append(NIL);
			}
		} else {
			comment.append(NIL);
		}

		comment.append(COMMA);
		if (enabledCommentCategories.contains(COMMENT_CATEGORY_TABLET_APP)) {
			if (reportSummaryVo.isRuleEnabled() && reportSummaryVo.getRuleId() != null 
					&& reportSummaryVo.getRuleId().toLowerCase().startsWith(FRM.toLowerCase())) {
				comment.append(reportSummaryVo.getCommentCount(COMMENT_CATEGORY_TABLET_APP));
			} else {
				comment.append(NIL);
			}
		} else {
			comment.append(NIL);
		}
		
		comment.append(COMMA);
		if (enabledCommentCategories.contains(COMMENT_CATEGORY_FORKED_TABLET_APP)) {
			if (reportSummaryVo.isRuleEnabled() && reportSummaryVo.getRuleId() != null 
					&& reportSummaryVo.getRuleId().toLowerCase().startsWith(FRM.toLowerCase())) {
				comment.append(reportSummaryVo.getCommentCount(COMMENT_CATEGORY_FORKED_TABLET_APP));
			} else {
				comment.append(NIL);
			}
		} else {
			comment.append(NIL);
		}
		
		comment.append(COMMA);
		if (enabledCommentCategories.contains(COMMENT_CATEGORY_DESKTOP_APP)) {
			if (reportSummaryVo.isRuleEnabled() && reportSummaryVo.getRuleId() != null 
					&& reportSummaryVo.getRuleId().toLowerCase().startsWith(FRM.toLowerCase())) {
				comment.append(reportSummaryVo.getCommentCount(COMMENT_CATEGORY_DESKTOP_APP));
			} else {
				comment.append(NIL);
			}
		} else {
			comment.append(NIL);
		}
		
		comment.append(COMMA);
		if (enabledCommentCategories.contains(COMMENT_CATEGORY_MISC)) {
			if (reportSummaryVo.isRuleEnabled() && reportSummaryVo.getRuleId() != null 
					&& reportSummaryVo.getRuleId().toLowerCase().startsWith(MISC.toLowerCase())) {
				comment.append(reportSummaryVo.getCommentCount(COMMENT_CATEGORY_MISC));
			} else {
				comment.append(NIL);
			}
		} else {
			comment.append(NIL);
		}
		
		return comment.toString();
	}
	
	private String formatReportCount(int count) {
		if (count <=0) {
			return NIL;
		} else {
			return (BLANK + count);
		}
	}
	
	
	private static void  initEnabledCommentCatgeories() {
		String propValue1 = null;
		String propValue2 = null;
		String propValue3 = null;
		String propValue4 = null;
		
		enabledCommentCategories = new ArrayList<String>();
		
		enabledCommentCategories.add(COMMENT_CATEGORY_JS);
		
		propValue1 = codeReviewConfig.getCodeReviewProperty(PROP_MOBILE_NATIVE_APP_REVIEW_FLAG);
		propValue2 = codeReviewConfig.getCodeReviewProperty(PROP_MOBILE_SPA_APP_REVIEW_FLAG);
		propValue3 = codeReviewConfig.getCodeReviewProperty(PROP_MOBILE_MOBILEWEB_APP_REVIEW_FLAG);
		propValue4 = codeReviewConfig.getCodeReviewProperty(PROP_FORKED_FORMS_REVIEW_FLAG);
		
		if ((propValue1!=null && propValue1.trim().equalsIgnoreCase(YES)) ||
				(propValue2!=null && propValue2.trim().equalsIgnoreCase(YES)) || 
				(propValue3!=null && propValue3.trim().equalsIgnoreCase(YES))) {
			enabledCommentCategories.add(COMMENT_CATEGORY_MOBILE_APP);
			
			if(propValue4!=null && propValue4.trim().equalsIgnoreCase(YES)) {
				enabledCommentCategories.add(COMMENT_CATEGORY_FORKED_MOBILE_APP);
			}
		}
		
		propValue1 = codeReviewConfig.getCodeReviewProperty(PROP_TABLET_NATIVE_APP_REVIEW_FLAG);
		propValue2 = codeReviewConfig.getCodeReviewProperty(PROP_TABLET_SPA_APP_REVIEW_FLAG);
		propValue4 = codeReviewConfig.getCodeReviewProperty(PROP_FORKED_FORMS_REVIEW_FLAG);
		
		if ((propValue1!=null && propValue1.trim().equalsIgnoreCase(YES)) ||
				(propValue2!=null && propValue2.trim().equalsIgnoreCase(YES))) {
			enabledCommentCategories.add(COMMENT_CATEGORY_TABLET_APP);
			
			if(propValue4!=null && propValue4.trim().equalsIgnoreCase(YES)) {
				enabledCommentCategories.add(COMMENT_CATEGORY_FORKED_TABLET_APP);
			}
		}
		
		propValue1 = codeReviewConfig.getCodeReviewProperty(PROP_DESKTOPWEB_APP_REVIEW_FLAG);
		if(propValue1!=null && propValue1.trim().equalsIgnoreCase(YES)) {
			enabledCommentCategories.add(COMMENT_CATEGORY_DESKTOP_APP);
		}
		
		enabledCommentCategories.add(COMMENT_CATEGORY_MISC);
	}
	
}
