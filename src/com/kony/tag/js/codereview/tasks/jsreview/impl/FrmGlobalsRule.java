package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class FrmGlobalsRule extends JsReviewMasterTask  {
	
	public static final String PROPS_RULE_NAME = "rules.js.015.frm_globals_rule";
	public static final String RULE_ID = "JS-015";
	private static final String FRM_GLOBALS_INFO = "(.*)frm(.*)Globals\\(\\)(.*)";
	private static final String WARNING_SHORT_DESC = "frm*Globals() is a generated function.  This should not be used in application code.";
	private static final String WARNING_DESC = "frm*Globals() is a generated function.  This should not be used in application code.  Use form.destroy() API for the same results.";

	public FrmGlobalsRule(ProjectConfig codeReviewConfig) {
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
		if(reviewLine.matches(FRM_GLOBALS_INFO)){
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

