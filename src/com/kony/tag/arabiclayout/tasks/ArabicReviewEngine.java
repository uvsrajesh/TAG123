package com.kony.tag.arabiclayout.tasks;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.kony.tag.arabiclayout.impl.ArabicAlignmentsController;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;
import com.kony.tag.tasks.base.CodeReviewMasterTask;
import com.kony.tag.util.RuleFactory;
import com.kony.tag.util.eclipse.JSReviewUtil;


public class ArabicReviewEngine extends CodeReviewMasterTask {
	
	ArabicAlignmentsController arabicAlignmentsController = null;
	List<String> filesList = null;
	Map<String, Map<String,Boolean>> ruleLevelExclusionMap = null;
	
	boolean _isCodeCommented = false;

	public ArabicReviewEngine(ProjectConfig projectConfig) {
		super(projectConfig);
	}
	
	//code for ignoring comments - please write
	public void execute(File reviewFile, int passNumber) throws TagToolException {
		JSReviewUtil.printDetailedLog("Arabic Transformation execute START");
		List<String> reviewLines  = fetchLinesOfFile(reviewFile);
		int reviewLinesCount = 0;
		String fileName = reviewFile.getName();
		String reviewLine = null;
		String reviewedLine = null;
		int commentedLinesCount = 0;
		
		if (null != reviewLines) {
			reviewLinesCount = reviewLines.size();
		}
		
		init();
		JSReviewUtil.printToConsole("# Transforming : " + reviewFile.getName());
		
		for (int lineNum=0; lineNum<reviewLinesCount;lineNum++) {
			reviewLine = reviewLines.get(lineNum);
			reviewedLine = executeLine(fileName, new String(reviewLine), lineNum, passNumber, reviewLinesCount,commentedLinesCount);
			
		}
		JSReviewUtil.printDetailedLog("jsReviewEngine execute END");
}
	
	private void init() throws TagToolException {
		arabicAlignmentsController = (ArabicAlignmentsController) RuleFactory.getCodeReviewRuleInstance(ArabicAlignmentsController.PROPS_RULE_NAME,getProjectConfig());
		arabicAlignmentsController.init();
	}
	
	//code for ignoring comments - please write
	public String executeLine(String fileName, String line, int lineNum, int passNumber, int reviewLinesCount,int prevCommentedLinesCount) throws TagToolException {
		
		int index = 0;
		String reviewLine = null;
		String tempStr = null;

			// clean up comments
			line = cleanupCommentPairs(line);

			if (line == null || line.trim().length()==0) {
				JSReviewUtil.printDetailedLog(" (1) Cannot Transform Line Number : " + lineNum);
				return null;
			}
			
			// No Need to review a commented line.
			index = line.indexOf(SINGLE_LINE_COMMENT);
			if (index == 0) {
				JSReviewUtil.printDetailedLog(" (2) Cannot Transform Line Number: " + lineNum);
				return null;
			} else if (index>0) {
				tempStr = line.substring(0,index).trim();
				if (tempStr.length() == 0) {
					JSReviewUtil.printDetailedLog(" (3) Cannot Transform Line Number: " + lineNum);
					return null;
				}
			}

			// Now it is sure that we are only reviewing the un commented portion of the line (assuming multi line comments are not there)
			reviewLine = new String(line);

			arabicAlignmentsController.reviewJSLine(fileName, (lineNum+1), reviewLinesCount,reviewLine, passNumber,prevCommentedLinesCount);

			return line;
	}	
	
	// This function will delete all pairs of "/*...*/" comments from this line
	private String cleanupCommentPairs(String reviewLine) {
		int index=0;
		int index2=0;
		String cleanedupLine=null;
		String beforeStr = null;
		String afterStr = null;
		
		if (null == reviewLine || reviewLine.trim().length() ==0) {
			return BLANK;
		}

		index = getKeywordIndex(reviewLine, MULTI_LINE_COMMENT_START, null, null);
		index2 = getKeywordIndex(reviewLine, MULTI_LINE_COMMENT_END, null, null);
		
		if (index >=0 && index2 >index) {
			// Comment Pair found ..delete it from review
			if(index==0) {
				beforeStr = BLANK;
			} else {
				beforeStr = reviewLine.substring(0,index);
			}
			
			if (reviewLine.length() > (index2+2)) {
				afterStr = reviewLine.substring(index2+2);
			}else {
				afterStr = BLANK;
			}
			
			cleanedupLine = (beforeStr + afterStr).trim();

			index = getKeywordIndex(cleanedupLine, MULTI_LINE_COMMENT_START, null, null);
			index2 = getKeywordIndex(cleanedupLine, MULTI_LINE_COMMENT_END, null, null);
			
			if (index >=0 && index2 >index) {
				cleanedupLine = cleanupCommentPairs(cleanedupLine);
			} else {
				return cleanedupLine;
			}
		}	else {
			cleanedupLine = reviewLine;
		}
		
		return cleanedupLine;
	}
	
	/*private boolean isExcludedFile(String ruleId, String fileName) {
		boolean isExcluded = false;
		Boolean boolObjVal = null;
		Map<String,Boolean> fileLevelExclusionMap = null;
		
		if (null != ruleId && null != fileName) {
			
			ruleId = ruleId.trim().toUpperCase();
			fileName = fileName.trim().toLowerCase();
			
			fileLevelExclusionMap = ruleLevelExclusionMap.get(ruleId);
			
			if (fileLevelExclusionMap == null) {
				fileLevelExclusionMap = new HashMap<String,Boolean>();
				ruleLevelExclusionMap.put(ruleId, new HashMap<String,Boolean>());
			}
			
			boolObjVal = fileLevelExclusionMap.get(fileName);
			
			if(boolObjVal != null) {
				return boolObjVal.booleanValue();
			} else {
				filesList = ReviewPropertiesUtil.getFilesToIgnore(ruleId);
				if (filesList != null && filesList.size()>0 &&  
						filesList.contains(fileName)) {
					isExcluded = true;
				}
				
				fileLevelExclusionMap.put(fileName, Boolean.valueOf(isExcluded));
			}
		}
		
		
		return isExcluded;
	}*/
}
