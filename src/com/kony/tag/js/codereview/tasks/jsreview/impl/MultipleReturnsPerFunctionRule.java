package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.FunctionVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class MultipleReturnsPerFunctionRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.js.005.multiple_returns_per_function_rule";
	public static final String PROPS_MAX_RETURNS_PER_FUNCTION = "rules.js.005.max_returns_per_function";
	public static final String RULE_ID = "JS-005";
	
	public static final String ERR_MULTIPLE_RETURN_STATEMENTS = "Ensure that there is only one return statement per function";
	private static int MAX_RETURNS_PER_FUNCTION = 1;
	private static final String ERR_SHORT_DESC = "Multiple Return Statements found";
	
	private final String RETURN = "return";
	
	private String _functionName = null;
	private int _returnStatementCount = 0;
	private int _functionStartLineNum = 0;

	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public MultipleReturnsPerFunctionRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		//super.init(PROPS_RULE_NAME,1);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
		
		init();
		
		MAX_RETURNS_PER_FUNCTION = Integer.parseInt(
					getProjectConfig().getCodeReviewProperty(PROPS_MAX_RETURNS_PER_FUNCTION));
					
	}
	
	public void init() {
		_functionName = null;
	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFucntionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {
		

		//final String CHECK_STRING = "Someone else is enrolled on this device and enrolling will unenroll the other customer";
		/*
		if (line.indexOf(CHECK_STRING)>=0) {
			System.out.println("CHECKING : ");
			System.out.println(line);
		}
		*/
		
		String fName = null;
		FunctionVo fnVo = null;
		
		fnVo = getFunctionName(line);
		fName = fnVo.getFunctionName();
		
		if (fName != null) {
			if (fName.equals(NULL)) {
				_functionName = null;
				_functionStartLineNum = 0;
				_returnStatementCount = 0;
			} else {
				if (_functionName == null || !_functionName.equals(fName)) {
					
					if (_functionName != null && _returnStatementCount>MAX_RETURNS_PER_FUNCTION) {
						addWarning(ERR_MULTIPLE_RETURN_STATEMENTS, ERR_SHORT_DESC," function : " + _functionName + " # of return statements: " +_returnStatementCount,fileName, _functionStartLineNum, SEV_LOW, RULE_ID);
					}
					// Start counting return statements for the new function
					_functionName = fName;
					_functionStartLineNum = lineNum;
					_returnStatementCount = 0;
					//System.out.println("ADDING FUNCTION NAME " + _functionName);
					//myFunctions.add(_functionName);
				}
			}
		}
		
		 if (_functionName != null) {
			countReturnStatements(line);
		}
		
		return getComments();
}
	
	
	private void countReturnStatements(String reviewLine) {
		int index = 0;
		char prevChar;
		char postChar;
		boolean prevCharOk = false;
		boolean postCharOk = false;
		String keyword = RETURN;
		int keyWordLength = 0;
		
		reviewLine = truncateBetweenQuotes(reviewLine);
		
		if (reviewLine == null || reviewLine.length()==0) {
			return;
		}

		keyWordLength = RETURN.length();
		index = reviewLine.indexOf(keyword);
		
		while (index >=0) {
			
			prevCharOk = false;
			postCharOk = false;
			
			if (index ==0) {
				prevCharOk = true;
			}
			if (index >0 && !prevCharOk) {
				prevChar = reviewLine.charAt(index-1);
				if (Character.isWhitespace(prevChar)) {
					prevCharOk = true;	
				}
			}
			
			if (reviewLine.length() > (index+keyWordLength)) {
				postChar = reviewLine.charAt(index+keyWordLength);
				if (postChar == CHAR_SEMI_COLON || Character.isWhitespace(postChar)) {
					postCharOk = true;	
				}
			} else {
				postCharOk = true;
			}
			
			if (prevCharOk && postCharOk) {
				_returnStatementCount++;
			} 

			if (reviewLine.length()>(index+keyWordLength)) {
				index = reviewLine.indexOf(keyword,index+keyWordLength);
			} else {
				index = -1;
			}
		}
		
		return;
	}
}
