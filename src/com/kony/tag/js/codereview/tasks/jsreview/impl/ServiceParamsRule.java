package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class ServiceParamsRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.js.001.max_function_size_rule";
	public static final String PROPS_MAX_FUNCTION_SIZE_WITHOUT_COMMENTS = "rules.js.001.max_function_size_without_comments";
	public static final String PROPS_MAX_FUNCTION_SIZE_WITH_COMMENTS = "rules.js.001.max_function_size_with_comments";
	
	//public static final String ERR_MULTIPLE_FUNCTIONS_PER_LINE = "Multiple Functions defined in a single line !";
	public static String ERR_MAX_FUNCTION_SIZE = "Consider splitting the function into multiple re-usable functions";
	
	private static final String ERR_SHORT_DESC = "Threshold Function Size Exceeded";

	public static final String RULE_ID = "JS-001";
	
	private static int MAX_PERMITTED_LINES_WITHOUT_COMMENTS = 100;
	private static int MAX_PERMITTED_LINES_WITH_COMMENTS = 200;
	
	private String _fnName = null;
	private int _functionStartLineNum = 0;
	private int _functionSizeWithoutComments = 0;

	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public ServiceParamsRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		//super.init(PROPS_RULE_NAME,1);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);

		init();
		
		MAX_PERMITTED_LINES_WITHOUT_COMMENTS = Integer.parseInt(
				getProjectConfig().getCodeReviewProperty(PROPS_MAX_FUNCTION_SIZE_WITHOUT_COMMENTS));

		MAX_PERMITTED_LINES_WITH_COMMENTS = Integer.parseInt(
				getProjectConfig().getCodeReviewProperty(PROPS_MAX_FUNCTION_SIZE_WITH_COMMENTS));

		ERR_MAX_FUNCTION_SIZE += ". Suggested Function Size : " + MAX_PERMITTED_LINES_WITHOUT_COMMENTS + " lines (without comments) & " +   + MAX_PERMITTED_LINES_WITH_COMMENTS + " lines (with comments) ";
	}
	
	public void init() {
		_fnName = null;
		_functionStartLineNum=0;
		_functionSizeWithoutComments = 0;
	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {
		

		/*
		final String CHECK_STRING = "\"isArray\" : function(val) {";
		if (line.indexOf(CHECK_STRING)>=0) {
			System.out.println("CHECKING : ");
			System.out.println(line);
		}
		*/
		
		int functionSize = 0;
		
		if (currFunctionName != null && _fnName==null) {
			_fnName = currFunctionName;
			_functionStartLineNum = lineNum;
			_functionSizeWithoutComments = 1;
		}
		
		if (_fnName != null && _functionSizeWithoutComments >0) {
			_functionSizeWithoutComments++;
		}
		
		if ((currFunctionName != null && functionEndFlag) ||
				((lineNum == lastLineNum) && (_fnName!=null))) {
			
			if (currFunctionName.equals(_fnName)) {
				functionSize = (lineNum - _functionStartLineNum) + 1;

				//System.out.println("CHECK SIZE :: " + fileName + " : " + lineNum +  " : " + _fnName +  " SIZE : " +functionSize + " start: " +_functionStartLineNum + "end: " + lineNum);
				if (_fnName != null && (functionSize>MAX_PERMITTED_LINES_WITH_COMMENTS || _functionSizeWithoutComments>MAX_PERMITTED_LINES_WITHOUT_COMMENTS)) {
					addWarning(ERR_MAX_FUNCTION_SIZE,ERR_SHORT_DESC,"function : " + _fnName + " ::" +functionSize + " lines(with comments) ;"+ _functionSizeWithoutComments + " lines(without comments)",
								fileName, _functionStartLineNum, SEV_LOW, RULE_ID);
				}
				
			} else {
				// System.out.println("ERROR END OF FUNCTION NOT FOUND FOR " + _fnName + " current function expected " + currFunctionName);
				CodeReviewStatus.getInstance().addErrorMessage("ERROR END OF FUNCTION NOT FOUND FOR " + _fnName + " current function expected " + currFunctionName);
			}
			
			_fnName = null;
			_functionSizeWithoutComments = 0;
			
		}
		return getComments();
	}
}

