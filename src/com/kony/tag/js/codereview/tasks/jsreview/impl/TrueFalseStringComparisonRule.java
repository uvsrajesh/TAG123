package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class TrueFalseStringComparisonRule extends JsReviewMasterTask  {
	
	public static final String PROPS_RULE_NAME = "rules.js.012.true_false_string_comparison_rule";
	public static final String RULE_ID = "JS-012";
	private static final String TRUE_FALSE_STRING_COMPARING_INFO = "(.*)== *['\"](true|false)['\"](.*)";
	private static final String WARNING_SHORT_DESC = "Comparing to strings 'true' or 'false'";
	private static final String WARNING_DESC = "Perform boolean true/false comparisons instead of strings 'true'/'false' comaparisons.";

	public TrueFalseStringComparisonRule(ProjectConfig codeReviewConfig) {
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
		if(reviewLine.matches(TRUE_FALSE_STRING_COMPARING_INFO)){
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
