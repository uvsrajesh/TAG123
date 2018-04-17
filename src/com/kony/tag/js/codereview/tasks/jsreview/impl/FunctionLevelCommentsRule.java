package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.FunctionVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class FunctionLevelCommentsRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.js.007.function_level_comments_rule";
	public static final String RULE_ID = "JS-007";
	
	public static final String ERR_FUNCTION_LEVEL_COMMENTS = "Add one or more lines of comments above the function definition";
	private final String NULL = "null"; 
	
	private static final String ERR_SHORT_DESC = "Function Level Comments missing";
	
	//private static List<String> myFunctions = new ArrayList<String>();
	//private static List<String> allFunctions = new ArrayList<String>();
	//private static boolean taskDone = false;
	
	private String _functionName = null;

	@Override
	protected boolean isLineSplitNeeded(){return true;}
	
	public FunctionLevelCommentsRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		//super.init(PROPS_RULE_NAME,2);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,2);
		
		init();
	}
	
	public void init() {
		_functionName = null;
	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {

		String fName = null;
		FunctionVo fnVo = null;

		fnVo = getFunctionName(line);
		fName = fnVo.getFunctionName();
		
		//System.out.println("fName: "+fName);
		
		if (fName != null) {
			if (fName.equals(NULL)) {
				_functionName = null;
			} else {
				if (_functionName == null || !_functionName.equals(fName)) {
					_functionName = fName;
				}
			}
		}
		
		return getComments();
}
	
	public void updateFileLevelComments() {
	}
}
