package com.kony.tag.tasks;

import java.awt.PageAttributes.OriginType;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.tag.arabiclayout.util.ArabicLayoutUtil;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.tasks.jsreview.impl.AlertDisplayRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.FunctionLevelCommentsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.FunctionSizeRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.GlobalVariablesRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.HardCodedStringsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.MultipleReturnsPerFunctionRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.MultipleTimersRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.NetworkAPIWrapperRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.SegmentUpdateInLoopRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedFunctionsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedI18Rule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedImagesRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedSkinsRule;
import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;
import com.kony.tag.tasks.base.CodeReviewMasterTask;
import com.kony.tag.util.RuleFactory;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class JSReviewEngine extends CodeReviewMasterTask {
	
	FunctionSizeRule functionSizeRule = null;
	HardCodedStringsRule hardCodedStringsRule = null;
	AlertDisplayRule alertDisplayRule = null;
	GlobalVariablesRule globalVarRule = null;
	SegmentUpdateInLoopRule segmentUpdateInLoopRule = null;
	MultipleReturnsPerFunctionRule multipleReturnsPerFunctionRule = null;
	NetworkAPIWrapperRule networkAPIWrapperRule = null;
	FunctionLevelCommentsRule functionLevelCommentsRule = null;
	MultipleTimersRule multipleTimersRule = null;
	List<String> filesList = null;
	Map<String, Map<String,Boolean>> ruleLevelExclusionMap = null;
	
	UnusedFunctionsRule unusedFunctionsRule = null;
	UnusedSkinsRule unusedSkinsRule = null;
	UnusedImagesRule unusedImagesRule = null;
	UnusedI18Rule unusedI18Rule = null;
	boolean _isCodeCommented = false;
	ArrayList<String> linesOfCode;
	String functionName = "";
	boolean functionStartAdded = false;
	boolean functionEndAdded = false;

	public JSReviewEngine(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	//code for ignoring comments - please write
	public List<ReviewComment> execute(File reviewFile, int passNumber) throws TagToolException {
		List<ReviewComment> allComments = null;
		List<String> reviewLines  = fetchLinesOfFile(reviewFile);
		ArrayList<String> callBackArray = getProjectConfig().getCallBackArray();
		int reviewLinesCount = 0;
		String fileName = reviewFile.getName();
		String reviewLine = null;
		String reviewedLine = null;
		int commentedLinesCount = 0;
		
		if (null != reviewLines) {
			reviewLinesCount = reviewLines.size();
		}
		
		init();
		JSReviewUtil.printToConsole("# Reviewing File : " + reviewFile.getAbsolutePath() + " Pass #"+passNumber);
		
		for (int lineNum=0; lineNum<reviewLinesCount;lineNum++) {
			reviewLine = reviewLines.get(lineNum);
				
			if(passNumber == 1){
				// This iteration will help in identifying all the network call backs
				if(reviewLine.contains(FUNCTION_TYPE5)) {
					int paramStartIndex = reviewLine.indexOf("(");
					int paramEndIndex = reviewLine.indexOf(")");
					if(paramStartIndex >= 0 && paramEndIndex >= 0 && paramEndIndex > paramStartIndex) {
						String functionParameters = reviewLine.substring(paramStartIndex,paramEndIndex);
						String[] paramArray = functionParameters.split(",");
						if(paramArray.length > 1) {
							if(!callBackArray.contains(paramArray[2].trim())){
								callBackArray.add(paramArray[2].trim());
							}
						}
					}
				}
				continue;
			} else {
				getProjectConfig().setCallBackArray(callBackArray);
			}
			/*
			final String CHECK_STRING = "window.io_last_error=";
			if (reviewLine.indexOf(CHECK_STRING)>=0) {
				System.out.println("CHECKING : ");
				System.out.println(reviewLine);
			}
			*/
			//System.out.println("reviewLine: "+reviewLine);
			reviewedLine = executeLine(fileName, new String(reviewLine), lineNum, passNumber, reviewLinesCount,commentedLinesCount);
			
			if (reviewLine != null && reviewLine.trim().length()>0 && 
					(reviewedLine == null || reviewedLine.trim().length()==0)) {
				// This was a commented line
				commentedLinesCount++;
				
			} else if (reviewLine != null && reviewLine.trim().length()>0) {
				// This was not commented line. previously commented lines ended. reset counter
				commentedLinesCount = 0;
			}
		}
		if(passNumber == 2) {
			ArabicLayoutUtil.getInstance().createReportFile(linesOfCode, "C:\\Users\\kit249\\Desktop\\Instrumentation\\tool\\output\\"+fileName);
		}
		//alertDisplayRule.updateFileLevelComments();
	
	return allComments;
}
	
	private void init() throws TagToolException {
		functionSizeRule = (FunctionSizeRule) RuleFactory.getCodeReviewRuleInstance(FunctionSizeRule.PROPS_RULE_NAME,getProjectConfig());
		hardCodedStringsRule = (HardCodedStringsRule) RuleFactory.getCodeReviewRuleInstance(HardCodedStringsRule.PROPS_RULE_NAME,getProjectConfig());
		alertDisplayRule = (AlertDisplayRule) RuleFactory.getCodeReviewRuleInstance(AlertDisplayRule.PROPS_RULE_NAME,getProjectConfig());
		globalVarRule = (GlobalVariablesRule) RuleFactory.getCodeReviewRuleInstance(GlobalVariablesRule.PROPS_RULE_NAME,getProjectConfig());
		segmentUpdateInLoopRule = (SegmentUpdateInLoopRule) RuleFactory.getCodeReviewRuleInstance(SegmentUpdateInLoopRule.PROPS_RULE_NAME,getProjectConfig());
		multipleReturnsPerFunctionRule = (MultipleReturnsPerFunctionRule) RuleFactory.getCodeReviewRuleInstance(MultipleReturnsPerFunctionRule.PROPS_RULE_NAME,getProjectConfig());
		networkAPIWrapperRule = (NetworkAPIWrapperRule) RuleFactory.getCodeReviewRuleInstance(NetworkAPIWrapperRule.PROPS_RULE_NAME,getProjectConfig());
		functionLevelCommentsRule = (FunctionLevelCommentsRule) RuleFactory.getCodeReviewRuleInstance(FunctionLevelCommentsRule.PROPS_RULE_NAME,getProjectConfig());
		multipleTimersRule = (MultipleTimersRule) RuleFactory.getCodeReviewRuleInstance(MultipleTimersRule.PROPS_RULE_NAME,getProjectConfig());
		
		unusedFunctionsRule = (UnusedFunctionsRule) RuleFactory.getCodeReviewRuleInstance(UnusedFunctionsRule.PROPS_RULE_NAME,getProjectConfig());
		unusedSkinsRule = (UnusedSkinsRule) RuleFactory.getCodeReviewRuleInstance(UnusedSkinsRule.PROPS_RULE_NAME,getProjectConfig());
		unusedImagesRule = (UnusedImagesRule) RuleFactory.getCodeReviewRuleInstance(UnusedImagesRule.PROPS_RULE_NAME,getProjectConfig());
		unusedI18Rule = (UnusedI18Rule) RuleFactory.getCodeReviewRuleInstance(UnusedI18Rule.PROPS_RULE_NAME,getProjectConfig());
		
		functionSizeRule.init();
		hardCodedStringsRule.init();
		alertDisplayRule.init();
		globalVarRule.init();
		segmentUpdateInLoopRule.init();
		multipleReturnsPerFunctionRule.init();
		networkAPIWrapperRule.init();
		functionLevelCommentsRule.init();
		multipleTimersRule.init();
		
		unusedFunctionsRule.init();
		unusedSkinsRule.init();
		unusedImagesRule.init();
		unusedI18Rule.init();
		
		_isCodeCommented = false;
		filesList = null;
		ruleLevelExclusionMap = new HashMap<String, Map<String,Boolean>>();
		linesOfCode = new ArrayList<String>();
	}
	
	//code for ignoring comments - please write
	public String executeLine(String fileName, String line, int lineNum, int passNumber, int reviewLinesCount,int prevCommentedLinesCount) throws TagToolException {
		int index = 0;
		int index2 = 0;
		int index3 = 0;
		String reviewLine = null;
		String originalLine = line;
		boolean balanceLineExists = false;
		
			// clean up comments
			line = cleanupCommentPairs(line);

			if (line == null || line.trim().length()==0) {
				linesOfCode.add(originalLine);
				return null;
			}

			//index = line.indexOf(MULTI_LINE_COMMENT_START);
			//index2 = line.indexOf(MULTI_LINE_COMMENT_END);
			//index = getKeywordIndex(line, MULTI_LINE_COMMENT_START, null, null);
			index2 = getKeywordIndex(line, MULTI_LINE_COMMENT_END, null, null);
			index3 = getKeywordIndex(line, SINGLE_LINE_COMMENT, null, null);
			
			if (index3 >=0) {
				if (_isCodeCommented) {

					/*if (index2<0) {
						// No action needed;
						
					} else {
						if (index3>(index2+1)) {
							line = cleanupSingleLineComment(line);
						}
					}*/
					if(index2 > 0){
						_isCodeCommented = false;
					}
					//If it is a commented line, no need to process it further
					linesOfCode.add(originalLine);
					return null;
				} else {
					line = cleanupSingleLineComment(line);
				}
			}
			
			index = getKeywordIndex(line, MULTI_LINE_COMMENT_START, null, null);
			
			if (!_isCodeCommented && index>=0) {
				_isCodeCommented = true;
				if (index>0 && line.length()>index) {
					// Some more content to be reviewed in this line
					line = line.substring(0,index);
					if(line.indexOf(CHAR_CURLY_OPEN) >= 0 || line.indexOf(CHAR_CURLY_CLOSE) >= 0){
						balanceLineExists = true;
					} else {
						//If the remaining line does not have braces, no need to process it further
						linesOfCode.add(originalLine);
						if(index >= 0 && index2 > 0 && index2 > index) {
							_isCodeCommented = false;
						}
						return null;
					}
					
				}
			}

			if (_isCodeCommented && index2 <0 && !balanceLineExists) {
				// This line of code is still a comment. just continue to next line
				linesOfCode.add(originalLine);
				return null;
			} else if (_isCodeCommented && index2 >=0){
				
				// Multi Line Comment Ends 
				_isCodeCommented = false;
				
				// Multi Line Comment ended.. Process uncommented part of this line
				if ((index2+3)<=line.length()) {
					// get the balance un commented text in this line
					
					index3 = getKeywordIndex(line, MULTI_LINE_COMMENT_START, null, null);
					if (index3>(index2+2)) {
						line = line.substring(index2+2,index3);
					} else {
						line = line.substring(index2+2);
					}
				} else {
					line = null;
				}
			}

			if (line == null || line.trim().length()==0) {
				linesOfCode.add(originalLine);
				return null;
			}

			// Now it is sure that we are only reviewing the un conmmented line of this file
			reviewLine = new String(line);

			if (functionLevelCommentsRule.isEnabled()  && passNumber <= 2 && !isExcludedFile(functionLevelCommentsRule.RULE_ID, fileName)) {
				linesOfCode.add(originalLine);
				functionLevelCommentsRule.reviewJSLine(fileName, (lineNum+1), reviewLinesCount,reviewLine, passNumber,prevCommentedLinesCount, linesOfCode);
			}
			return line;
}	

	private String cleanupComments(String reviewLine) {
		
		if (reviewLine == null || reviewLine.trim().length()==0) {
			return null;
		}

		reviewLine=cleanupCommentPairs(reviewLine);
		
		if (reviewLine == null || reviewLine.trim().length()==0) {
			return null;
		}
		
		reviewLine = cleanupSingleLineComment(reviewLine);
		
		if (reviewLine == null || reviewLine.trim().length()==0) {
			return null;
		}
		
		return reviewLine;
	}
	
	private String cleanupSingleLineComment(String line) {
	
		int index = 0;
		/*
		int prevIndex = 0;
		int quoteCount=0;
		char[] mychars = null;
		char c;
		*/
		if (null == line || line.trim().length() ==0) {
			return null;
		}
		
		//mychars = line.toCharArray();
		
		// Delete the the trailing single line comment in any
		//index = line.indexOf(SINGLE_LINE_COMMENT);
		index = getKeywordIndex(line, SINGLE_LINE_COMMENT, null, null);
		
		if (index == 0) {
			// Full Line is a comment
			return null;
		} else if (index >=1) {
			// Part of this line is comment. Delete the trailing comment for code review  
			line=line.substring(0,index);
		}
		return line;

		/*
		prevIndex = 0;
		
		while (index >=0) {
			
			quoteCount = 0;
			for (int j=prevIndex; j<index;j++) {
				c = mychars[j];
				if (c== CHAR_DOUBLE_QUOTE || c == CHAR_SINGLE_QUOTE) {
					quoteCount++;
				}
			}
			
			if ((quoteCount%2)==0) {
				// rest of the line is single line comment - delete it
				
				if (index == 0) {
					// Full Line is a comment
					return null;
				} else if (index >=1) {
					// Part of this line is comment. Delete the trailing comment for code review  
					line=line.substring(0,index);
					return line;
				}				
				
			}
			
			// the single line comment indicator is part of a string so skip considering it
			if (line.length()> (index+2)) {
				index = line.substring(index+2).indexOf(SINGLE_LINE_COMMENT);
				//index = getKeywordIndex(line.substring(index+2), SINGLE_LINE_COMMENT, null, null);
			} else {
				index = -1;
			}
		}
		
	
	return line;
	*/
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
	
	private boolean isExcludedFile(String ruleId, String fileName) {
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
	}
}
