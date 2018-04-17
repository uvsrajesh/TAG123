package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class KonyPrintRule extends JsReviewMasterTask  {
	
	public static final String PROPS_RULE_NAME = "rules.js.011.kony_print_rule";
	public static final String RULE_ID = "JS-011";
	private static final String KONY_PRINT_INFO = "kony.print";
	private static final String WARNING_SHORT_DESC = "kony.print() should be removed in release build.";
	private static final String WARNING_DESC = "kony.print() is executed as an empty function in release build. For better performance, remove kony.print statements. Please contact TAG for script.";

	public KonyPrintRule(ProjectConfig codeReviewConfig) {
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
		if(reviewLine.contains(KONY_PRINT_INFO)){
			addWarning(WARNING_DESC, WARNING_SHORT_DESC, reviewLine, fileName, lineNum, SEV_MED, RULE_ID);
		}
		return getComments();
	}

	@Override
	protected boolean isLineSplitNeeded() {
		// TODO Auto-generated method stub
		return false;
	}

}
