package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class TraditionalForLoopRule extends JsReviewMasterTask  {
	
	public static final String PROPS_RULE_NAME = "rules.js.013.traditional_for_loop_rule";
	public static final String RULE_ID = "JS-013";
	private static final String TRADITIONAL_FOR_LOOP_INFO = "(.*)(for)( *)(.+)(var)( *)(.*)(in)( +)(.*)";
	private static final String WARNING_SHORT_DESC = "Using newer version of for loop.";
	private static final String WARNING_DESC = "Use traditional version of for loop as for(var i=0; i<arr.length; i++) instead of newer version for(var i in arr) for better performance.";

	public TraditionalForLoopRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		// TODO Auto-generated constructor stub
		String[] ruleDescriptions = {WARNING_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
	}

	public void init() {
	}
	
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName,
			int lineNum, int lastLineNum, String reviewLine, int passNumber,
			String currFunctionName, boolean functionEndFlag,
			int prevCommentedLinesCount) throws TagToolException {
		// TODO Auto-generated method stub
		if(reviewLine.matches(TRADITIONAL_FOR_LOOP_INFO)){
			addWarning(WARNING_DESC, WARNING_SHORT_DESC, reviewLine, fileName, lineNum, SEV_LOW, RULE_ID);
		}
		return getComments();
	}

	@Override
	protected boolean isLineSplitNeeded() {
		// TODO Auto-generated method stub
		return false;
	}

}
